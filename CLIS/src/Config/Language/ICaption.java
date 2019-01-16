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

public abstract interface ICaption
{
	/**
	 * 
	 * @param language: caption language. This must be contain in java.util.Locale.getISOLanguages()
	 * @return caption. If language is not in java.util.Locale.getISOLanguages(), then default caption is returned. 
	 */
	public String getCaption( String language );

	/**
	 * Set parameter caption for a language. This is added if there is not caption for this language.  
	 * @param language: parameter language
	 * @param Caption: parameter caption
	 * @throws IllegalArgumentException: If language is not in java.util.Locale.getISOLanguages().  
	 */
	public void setCaption( String Language, String Caption ) throws IllegalArgumentException;

	/**
	 * 
	 * @return parameter identifier
	 */
	public String getID();
}