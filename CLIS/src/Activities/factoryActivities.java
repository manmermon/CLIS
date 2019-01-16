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

package Activities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class factoryActivities
{
	public static Activity getActivity(String task)
	{
		Activity t = null;

		try
		{
			Class act = ActivityRegister.getActivityClass(task);
			Method instance = act.getMethod( "getInstance" );

			t = (Activity)instance.invoke( null );
		}
		catch (NoSuchMethodException localNoSuchMethodException) 
		{}
		catch (SecurityException localSecurityException) 
		{}
		catch (IllegalAccessException localIllegalAccessException) 
		{}
		catch (InvocationTargetException localInvocationTargetException) 
		{}


		/*
	if( task.equals( IActivity.OpMemoria ) )
	{ 	
		t = MemoryTask.getInstance();			
	}
	else if( task.equals( IActivity.OpStroopTest ) )
	{
		t = StroopActivity.getInstance();
	}
	else if( task.equals( IActivity.OpBuscaYcuenta ) )
	{	
		t = SearchCountTask.getInstance();			
	}
	else if( task.equals( IActivity.OpUnirPuntos ) )
	{
		t = SortedDotsTask.getInstance();
	}
	else if( task.equals( IActivity.OpTrajectory ) )
	{
		t = TrajectoryTask.getInstance();
	}
	else if( task.equals( IActivity.OpAffective ) )
	{
		t = AffectiveActivity.getInstance();
	}
	else if( task.equals( IActivity.OpSAM ) )
	{
		t = SAM_Test.getInstance();
	}
	else
	{
		t = AritmeticActivity.getInstance();
	}
		 */

		return t;
	}
}