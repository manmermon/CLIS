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

import GUI.guiLSLDataPlot;
import edu.ucsd.sccn.LSL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import Deprecated_Version.Auxiliar.LSLConfigParameters;

public class outputDataPlot extends readInputData
{
	private guiLSLDataPlot plot;
	private int nByteData = 1;

	public outputDataPlot(guiLSLDataPlot Plot, LSL.StreamInfo info, LSLConfigParameters lslCfg ) throws Exception
	{
		super( info, lslCfg );

		if (Plot == null)
		{
			throw new IllegalArgumentException( "Plot is null." );
		}

		this.plot = Plot;
		this.plot.setPlotName( info.name() );

		this.plot.setVisible(true);
	}

	protected int createArrayData() throws Exception
	{
		this.nByteData = super.createArrayData();

		return this.nByteData;
	}

	protected void managerData( byte[] data ) throws Exception
	{
		List< List< Double > > d = new ArrayList< List< Double > >();

		byte[] aux = new byte[ this.nByteData ];
		int numReadChunk = ( data.length / this.nByteData ) / super.lslChannelCounts;

		List< Double > DATA = new ArrayList< Double >();
		
		if( !super.interleavedData )
		{		
			for (int i = 0; i < data.length; i += this.nByteData)
			{	
				if (data.length - i >= this.nByteData)
				{
					for (int j = 0; j < this.nByteData; j++)
					{
						aux[ j ] = data[ i + j ];
					}
					 
					DATA.add( this.getDataValue( aux ) );
					//System.out.print( DATA + " " );				
				}
	
				if( DATA.size() >= numReadChunk )
				{
					d.add( DATA );
					DATA = new ArrayList< Double >();
				}			
			}
		}
		else
		{
			int N = super.lslChannelCounts * this.nByteData;
			for( int c = 0; c <= ( N - this.nByteData ) ; c += this.nByteData )
			{
				for( int j = c; j < data.length; j += N )
				{
					for( int k = 0; k < this.nByteData; k++ )
					{
						aux[ k ] = data[ j + k ];
					}					
					
					DATA.add( this.getDataValue( aux ) );
				}
				
				d.add( DATA );
				DATA = new ArrayList< Double >();
			}
		}
		
		if( DATA.size() > 0 )
		{			
			d.add( DATA );
		}
		//System.out.println("outputDataPlot.managerData() " + numReadChunk + " -> " + d);		
		this.plot.addXYData( d );
	}
	
	private double getDataValue( byte[] aux )
	{
		double out = 0D;
		
		switch( super.LSLFormatData ) 
		{
			case( LSL.ChannelFormat.double64 ):
			{
				out = ByteBuffer.wrap(aux).getDouble();
				break;
			}
			case( LSL.ChannelFormat.float32 ):
			{
				out = new Double( ByteBuffer.wrap(aux).getFloat() );
				break;
			}
			case( LSL.ChannelFormat.int8 ):
			{
				out = new Double( ByteBuffer.wrap(aux).get() );
				break;
			}
			case( LSL.ChannelFormat.int16 ):
			{
				out = new Double( ByteBuffer.wrap(aux).getShort() );
				break;
			}
			case( LSL.ChannelFormat.int32 ):
			{
				out = new Double( ByteBuffer.wrap(aux).getInt() );
				break;
			}
			case( LSL.ChannelFormat.int64 ):
			{
				out = new Double( ByteBuffer.wrap(aux).getLong() );
				break;
			}
			default: // String
			{					
				
			}
		}
		
		return out;
	}

	protected void postCleanUp() throws Exception
	{}
}