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
package Config.Language;

import java.util.HashMap;
import java.util.Locale;

public class Caption implements ICaption 
{
	private final String defaultCaptionID;
		
	private HashMap< String, String > caption = null;
	
	private String ID;
	
	/**
	 * 
	 * @param captionID: caption ID
	 * @param defaultLanguage: default language id. It must not be in java.util.Locale.getISOLanguages(). 
	 * @param txt: default caption
	 * @throws IllegalArgumentException: If defaultLanguage or captionID is empty or null. 
	 */
	public Caption( String captionID, String defaultLanguage, String txt ) throws IllegalArgumentException
	{
		checkLanguage( captionID );
		checkLanguage( defaultLanguage );
		
		for( String lang : Locale.getISOLanguages() )
		{
			if( lang.equals( defaultLanguage ) )
			{
				throw new IllegalArgumentException( "Default language Id is in java.util.Locale.getISOLanguages()." );
			}			
		}
		
		this.caption = new HashMap< String, String >();
		
		this.ID = captionID;
		
		this.defaultCaptionID = defaultLanguage;		
		
		if( txt == null )
		{
			txt = "";
		}
		
		this.caption.put( defaultLanguage, txt );
	}
	
	/**
	 * 
	 * @param language: caption language.
	 * @return caption. If language is not registered, then default caption is returned. 
	 */
	public String getCaption( String language )
	{
		if( language == null )
		{
			language = this.defaultCaptionID;
		}
		
		language = language.trim();
		
		String lbl = this.caption.get( language );
		
		return lbl;
	}
	
	/**
	 * Set parameter caption for a language. This is added if there is not caption for this language.  
	 * @param language: parameter language
	 * @param Caption: parameter caption
	 * @throws IllegalArgumentException: If language is not in java.util.Locale.getISOLanguages() or is empty/null.  
	 */
	public void setCaption( String Language, String Caption ) throws IllegalArgumentException
	{
		checkLanguage( Language );
		
		boolean ok = false;
		
		Language = Language.trim();
		
		for( String lang : Locale.getISOLanguages() )
		{
			ok = lang.equals( Language );
			
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
	 * Check language id
	 * 
	 * @param id String with parameter identifier
	 * @throws IllegalArgumentException: id is null or empty
	 */
	private void checkLanguage( String id ) throws IllegalArgumentException
	{
		if( id == null 
				|| id.trim().isEmpty() )
		{
			throw new IllegalArgumentException( "Input parameter is null or empty." );
		}
	}
	

	/**
	 * 
	 * @return parameter identifier
	 */
	public String getID()
	{
		return this.ID;
	}
	
	public void setID( String id )
	{
		this.ID = id;
	}
}
