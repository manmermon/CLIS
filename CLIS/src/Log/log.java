/*
 * Copyright 2011-2016 by Manuel Merino Monge <manmermon@dte.us.es>
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

package Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.FilerException;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Controls.eventInfo;
import Controls.eventType;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public class log extends AbstractStoppableThread implements INotificationTask
{
	private ITaskMonitor monitor = null;
	private boolean isRunning = false;
	private List< eventInfo > events = new ArrayList< eventInfo >();
	
	private File file = null;
	private String Text = "";
	
	public log( String filePath ) throws Exception
	{
		super.setName( "Cliss-Log" );
		
		this.file = new File( filePath );
		
		boolean ok = true;
		String errorMsg = "Problem: file " + filePath;
		
		if( this.file != null )
		{
			if( !this.file.exists() )
			{
				this.file.createNewFile();
			}
			
			if( !this.file.isFile() || !this.file.canWrite() )
			{   
				ok = false;
				errorMsg += " is not a file or it is not possible to write";
			}
		}
		else
		{
			ok = false;
			errorMsg += " not found";
		}	
		
		if( !ok )
		{
			throw new FilerException( errorMsg );
		}
	}
	
	public synchronized void append( String text )
	{
		if( this.isRunning )
		{
			synchronized ( this.Text)
			{
				this.Text += text;
			}
			
			synchronized( this ) 
			{
				super.notify();
			}
		}
	}
	
	@Override
	protected void preStart() throws Exception 
	{
		super.preStart();
		
		this.isRunning = true;
	}
	
	@Override
	protected void preStopThread(int friendliness) throws Exception 
	{	
	}

	@Override
	protected void postStopThread(int friendliness) throws Exception 
	{	
		if( ( friendliness == IStoppableThread.StopInNextLoopInteraction 
				|| friendliness == IStoppableThread.StopWithTaskDone )
				&& this.getState().equals( State.WAITING ) )
		{ 			
			//this.interrupt();
			synchronized( this ) 
			{
				super.notify();
			}
		}
	}
	
	@Override
	protected void targetDone() throws Exception 
	{
		super.targetDone();
	}
	
	@Override
	protected void runInLoop() throws Exception 
	{		
		super.wait();
		
		if( this.file != null )
		{
			try 
			{
				if( !this.file.exists() )
				{
					this.file.createNewFile();
				}
				
				if( this.file.isFile() && this.file.canWrite() )
				{   
				    PrintWriter  out = new PrintWriter( new BufferedWriter(
				    									new FileWriter( this.file, true ) ), false );
				    synchronized ( this.Text )
				    {
				    	out.print( this.Text );
				    	this.Text = "";
					}
				    
				    out.close();
				}
			} 
			catch (IOException e)
			{
				this.notifyProblem( e );
			}
		}		
	}
	
	@Override
	protected void runExceptionManager(Exception e) 
	{
		if( !(e instanceof InterruptedException ) )
		{
			this.stopThread = true;
			this.notifyProblem( e );
		}
	}
	
	@Override
	protected void cleanUp() throws Exception 
	{
		super.cleanUp();
		
		this.isRunning = false;
	}

	private void notifyProblem( Exception e )
	{
		String errorMsg = "" + e.getMessage();
		if( errorMsg.isEmpty() )
		{
			Throwable t = e.getCause();
			if( t != null )
			{
				errorMsg += t.toString();
			}
			
			if( errorMsg.isEmpty() )
			{
				errorMsg += e.getLocalizedMessage();
			}
		}	
		
		this.events.add( new eventInfo( eventType.PROBLEM, errorMsg ) );
		
		if( this.monitor != null )
		{
			try 
			{
				this.monitor.taskDone( this );
			}
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void taskMonitor( ITaskMonitor m ) 
	{
		if( this.isRunning )
		{
			throw new IllegalStateException( "Log is running." );
		}
		
		this.monitor = m;
	}

	@Override
	public List<eventInfo> getResult() 
	{
		synchronized( this.events )
		{
			return this.events;
		}
	}

	@Override
	public void clearResult() 
	{
		synchronized( this.events ) 
		{
			this.events.clear();
		}
	}	
}