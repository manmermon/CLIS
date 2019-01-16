/*
 * Copyright 2011-2018 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General public static License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General public static License for more details.
 *
 *   You should have received a copy of the GNU General public static License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package Deprecated_Version.Auxiliar;


public class LSLConfigParameters
{
	public static final String ID_EXTRA_INFO_LABEL = "extra";
	
	private String uid;
	private String deviceName;
	private String deviceType;
	private String additionalInfo;
	private boolean selectedDevice;
	private String source_id;
	private int chunckSize = 1;
	private boolean interleavedData = false;
	
	public LSLConfigParameters( String uid, String name, String type )
	{
		this( uid, name, type, "", name + type, false );
	}
	
	public LSLConfigParameters( String uid, String name, String type, String sourceID )
	{
		this( uid, name, type, sourceID, "", false );
	}
	
	public LSLConfigParameters( String uid, String name, String type, String sourceID, String info )
	{
		this( uid, name, type, sourceID, info, false );
	}
	
	public LSLConfigParameters( String uid, String name, String type, String sourceID, String info, boolean selected )
	{
		this( uid, name, type, sourceID, info, selected, 1 );
	}
	
	public LSLConfigParameters( String uid, String name, String type, String sourceID, String info, boolean selected, int chunckSize )
	{
		this( uid, name, type, sourceID, info, selected, chunckSize, false );
	}
	
	public LSLConfigParameters( String uid, String name, String type, String sourceID, String info
								, boolean selected, int chunckSize, boolean interleaved )
	{
		this.uid = uid;
		this.deviceName = name;
		this.deviceType = type;
		this.additionalInfo = info;
		this.selectedDevice = selected;
		this.source_id = sourceID;
		this.chunckSize = chunckSize;
		this.interleavedData = interleaved;
	}

	public String getDeviceName()
	{
		return this.deviceName;
	}

	public void setDeviceName( String name )
	{
		this.deviceName = name;
	}

	public String getDeviceType()
	{
		return this.deviceType;
	}

	public void setDeviceType( String type )
	{
		this.deviceType = type;
	}

	public String getAdditionalInfo()
	{
		return this.additionalInfo;
	}

	public void setAdditionalInfo( String info )
	{
		this.additionalInfo = info;
	}
	
	public boolean isSelected()
	{
		return this.selectedDevice;
	}
	
	public void setSelected( boolean select )
	{
		this.selectedDevice = select;
	}
	
	public String getUID()
	{
		return this.uid;
	}	
	
	public String getSourceID()
	{
		return this.source_id;
	}
	
	public int getChunckSize()
	{
		return this.chunckSize;
	}
	
	public void setChunckSize( int size )
	{
		this.chunckSize = size;		
		
		if( this.chunckSize < 1 )
		{
			this.chunckSize = 1;
		}
	}
	
	public boolean isInterleavedData()
	{
		return this.interleavedData;
	}
	
	public void setInterleaveadData( boolean interleaved )
	{
		this.interleavedData = interleaved;
	}
	
	public String toString()
	{
		return "<" + source_id + ", " + deviceName + ", " + deviceType + ", " + additionalInfo + ", " + selectedDevice + ", " + chunckSize + ", " + this.interleavedData + ">";
	}	
	
	@Override
	public int hashCode() 
	{		
		return ( deviceName + deviceType ).hashCode();
	}
}
