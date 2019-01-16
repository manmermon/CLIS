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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import StoppableThread.IStoppableThread;
import Activities.Activity;
import Activities.ActivityRegister;
import Activities.IActivity;
import Activities.activityParameters;
import Activities.factoryActivities;
import Auxiliar.WarningMessage;
import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;

public class activityControl extends controlOfSubordinateTemplate implements ITaskMonitor
{
	private static activityControl ctrActivity = null;

	public static final String PREPARE_ACTIVITY = "prepare";
	public static final String START_OR_CONTINUE_ACTIVITY = "start";

	private boolean stop = false;

	private String activityType = "";
	private String activityName = "";
	private String prevActivityName = "";

	//private int activityIndex = -1;

	private activityParameters activityPars = null; 

	private int activityState;

	private Activity activity = null;

	private int repetitions = -1;

	private activityControl() 
	{
		this.activityState = IActivity.ACTIVITY_STATE_WAIT_START;

		this.setName( this.getClass().getSimpleName() );

		ActivityRegister.LoadPredeterminateActivities();
		ActivityRegister.searchPlugin();
	}

	public static activityControl getInstance()
	{
		if( ctrActivity == null )
		{
			ctrActivity = new activityControl();
		}

		return ctrActivity;
	}	

	@Override
	public synchronized void run() 
	{
		while( !this.stop )
		{
			try 
			{
				super.wait();

				if( this.repetitions != 0 )
				{
					if( this.repetitions > 0 )
					{
						this.repetitions--;
					}

					boolean newActivity = false;

					synchronized ( activityName )
					{						
						this.prevActivityName = this.activityName;	

						this.activityName = this.activityType;						
						/*
						if( this.activityType.equals( IActivity.opRandom ) )
						{
							this.activityIndex = ThreadLocalRandom.current().nextInt( 0, IActivity.numberActivities );
							newActivity = true;
						}
						else if( this.activityType.equals( IActivity.opTodas ) )
						{
							this.activityIndex++;
							newActivity = true;
						}

						if( this.activityIndex >= IActivity.numberActivities )
						{
							this.activityIndex = 0;
						}

						if( this.activityIndex >= 0 )
						{
							this.activityName = IActivity.activities[ this.activityIndex ];
						}
						 */
					}

					if( this.activity != null )
					{
						this.activity.stopActivity();						
					}
					else
					{
						newActivity = true;
					}

					if( newActivity )
					{
						if( this.activity != null )
						{
							this.activity.stopThread( IStoppableThread.ForcedStop );
						}

						this.activity = factoryActivities.getActivity( this.activityName );

						this.activity.taskMonitor( this );

						try
						{
							this.activity.startThread();
						}
						catch( Exception e )
						{
							e.printStackTrace();
						}

						try
						{
							this.activity.addParameter( Activity.ACTIVITY_PARAMETERS, this.activityPars );
						}
						catch( Exception e )
						{
							super.event = new eventInfo( eventType.ACTIVITY_PROBLEM, e );
							super.supervisor.eventNotification( this, super.event );
						}
					}									

					this.activity.createTask();
				}
				else
				{
					super.event = new eventInfo( eventType.TEST_END, null );					
					super.supervisor.eventNotification( this, super.event );
				}
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			finally
			{				
			}
		}
	}

	@Override
	protected synchronized void startWork( Object info ) 
	{
		if( info.toString().equals( PREPARE_ACTIVITY ) )
		{
			super.notify();			
		}
		else
		{
			try 
			{
				if( this.activity != null )
				{
					if( this.activity.isActivitySupended() )
					{
						this.activity.resumeActivity();
					}
					else
					{
						this.activity.startTask();
					}
				}
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get activity parameters.
	 * 
	 * Not create subordiante threads.
	 */
	@Override
	protected List< IStoppableThread > createSubordinates( Map<String, Object> parameters ) 
	{
		if( parameters == null 
				|| parameters.isEmpty() )
		{
			throw new IllegalArgumentException( "Parameters are null or empty." );
		}

		this.activityType = parameters.keySet().iterator().next();

		/*
		if( !Arrays.asList( IActivity.activities).contains( this.activityType ) 
				&& !IActivity.OpAffective.equals( this.activityType ) )
		 */
		if( !Arrays.asList( ActivityRegister.getActivitiesID() ).contains( this.activityType ) ) 
			//&& !IActivity.OpAffective.equals( this.activityType ) )
		{
			throw new IllegalArgumentException( "The activity type=" + this.activityType + " is not correct. Check the interface IActivity" );
		}

		this.activityPars = (activityParameters)parameters.get( this.activityType );

		//this.activityIndex = -1;
		this.repetitions = this.activityPars.getRepetitions();

		return new ArrayList<IStoppableThread>( );
	}

	/*
	 * (non-Javadoc)
	 * @see Controls.controlOfSubordinateTemplate#deleteSubordinates(int)
	 */
	@Override
	protected void cleanUpSubordinates() 
	{
		if( this.activity != null )
		{
			this.activity.stopActivity();
			this.activity.stopThread( IStoppableThread.ForcedStop );
			this.activity = null;

			//this.activityIndex = -1;
			this.activityName = "";
			this.activityPars = null;
			this.activityState = IActivity.ACTIVITY_STATE_END;
			this.activityType = "";
			if( this.activityPars != null )
			{
				this.repetitions = this.activityPars.getRepetitions();
			}
		}
	}

	@Override
	protected synchronized void preToWorkSubordinates() throws Exception 
	{
		// TODO Auto-generated method stub
		super.preToWorkSubordinates();
	}

	/**
	 * 
	 * @return 
	 */
	/*
	public boolean activityRequestPreAnswerTimer()
	{
		boolean request = false;

		if( this.activity != null )
		{
			request = this.activity.requestPreTimer();
		}

		return request;
	}
	 */
	/**
	 * 
	 * @return
	 */
	public boolean activityWithAdaptableTimer()
	{
		boolean adapt = true;

		if( this.activity != null )
		{
			adapt = this.activity.isAdaptableTimer();
		}

		return adapt;
	}

	public int getActivityState()
	{
		return this.activityState;
	}

	@Override
	public void taskDone( INotificationTask task ) 
	{		
		this.event = task.getResult().get( 0 );

		if( this.event.getEventType().equals( eventType.ACTIVITY_ANSWER ) )
		{
			//this.activity.stopActivity();
			this.suspendActivity();
		}
		else if( this.event.getEventType().equals( eventType.ACTIVITY_PROBLEM ) )
		{
			this.suspendActivity();
		}

		super.supervisor.eventNotification( this, this.event );
	}

	public String getActivityType()
	{
		synchronized ( this.activityName )
		{
			return this.activityName;
		}
	}

	public int getNumberOfCurrentPhase() 
	{
		if( this.activity == null )
		{
			throw new NullPointerException( "Activity has not been created.");
		}

		return this.activity.getNumberOfCurrentPhase();
	}

	public int getNextPhase()
	{
		if( this.activity == null )
		{
			throw new NullPointerException( "Activity has not been created.");
		}

		int phase = this.activity.getNumberOfCurrentPhase() + 1;
		if( phase >= this.activity.getNumberOfPhases() )
		{
			phase = 0;
		}

		return phase;
	}

	public void stopActivity()
	{
		if( this.activity != null )
		{
			this.activity.stopActivity();
		}
	}

	public void suspendActivity()
	{
		if( this.activity != null )
		{
			this.activity.suspendActivity();
		}
	}

	public void resumeActivity()
	{
		if( this.activity != null )
		{
			this.activity.resumeActivity();;
		}
	}

	public boolean isActivityFinished()
	{
		boolean end = true;

		if( this.activity != null )
		{
			end = this.activity.isFinished();
		}

		return end;
	}

	public boolean isLastActivityPhase()
	{
		return ( this.activity  == null || this.activity.getNumberOfCurrentPhase() == ( this.activity.getNumberOfPhases() - 1 ) );
	}

	public boolean isAnswerPhase()
	{
		boolean ans = false;

		if( this.activity != null )
		{
			ans = this.activity.isAnswerPhase();
		}

		return ans;
	}

	public boolean isShowActivityPhase()
	{
		return this.activity.isShowActivityPhase();
	}

	public boolean isShowNextActivityPhase()
	{
		return this.activity.isShowNextActivityPhase();
	}

	public boolean isMainPhase()
	{
		return this.activity.isMainPhase();
	}

	public boolean isActivityChanged()
	{
		synchronized ( this.activityName )
		{
			return !this.prevActivityName.equals( this.activityName );
		}
	}

	@Override
	public void setSubordinates(String activityID, Object parameters) throws Exception 
	{
		if( parameters == null )
		{
			throw new IllegalArgumentException( "Parameters are null." );
		}

		this.activityType = activityID;

		//if( !Arrays.asList( Activities.IActivity.activities).contains( this.activityType ) )
		if( !Arrays.asList( ActivityRegister.getActivitiesID() ).contains( this.activityType ) )
		{
			throw new IllegalArgumentException( "The activity type=" + this.activityType + " is not correct. Check the interface IActivity" );
		}

		this.activityPars = (activityParameters)parameters;

		//this.activityIndex = -1;
	}

	public String getActivityReport()
	{
		String report = "";

		if( this.activity != null )
		{
			report = this.activity.getReport();
		}

		return report;
	}

	public String getActivityReportHeader()
	{
		String header = "";

		if( this.activity != null )
		{
			header = this.activity.getReportHeader();
		}

		return header;
	}

	@Override
	public WarningMessage checkParameters()
	{
		WarningMessage msg = new WarningMessage();

		Activity act = factoryActivities.getActivity(this.activityPars.getActivityType());

		try
		{
			act.addParameter((short)0, this.activityPars);
			act.createTask();

			WarningMessage m = act.checkParameters();
			msg.setMessage(m.getMessage(), m.getWarningType());

			act.stopThread( IStoppableThread.ForcedStop );
		}
		catch (Exception e)
		{
			msg.setMessage(e.getMessage(), WarningMessage.ERROR_MESSAGE );
		}

		return msg;
	}
}
