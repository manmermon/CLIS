package Auxiliar.Tasks;

import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public class NotifierThread extends AbstractStoppableThread 
{
	private ITaskMonitor Monitor;
	private INotificationTask Task;
	
	private boolean isWait = false;
	private boolean wakeUpToStop = false;
	
	private boolean isRunning = false;
	
	public NotifierThread( ITaskMonitor monitor, INotificationTask task ) throws IllegalArgumentException 
	{
		if( monitor == null || task == null )
		{
			throw new IllegalArgumentException( "Null input(s)." );
		}
		
		this.Monitor = monitor;
		this.Task = task;
		
		super.setName( this.getClass() + "<" + this.Monitor + ", " + this.Task + ">" );
	}
	
	public void setMonitor( ITaskMonitor m ) throws IllegalStateException
	{
		if( this.isRunning )
		{
			throw new IllegalStateException( "Notifier thread is run." );
		}
		
		this.Monitor = m;
	}
	
	public void setMonitor( INotificationTask t ) throws IllegalStateException
	{
		if( this.isRunning )
		{
			throw new IllegalStateException( "Notifier thread is run." );
		}
		
		this.Task = t;
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
				&& this.isWait )
		{	
			synchronized ( this )
			{				
				this.wakeUpToStop = true;
				super.notify();
			}
		}
	}

	@Override
	protected void runInLoop() throws Exception 
	{
		this.isWait = true;
		
		super.wait();
		
		this.isWait = false;
		
		if( !this.wakeUpToStop )
		{
			try
			{
				this.Monitor.taskDone( this.Task );
			}
			catch( InterruptedException e )
			{			
			}
		}
	}
	
	@Override
	protected void runExceptionManager( Exception e ) 
	{
		e.printStackTrace();
		super.stopThread = true;
	}

	@Override
	protected void cleanUp() throws Exception 
	{
		this.Monitor = null;
		this.Task = null;		
	}
}
