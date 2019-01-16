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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PluginParameter 
{
	private String ID;
	private Object value = null;
	private HashMap< String, String > caption = null;
	
	private final String defaultCaptionID = "defaultCaption";
	
	/**
	 * Create a parameter
	 * 
	 * @param id: parameter identifier. This is used as default caption.
	 * @param defaultValue: parameter value.
	 * @throws IllegalArgumentException: 
	 * 			if id == null || id.trim().isempty() 
	 * 				|| defaultValue == null || !AdmitedType.checkType( defaultValue.getClass() ) 
	 */
	public PluginParameter( String id, Object defaultValue ) throws IllegalArgumentException
	{
		if( defaultValue == null
				|| !AdmitedType.checkType( defaultValue.getClass() ) )
		{
			throw new IllegalArgumentException( "Parameter type is not correct. Admited type: " 
												+ AdmitedType.getAdmitedType() );
		}
				
		this.setID( id );
		
		this.value = defaultValue;
		
		this.caption = new HashMap<String, String>();
		this.caption.put( this.defaultCaptionID, id );
	}
	
	/**
	 * 
	 * @param language: caption language. This must be contain in java.util.Locale.getISOLanguages()
	 * @return caption. If language is not in java.util.Locale.getISOLanguages(), then default caption is returned. 
	 */
	public String getCaption( String language )
	{
		String lbl = this.caption.get( this.defaultCaptionID );
		boolean ok = false;
		
		for( String lang : Locale.getISOLanguages() )
		{
			ok = lang == language;
			
			if( ok )
			{
				lbl = this.caption.get( language );
				break;
			}
		}
		
		return lbl;
	}
	
	/**
	 * Set parameter caption for a language. This is added if there is not caption for this language.  
	 * @param language: parameter language
	 * @param Caption: parameter caption
	 * @throws IllegalArgumentException: If language is not in java.util.Locale.getISOLanguages().  
	 */
	public void setCaption( String Language, String Caption ) throws IllegalArgumentException
	{
		boolean ok = false;
		
		for( String lang : Locale.getISOLanguages() )
		{
			ok = lang == Language;
			
			if( ok )
			{
				this.caption.put( Language, Caption );
				break;
			}
		}
		
		if( !ok )
		{
			throw new IllegalArgumentException( "Caption language is not in java.util.Locale.getISOLanguages()." );
		}
	}
	
	/**
	 * Set parameter identifier.
	 * 
	 * @param id String with parameter identifier
	 * @throws IllegalArgumentException: id is null or empty
	 */
	public void setID( String id ) throws IllegalArgumentException
	{
		if( id == null 
				|| id.trim().isEmpty() )
		{
			throw new IllegalArgumentException( "Parameter ID is null or empty." );
		}
		
		this.ID = id;
	}
	
	/**
	 * 
	 * Set parameter value
	 * 
	 * @param newValue: new value
	 * @return previous value
	 * @throws ClassCastException: Class new value is different
	 */
	public Object setValue( Object newValue ) throws ClassCastException
	{
		String parClass = this.value.getClass().getCanonicalName(); 
		if( !parClass.equals( newValue.getClass().getCanonicalName() ) )
		{
			throw new ClassCastException( "New value class is different to parameter class: " 
												+ parClass );
		}
		
		Object prev = this.value;
		this.value = newValue;
		
		return prev;		
	}

	/**
	 * 
	 * @return parameter identifier
	 */
	public String getID()
	{
		return this.ID;
	}
	
	/**
	 * 
	 * @return parameter value
	 */
	public Object getValue()
	{
		return this.value;
	}

	///////////////////////////////
	//
	// Check type parameters
	//
	public static class AdmitedType
	{
		private static List< String > admitedType = null;
		
		static
		{
			admitedType = new ArrayList< String >();
			admitedType.add( String.class.getCanonicalName() );
			admitedType.add( Boolean.class.getCanonicalName() );
			admitedType.add( Byte.class.getCanonicalName() );
			admitedType.add( Short.class.getCanonicalName() );
			admitedType.add( Integer.class.getCanonicalName() );
			admitedType.add( Long.class.getCanonicalName() );
			admitedType.add( Float.class.getCanonicalName() );
			admitedType.add( Double.class.getCanonicalName() );
		}
		
		public static boolean checkType( Class type )
		{
			boolean ok = false;
			
			if( type != null )
			{
				ok = admitedType.contains( type.getCanonicalName() );
			}
						
			return ok;
		}
		
		public static String getAdmitedType()
		{
			return admitedType.toString();
		}
	}
}
