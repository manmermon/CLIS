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
package Controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamingParameters;

public class streamInformations 
{
	private List< streamingParameters > clientSocketInfos = null;
	private streamingParameters serverSocketInfo = null;
	private Map< String, String > inputCmds = null;
	private Map< String, Map< String, List< streamSocketInfo > > > outputCmds = null;
	
	public streamInformations() 
	{
		this.clientSocketInfos = new ArrayList< streamingParameters >();
		this.inputCmds = new HashMap< String, String >();
		this.outputCmds = new HashMap< String, Map< String, List< streamSocketInfo > > >();
	}

	public List< streamingParameters > getClientsInformation()
	{
		return this.clientSocketInfos;
	}
	
	public streamingParameters getServerInformation()
	{
		return this.serverSocketInfo;
	}
	
	public Map< String, String > getInputCommands()
	{
		return this.inputCmds;
	}
	
	public Map< String, Map< String, List< streamSocketInfo > > > getOutputCommands()
	{
		return this.outputCmds;
	}
	
	public void setServerInformation( streamingParameters info )
	{
		this.serverSocketInfo = info;
	}
	
	public void setClientInformations( int pos, streamingParameters info )
	{
		this.clientSocketInfos.set( pos, info );
	}
	
	public void addClientInformations( streamingParameters info )
	{
		this.clientSocketInfos.add( info );
	}
	
	public void addInputCommands( String textCommand, String commandType )
	{
		this.inputCmds.put( textCommand, commandType  );
	}
	
	public void putOutputCommand( String commandType, String commandText, List< streamSocketInfo > socketInfos )
	{
		if( socketInfos == null )
		{
			throw new IllegalArgumentException( "Socket information null." );
		}
		
		Map< String, List< streamSocketInfo > > cmd = new HashMap<String, List<streamSocketInfo>>( );
		cmd.put( commandText, socketInfos );
		
		this.outputCmds.put( commandType, cmd );
	}	
	
	public void addOutputCommand( String commandType, String commandText, List< streamSocketInfo > socketInfos )
	{
		if( socketInfos == null )
		{
			throw new IllegalArgumentException( "Socket information null." );
		}
		
		Map< String, List< streamSocketInfo > > cmd = this.outputCmds.get( commandType );
		
		if( cmd == null )
		{
			this.putOutputCommand( commandType, commandText, socketInfos );
		}
		else
		{
			List< streamSocketInfo > ss = cmd.get( commandText );
			if( ss == null )
			{
				ss = new ArrayList< streamSocketInfo >();
				cmd.put( commandText, ss );
			}
			
			ss.addAll( socketInfos );
		}
	}
	
	public void addOutputCommand( String commandType, String commandText, streamSocketInfo socketInfo )
	{
		if( socketInfo == null )
		{
			throw new IllegalArgumentException( "Socket information null." );
		}
		
		Map< String, List< streamSocketInfo > > cmd = this.outputCmds.get( commandType );
		
		if( cmd == null )
		{
			List< streamSocketInfo > socketInfoList = new ArrayList< streamSocketInfo >();
			socketInfoList.add( socketInfo );
			this.putOutputCommand( commandType, commandText, socketInfoList );
		}
		else
		{
			List< streamSocketInfo > ss = cmd.get( commandText );
			if( ss == null )
			{
				ss = new ArrayList< streamSocketInfo >();
				cmd.put( commandText, ss );
			}
			
			ss.add( socketInfo );
		}
	}
}
