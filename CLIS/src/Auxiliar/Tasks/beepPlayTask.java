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

package Auxiliar.Tasks;

import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import Controls.eventInfo;
import GUI.MyComponents.SoundUtils;
import StoppableThread.AbstractStoppableThread;

public class beepPlayTask extends AbstractStoppableThread implements ITask 
{
	public static final short FREQUENCY_TONE = 0;
	public static final short TIME_TONE = 1;
	public static final short VOLUMEN_TONE = 2;
	
	private int hz = 1500;
	private int msecs = 150;
	private int vol = 500;

	public beepPlayTask() 
	{
		super.setName( this.getClass().getSimpleName() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#addParameter(short, java.lang.Object)
	 */	
	@Override
	public void addParameter( short parameterID, Object parameter ) throws Exception
	{
		this.setParameter( parameterID, parameter );
	}
	

	@Override
	public void setParameter(short parameterID, Object parameter) throws Exception 
	{		
		int value = (new Integer( parameter.toString() ) ).intValue();			
		
		if( value <= 0 )
		{
			throw new IllegalArgumentException( "Parameter must be >0." );
		}
		
		if( parameterID == FREQUENCY_TONE )
		{
			this.hz = value; 
		}
		else if( parameterID == TIME_TONE )
		{
			this.msecs = value;
		}
		else if( parameterID == VOLUMEN_TONE )
		{
			this.msecs = value;
		}
	}

	@Override
	public void clearParameters() 
	{
		this.hz = 1500;
		this.msecs = 150;
		this.vol = 500;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#startTask()
	 */
	@Override
	public synchronized void startTask( ) throws Exception 
	{	
		super.notify();
	}

	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#preStopThread()
	 */
	@Override
	protected synchronized void preStopThread(int friendliness) 
	{		
		//Tone is off.
		SoundUtils.stop();
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#postStopThread()
	 */
	@Override
	protected void postStopThread(int friendliness) 
	{
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#runInLoop()
	 */
	@Override
	protected synchronized void runInLoop() 
	{	
		try 
		{
			super.wait();
			
			//Tone is on. 
			SoundUtils.tone( this.hz, this.msecs, this.vol );
		} 
		catch( InterruptedException e )
		{
			
		}
		catch (LineUnavailableException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see StoppableThread.AbstractStoppableThread#startUp()
	 */
	@Override
	protected synchronized void startUp() 
    {
		//Only one loop interaction
        //super.stopThread( IStoppableThread.StopInNextLoopInteraction );
    }

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#getResult()
	 */
	@Override
	public List< eventInfo > getResult() 
	{
		//No result
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#taskMonitor(Auxiliar.Tasks.ITaskMonitor)
	 */
	@Override
	public void taskMonitor( ITaskMonitor monitor ) 
	{	
		//No monitor.
	}


	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.ITask#createTask()
	 */
	@Override
	public void createTask() 
	{		
		//Nothing
	}


	@Override
	public void clearResult() 
	{	
	}
}
