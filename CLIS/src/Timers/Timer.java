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

package Timers;

import java.awt.Toolkit;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.Tasks.beepPlayTask;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public class Timer extends AbstractStoppableThread implements ITimer, ITaskMonitor
{
	private ITimerMonitor monitor = null;
	
	//private List< Long > timerTimeList = new ArrayList< Long >();
	//private int timerIDx = 0;
	
	private long timerTime = 0L;
	private boolean stopTimer = false;
	private long timeSleep = 10L; //10 milisecond
	
	private boolean activedBeep = true;
	private long startedBeep = 10 * 1000L; //10s = 10000ms
	private long beepTime = 1000L; //1s
	private beepPlayTask beepThread = null;
	
	private boolean activedReportTime = false;
	
	private long timerCount = 0;
	
	private boolean wait = true;
	private boolean interruption = false;
	private boolean isWorking = false;
			
	@Override
	public void setTimerMonitor( ITimerMonitor m ) 
	{
		this.monitor = m;
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#startUp()
	 */
	@Override
	protected void startUp() throws Exception
	{	
		if( this.activedBeep )
		{
			this.beepThread = new beepPlayTask();
			this.beepThread.taskMonitor( this );
			this.beepThread.startThread();		
		}
				
		//this.timerIDx = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#runInLoop()
	 */
	@Override
	protected void runInLoop() throws Exception  
	{	
		//this.timerTime = this.timerTimeList.get( this.timerIDx );
		
		this.timerCount = System.currentTimeMillis();
		long delay = 0L;
		long tSleep = 0L;
		long tBeep = 0L;		
		this.isWorking = true;
		this.wait = true;
		
		//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.runInLoop() TIEMPO="+timerTime);
		while( (System.currentTimeMillis() - this.timerCount) < this.timerTime && !this.stopTimer && !this.interruption )
		{				
			tSleep = this.timeSleep - delay;
			if( tSleep > 0 )
			{
				super.sleep( tSleep );
				
				tBeep -= tSleep;
			}			
				
			delay = System.currentTimeMillis(); //To mesaure delay
			
			if( this.activedReportTime  )
			{	
				this.monitor.reportClockTime( System.currentTimeMillis()- timerCount );
			}	
				
			if( this.beepThread != null 
					&& this.timerTime - (System.currentTimeMillis() - this.timerCount) <= this.startedBeep 
					&& tBeep <= 0 )
			{
				this.beepThread.startTask();
				tBeep = this.beepTime;
			}
				
			delay = System.currentTimeMillis() - delay;	
		}		
	}
	
	@Override
	protected void targetDone() throws Exception 
	{
		if( super.stopWhenTaskDone )
		{
			this.wait = false;
			super.stopThread = true;
		}
		
		if( !this.stopTimer )
		{
			if( !this.interruption
					&& ( System.currentTimeMillis() - this.timerCount ) >= this.timerTime 
					&& this.timerTime > 0 )
			{
				//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.targetDone() TIME OVER NOTIFY ");
				/*
				ThreadMXBean bean = ManagementFactory.getThreadMXBean();
				if( this.monitor != null )
				{
					String infor = ""+bean.getThreadInfo( ((Thread)this.monitor).getId() );
					//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.targetDone( 1 ) "+infor);
				}
				*/
				
				this.monitor.timeOver( super.getName() );
				
				//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.targetDone( 2 ) TIME OVER NOTIFIED");
			}
			
			this.interruption = false;
			
			if( this.wait )
			{
				this.isWorking = false;
				//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.targetDone( 3 ) WAIT");
				super.wait();
			}			
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#runExceptionManager(java.lang.Exception)
	 */
	@Override
	protected void runExceptionManager(Exception e) 
	{
		if( !( e instanceof InterruptedException ) )
		{
			e.printStackTrace();
			super.stopThread = true;
		}
		else
		{
			//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.runExceptionManager()");
		}
	}
	
	public void interruptTimer()
	{
		if( this.isWorking )
		{
			this.interruption = true;
			super.interrupt();			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#preStopThread()
	 */
	@Override
	protected void preStopThread(int friendliness) 
	{	
		this.stopTimer = true;
		
		if( this.beepThread != null )
		{
			this.beepThread.stopThread( IStoppableThread.ForcedStop );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#cleanUp()
	 */
	@Override
	protected void cleanUp()
	{
		this.beepThread = null;
		
		//this.timerIDx = 0;
		
		//this.clearTimerValues();
		
		this.isWorking = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITaskMonitor#taskDone()
	 */
	@Override
	public void taskDone( INotificationTask t ) 
	{		
	}

	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#postStopThread()
	 */
	@Override
	protected void postStopThread(int friendliness) 
	{
		this.wait = false;
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#resumeTimer()
	 */
	@Override
	public void restartTimer() 
	{			
		if( this.getState().equals( State.WAITING ) )
		{	
			synchronized( this )
			{				
				super.notify();
			}
		}
		else
		{
			//System.out.println((System.nanoTime()-coreControl.tiempo)+"-Timer.restartTimer() 4");
			super.interrupt();
		}		
	}
	
	/**
	 * 
	 * @param timerIndex indicates next timer.
	 * 
	 * @throws IndexOutOfBoundsException if timerIndex < 0 or timerIndex >= number of timers.
	 */
	/*
	public void setNextTimer( int timerIndex ) throws IndexOutOfBoundsException
	{
		if( timerIndex < 0 || timerIndex >= this.timerTimeList.size() )
		{
			throw new IndexOutOfBoundsException( "timer index (" + timerIndex + 
													") >= number of timers (" + 
													this.timerTimeList.size() +")."  );			
		}
		
		this.timerIDx = timerIndex;
	}
	*/
	
	
	/**
	 * Returns true if there are more timers and pointing to next timer.
	 * @return true if there are more timers.
	 */
	/*
	public boolean hasNextTimer()
	{
		this.timerIDx++;
		
		return this.timerIDx < this.timerTimeList.size();
	}
	*/

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#addTimerValue(long)
	 */
	/*
	@Override
	public void addTimerValue( long time ) 
	{	
		this.timerTimeList.add( time );
	}
	*/

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#clearTimerValues()
	 */
	/*
	@Override
	public void clearTimerValues() 
	{	
		this.timerTimeList.clear();
	}
	*/

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#getNumberOfTimers()
	 */
	/*
	@Override
	public int getNumberOfTimers() 
	{
		return this.timerTimeList.size();
	}
	*/

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#setTimerValue(int, long)
	 */
	/*
	@Override
	public void setTimerValue(int pos, long time) throws IndexOutOfBoundsException, IllegalArgumentException 
	{
		if( time < 0 )
		{
			throw new IllegalArgumentException( "New time value must be >= 0." );
		}
		
		this.timerTimeList.set( pos, time );
	}
	 */
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#setTimerValue(int, long)
	 */
	@Override
	public void setTimerValue( long time) throws IllegalArgumentException 
	{
		/*
		if( time < 0 )
		{
			throw new IllegalArgumentException( "New time value must be >= 0." );
		}
		*/
		
		this.timerTime = time;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#sumValueToTimer(int, long)
	 */
	/*
	@Override
	public void sumValueToTimer(int pos, long rise) throws IndexOutOfBoundsException, IllegalArgumentException 
	{
		long t = this.timerTimeList.get( pos ) + rise;
		
		if( t < 0 )
		{
			throw new IllegalArgumentException( "New timer time < 0." );
		}
		
		this.timerTimeList.set( pos, t );
	 }
	 */
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#sumValueToTimer(int, long)
	 */
	@Override
	public void sumValueToTimer( long rise) throws IllegalArgumentException 
	{
		this.timerTime += rise;
		
		if( this.timerTime < 0 )
		{
			throw new IllegalArgumentException( "New timer time < 0." );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#sumRelativeValueToTimer(int, long)
	 */
	/*
	@Override
	public void sumRelativeValueToTimer(int pos, float soMuchPerOne) throws IndexOutOfBoundsException, IllegalArgumentException 
	{
		if( soMuchPerOne < 0 )
		{
			throw new IllegalArgumentException( "soMuchPerOne < 0." );
		}
		
		long t = this.timerTimeList.get( pos );
		this.timerTimeList.set( pos, (long)( t * soMuchPerOne ) );		
	}
	*/
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimer#sumRelativeValueToTimer(int, long)
	 */
	@Override
	public void sumRelativeValueToTimer( float soMuchPerOne) throws IllegalArgumentException 
	{
		if( soMuchPerOne < 0 )
		{
			throw new IllegalArgumentException( "soMuchPerOne < 0." );
		}
		
		long aux = (long)( this.timerTime * soMuchPerOne );
		
		if( aux > 0 )
		{
			this.timerTime = aux;		
		}
	}
	
	public void setActivedBeep( boolean act )
	{
		this.activedBeep = act;
	}
	
	public void setActiveTimeReport( boolean act )
	{
		this.activedReportTime = act;
	}
	
	@Override
	public String toString() 
	{
		return "[" + super.getName() + ": " + this.timerTime;
	}
}
