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

import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.WarningMessage;
import Config.Language.ICaption;
import Controls.eventInfo;
import Controls.eventType;
import StoppableThread.AbstractStoppableThread;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public abstract class Activity extends AbstractStoppableThread implements IActivity
{
	protected static Activity task = null;

	public static final String ID_PARAMETER_X_AXIS = "X_AXIS";

	public static final String ID_PARAMETER_Y_AXIS = "Y_AXIS";

	public static final String ID_PARAMETER_WAIT_SOUND_END = "WAIT_SOUND_END";

	public static final String ID_PARAMETER_SLIDES = "SLIDES";
	public static final String ID_PARAMETER_SOUND_CLIPS = "SOUND_CLIPS";
	public static final String ID_PARAMETER_RANDOM_SAMPLES = "ID_PARAMETER_RANDOM_SAMPLES";
	public static final String ID_PARAMETER_PRESERVE_SLIDE_SOUND_ORDER = "ID_PARAMETER_PRESERVE_SLIDE_SOUND_ORDER";
	public static final String ID_PARAMETER_SLIDE_GROUP_MAIN = "ID_PARAMETER_SLIDE_GROUP_MAIN";
	public static final String ID_PARAMETER_SAM_DOMINANCE = "ID_PARAMETER_SAM_DOMINANCE";

	public static final String ID_ACTIVITY_PANEL_SIZE = "PANEL_SIZE";
	public static final String ID_PARAMETER_SAM_SET_SIZE = "SAM_SET_SIZE";
	public static final String ID_PARAMETER_SAM_VALENCE_RANGE = "ID_SAM_VALENCE_RANGE";
	public static final String ID_PARAMETER_SAM_AROUSAL_RANGE = "ID_SAM_AROUSAL_RANGE";
	public static final String ID_PARAMETER_SAM_DOMINANCE_RANGE = "ID_SAM_DOMINANCE_RANGE";
	public static final String ID_PARAMETER_SAM_EMOTION_SET = "ID_SAM_EMOTION_SET";
	public static final String ID_PARAMETER_SAM_BEEP = "ID_SAM_BEEP";
	
	
	public static final short ACTIVITY_PARAMETERS = 0;	

	protected int dificultad = 0;
	protected JPanel panelTask = null;

	protected boolean isEndedTask = false;

	private List<eventInfo> results = new ArrayList< eventInfo >();

	protected activityParameters parameters = new activityParameters();

	protected ITaskMonitor monitor;

	private boolean toWait = true;

	protected boolean activitySupend = false;

	protected int currentPhase = -1;

	protected ActivityInfo info = new ActivityInfo(1, 1, 1, true, true);

	protected boolean reportActive = false;

	protected boolean isShowActivityPhase = true;
	protected boolean isShowNextActivityPhase = true;

	protected String reportActivity = "-";
	protected String reportAnswer = "-";
	protected String reportHeader = "";

	protected String separator = " ";

	protected ICaption activityCaption;

	public ActivityInfo getActivityInfo()
	{
		return this.info;
	}

	public static void registerActivity() 
	{

	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#addParameter(short, java.lang.Object)
	 */
	@Override
	public void addParameter(short parameterID, Object parameter) throws Exception
	{
		setParameter(parameterID, parameter);
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#setParameter(short, java.lang.Object)
	 */
	@Override
	public void setParameter(short parameterID, Object parameter)
			throws Exception
	{
		this.parameters = ((activityParameters)parameter);

		if ( !this.parameters.getEnablePhases()[(this.info.getMainPhase() - 1)]  )
		{
			throw new IllegalArgumentException("Answer phase is disable");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#clearParameters()
	 */
	@Override
	public void clearParameters()
	{
		this.parameters = new activityParameters();
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.INotificationTask#taskMonitor(Auxiliar.Tasks.ITaskMonitor)
	 */
	@Override
	public void taskMonitor(ITaskMonitor m)
	{
		this.monitor = m;
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#startTask()
	 */
	@Override
	public void startTask()
			throws Exception
	{
		this.notifiedNewActivityPhase();
	}


	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#createTask()
	 */
	@Override
	public synchronized void createTask()
	{
		synchronized (this)
		{
			if (super.getState().equals(Thread.State.WAITING))
			{
				super.notify();
			}
			else
			{
				this.toWait = false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.INotificationTask#getResult()
	 */
	@Override
	public List<eventInfo> getResult()
	{
		return this.results;
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.INotificationTask#clearResult()
	 */
	@Override
	public void clearResult()
	{
		this.results.clear();
	}

	/*
	 * 
	 */
	protected void setResult(String type, Object result)
	{
		this.results.clear();
		this.results.add(new eventInfo(type, result));
	}

	/*
	 * (non-Javadoc)
	 * @see Activities.IActivity#stopActivity()
	 */
	@Override
	public void stopActivity()
	{
		if (this.panelTask != null)
		{
			this.panelTask.setVisible(false);
			this.panelTask.removeAll();
			this.panelTask = null;
			this.setPanelTask();
			this.currentPhase = -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#preStart()
	 */
	@Override
	protected void preStart()
			throws Exception
	{
		super.preStart();

		setPanelTask();

		if (this.reportActive)
		{
			this.reportHeader = ("Target" + this.separator + "Answer");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#runInLoop()
	 */
	@Override
	protected synchronized void runInLoop() throws Exception
	{
		if( this.toWait )
		{
			super.wait();			
		}

		try
		{			
			this.runActivity( this.parameters.getDifficultLevel() );
		}
		catch( Exception e )
		{		
			throw new Exception( e ); 
		}
		finally
		{
			this.toWait = true;
		}
	}

	@Override
	protected void runExceptionManager(Exception e)
	{
		this.stopThread = true;
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.Activities.IActivity#suspendActivity()
	 */
	@Override
	public void suspendActivity()
	{
		preSuspendActivity();

		this.activitySupend = true;

		if (this.panelTask != null)
		{
			this.panelTask.setVisible(false);
		}
	}

	public synchronized void resumeActivity()
	{
		try
		{
			int prevPhase = this.currentPhase;

			if ( this.nextStateActivity() )
			{
				this.activitySupend = false;

				if (this.panelTask != null)
				{
					this.panelTask.setVisible(true);
				}

				if (prevPhase != this.currentPhase)
				{
					this.notifiedNewActivityPhase();
				}
			}
			else
			{
				this.notifiedActivityEnd();
			}
		}
		catch (Exception e)
		{
			this.notifyActivityProblem(e);
		}
	}

	protected boolean nextStateActivity() throws Exception
	{
		boolean next = selectNextEnablePhase();
		if (next)
		{
			nextState();
		}

		return next;
	}


	protected void nextState() throws Exception
	{

	}

	@Override
	public boolean isActivitySupended()
	{
		return this.activitySupend;
	}

	@Override
	protected void preStopThread(int friendliness) throws Exception
	{
		task = null;
	}

	@Override
	protected void postStopThread(int friendliness) throws Exception
	{

	}

	protected void cleanUp() throws Exception
	{
		super.cleanUp();

		System.gc();
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.Activities.IActivity#isAdaptableTimer()
	 */
	@Override
	public boolean isAdaptableTimer()
	{
		return this.info.isAdaptableTimer();
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.Activities.IActivity#getNumberOfCurrentPhase()
	 */
	@Override
	public int getNumberOfCurrentPhase()
	{
		return this.currentPhase;
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.Activities.IActivity#getNumberOfPhases()
	 */
	@Override
	public int getNumberOfPhases()
	{
		return this.info.getPhaseNumber();
	}

	private void runActivity(int difficulty)
	{
		this.activitySupend = false;
		try
		{
			if ( this.selectNextEnablePhase() )
			{
				this.makeSpecificTask(difficulty);

				this.notifiedActivity();

				this.postMakeActivity(difficulty);
			}
			else
			{
				this.notifiedActivityEnd();
			}
		}
		catch (Exception e)
		{
			this.notifyActivityProblem(e);
		}
	}


	private void notifiedActivity()
	{
		if (this.monitor != null)
		{
			this.setResult( eventType.ACTIVITY_READY, this.panelTask);
			try
			{
				this.monitor.taskDone(this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void notifiedNewActivityPhase()
	{
		if (this.monitor != null)
		{
			this.setResult( eventType.ACTIVITY_NEW_PHASE, this.currentPhase);
			try
			{
				this.monitor.taskDone(this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	protected void notifiedActivityEnd()
	{
		if (this.monitor != null)
		{
			this.setResult( eventType.ACTIVITY_END, null);
			try
			{
				this.monitor.taskDone(this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	protected boolean selectNextEnablePhase()
	{
		boolean select = false;
		while ((this.hasNextPhase()) 
				&& (!select))
		{
			this.nextPhase();
			select = isEnableCurrentPhase();
		}

		return select;
	}

	protected void nextPhase()
	{
		this.currentPhase += 1;
	}

	protected boolean hasNextPhase()
	{
		return this.currentPhase < this.info.getPhaseNumber() - 1;
	}

	protected boolean isEnableCurrentPhase()
	{
		return this.parameters.getEnablePhases()[this.currentPhase];
	}


	protected void postMakeActivity(int difficutly) throws Exception
	{

	}

	protected void notifiedAnswer(boolean answer)
	{
		if (this.monitor != null)
		{
			this.setResult( eventType.ACTIVITY_ANSWER, answer );
			try
			{
				this.monitor.taskDone(this);
			}
			catch (Exception localException) {}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.Activities.IActivity#isFinished()
	 */
	@Override
	public boolean isFinished()
	{
		return this.isEndedTask;
	}

	/*
	 * (non-Javadoc)
	 * @see Activities.IActivity#isAnswerPhase()
	 */
	@Override
	public boolean isMainPhase()
	{
		return this.info.getMainPhase() - 1 == this.currentPhase;
	}

	public boolean isAnswerPhase()
	{
		return this.info.getAnswerPhase() - 1 == this.currentPhase;
	}

	private void notifyActivityProblem(Exception ex)
	{
		if (this.monitor != null)
		{
			this.setResult( eventType.ACTIVITY_PROBLEM, ex);
			try
			{
				this.monitor.taskDone(this);
			}
			catch (Exception e ) 
			{

			}
		}
	}

	public boolean hasReport()
	{
		return this.reportActive;
	}

	public String getReport()
	{
		return this.reportActivity + this.separator + this.reportAnswer;
	}

	public String getReportHeader()
	{
		return this.reportHeader;
	}

	public boolean isShowActivityPhase()
	{
		return this.isShowActivityPhase;
	}

	public boolean isShowNextActivityPhase()
	{
		return this.isShowNextActivityPhase;
	}


	public WarningMessage checkParameters()
	{
		WarningMessage msg = new WarningMessage();

		boolean ok = this.parameters != null;

		if (ok)
		{
			boolean[] ena = this.parameters.getEnablePhases();
			boolean enable = false;
			for (int i = 0; (i < ena.length) && (!enable); i++)
			{
				enable = ena[i];
			}

			ok = (ok) && (enable);

			if (!ok)
			{
				msg.setMessage( "All activity phases are disable.\n",  WarningMessage.ERROR_MESSAGE);
			}

			WarningMessage m = this.checkSpecifyParameters();
			msg.setMessage( msg.getMessage() + m.getMessage(), m.getWarningType());

		}
		else
		{
			msg.setMessage("Activity parameters not defined.", WarningMessage.ERROR_MESSAGE);
		}

		return msg;
	}

	protected abstract WarningMessage checkSpecifyParameters();

	protected abstract void preSuspendActivity();

	protected abstract void activityEnded();

	protected abstract void setPanelTask();

	protected abstract void makeSpecificTask(int dificultad )  throws Exception;
}