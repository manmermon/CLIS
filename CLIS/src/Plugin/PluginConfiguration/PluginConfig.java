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

package Plugin.PluginConfiguration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

public class PluginConfig 
{
	private static PluginConfig pCfg = null;
	
	private LinkedHashMap< String, PluginParameter > cfgList = null;	
	
	private PluginConfig()
	{
		this.cfgList = new LinkedHashMap< String, PluginParameter >();
	}
	
	public static PluginConfig getInstance()
	{
		if( pCfg == null )
		{
			pCfg = new PluginConfig();
		}
		
		return pCfg;
	}
	
	public void setConfigParameter( PluginParameter parameter )
	{		
		this.cfgList.put( parameter.getID(), parameter );
	}
		
	public Collection< PluginParameter > getParameters()
	{
		return this.cfgList.values();
	}

	public Set< String > getIdParameters()
	{
		return this.cfgList.keySet();
	}
	
	public PluginParameter getParameter( String id )
	{
		return this.cfgList.get( id );
	}
}
