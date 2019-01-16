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

package Config;

import Config.Language.ICaption;

public abstract interface IParameter extends ICaption
{
	/**
	 * 
	 * Set parameter value
	 * 
	 * @param newValue: new value
	 * @return previous value
	 * @throws ClassCastException: Class new value is different
	 */
	public abstract Object setValue(Object paramObject) throws ClassCastException;

	/**
	 * 
	 * @return parameter value
	 */
	public abstract Object getValue();
}