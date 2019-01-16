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
package Deprecated_Version.OutputDataFile;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Controls.Commands;
import Controls.eventInfo;
import Controls.eventType;
import Deprecated_Version.Auxiliar.LSLConfigParameters;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;
import Timers.ITimerMonitor;
import Timers.Timer;
import edu.ucsd.sccn.LSL;
import edu.ucsd.sccn.LSL.StreamInlet;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;
import javax.activation.UnsupportedDataTypeException;


public abstract class readInputData extends AbstractStoppableThread implements INotificationTask, ITimerMonitor
{
	protected ITaskMonitor monitor = null;

	private LSL.StreamInlet inLet = null;

	private Timer timer = null;

	private byte[] byteData;

	private short[] shortData;

	private int[] intData;

	private float[] floatData;
	private double[] doubleData;
	//private long[] longData;
	private String[] stringData;

	private List tempSampleBytes;
	private List< Double > tempTimeMark;
	
	protected double timeCorrection;
	protected double[] timeMark;
	protected int numberChannelFormat = 0;

	protected Semaphore syncMarkSem = null;

	protected String LSLName = "";

	protected String lslXML = "";

	protected int lslChannelCounts = 0;

	protected List< eventInfo > events;

	protected int LSLFormatData = LSL.ChannelFormat.float32;
	
	protected int chunckLength = 1;
	private int readChunckLength = 0;
	
	protected boolean interleavedData = false;

	public readInputData( LSL.StreamInfo info, LSLConfigParameters lslCfg ) throws Exception
	{
		if (info == null)
		{
			throw new IllegalArgumentException("LSL.StreamInlet is null");
		}
		
		if( lslCfg == null )
		{
			throw new IllegalArgumentException( "LSL parameters is null" );
		}
		
		this.chunckLength = lslCfg.getChunckSize();
		this.interleavedData = lslCfg.isInterleavedData();
		
		this.inLet = new StreamInlet( info );
		this.LSLFormatData = info.channel_format();

		this.numberChannelFormat = this.inLet.info().channel_format();

		this.LSLName = info.name();

		Map< String, Integer > MARKS = Commands.getOutputDataFileMark();
		String markInfoText = "";
		for( String idMark : MARKS.keySet() )
		{
			Integer v = MARKS.get( idMark );
			markInfoText += idMark + "=" + v + ";";
		}		
		info.desc().append_child_value( "markInfo", markInfoText );
		
		this.lslXML = info.as_xml();
		
		this.lslChannelCounts = this.inLet.info().channel_count();

		this.syncMarkSem = new Semaphore(1, true);

		this.events = new ArrayList< eventInfo >();
	}

	protected int createArrayData() throws Exception
	{
		int nBytes = 1;
		switch (this.numberChannelFormat)
		{
			case( LSL.ChannelFormat.int8 ):
			{
				this.byteData = new byte[ this.inLet.info().channel_count() * this.chunckLength ];			
				break;
	
			}
			case( LSL.ChannelFormat.int16 ):
			{
				nBytes = Short.BYTES;
	
				this.shortData = new short[this.inLet.info().channel_count() * this.chunckLength ];
				break;
			}
			case( LSL.ChannelFormat.int32 ):
			{
				nBytes = Integer.BYTES;
	
				this.intData = new int[this.inLet.info().channel_count() * this.chunckLength ];
				break;
			}
			/*
			case( LSL.ChannelFormat.int64 ):
			{
				nBytes = Long.BYTES;
	
				this.longData = new long[ inLet.info().channel_count() ];
				break;
			}
			 */	
			case( LSL.ChannelFormat.float32 ):
			{
				nBytes = Float.BYTES;
	
				this.floatData = new float[this.inLet.info().channel_count() * this.chunckLength ];
				break;
			}
			case( LSL.ChannelFormat.double64 ):
			{
				nBytes = Double.BYTES;
	
				this.doubleData = new double[this.inLet.info().channel_count() * this.chunckLength ];
				break;
			}
			case( LSL.ChannelFormat.string ):
			{
				nBytes = Character.BYTES;
	
				this.stringData = new String[ this.inLet.info().channel_count() * this.chunckLength ];
				break;
			}
			default:
			{
				throw new UnsupportedDataTypeException();
			}
		}

		this.timeMark = new double[ this.chunckLength ];

		return nBytes;
	}

	@Override
	protected void preStart() throws Exception
	{
		super.preStart();
		
		this.createArrayData();
		
		this.tempSampleBytes = new ArrayList();
		this.tempTimeMark = new ArrayList< Double >();

		double samplingRate = this.inLet.info().nominal_srate();
		if ( samplingRate != LSL.IRREGULAR_RATE )
		{
			this.timer = new Timer();

			long time = (long)(3*1000.0D / samplingRate);
			if (time < 3000L)
			{
				time = 3000L; // 3 seconds
			}

			this.timer.setTimerValue( time );
			this.timer.setName( this.getClass() + "-Timer");
			this.timer.setActiveTimeReport( false );
			this.timer.setActivedBeep( false );
			this.timer.setTimerMonitor( this );

			this.timer.startThread();
		}

		this.timeCorrection = this.inLet.time_correction();
	}

	protected void startUp() throws Exception
	{
		super.startUp();

		// Flush
		this.inLet.close_stream();
		this.inLet.open_stream();
	}


	protected void preStopThread(int friendliness) throws Exception
	{		
	}

	protected void postStopThread(int friendliness) throws Exception
	{}

	protected void runInLoop() throws Exception
	{
		try
		{
			this.syncMarkSem.acquire();
		}
		catch (InterruptedException localInterruptedException) 
		{
		}
		
		byte[] data = this.readData();

		if( data != null )
		{
			this.managerData( data );
	
			if (this.timer != null)
			{
				this.timer.restartTimer();
			}
		}

		if( this.syncMarkSem.availablePermits() < 1 )
		{
			this.syncMarkSem.release();
		}
	}
		
	@Override
	protected void finallyManager() 
	{
		super.finallyManager();
	}
	
	/*
	private byte[] readData_ini() throws Exception
	{
		byte[] out = null;
		
		if( this.chunckLength == 1 )
		{
			out = this.readDataBySamples();
		}
		else
		{
			//out = this.readDataByChuncks();
		}
		
		return out;
	}
		
	private byte[] readDataBySamples() throws Exception
	{
		byte[] out = null;
		ByteBuffer data = null;

		this.timeMark = new double[] { Double.NaN };
		
		switch (this.numberChannelFormat)
		{
			case( LSL.ChannelFormat.int8 ):
			{
				this.timeMark[ 0 ] = this.inLet.pull_sample(this.byteData);
				out = this.byteData;
	
				break;
			}
			case( LSL.ChannelFormat.int16 ):
			{
		
				this.timeMark[ 0 ] = this.inLet.pull_sample(this.shortData);
	
				int nBytes = this.shortData.length * Short.BYTES;
				out = new byte[nBytes];
	
				data = ByteBuffer.wrap(out);
				ShortBuffer fBuf = data.asShortBuffer();
				fBuf.put(this.shortData);
	
				break;
			}
			case( LSL.ChannelFormat.int32 ):
			{
				this.timeMark[ 0 ] = this.inLet.pull_sample(this.intData);
	
				int nBytes = this.intData.length * Integer.BYTES;
				out = new byte[nBytes];
	
				data = ByteBuffer.wrap(out);
				IntBuffer fBuf = data.asIntBuffer();
				fBuf.put(this.intData);
	
				break;
			}
			case( LSL.ChannelFormat.float32 ):
			{
				this.timeMark[ 0 ] = this.inLet.pull_sample(this.floatData);
				
				int nBytes = this.floatData.length * Float.BYTES;
				out = new byte[nBytes];
	
				data = ByteBuffer.wrap(out);
				FloatBuffer fBuf = data.asFloatBuffer();
				fBuf.put(this.floatData);
	
				break;
			}
			case( LSL.ChannelFormat.double64 ):
			{
				this.timeMark[ 0 ] = this.inLet.pull_sample(this.doubleData);
	
				int nBytes = this.doubleData.length * Double.BYTES;
				out = new byte[nBytes];
	
				data = ByteBuffer.wrap(out);
				DoubleBuffer fBuf = data.asDoubleBuffer();
				fBuf.put(this.doubleData);
	
				break;
			}
			case( LSL.ChannelFormat.string ):
			{
				this.timeMark[ 0 ] = this.inLet.pull_sample( this.stringData );
	
				String txt = "";
	
				for (int i = 0; i < this.stringData.length; i++)
				{
					if (i > 0)
					{
						txt = txt + " ";
					}
					txt = txt + this.stringData[i];
				}
	
				txt = txt + "\n";
				out = txt.getBytes();
	
				break;
			}
			default:
			{
				throw new UnsupportedDataTypeException();
			}
		}
		
		if ( !Double.isNaN( this.timeMark[ 0 ] ) )
		{
			this.timeMark[ 0 ] += this.timeCorrection;
		}

		return out;
	}
	 */
	
	//private byte[] readDataByChuncks() throws Exception
	private byte[] readData() throws Exception
	{
		byte[] out = null;
		ByteBuffer data = null;
				
		double timestamp_buffer[] = new double[ this.chunckLength ];
		int nReadData = 0;
		int chunckSize = this.chunckLength * this.lslChannelCounts;
		
		switch (this.numberChannelFormat)
		{
			case( LSL.ChannelFormat.int8 ):
			{
				nReadData = this.inLet.pull_chunk( this.byteData, timestamp_buffer );
				//nChunck = nBytes / this.lslChannelCounts;
				
				if( nReadData > 0 )
				{
					//this.timeMark = timestamp_buffer[ 0 ];
										
					int i = 0;					
					while( i < nReadData  
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.byteData[ i ] );						
						i++;
					}
					
					int j = 0;
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D 
							&& this.tempTimeMark.size() < this.chunckLength )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
					
					if( this.tempSampleBytes.size() >= chunckSize )
					{
						out = new byte[ this.tempSampleBytes.size() ];						
						for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
						{
							out[ iS ] = (Byte)this.tempSampleBytes.get( iS );							
						}
						
						for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
						{
							this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
						}
						
						this.tempSampleBytes.clear();
						this.tempTimeMark.clear();
					}
					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.byteData[ i ] );
						i++;
					}
					
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
				}
	
				break;
			}
			case( LSL.ChannelFormat.int16 ):
			{
				nReadData = this.inLet.pull_chunk( this.shortData, timestamp_buffer );
				//nChunck = nBytes / this.lslChannelCounts;
				
				if( nReadData > 0 )
				{
					int i = 0;					
					while( i < nReadData  
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.shortData[ i ] );
						i++;
					}
					
					int j = 0;
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D 
							&& this.tempTimeMark.size() < this.chunckLength )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
					
					if( this.tempSampleBytes.size() >= chunckSize )
					{			
						short[] aux = new short[ this.tempSampleBytes.size() ];
						for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
						{
							aux[ iS ] = (Short)this.tempSampleBytes.get( iS );
						}						
						
						int nBytes = this.tempSampleBytes.size() * Short.BYTES;						
						out = new byte[ nBytes ];
						
						data = ByteBuffer.wrap( out );
						ShortBuffer fBuf = data.asShortBuffer();
						fBuf.put( aux );
						
						for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
						{
							this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
						}
						
						this.tempSampleBytes.clear();
						this.tempTimeMark.clear();
					}
					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.byteData[ i ] );
						i++;
					}					
					
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D  )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
				}
	
				break;
			}
			case( LSL.ChannelFormat.int32 ):
			{				
				nReadData = this.inLet.pull_chunk( this.intData, timestamp_buffer );
				//nChunck = nBytes / this.lslChannelCounts;
				
				if( nReadData > 0 )
				{
					//this.timeMark = timestamp_buffer[ 0 ];
						
					/*
					int[] aux = this.intData;					
					if( nBytes < this.intData.length )
					{
						aux = new int[ nBytes ];
						for( int iAux = 0; iAux < nBytes; iAux++ )
						{
							aux[ iAux ] = this.intData[ iAux ];
						}			
						
						this.timeMark = new double[ nChunck ];
						for( int i = 0; i < nChunck; i++ )
						{
							this.timeMark[ i ] = timestamp_buffer[ i ];
						}
						
					}				
					else
					{
						this.timeMark = timestamp_buffer;
					}
					
					nBytes *= Integer.BYTES;
		
					//int nBytes = this.intData.length * Integer.BYTES;
					out = new byte[nBytes];
		
					data = ByteBuffer.wrap(out);
					IntBuffer fBuf = data.asIntBuffer();
					fBuf.put( aux );
					*/					
					
					int i = 0;					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.intData[ i ] );
						i++;
					}
					
					int j = 0;
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D 
							&& this.tempTimeMark.size() < this.chunckLength )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
					
					if( this.tempSampleBytes.size() >= chunckSize )
					{			
						int[] aux = new int[ this.tempSampleBytes.size() ];
						for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
						{
							aux[ iS ] = (Integer)this.tempSampleBytes.get( iS );
						}						
						
						for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
						{
							this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
						}	
						
						int nBytes = this.tempSampleBytes.size() * Integer.BYTES;						
						out = new byte[ nBytes ];
						
						data = ByteBuffer.wrap(out);
						IntBuffer fBuf = data.asIntBuffer();
						fBuf.put( aux );
						
						this.tempSampleBytes.clear();
						this.tempTimeMark.clear();
					}
					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.byteData[ i ] );
						i++;
					}					
					
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
				}
	
				break;
			}
			case( LSL.ChannelFormat.float32 ):
			{
				//this.timeMark = this.inLet.pull_sample(this.floatData);
				nReadData = this.inLet.pull_chunk( this.floatData, timestamp_buffer );
				//nChunck = nBytes / this.lslChannelCounts;
				
				if( nReadData > 0 )
				{
					//this.timeMark = timestamp_buffer[ 0 ];
					
					/*
					float[] aux = this.floatData;
					if( nBytes < this.floatData.length )
					{
						aux = new float[ nBytes ];
						for( int iAux = 0; iAux < nBytes; iAux++ )
						{
							aux[ iAux ] = this.floatData[ iAux ];
						}						
						
						this.timeMark = new double[ nChunck ];
						for( int i = 0; i < nChunck; i++ )
						{
							this.timeMark[ i ] = timestamp_buffer[ i ];
						}
					}
					else
					{
						this.timeMark = timestamp_buffer;
					}
					
					nBytes *= Float.BYTES;
					
					System.out.println("readInputData.readData() " + Arrays.toString( aux ) );
							
					//int nBytes = this.floatData.length * Float.BYTES;
					out = new byte[ nBytes ];
		
					data = ByteBuffer.wrap(out);
					FloatBuffer fBuf = data.asFloatBuffer();
					fBuf.put( aux );
					*/
					
					int i = 0;					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.floatData[ i ] );
						i++;
					}
					
					int j = 0;
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D 
							&& this.tempTimeMark.size() < this.chunckLength )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
					
					if( this.tempSampleBytes.size() >= chunckSize )
					{			
						float[] aux = new float[ this.tempSampleBytes.size() ];
						for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
						{
							aux[ iS ] = (Float)this.tempSampleBytes.get( iS );
						}
						
						for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
						{
							this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
						}
						
						int nBytes = this.tempSampleBytes.size() * Float.BYTES;						
						out = new byte[ nBytes ];
						
						data = ByteBuffer.wrap( out );
						FloatBuffer fBuf = data.asFloatBuffer();
						fBuf.put( aux );
						
						this.tempSampleBytes.clear();
						this.tempTimeMark.clear();
					}
					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.floatData[ i ] );
						i++;
					}				
					
					while( j < timestamp_buffer.length && timestamp_buffer[ j ] > 0.0D )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
				}
	
				break;
			}
			case( LSL.ChannelFormat.double64 ):
			{
				//this.timeMark = this.inLet.pull_sample(this.doubleData);
				nReadData = this.inLet.pull_chunk( doubleData, timestamp_buffer );
				//nChunck = nBytes / this.lslChannelCounts;
				
				if( nReadData > 0 )
				{	
					//this.timeMark = timestamp_buffer[ 0 ];
					
					/*
					double[] aux = this.doubleData;
					if( nBytes < this.doubleData.length )
					{
						aux = new double[ nBytes ];
						for( int iAux = 0; iAux < nBytes; iAux++ )
						{
							aux[ iAux ] = this.doubleData[ iAux ];
						}
						
						this.timeMark = new double[ nChunck ];
						for( int i = 0; i < nChunck; i++ )
						{
							this.timeMark[ i ] = timestamp_buffer[ i ];
						}
					}
					else
					{
						this.timeMark = timestamp_buffer;
					}					
					
					nBytes *= Double.BYTES;
		
					//int nBytes = this.doubleData.length * Double.BYTES;
					out = new byte[nBytes];
		
					data = ByteBuffer.wrap(out);
					DoubleBuffer fBuf = data.asDoubleBuffer();
					fBuf.put( aux );
					*/
					
					int i = 0;					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.doubleData[ i ] );
						i++;
					}
					
					int j = 0;
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D 
							&& this.tempTimeMark.size() < this.chunckLength )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
					
					if( this.tempSampleBytes.size() >= chunckSize )
					{			
						double[] aux = new double[ this.tempSampleBytes.size() ];
						for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
						{
							aux[ iS ] = (Double)this.tempSampleBytes.get( iS );
						}	
						
						for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
						{
							this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
						}	
						
						int nBytes = this.tempSampleBytes.size() * Double.BYTES;						
						out = new byte[ nBytes ];
						
						data = ByteBuffer.wrap( out );
						DoubleBuffer fBuf = data.asDoubleBuffer();
						fBuf.put( aux );
						
						this.tempSampleBytes.clear();
						this.tempTimeMark.clear();
					}
					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.doubleData[ i ] );
						i++;
					}
					
					while( j < timestamp_buffer.length && timestamp_buffer[ j ] > 0.0D )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
						j++;
					}
				}
	
				break;
			}
			case( LSL.ChannelFormat.string ):
			{
				nReadData = this.inLet.pull_chunk( this.stringData, timestamp_buffer );
				//nChunck = nBytes / this.lslChannelCounts;
				
				if( nReadData > 0 )
				{
					//this.timeMark = timestamp_buffer[ 0 ];
		
					/*
					String txt = "";
					this.timeMark = new double[ nBytes ];
					for (int i = 0; i < nBytes; i++)
					{
						if (i > 0)
						{
							txt = txt + " ";
						}
						txt = txt + this.stringData[i];						
					}

					txt = txt + "\n";
					out = txt.getBytes();
					
					this.timeMark = new double[ nChunck ];
					for( int i = 0; i < nChunck; i++ )
					{
						this.timeMark[ i ] = timestamp_buffer[ i ];
					}
					*/
					
					int i = 0;					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.stringData[ i ] );
						i++;
					}
					
					int j = 0;
					while( j < timestamp_buffer.length 
							&& timestamp_buffer[ j ] > 0.0D 
							&& this.tempTimeMark.size() < this.chunckLength )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
					}
					
					if( this.tempSampleBytes.size() >= chunckSize )
					{			
						String txt = "";
						for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
						{
							if( iS > 0 )
							{
								txt += " ";
							}
							
							txt += this.tempSampleBytes.get( iS );														
						}	
						txt += "\n";
												
						out = txt.getBytes();
												
						
						for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
						{							
							this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
						}	
						
						this.tempSampleBytes.clear();
						this.tempTimeMark.clear();
					}
					
					while( i < nReadData 
							&& this.tempSampleBytes.size() < chunckSize )
					{
						this.tempSampleBytes.add( this.stringData[ i ] );
						i++;
					}		
					
					while( j < timestamp_buffer.length && timestamp_buffer[ j ] > 0.0D )
					{
						this.tempTimeMark.add( timestamp_buffer[ j ] );
					}
				}
	
				break;
			}
			default:
			{
				throw new UnsupportedDataTypeException();
			}
		}
		
		return out;
	}
	
	protected void runExceptionManager(Exception e)
	{
		if (!(e instanceof InterruptedException))
		{
			this.stopThread = true;
			this.notifyProblem( e );
		}
	}

	protected void cleanUp() throws Exception
	{
		super.cleanUp();

		if (this.timer != null)
		{
			this.timer.stopThread( IStoppableThread.ForcedStop );
		}
		this.timer = null;

		this.inLet.close_stream();
		
		try
		{
			this.syncMarkSem.acquire();
		}
		catch (InterruptedException localInterruptedException) 
		{

		}
		
		try
		{
			/* 
			 while (this.inLet.samples_available() > 0) // Block
			{
				this.managerData( readData() );
			}
			 */
			this.readRemainingData();
		}
		catch (Exception localException) {}

		if (this.syncMarkSem.availablePermits() < 1)
		{
			this.syncMarkSem.release();
		}
		
		this.inLet.close();
		
		this.postCleanUp();
	}

	/*
	private void readRemainingData_init() throws Exception
	{
		if( this.chunckLength == 1 )
		{
			this.readRemaingDataBySamples();
		}
		else
		{
			this.readRemainingDataByChuncks();
		}
	}
	
	private void readRemaingDataBySamples() throws Exception
	{
		byte[] out = null;
		ByteBuffer data = null;
		
		double timeout = 0.0D;
		
		do
		{
			this.timeMark = new double[] { Double.NaN };
			
			switch (this.numberChannelFormat)
			{
				case( LSL.ChannelFormat.int8 ):
				{
					this.timeMark[ 0 ] = this.inLet.pull_sample(this.byteData, timeout );
					out = this.byteData;
		
					break;
				}
				case( LSL.ChannelFormat.int16 ):
				{	
					this.timeMark[ 0 ] = this.inLet.pull_sample(this.shortData, timeout );
		
					int nBytes = this.shortData.length * Short.BYTES;
					out = new byte[nBytes];
		
					data = ByteBuffer.wrap(out);
					ShortBuffer fBuf = data.asShortBuffer();
					fBuf.put(this.shortData);
		
					break;
				}
				case( LSL.ChannelFormat.int32 ):
				{
					this.timeMark[ 0 ] = this.inLet.pull_sample(this.intData, timeout );
		
					int nBytes = this.intData.length * Integer.BYTES;
					out = new byte[nBytes];
		
					data = ByteBuffer.wrap(out);
					IntBuffer fBuf = data.asIntBuffer();
					fBuf.put(this.intData);
		
					break;
				}
				case( LSL.ChannelFormat.float32 ):
				{
					this.timeMark[ 0 ] = this.inLet.pull_sample(this.floatData, timeout );
						
					int nBytes = this.floatData.length * Float.BYTES;
					out = new byte[nBytes];
		
					data = ByteBuffer.wrap(out);
					FloatBuffer fBuf = data.asFloatBuffer();
					fBuf.put(this.floatData);
		
					break;
				}
				case( LSL.ChannelFormat.double64 ):
				{
					this.timeMark[ 0 ] = this.inLet.pull_sample(this.doubleData, timeout );
		
					int nBytes = this.doubleData.length * Double.BYTES;
					out = new byte[nBytes];
		
					data = ByteBuffer.wrap(out);
					DoubleBuffer fBuf = data.asDoubleBuffer();
					fBuf.put(this.doubleData);
		
					break;
				}
				case( LSL.ChannelFormat.string ):
				{
					this.timeMark[ 0 ] = this.inLet.pull_sample( this.stringData, timeout );
		
					String txt = "";
		
					for (int i = 0; i < this.stringData.length; i++)
					{
						if (i > 0)
						{
							txt = txt + " ";
						}
						txt = txt + this.stringData[i];
					}
		
					txt = txt + "\n";
					out = txt.getBytes();
		
					break;
				}
				default:
				{
					throw new UnsupportedDataTypeException();
				}
			}
			
			if ( !Double.isNaN( this.timeMark[ 0 ] ) && this.timeMark[ 0 ] > 0.0D )
			{
				this.timeMark[ 0 ] += this.timeCorrection;
				
				this.managerData( out );
			}
		}
		while( !Double.isNaN( this.timeMark[ 0 ] ) && this.timeMark[ 0 ] > 0.0D );
	}
	 */
	
	//private void readRemainingDataByChuncks() throws Exception
	
	private void readRemainingData() throws Exception
	{						
		double timestamp_buffer[] = new double[ this.chunckLength ];
		int nReadData = 0;
		int chunckSize = this.chunckLength * this.lslChannelCounts;		
		
		double timeout = 0.0D;
				
		boolean rep = true;
		int rerun = (int)this.inLet.info().nominal_srate() / 2;
		if( rerun < 10 )
		{
			rerun = 10;
		}		
		
		do
		{
			rerun--;
			
			ByteBuffer data = null;
			byte[] out = null;			
			
			switch ( this.numberChannelFormat )
			{
				case( LSL.ChannelFormat.int8 ):
				{
					nReadData = this.inLet.pull_chunk( this.byteData, timestamp_buffer, timeout );
					//nChunck = nBytes / this.lslChannelCounts;
					
					if( nReadData > 0 )
					{
						int i = 0;					
						while( i < nReadData  
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.byteData[ i ] );						
							i++;
						}
						
						int j = 0;
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D 
								&& this.tempTimeMark.size() < this.chunckLength )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
						
						if( this.tempSampleBytes.size() >= chunckSize )
						{
							out = new byte[ this.tempSampleBytes.size() ];						
							for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
							{
								out[ iS ] = (Byte)this.tempSampleBytes.get( iS );							
							}
							
							for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
							{
								this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
							}
							
							this.tempSampleBytes.clear();
							this.tempTimeMark.clear();
						}
						
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.byteData[ i ] );
							i++;
						}
						
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
					}
		
					break;
				}
				case( LSL.ChannelFormat.int16 ):
				{
					nReadData = this.inLet.pull_chunk( this.shortData, timestamp_buffer, timeout );
					//nChunck = nBytes / this.lslChannelCounts;
					
					if( nReadData > 0 )
					{
						int i = 0;					
						while( i < nReadData  
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.shortData[ i ] );
							i++;
						}
						
						int j = 0;
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D 
								&& this.tempTimeMark.size() < this.chunckLength )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
						
						if( this.tempSampleBytes.size() >= chunckSize )
						{			
							short[] aux = new short[ this.tempSampleBytes.size() ];
							for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
							{
								aux[ iS ] = (Short)this.tempSampleBytes.get( iS );
							}						
							
							int nBytes = this.tempSampleBytes.size() * Short.BYTES;						
							out = new byte[ nBytes ];
							
							data = ByteBuffer.wrap( out );
							ShortBuffer fBuf = data.asShortBuffer();
							fBuf.put( aux );
							
							for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
							{
								this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
							}
							
							this.tempSampleBytes.clear();
							this.tempTimeMark.clear();
						}
						
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.byteData[ i ] );
							i++;
						}					
						
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D  )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
					}
		
					break;
				}
				case( LSL.ChannelFormat.int32 ):
				{				
					nReadData = this.inLet.pull_chunk( this.intData, timestamp_buffer, timeout );
					//nChunck = nBytes / this.lslChannelCounts;
					
					if( nReadData > 0 )
					{
						int i = 0;					
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.intData[ i ] );
							i++;
						}
						
						int j = 0;
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D 
								&& this.tempTimeMark.size() < this.chunckLength )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
						
						if( this.tempSampleBytes.size() >= chunckSize )
						{			
							int[] aux = new int[ this.tempSampleBytes.size() ];
							for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
							{
								aux[ iS ] = (Integer)this.tempSampleBytes.get( iS );
							}						
							
							for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
							{
								this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
							}	
							
							int nBytes = this.tempSampleBytes.size() * Integer.BYTES;						
							out = new byte[ nBytes ];
							
							data = ByteBuffer.wrap(out);
							IntBuffer fBuf = data.asIntBuffer();
							fBuf.put( aux );
							
							this.tempSampleBytes.clear();
							this.tempTimeMark.clear();
						}
						
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.byteData[ i ] );
							i++;
						}					
						
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
					}
		
					break;
				}
				case( LSL.ChannelFormat.float32 ):
				{
					//this.timeMark = this.inLet.pull_sample(this.floatData);
					
					nReadData = this.inLet.pull_chunk( this.floatData, timestamp_buffer, timeout );
					//nChunck = nBytes / this.lslChannelCounts;
					
					if( nReadData > 0 )
					{
						//this.timeMark = timestamp_buffer[ 0 ];
						
						int i = 0;					
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.floatData[ i ] );
							i++;
						}
						
						int j = 0;
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D 
								&& this.tempTimeMark.size() < this.chunckLength )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
						
						if( this.tempSampleBytes.size() >= chunckSize )
						{			
							float[] aux = new float[ this.tempSampleBytes.size() ];
							for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
							{
								aux[ iS ] = (Float)this.tempSampleBytes.get( iS );
							}
							
							for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
							{
								this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
							}
							
							int nBytes = this.tempSampleBytes.size() * Float.BYTES;						
							out = new byte[ nBytes ];
							
							data = ByteBuffer.wrap( out );
							FloatBuffer fBuf = data.asFloatBuffer();
							fBuf.put( aux );
							
							this.tempSampleBytes.clear();
							this.tempTimeMark.clear();
						}
						
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.floatData[ i ] );
							i++;
						}				
						
						while( j < timestamp_buffer.length && timestamp_buffer[ j ] > 0.0D )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
					}
		
					break;
				}
				case( LSL.ChannelFormat.double64 ):
				{
					//this.timeMark = this.inLet.pull_sample(this.doubleData);
					nReadData = this.inLet.pull_chunk( doubleData, timestamp_buffer, timeout );
					//nChunck = nBytes / this.lslChannelCounts;
					
					if( nReadData > 0 )
					{	
						//this.timeMark = timestamp_buffer[ 0 ];
																		
						int i = 0;					
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.doubleData[ i ] );
							i++;
						}
						
						int j = 0;
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D 
								&& this.tempTimeMark.size() < this.chunckLength )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
						
						if( this.tempSampleBytes.size() >= chunckSize )
						{			
							double[] aux = new double[ this.tempSampleBytes.size() ];
							for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
							{
								aux[ iS ] = (Double)this.tempSampleBytes.get( iS );
							}	
							
							for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
							{
								this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
							}	
							
							int nBytes = this.tempSampleBytes.size() * Double.BYTES;						
							out = new byte[ nBytes ];
							
							data = ByteBuffer.wrap( out );
							DoubleBuffer fBuf = data.asDoubleBuffer();
							fBuf.put( aux );
							
							this.tempSampleBytes.clear();
							this.tempTimeMark.clear();
						}
						
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.doubleData[ i ] );
							i++;
						}
						
						while( j < timestamp_buffer.length && timestamp_buffer[ j ] > 0.0D )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
							j++;
						}
					}
		
					break;
				}
				case( LSL.ChannelFormat.string ):
				{
					nReadData = this.inLet.pull_chunk( this.stringData, timestamp_buffer, timeout );
					//nChunck = nBytes / this.lslChannelCounts;
					
					if( nReadData > 0 )
					{
						//this.timeMark = timestamp_buffer[ 0 ];
						
						int i = 0;					
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.stringData[ i ] );
							i++;
						}
						
						int j = 0;
						while( j < timestamp_buffer.length 
								&& timestamp_buffer[ j ] > 0.0D 
								&& this.tempTimeMark.size() < this.chunckLength )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
						}
						
						if( this.tempSampleBytes.size() >= chunckSize )
						{			
							String txt = "";
							for( int iS = 0; iS < this.tempSampleBytes.size(); iS++ )
							{
								if( iS > 0 )
								{
									txt += " ";
								}
								
								txt += this.tempSampleBytes.get( iS );														
							}	
							txt += "\n";
													
							out = txt.getBytes();
													
							
							for( int iS = 0; iS < this.tempTimeMark.size(); iS++ )
							{							
								this.timeMark[ iS ] = (Double)this.tempTimeMark.get( iS ) + this.timeCorrection;
							}	
							
							this.tempSampleBytes.clear();
							this.tempTimeMark.clear();
						}
						
						while( i < nReadData 
								&& this.tempSampleBytes.size() < chunckSize )
						{
							this.tempSampleBytes.add( this.stringData[ i ] );
							i++;
						}		
						
						while( j < timestamp_buffer.length && timestamp_buffer[ j ] > 0.0D )
						{
							this.tempTimeMark.add( timestamp_buffer[ j ] );
						}
					}
		
					break;
				}
				default:
				{
					throw new UnsupportedDataTypeException();
				}
			}
			
			if( out != null )
			{
				this.managerData( out );
			}
			
			rep = !this.tempSampleBytes.isEmpty();
		}
		while( rep && rerun > 0 );
	}
	
	
	
	protected void notifyProblem(Exception e)
	{		
		String errorMsg = e.getMessage();
		if( errorMsg == null )
		{
			errorMsg = "";
		}
		
		if ( errorMsg.isEmpty())
		{
			Throwable t = e.getCause();
			if (t != null)
			{
				errorMsg = errorMsg + t.toString();
			}

			if (errorMsg.isEmpty())
			{
				errorMsg = errorMsg + e.getLocalizedMessage();
			}
		}

		if (this.monitor != null)
		{
			this.events.add( new eventInfo( eventType.PROBLEM, errorMsg ) );
			try
			{
				this.monitor.taskDone(this);
			}
			catch (Exception localException) 
			{
				
			}
			finally
			{
				this.stopThread = true;
			}
		}
	}

	public void taskMonitor(ITaskMonitor m)
	{
		this.monitor = m;
	}


	public List<eventInfo> getResult()
	{
		return this.events;
	}


	public void clearResult()
	{
		this.events.clear();
	}

	public void timeOver( String timerName )
	{
		this.stopThread( IStoppableThread.ForcedStop );
		this.notifyProblem( new TimeoutException("Waiting time for input data from device was exceeded." ) );		
	}

	public void reportClockTime(long time) {}

	protected abstract void postCleanUp() throws Exception;

	protected abstract void managerData(byte[] paramArrayOfByte) throws Exception;
}