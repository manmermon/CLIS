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

import Auxiliar.Tasks.ITaskMonitor;
import Controls.eventInfo;
import Controls.eventType;
import Deprecated_Version.Auxiliar.LSLConfigParameters;
import edu.ucsd.sccn.LSL;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.processing.FilerException;

public class TemporalOutputDataFile extends readInputData
{
	private byte[] NO_MARK;
	private byte[] mark;

	private File file = null;
	private File timeStampFile = null;

	private BufferedOutputStream out;  
	private DataOutputStream outTimeStampFile;

	private String ext = ".temp";
	
	private String outFileName = "";
	private String outFileFormat = DataFileFormat.CLIS;
	
	public TemporalOutputDataFile( String filePath, LSL.StreamInfo info
										, LSLConfigParameters lslCfg, int Number ) throws Exception
	{
		super( info, lslCfg );
	
		String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

		this.outFileName = filePath;
		
		super.setName( info.name() + "(" + info.uid() + ")");

		this.file = new File( filePath + "_" + date + this.ext + Number);
		this.timeStampFile = new File(filePath + "_timeStamp_" + date + this.ext + Number);

		int index = filePath.lastIndexOf("/");
		if (index < 0)
		{
			index = filePath.lastIndexOf("\\");
		}

		File dir = null;
		if (index >= 0)
		{
			String folder = filePath.substring(0, index + 1);
			dir = new File(folder);
		}

		boolean ok = true;
		String errorMsg = "Problem: file " + filePath;

		if ((dir != null) && (this.file != null))
		{
			try
			{
				if (!dir.exists())
				{
					if (!dir.mkdir())
					{
						ok = false;
					}
				}

				if (!this.file.exists())
				{
					this.file.createNewFile();
				}

				if (!this.timeStampFile.exists())
				{
					this.timeStampFile.createNewFile();
				}

				if( !this.file.isFile() || !this.file.canWrite() 
						|| !this.timeStampFile.isFile() || !this.timeStampFile.canWrite() )
				{   
					ok = false;
					errorMsg += " is not files or it is not possible to write";
				}

			}
			catch (Exception e)
			{
				ok = false;
				errorMsg = errorMsg + e.getMessage();
			}
		}
		else
		{
			ok = false;
			errorMsg = errorMsg + " not found";
		}

		if (!ok)
		{
			throw new FilerException(errorMsg);
		}
	}
	
	public void addMark( Integer value )
	{
		try
		{
			this.syncMarkSem.acquire();
		}
		catch (InterruptedException localInterruptedException) 
		{}
		
		switch( super.LSLFormatData ) 
		{
			case LSL.ChannelFormat.double64:
			{				
				double auxMark = 0.0D;
				if( this.mark != null )
				{
					auxMark = ByteBuffer.wrap( this.mark ).getDouble();
				}
				
				double v = (value.intValue() | ( int )auxMark);
				
				this.mark = ByteBuffer.allocate( Double.BYTES).putDouble( v ).array();
				break;
			}
			case LSL.ChannelFormat.float32:
			{
				float auxMark = 0.0F;
				if( this.mark != null )
				{
					auxMark = ByteBuffer.wrap( this.mark ).getFloat();
				}
				float v = ( value.intValue() | (int)auxMark );
				
				this.mark = ByteBuffer.allocate( Float.BYTES).putFloat( v ).array();
				break;
			}
			case LSL.ChannelFormat.int64:
			{
				long auxMark = 0L;
				if( this.mark != null )
				{
					auxMark = ByteBuffer.wrap( this.mark ).getLong();
				}
				long v = value.longValue() | auxMark; 
				
				this.mark = ByteBuffer.allocate( Long.BYTES).putLong( v ).array();
				break;
			}
			case LSL.ChannelFormat.int32:
			{
				int auxMark = 0;
				if( this.mark != null )
				{
					auxMark = ByteBuffer.wrap( this.mark ).getInt();
				}
				int v = value.intValue() | auxMark;
				
				this.mark = ByteBuffer.allocate( Integer.BYTES).putInt( v ).array();
				break;
			}
			case LSL.ChannelFormat.int16:
			{
				short auxMark = 0;
				if( this.mark != null )
				{
					auxMark = ByteBuffer.wrap( this.mark ).getShort();
				}
				short v = (short)( auxMark | value.shortValue() );
				
				this.mark = ByteBuffer.allocate( Short.BYTES).putShort( v ).array();
				break;
			}
			case LSL.ChannelFormat.int8:
			{
				byte auxMark = 0;
				if( this.mark != null )
				{
					auxMark = this.mark[ 0 ];
				}
				
				byte v = (byte)( value.byteValue() | auxMark );
				
				this.mark = ByteBuffer.allocate( 1 ).put( v ).array();
				break;
			}
			default:
			{
				int auxMark = 0;
				if( this.mark != null )
				{
					auxMark = new Integer( new String( this.mark ) );
				}
				int v = value.intValue() | auxMark;
				
				this.mark = ("" + v ).getBytes();
				break;
			}
		}		    

		if (this.syncMarkSem.availablePermits() < 1)
		{
			this.syncMarkSem.release();
		}
	}

	protected int createArrayData() throws Exception
	{
		int nBytes = super.createArrayData();

		if (this.numberChannelFormat == LSL.ChannelFormat.string )
		{
			this.mark = new String("0").getBytes();
			this.NO_MARK = this.mark;
		}
		else
		{
			this.NO_MARK = new byte[nBytes];
			this.mark = this.NO_MARK;
		}

		return nBytes;
	}
	
	protected void preStart() throws Exception
	{
		super.preStart();

		if (!this.file.exists())
		{
			this.file.createNewFile();
		}

		if (!this.timeStampFile.exists())
		{
			this.timeStampFile.createNewFile();
		}

		if (( !this.file.isFile() ) || ( !this.file.canWrite() ) || 
				( !this.timeStampFile.isFile() ) || ( !this.timeStampFile.canWrite() ) )
		{
			throw new FilerException(this.file.getAbsolutePath() + " is not a file or is only read mode");
		}

		this.out = new BufferedOutputStream( new FileOutputStream( this.file ) );
		this.outTimeStampFile = new DataOutputStream( new BufferedOutputStream( new FileOutputStream( this.timeStampFile ) ) );
	}
	
	protected void managerData( byte[] data ) throws Exception
	{
		int numReadChunk = super.timeMark.length;
		int dataTypeByteLength = this.mark.length;
		
		byte[] aux = new byte[ data.length + dataTypeByteLength * numReadChunk ];
				
		if( numReadChunk == 1 )
		{
			int i = 0;
			while (i < data.length)
			{
				aux[ i ] = data[ i ];
				
				i++;
			}
	
			for (int j = 0; j < this.mark.length; j++)
			{
				aux[ i ] = this.mark[ j ];
				i++;
			}
		}
		else
		{	
			int indexAux = 0;
			int count = super.lslChannelCounts;
			int N = super.lslChannelCounts * dataTypeByteLength;
			
			if( super.interleavedData )
			{				
				for( int j = 0; j < data.length; j += dataTypeByteLength )
				{	
					for( int k = 0; k < dataTypeByteLength; k++ )
					{
						aux[ indexAux ] = data[ j + k ];
						indexAux++;
					}
					
					count--;
					if( count == 0 )
					{
						indexAux += dataTypeByteLength;
						count = super.lslChannelCounts;
					}					
				}	
			}
			else // Sequential
			{				
				int dLen = numReadChunk * dataTypeByteLength;				
				
				for( int c = 0; c < this.lslChannelCounts; c++ )
				{					
					int indexA = c * dLen;
					int indexB = ( c + 1 ) * dLen;
					indexAux = c * dataTypeByteLength;
					
					for( int j = indexA; j < indexB; j += dataTypeByteLength )
					{
						for( int k = 0; k < dataTypeByteLength; k++ )
						{
							aux[ indexAux ] = data[ j + k ];
							indexAux++;							
						}
						
						indexAux += N;
					}
				}
			}
			
			for( int j =  N
					; j < aux.length
					; j += ( N + dataTypeByteLength ) )
			{
				for( int k = 0; k < this.NO_MARK.length; k++ )
				{
					aux[ j + k ] = this.NO_MARK[ k ];
				}
			}
			
			for(   int j = aux.length - dataTypeByteLength, k = 0
					 ; j < aux.length && k < this.mark.length
					 ; j++, k++ )
				{
					aux[ j ] = this.mark[ k ];
				}			
			
			/*
			byte[][] auxData = new byte[ super.lslChannelCounts + 1 ][ numReadChunk * dataTypeByteLength ];
			
			int iData = 0;
			for( int i = 0; i < auxData.length - 1; i++ )
			{
				for( int j = 0; j < auxData[ 0 ].length; j++ )
				{
					try
					{
					auxData[ i ][ j ] = data[ iData ];
					iData++;
					}
					catch( Exception e )
					{
						e.printStackTrace();
						throw new Exception( e ); 
					}
				}
			}			
			
			int i = super.lslChannelCounts;
			for( int j = 0; j < auxData[ 0 ].length - dataTypeByteLength; j += dataTypeByteLength )
			{
				for( int k = 0; k < this.NO_MARK.length; k++ )
				{
					auxData[ i ][ j + k ] = this.NO_MARK[ k ];
				}
			}
			
			for(   int j = auxData[ 0 ].length - dataTypeByteLength, k = 0
				 ; j < auxData[ 0 ].length && k < this.mark.length
				 ; j++, k++ )
			{
				auxData[ i ][ j ] = this.mark[ k ];
			}
			
			aux = new byte[ auxData.length * auxData[ 0 ].length ];
			int iAux = 0;
			for( int c = 0; c < auxData[ 0 ].length; c += dataTypeByteLength )
			{
				for( int j = 0; j < auxData.length; j++ )
				{
					for( int k = 0; k < dataTypeByteLength; k++ )
					{
						aux[ iAux ] = auxData[ j ][ c + k ];
						iAux++;
					}
				}
			}
			*/
		}
			
		this.out.write( aux );

		this.mark = this.NO_MARK;

		for( int iT = 0; iT < super.timeMark.length; iT++ )
		{
			this.outTimeStampFile.writeDouble( super.timeMark[ iT ] );		
		}
	}

	/*
	@Override
	protected void targetDone() throws Exception 
	{
		if( this.stopWhenTaskDone && this.mark.equals( this.NO_MARK ) )
    	{	
    		this.stopThread = true;
    		
    		super.interrupt();
    	}
	}
	*/
	
	protected void cleanUp() throws Exception
	{
		//this.mark = this.NO_MARK;

		super.cleanUp();
	}

	protected void postCleanUp() throws Exception
	{
		this.out.close();
		
		this.outTimeStampFile.close();
		
		
		eventInfo event = new eventInfo( eventType.OUTPUT_TEMPORAL_FILE_READY, this.getTemporalFileData() );

		/*
	if( (Boolean)ConfigApp.getProperty( ConfigApp.LSL_OUTPUT_TEMP_FILE_DELETE ) )
	{
		 */
		this.file.delete();
		this.timeStampFile.delete();
		/*
    }
		 */

		this.events.add(event);

		if (this.monitor != null)
		{
			this.monitor.taskDone(this);
		}
	}
	
	protected void notifyProblem(Exception e)
	{
		super.notifyProblem( e );
		
		if (this.syncMarkSem.availablePermits() < 1)
		{
			this.syncMarkSem.release();
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

	public void setOutputFileFormat( String fileFormat )
	{
		this.outFileFormat = fileFormat;
	}
		
	private TemporalData getTemporalFileData()
	{		
		List< Object > Data = new ArrayList< Object >();
		List< Double > Time = new ArrayList< Double >();

		BufferedInputStream din = null;

		//byte[] buf = new byte[ Float.BYTES ];
		byte[] buf = new byte[ this.NO_MARK.length ];

		try
		{
			din = new BufferedInputStream( new FileInputStream( this.file ) );
			switch( super.LSLFormatData ) 
			{
				case( LSL.ChannelFormat.double64 ):
				{
					while( din.read( buf ) > 0 )
					{
						Data.add( ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getDouble() );
					}
					break;
				}
				case( LSL.ChannelFormat.float32 ):
				{
					while( din.read( buf ) > 0 )
					{
						Data.add( ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getFloat() );
					}
					break;
				}
				case( LSL.ChannelFormat.int8 ):
				{
					while( din.read( buf ) > 0 )
					{
						Data.add( buf );
					}
					break;
				}
				case( LSL.ChannelFormat.int16 ):
				{
					while( din.read( buf ) > 0 )
					{
						Data.add( ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getShort() );
					}
					break;
				}
				case( LSL.ChannelFormat.int32 ):
				{
					while( din.read( buf ) > 0 )
					{
						Data.add( ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getInt() );
					}
					break;
				}
				case( LSL.ChannelFormat.int64 ):
				{
					while( din.read( buf ) > 0 )
					{
						Data.add( ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getLong() );
					}
					break;
				}
				default: // String
				{					
					while( din.read( buf ) > 0 )
					{
						Data.add( ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getChar() );
					}
					break;
				}
			}

			din.close();

			//buf = new byte[ Double.SIZE ];
			DataInputStream dinTime = new DataInputStream( new FileInputStream( this.timeStampFile ) );
			
			while( dinTime.available() > 0 )
			{
				//Time.add( (double)ByteBuffer.wrap( buf ).order( ByteOrder.BIG_ENDIAN ).getDouble() );
				Time.add( dinTime.readDouble() );
			}

			dinTime.close();

		}
		catch( EOFException ignore)
		{        	
		}
		catch (Exception ioe)
		{
		}
		finally
		{
			if( din != null )
			{
				try 
				{
					din.close();
				} 
				catch (IOException e) 
				{
				}
			}			
		}

		/*
		float[] out = new float[ Data.size() ];
		int i = 0;
		for( Float f : Data )
		{			
			out[ i ] = ( f != null ? f : Float.NaN );
			i++;
		}

		return out;
		 */

		TemporalData data = new TemporalData( Data, Time, super.LSLFormatData, super.lslChannelCounts
												, super.LSLName, super.lslXML, this.outFileName, this.outFileFormat );
		return data;
	}

	/*
	public void timeOver(String timerName)
	{
		System.out.println("TemporalOutputDataFile.timeOver()");
		this.stopThread( IStoppableThread.ForcedStop );
		super.notifyProblem(new TimeoutException("Waiting time for input data from device was exceeded."));		
	}
	*/

	public void reportClockTime(long time) 
	{}
}