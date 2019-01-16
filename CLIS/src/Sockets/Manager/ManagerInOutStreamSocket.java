package Sockets.Manager;

import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayList;
import java.util.List;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Controls.eventInfo;
import Controls.eventType;
import GUI.MyComponents.Tuple;
import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamingOutputMessage;
import Sockets.Info.streamingParameters;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public class ManagerInOutStreamSocket extends AbstractStoppableThread implements INotificationTask, ITaskMonitor 
{
	private boolean isRunning = false;
	private boolean wakeUpToStop = false;
	
	private ITaskMonitor monitor = null;
	//private NotifierThread monitorThread = null;
	
	private List< eventInfo > events = new ArrayList<eventInfo>();
	private int numEvents = 0;
	
	private readSocketThread readThread = null;
	
	private writeSocketThread writeThread = null;
	
	private streamSocketInfo localSocket = null;
	
	private streamSocketInfo remoteSocket = null;	
	
	private int counterThreads = 0;
		
	private AbstractSelectableChannel inOutChannel = null;

	private boolean reportCloseByInterrutpExceptionReader = true;
	private boolean reportCloseByInterrutpExceptionWriter = true;
	
	/**
	 * Create a read and write socket threads.
	 * Do it nothing if this one just exists.
	 * @throws Exception 
	 */
	public ManagerInOutStreamSocket( AbstractSelectableChannel channel, int direction ) throws Exception 
	{
		if( direction != streamingParameters.SOCKET_CHANNEL_IN 
				&& direction != streamingParameters.SOCKET_CHANNEL_INOUT 
				&& direction != streamingParameters.SOCKET_CHANNEL_OUT )
		{
			throw new IllegalArgumentException( "Socket streaming direction unknown. See streamingParameters class." );
		}
		
		this.inOutChannel = channel;
		
		if( this.readThread == null 
				&& 
				( direction == streamingParameters.SOCKET_CHANNEL_IN 
					|| direction == streamingParameters.SOCKET_CHANNEL_INOUT ) )
		{
			this.counterThreads++;
			
			this.readThread = new readSocketThread( channel );
			this.readThread.taskMonitor( this );
			
			this.localSocket = this.readThread.getLocalSocketInfo();
			this.remoteSocket = this.readThread.getRemoteSocketInfo();			
		}
		
		if( this.writeThread == null 
				&& 
				( direction == streamingParameters.SOCKET_CHANNEL_OUT 
					|| direction == streamingParameters.SOCKET_CHANNEL_INOUT ) )
		{
			this.counterThreads++;
			
			this.writeThread = new writeSocketThread( channel );
			this.writeThread.taskMonitor( this );
			
			this.localSocket = this.writeThread.getLocalSocketInfo();
			this.remoteSocket = this.writeThread.getRemoteSocketInfo();	
		}
		
		super.setName( super.getClass().getName() + "<" + this.localSocket + "-" + this.remoteSocket + ">");
	}
	
	@Override
	public synchronized void startThread() throws Exception 
	{
		super.startThread();
		
		if( this.readThread != null )
		{
			this.readThread.setReportClosedSocketByInterruptException( this.reportCloseByInterrutpExceptionReader );
			this.readThread.startThread();
		}
		
		if( this.writeThread != null )
		{
			this.writeThread.setReportClosedSocketByInterruptException( this.reportCloseByInterrutpExceptionWriter );
			this.writeThread.startThread();
		}
	}
	
	@Override
	public synchronized void taskDone( INotificationTask task)  throws Exception
	{
		synchronized ( this.events ) 
		{
			List< eventInfo > eventList = new ArrayList<eventInfo>( task.getResult() );
			task.clearResult();
			
			for( eventInfo e : eventList  )
			{	
				if( !e.getEventType().equals( eventType.THREAD_STOP ) )
				{
					this.events.add( e );
				}
				else
				{
					this.counterThreads--;
					
					if( this.readThread != null 
							&& this.readThread.equals( task ) )
					{
						this.readThread = null;
					}
					else
					{
						this.writeThread = null;
					}
					
					if( this.counterThreads == 0 )
					{
						this.events.add( new eventInfo( eventType.THREAD_STOP, new Tuple< streamSocketInfo, streamSocketInfo >( this.localSocket, this.remoteSocket ) ) );
					}
				}
			}
		}
		
		if( this.events.size() > 0 )
		{
			synchronized ( this ) 
			{
				super.notify();
			}
		}
	}
	@Override
	public void taskMonitor( ITaskMonitor m ) 
	{	
		if( this.isRunning )
		{
			throw new IllegalThreadStateException( "Manager of In-out stream socket is working");
		}
		
		this.monitor = m;
	}
	
	public void setReportClosedSocketByInterruptExceptionReader( boolean flag )
	{
		this.reportCloseByInterrutpExceptionReader = flag;
	}

	public void setReportClosedSocketByInterruptExceptionWriter( boolean flag )
	{
		this.reportCloseByInterrutpExceptionWriter = flag;
	}
	
	/**
	 * Return a eventInfo where eventInformation is a
	 * list of eventInfo.
	 */
	@Override
	public List< eventInfo > getResult() 
	{
		synchronized ( this.events )
		{
			this.numEvents = this.events.size();
			
			return this.events;
		}		
	}

	@Override
	public void clearResult() 
	{
		synchronized ( this.events ) 
		{
			while( this.numEvents > 0 )
			{
				this.events.remove( 0 );
				this.numEvents--;
			}
		}				
	}

	public boolean existReadSocketThread()
	{
		return this.readThread != null;
	}
	
	public boolean existWriteSocketThread()
	{
		return this.writeThread != null;
	}
	
	public streamSocketInfo getLocalSocketInfo()
	{				
		return this.localSocket;
	}
	
	public streamSocketInfo getRemoteSocketInfo()
	{	
		return this.remoteSocket;
	}
	
	/**
	 * Send message to remote socket. 
	 * @param msg: String data to send.
	 * @param destinations: remote socket list (only for UDP).
	 */
	public void sendMessage( final streamingOutputMessage msg ) throws NullPointerException
	{
		if( this.writeThread != null )
		{
			this.writeThread.sendMessage( msg );
		}
	}
	
	public boolean isFinished()
	{
		return this.counterThreads < 1;
		//return this.writeThread == null	&& this.readThread == null;
	}
	
	public String toString() 
	{
		return this.getClass() + " -> " + this.localSocket + " - " + this.remoteSocket;
	}

	@Override
	protected void preStopThread( int friendliness ) throws Exception 
	{
		if( this.counterThreads > 0 )
		{			
			if( this.readThread != null )
			{
				this.readThread.stopThread( friendliness );
			}
			
			if( this.writeThread != null )
			{				
				this.writeThread.stopThread( friendliness );
			}
		}
	}

	@Override
	protected void postStopThread(int friendliness) throws Exception 
	{
		if( (friendliness == IStoppableThread.StopWithTaskDone 
				|| friendliness == IStoppableThread.StopInNextLoopInteraction )
				&& this.getState().equals( State.WAITING ) 
				&& this.isFinished() )
		{
			try
			{
				synchronized ( this ) 
				{
					this.wakeUpToStop = true;
					super.notify();	
				}
			}
			catch( SecurityException e )
			{		
			}
		}
	}

	@Override
	protected void runInLoop() throws Exception 
	{
		super.wait();
		
		if( !this.wakeUpToStop )
		{
			try
			{
				if( this.events.size() > 0 )
				{
					this.monitor.taskDone( this );
				}
			}
			catch( InterruptedException e )
			{			
			}
		}
	}
	
	@Override
	protected void cleanUp() throws Exception 
	{
		super.cleanUp();
		
		if( this.inOutChannel != null )
		{
			this.inOutChannel.close();
		}
	}
}
