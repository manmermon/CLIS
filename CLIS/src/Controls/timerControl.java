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

import Auxiliar.WarningMessage;
import GUI.MyComponents.Tuple;
import StoppableThread.IStoppableThread;
import Timers.ITimerMonitor;
import Timers.Timer;
import Timers.TimerParameters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class timerControl extends controlOfSubordinateTemplate implements ITimerMonitor
{
  private static timerControl ctrTimer = null;
  
  private Map<String, List<Timer>> timers = null;
  private Map<String, Integer> timerIndex = null;
  
  private List<Object> threadParameters = null;
  private Thread timerReportThread = null;
  
  private boolean stopSubThreads = false;

  private timerControl()
  {
    super.setName( this.getClass().getSimpleName());
    
    this.timers = new HashMap< String, List< Timer > >();
	this.timerIndex = new HashMap< String, Integer >();
    
	this.threadParameters = new ArrayList< Object >();	
    
    this.timerReportThread = new Thread()
    {
      public synchronized void run()
      {
        super.setName("Timer Report Thread");
        this.setName( this.getClass().getName() );
        
        while (!timerControl.this.stopSubThreads)
        {
          try
          {
            super.wait();
            
            if (timerControl.this.threadParameters.size() > 0)
            {
              long time = (Long)timerControl.this.threadParameters.get( 0 );
              timerControl.this.supervisor.updateClockDisplay(time / 1000L, 
												                String.format("%02d:%02d"
												                		, new Object[] {  TimeUnit.MILLISECONDS.toMinutes( time ) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time) ), 
												                						  TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)) }));
            }
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
          
        }
      }
    };
    
    this.timerReportThread.start();
  }
  
  public static timerControl getInstance()
  {
    if (ctrTimer == null)
    {
      ctrTimer = new timerControl();
    }
    
    return ctrTimer;
  }
	
	/*
	 * (non-Javadoc)
	 * @see Controls.IControlLevel2#setSubordinates(java.util.Map)
	 */
	@Override
  public synchronized void setSubordinates(String subordinateID, Object parameters) throws Exception
  {
    if ((subordinateID != null) && (parameters != null))
    {
      List<Timer> timerList = this.timers.get(subordinateID);
      
      if (timerList != null)
      {
        Tuple<Integer, TimerParameters> par = (Tuple<Integer, TimerParameters>)parameters;
        if (((Integer)par.x).intValue() < timerList.size())
        {
          Timer timer = (Timer)timerList.get(((Integer)par.x).intValue());
          
          timer.interruptTimer();
          
          timer.setActivedBeep(((TimerParameters)par.y).isEnabledBeep());
          timer.setActiveTimeReport(((TimerParameters)par.y).isEnableTimeReport());
          timer.setTimerValue(((TimerParameters)par.y).getTime());
        }
      }
    }
  }

  protected List<IStoppableThread> createSubordinates(Map<String, Object> parameters) throws Exception
  {
    if (parameters == null)
    {
      throw new IllegalArgumentException("Parameters null.");
    }
    
    List<IStoppableThread> subordinateList = new ArrayList< IStoppableThread >();    

    // Create timers

    Set<String> timerIDs = parameters.keySet();
    
    for (String timerID : timerIDs)
    {
      List<Timer> timerList = this.timers.get(timerID);
      
      List<TimerParameters> tpars = (List< TimerParameters >)parameters.get( timerID );
      
      if (tpars == null)
      {
        throw new IllegalArgumentException("Time parameter list null");
      }
      
      if (timerList == null)
      {
        timerList = new ArrayList< Timer >();
      }
      
      Iterator<TimerParameters> itPars = tpars.iterator();
      
      while (itPars.hasNext())
      {
        TimerParameters tpar = (TimerParameters)itPars.next();
        
        Timer timer = null;
        
        if ((tpar != null) && (tpar.getTime() > 0L))
        {
          timer = new Timer();
          timer.setTimerMonitor(this);
          
          timer.setName(timerID);
          
          timer.setActivedBeep(tpar.isEnabledBeep());
          timer.setActiveTimeReport(tpar.isEnableTimeReport());
          timer.setTimerValue(tpar.getTime());
        }
        
        timerList.add(timer);
        subordinateList.add(timer);
      }
      
      this.timers.put(timerID, timerList);
    }
    

    return subordinateList;
  }
  
  public void toWorkSubordinates(Object info) throws Exception
  {
    preToWorkSubordinates();
    
    this.isWorking = true;
    startWorking(info);
    
    postToWorkSubordinates();
  }
  
  protected void preToWorkSubordinates() throws Exception
  {}
  
  protected void postToWorkSubordinates() throws Exception
  {}

	/*
	 * (non-Javadoc)
	 * @see Controls.controlTemplate#startWorking(java.lang.Object)
	 */
	@Override
  protected void startWork(Object info) throws Exception
  {
    Tuple<String, Integer> timerInfo = (Tuple<String, Integer>)info;
    
    List<Timer> timers = (List< Timer >)this.timers.get(timerInfo.x);
    
    int indexTimer = ((Integer)timerInfo.y).intValue();
    
    if ((timers != null) && 
      (indexTimer < timers.size()))
    {
      this.timerIndex.put( (String)timerInfo.x, indexTimer );
      
      Timer timer = null;
      
      if (indexTimer >= 0)
      {
        timer = (Timer)timers.get(indexTimer);
      }
      
      if (timer != null)
      {
        if (timer.getState().equals(Thread.State.NEW))
        {
          timer.startThread();
        }
        else
        {
          timer.restartTimer();
        }
      }
    }
  }
  
  public void stopTimer(String timerName, int timerIndex)
  {
    List<Timer> timerList = (List< Timer >)this.timers.get(timerName);
   
    if ((timerList != null) 
    		&& (timerList.size() > timerIndex))
    {
      try
      {
        Timer t = (Timer)timerList.get(timerIndex);
        
        if (t != null)
        {
          t.interruptTimer();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void stopAllTimers(String timerName)
  {
    List<Timer> timerList = (List< Timer >)this.timers.get(timerName);
    
    for (Timer t : timerList)
    {
      try
      {
        if (t != null)
        {
          t.interruptTimer();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void resetControl() throws Exception
  {}
  
	/*
	 * (non-Javadoc)
	 * @see Controls.controlOfSubordinateTemplate#cleanUp()
	 */
	@Override
  protected void cleanUpSubordinates()
  {
    this.timers.clear();
  }
  
	/*
	 * (non-Javadoc)
	 * @see Controls.controlOfSubordinateTemplate#cleanUp()
	 */
	@Override
  public synchronized void timeOver(String timerName)
  {
    Tuple< String, Integer> t = new Tuple< String, Integer>(timerName, (Integer)this.timerIndex.get(timerName));
    this.timerIndex.get(timerName);
    
    super.event = new eventInfo( eventType.TIME_OVER, t);
    
    super.supervisor.eventNotification(this, super.event);
  }
  
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Timers.ITimerMonitor#reportClockTime(long)
	 */
	@Override
  public void reportClockTime(long time)
  {
    synchronized (this.timerReportThread)
    {
      this.threadParameters.clear();
      this.threadParameters.add( time );
      
      this.timerReportThread.notify();
    }
  }
	
	/**
	 * Delete timer
	 * 
	 * @param timerName
	 */
  public void unnecessaryTimer(String timerName)
  {
    List<Timer> timers = (List< Timer >)this.timers.get(timerName);
    
    if (timers != null)
    {
      for (Timer timer : timers)
      {
        if (timer != null)
        {
          timer.stopThread( IStoppableThread.ForcedStop );
        }
      }
      
      this.timers.remove(timerName);
    }
  }

	/**
	 * Delete one timer.
	 * 
	 * @param timerName timer id.
	 * @param indexTimer position timer
	 */
  public void unnecessaryTimer(String timerName, int indexTimer)
  {
    List<Timer> timers = (List< Timer >)this.timers.get(timerName);
    
    if (timers != null)
    {
      if (indexTimer < timers.size())
      {
        Timer t = (Timer)timers.get(indexTimer);
        
        if (t != null)
        {
          t.stopThread(1);
        }
        
        timers.remove(indexTimer);
      }
      
      if (timers.isEmpty())
      {
        this.timers.remove(timerName);
      }
    }
  }

	/**
	 * Stop and delete all timers
	 */
  public void clearTimers()
  {
    for (String idTimer : this.timers.keySet())
    {
      unnecessaryTimer(idTimer);
    }
  }
	
	/**
	 * 
	 * @param timerName
	 * @param timeIndex
	 * @return
	 */
  public boolean resumeTimer(String timerName, int timeIndex)
  {
    List<Timer> timerList = (List< Timer >)this.timers.get(timerName);
    
    boolean resume = false;
    
    if (timerList != null)
    {
      Timer timer = (Timer)timerList.get(timeIndex);
      try
      {
        timer.restartTimer();
        resume = true;
      }
      catch (Exception e)
      {
        resume = false;
        e.printStackTrace();
      }
    }
    
    return resume;
  }
	
	/**
	 * 
	 * @param idTimer
	 * @return
	 */
  public int getNumerofTimer(String idTimer)
  {
    int n = 0;
    
    List<Timer> timerList = this.timers.get(idTimer);
    if (timerList != null)
    {
      n = timerList.size();
    }
    
    return n;
  }
	
	/**
	 * 
	 * @param timerInfo
	 * @param soMuchPerOne
	 * @throws Exception
	 */
  public void sumRelativeValueToTimer(Tuple<String, Integer> timerInfo, float soMuchPerOne) throws Exception
  {
    String timerName = (String)timerInfo.x;
    int timerIDx = ((Integer)timerInfo.y).intValue();
    
    List<Timer> timerList = this.timers.get(timerName);
    
    if (timerList != null)
    {
      Timer t = (Timer)timerList.get(timerIDx);
      
      if (t != null)
      {
        t.sumRelativeValueToTimer(soMuchPerOne);
      }
    }
  }

	/**
	 * 
	 * @param timerName
	 * @param soMuchPerOne
	 */
  public void sumRelativeValueToAllTimer(String timerName, float soMuchPerOne)
  {
    List<Timer> timerList = (List)this.timers.get(timerName);
    
    if (timerList != null)
    {
      for (Timer t : timerList)
      {
        if (t != null)
        {
          t.sumRelativeValueToTimer(soMuchPerOne);
        }
      }
    }
  }
  

  public WarningMessage checkParameters()
  {
    return new WarningMessage();
  }
}