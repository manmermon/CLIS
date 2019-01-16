/*
 * Copyright 2011-2018 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package Deprecated_Version.Controls;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Deprecated_Version.Auxiliar.LSLConfigParameters;
import Auxiliar.WarningMessage;
import GUI.MyComponents.Tuple;
import OutputDataFile.DataFileFormat;
import OutputDataFile.OutputDataFileWriter;
import OutputDataFile.TemporalData;
import OutputDataFile.TemporalOutputDataFile;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;
import edu.ucsd.sccn.LSL;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class outputDataFileControl extends controlOfSubordinateTemplate implements ITaskMonitor
{
	public static final String ID_PARAMETER_FILE_FORMAT = "format";
	public static final String ID_PARMETER_SET_MARK = "mark";
	private static outputDataFileControl ctr = null;

	private List< TemporalOutputDataFile > temps;
	private String fileFormat = DataFileFormat.CLIS;	
	//private String fileName = null;
	private List<LSL.StreamInfo> streamInfos = null;

	private Map<String, SaveOutputFileThread> writers = null;
	private int NumberOfSavingThreads = 0;
	
	private outputDataFileControl()
	{
		this.temps = new ArrayList< TemporalOutputDataFile >();
		this.streamInfos = new ArrayList< LSL.StreamInfo >();
		this.writers = new HashMap< String, SaveOutputFileThread >();
	}

	public static outputDataFileControl getInstance()
	{
		if (ctr == null)
		{
			ctr = new outputDataFileControl();
		}

		return ctr;
	}


	public void setSubordinates( String subordinateID, Object parameters )throws Exception
	{}

	protected void cleanUpSubordinates()
	{
		synchronized ( this.temps )
		{	
			this.NumberOfSavingThreads += this.temps.size(); 
					
			for (TemporalOutputDataFile temp : this.temps)
			{
				temp.setOutputFileFormat( this.fileFormat );
				temp.stopThread( IStoppableThread.StopWithTaskDone );
			}

			this.temps.clear();
			this.streamInfos.clear();
		}
	}

	protected void startWork( Object info ) throws Exception
	{
		if (this.temps != null)
		{
			synchronized (this.temps)
			{
				for( final TemporalOutputDataFile temp : this.temps )
				{
					Tuple t = (Tuple)info;

					if (t.x.toString().equals( ID_PARAMETER_FILE_FORMAT ) )
					{
						String format = t.y.toString();

						if ( !DataFileFormat.isSupportedFileFormat( format ) )
						{
							throw new IllegalArgumentException( "Unsupport file format." );
						}

						this.fileFormat = format;

						temp.taskMonitor( this );

						final outputDataFileControl auxOutDataFileCtrl = this;
						
						Thread tLauch = new Thread()
						{

							public void run()
							{
								try
								{
									temp.startThread();
								}
								catch (Exception e)
								{
									eventInfo event = new eventInfo( eventType.PROBLEM, e);
									supervisor.eventNotification( auxOutDataFileCtrl, event );
								}
							}
						};
						
						tLauch.start();
					}
					else if ( t.x.toString().equals( ID_PARMETER_SET_MARK ) )
					{
						temp.addMark( (Integer)t.y );
					}
				}
			}
		}
	}

	protected List<IStoppableThread> createSubordinates( Map<String, Object> parameters ) throws Exception
	{
		/*
		if ( !this.writers.isEmpty() )
		{
			for ( String iW : this.writers.keySet() )
			{
				SaveOutputFileThread w = (SaveOutputFileThread)this.writers.get(iW);
				w.stopThread( IStoppableThread.ForcedStop );
			}
			this.writers.clear();
		}
		*/

		//this.fileName = null;

		List<IStoppableThread> list = new ArrayList< IStoppableThread >();
		if ( parameters != null )
		{
			this.streamInfos.clear();
			this.temps.clear();

			String filePath = (String)parameters.keySet().iterator().next();
			HashSet< LSLConfigParameters > lslCFGs = ( HashSet< LSLConfigParameters > )parameters.get(filePath);

			//LSL lsl = new LSL();

			LSL.StreamInfo[] results = LSL.resolve_streams();

			List< Tuple< LSL.StreamInfo, LSLConfigParameters > > inlets = new ArrayList< Tuple< LSL.StreamInfo, LSLConfigParameters > >();
			List< Integer > inLetsChunckSizes = new ArrayList< Integer >();

			for( int i = 0; i < results.length; i++ )
			{
				LSL.StreamInfo info = results[ i ];
				boolean found = false;
				Iterator< LSLConfigParameters > itLSLcfg = lslCFGs.iterator();

				while ( ( itLSLcfg.hasNext() ) && ( !found ) )
				{
					LSLConfigParameters t = itLSLcfg.next();
					if( t.isSelected() )
					{
						//found = (info.type().equals( t.getDeviceType() ) ) && (info.name().equals( t.getDeviceName() ) );
						found = info.uid().equals( t.getUID() );
	
						if ( found )
						{								
							info.desc().append_child_value( LSLConfigParameters.ID_EXTRA_INFO_LABEL, t.getAdditionalInfo() );
							Tuple< LSL.StreamInfo, LSLConfigParameters > tLSL = new Tuple<LSL.StreamInfo, LSLConfigParameters>( info , t );
							inlets.add( tLSL );
							
							inLetsChunckSizes.add( t.getChunckSize() );
						}						
					}
				}
			}

			if ( !inlets.isEmpty() )
			{
				//this.fileName = filePath;

				for ( int indexInlets = 0; indexInlets < inlets.size(); indexInlets++)
				{
					Tuple< LSL.StreamInfo, LSLConfigParameters > t = inlets.get( indexInlets );
					
					LSL.StreamInfo inletInfo = t.x;
					this.streamInfos.add( inletInfo );

					TemporalOutputDataFile temp = new TemporalOutputDataFile( filePath,  inletInfo, t.y, indexInlets );

					this.temps.add( temp );
				}
			}

			list.addAll( this.temps );
		}

		return list;
	}

	public void taskDone( INotificationTask task ) throws Exception
	{
		if (task != null)
		{
			List<eventInfo> events = task.getResult();

			for (eventInfo event : events)
			{
				if (event.getEventType().equals( eventType.PROBLEM ))
				{
					this.supervisor.eventNotification( this, event );
				}
				else if( event.getEventType().equals( eventType.OUTPUT_DATA_FILE_SAVED ) )
				{	
					this.NumberOfSavingThreads--;
					
					this.writers.remove( event.getEventInformation() );
					
					if( this.NumberOfSavingThreads < 1 )
					{
						this.supervisor.eventNotification( this, new eventInfo( eventType.ALL_OUTPUT_DATA_FILES_SAVED, event.getEventInformation() )  );
					}					
				}
				else if ( event.getEventType().equals( eventType.OUTPUT_TEMPORAL_FILE_READY ) )
				{					
					SaveOutputFileThread saveOutFileThread = null;
					
					try
					{
						TemporalData dat = (TemporalData)event.getEventInformation();

						Tuple<String, Boolean> res = checkOutputFileName( dat.getOutputFileName(), dat.getStreamingName() );

						OutputDataFileWriter writer = DataFileFormat.getDataFileWriter( dat.getOutputFileFormat(), (String)res.x );

						//TODO
						//coreControl sup = this.supervisor;
						//outputDataFileControl outDataFileCtr = this;
						
						saveOutFileThread = new SaveOutputFileThread( dat, writer );
						saveOutFileThread.taskMonitor( this );

						this.writers.put( dat.getStreamingName(), saveOutFileThread );
												
						saveOutFileThread.startThread();
						
						super.sleep( 1000L ); // To avoid problems if 2 or more input streaming are called equal.
						
						if (!((Boolean)res.y).booleanValue())
						{
							this.event = new eventInfo( eventType.WARNING, "The output data file exist. It was renamed as " + (String)res.x);
							this.supervisor.eventNotification( this, this.event );
						}
					}
					catch (Exception ex)
					{
						if( saveOutFileThread != null )
						{
							saveOutFileThread.stopThread( IStoppableThread.ForcedStop );
						}
						
						this.event = new eventInfo( eventType.PROBLEM, "Save process error: " + ex.getMessage() );
						this.supervisor.eventNotification( this, event );
					}
				}
			}
		}
	}

	private Tuple<String, Boolean> checkOutputFileName( String FilePath, String sourceID )
	{
		boolean ok = true;
		boolean cont = true;

		Calendar c = Calendar.getInstance();

		int index = FilePath.lastIndexOf(".");
		String name = FilePath;
		String ext = "";
		if (index > -1)
		{
			name = FilePath.substring(0, index);
			ext = FilePath.substring(index);
		}

		String aux2 = name + "_" + sourceID + ext;
		while (cont)
		{
			File file = new File(aux2);

			if (file.exists())
			{
				ok = false;

				c.add(13, 1);
				String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(c.getTime());

				aux2 = name + "_" + sourceID + "_" + date + ext;
			}
			else
			{
				cont = false;
			}
		}

		Tuple<String, Boolean> res = new Tuple< String, Boolean>(aux2,  ok );

		return res;
	}


	public WarningMessage checkParameters()
	{
		return new WarningMessage();
	}

	//////////////
	//
	//
	private class SaveOutputFileThread extends AbstractStoppableThread implements INotificationTask
	{
		private TemporalData DATA;
		private OutputDataFileWriter writer;
		private ITaskMonitor monitor;
		
		private List< eventInfo > events;

		public SaveOutputFileThread(TemporalData DAT, OutputDataFileWriter wr)
		{
			this.DATA = DAT;
			this.writer = wr;
			
			this.events = new ArrayList< eventInfo >();
		}

		protected void startUp() throws Exception
		{
			super.startUp();
		}

		protected void runInLoop() throws Exception
		{
			saveOutputFile();
		}

		protected void targetDone() throws Exception
		{
			super.targetDone();

			this.stopThread = true;
		}

		private void saveOutputFile() throws Exception
		{
			if (this.DATA != null)
			{
				List< Object > data = this.DATA.getData();
				List< Double > time = this.DATA.getTimeStamp();
				int dataType = this.DATA.getDataType();
				int nChannel = this.DATA.getNumberOfChannels() + 1;
				String lslName = this.DATA.getStreamingName();
				String lslXML = this.DATA.getLslXml();

				String variableName = "data";
				String timeVarName = "time";
				String info = "deviceInfo";
				if (this.writer != null)
				{
					String varName = variableName + "_" + lslName;
					String timeName = timeVarName + "_" + lslName;

					int r = (int)Math.floor(1.0D * data.size() / nChannel);

					if (r < 1)
					{
						r = 1;
					}

					this.writer.addHeader(info + "_" + lslName, lslXML);
					
					switch ( dataType )
					{
						case LSL.ChannelFormat.float32:
						{
							float[] aux = new float[data.size()];
							int i = 0;
							for (Object value  : data)
							{
								aux[i] = ((Float)value).floatValue();
								i++;
							}
	
							this.writer.saveData(varName, aux, r);
	
							break;
						}
						case LSL.ChannelFormat.double64:
						{
							double[] aux = new double[data.size()];
							int i = 0;
							for (Object value : data)
							{
								aux[i] = ((Double)value).doubleValue();
								i++;
							}
							this.writer.saveData(varName, aux, r);
	
							break;
						}
						case LSL.ChannelFormat.int64:
						{
							long[] aux = new long[data.size()];
							int i = 0;
							for( Object value : data )
							{
								aux[ i ] = (long)value;
								i++;
							}
							writer.saveData( varName, aux, r );
							break;
						}
						case  LSL.ChannelFormat.string:
						{
							String aux = new String();
							for( Object value : data )
							{
								aux += " " + (String)value;
							}
							writer.saveData( varName, aux );
							break;
						}
						case LSL.ChannelFormat.int8:
						{
							int[] aux = new int[data.size()];
							int i = 0;
							for( Object value : data )
							{
								for( byte b : ( (byte[])value ) )
								{
									aux[ i ] = (int)b;
									i++;
								}
							}
							writer.saveData( varName, aux, r );
							break;
						}
						case LSL.ChannelFormat.int16:
						{
							int[] aux = new int[data.size()];
							int i = 0;
							for (Object value : data)
							{
								aux[i] = ((Short)value).intValue();
								i++;
							}
							this.writer.saveData(varName, aux, r);
	
							break;
						}
						default: // LSL.ChannelFormat.int32
						{
							int[] aux = new int[data.size()];
							int i = 0;
							for (Object value : data)
							{
								aux[i] = ((Integer)value).intValue();
								i++;
							}
							this.writer.saveData(varName, aux, r);
						}
					}

					//System.out.println("outputDataFileControl.SaveOutputFileThread.saveOutputFile() num stamp time = " + time.size() );
					double[] aux = new double[time.size()];
					int i = 0;
					for (Double value : time)
					{
						aux[i] = value.doubleValue();						
						i++;
					}					
					
					this.writer.saveData(timeName, aux, aux.length);					
										
				}
				else
				{
					if( this.monitor != null )
					{
						eventInfo event = new eventInfo( eventType.PROBLEM, new IOException( "Problem: it is not possible to write in the file " + this.writer.getFileName() + ", because Writer null."));
					
						this.events.add( event );
						this.monitor.taskDone( this );
					}
				}
			}
		}

		protected void runExceptionManager(Exception e)
		{
			if (!(e instanceof InterruptedException))
			{
				if( this.monitor != null )
				{
					eventInfo event = new eventInfo( eventType.PROBLEM, new IOException("Problem: it is not possible to write in the file " + this.writer.getFileName() + "\n" + e.getClass()));
					
					this.events.add( event );
					try 
					{
						this.monitor.taskDone( this );
					} 
					catch (Exception e1) 
					{	
					}
				}
			}
		}

		protected void cleanUp() throws Exception
		{
			super.cleanUp();

			this.writer.closeFile();
			this.writer = null;
			
			if( this.monitor != null )
			{
			
				eventInfo event = new eventInfo( eventType.OUTPUT_DATA_FILE_SAVED, DATA.getStreamingName() );
				this.events.add( event );
			
				this.monitor.taskDone( this );
			}
		}

		protected void preStopThread(int friendliness) throws Exception
		{}

		protected void postStopThread(int friendliness) throws Exception
		{}

		@Override
		public void taskMonitor( ITaskMonitor m )
		{
			this.monitor = m;
		}

		@Override
		public synchronized List<eventInfo> getResult() 
		{			
			return this.events;
		}

		@Override
		public synchronized void clearResult() 
		{
			this.events.clear();
		}
	}
	
	public boolean isSavingData()
	{			
		return this.NumberOfSavingThreads > 0;
	}	
}
