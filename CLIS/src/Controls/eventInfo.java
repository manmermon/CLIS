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

public class eventInfo
{
	private String eventType = "";
	private Object eventInformation;

	public eventInfo(String type, Object result)
	{
		this.eventType = type;
		this.eventInformation = result;
	}

	public String getEventType()
	{
		return this.eventType;
	}

	public Object getEventInformation()
	{
		return this.eventInformation;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = obj instanceof eventInfo;

		if (equal)
		{
			eventInfo inObj = (eventInfo)obj;

			equal = (inObj.getEventType().equals(this.eventType)) 
					&& (inObj.equals(this.eventInformation));
		}

		return equal;
	}

	@Override
	public String toString()
	{
		return "[" + this.eventType + " -> " + this.eventInformation + "]";
	}
}