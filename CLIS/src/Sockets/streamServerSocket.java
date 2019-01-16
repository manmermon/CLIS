package Sockets;

import java.net.BindException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.Tasks.NotifierThread;
import Controls.eventInfo;
import Controls.eventType;
import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamingParameters;
import Sockets.Manager.ManagerInOutStreamSocket;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public class streamServerSocket extends AbstractStoppableThread implements INotificationTask 
{
	private streamingParameters socketParameters = null;
	
	private ITaskMonitor monitor = null;
	private NotifierThread monitorThread = null;
	
	private ServerSocketChannel streamTCPServer = null;
	private DatagramChannel streamUDPServer = null;
	
	private List< eventInfo > events = null;
	
	private boolean isBlockedWaiting = true;
	
	private Semaphore semEvents = null;
	
 	public streamServerSocket( streamingParameters pars ) throws Exception 
	{
		if( pars == null )
		{
			throw new IllegalArgumentException( "Socket parameter list null or empty." );
		}
		
		this.socketParameters = pars;
		
		super.setName( "ServerSocket");
				
		this.events = new ArrayList< eventInfo >();
		
		this.semEvents = new Semaphore( 1, true );
	}
		
	@Override
	public void taskMonitor( ITaskMonitor m ) 
	{
		if( this.monitorThread != null )
		{
			throw new IllegalThreadStateException( "Thread is working." );
		}
		
		this.monitor = m;
	}

	@Override
	public List<eventInfo> getResult() 
	{
		synchronized ( this.events ) 
		{
			return this.events;
		}
	}

	@Override
	public void clearResult() 
	{
		synchronized ( this.events )
		{
			this.events.clear();
			
			if( this.getState().equals( State.TERMINATED ) )
			{
				this.monitorThread.stopThread( IStoppableThread.StopInNextLoopInteraction );
			}
		}		
	}

	@Override
	protected void preStart() throws Exception 
	{
		this.setName( this.socketParameters.getSocketInfo().toString() );
		
		this.monitorThread = new NotifierThread( this.monitor, this );
		this.monitorThread.startThread();
		
		if( this.socketParameters.getSocketInfo().getProtocolType() == streamSocketInfo.TCP_PROTOCOL )
		{		
			try 
			{
				this.streamTCPServer = ServerSocketChannel.open();
				this.streamTCPServer.configureBlocking( true );
				this.streamTCPServer.socket().setReuseAddress( true );
				
				this.streamTCPServer.socket().bind( this.socketParameters.getSocketInfo().getSocketAddress() );								
			} 
			catch( BindException e )
			{
				if( this.streamTCPServer != null )
				{
					this.streamTCPServer.close();
					this.streamTCPServer = null;
				}
				
				throw new BindException( e.getMessage() + "\n"+ this.socketParameters.getSocketInfo().toString() + " is in used." );
			}
		}
		else if( this.socketParameters.getSocketInfo().getProtocolType() == streamSocketInfo.UDP_PROTOCOL )
		{
			this.streamUDPServer = DatagramChannel.open();
			this.streamUDPServer.bind( this.socketParameters.getSocketInfo().getSocketAddress() );
			
			this.streamUDPServer.socket().setSoTimeout( 0 );
			this.streamUDPServer.socket().setReuseAddress( true );
		}
	}
	
	@Override
	protected void preStopThread(int friendliness) throws Exception 
	{		
	}

	@Override
	protected void postStopThread(int friendliness) throws Exception 
	{	
		if( (friendliness == IStoppableThread.StopWithTaskDone 
				|| friendliness == IStoppableThread.StopInNextLoopInteraction )
				&& this.isBlockedWaiting )
		{
			try
			{
				super.interrupt();
			}
			catch( SecurityException e )
			{
				
			}
		}
	}

	@Override
	protected void runInLoop() throws Exception 
	{
		if( this.streamTCPServer != null )
		{	
			this.isBlockedWaiting = true;
			
			final SocketChannel socketChannel = this.streamTCPServer.accept();
			
			this.isBlockedWaiting = false;
			
			if( socketChannel != null )
			{
				// start communication in new thread
				socketChannel.configureBlocking( true );
				
				ManagerInOutStreamSocket ioss = new ManagerInOutStreamSocket( socketChannel, this.socketParameters.getDirectionSocketConnection() );
				
				synchronized( this.events )
				{
					this.events.add( new eventInfo( eventType.STREAMING_INOUT_CHANNEL_CREATED, ioss ) );
				}
				
				this.notifyEvent();
			}
		}
		else
		{
			ManagerInOutStreamSocket ioss = new ManagerInOutStreamSocket( this.streamUDPServer, this.socketParameters.getDirectionSocketConnection() );
			
			synchronized ( this.events )
			{
				this.events.add( new eventInfo( eventType.STREAMING_INOUT_CHANNEL_CREATED, ioss ) );
			}
			
			this.notifyEvent();
			
			this.stopThread( IStoppableThread.StopInNextLoopInteraction );
		}
	}
	
	@Override
	protected void cleanUp() throws Exception 
	{
		if( this.streamTCPServer != null )
		{
			this.streamTCPServer.close();
			this.streamTCPServer = null;
		}
		
		if( this.streamUDPServer != null )
		{
			this.streamUDPServer.close();
			this.streamUDPServer = null;
		}
		
		this.isBlockedWaiting = false;
	}
	
	@Override
	protected void runExceptionManager(Exception e) 
	{
		this.stopThread = true;
		
		synchronized ( this.events ) 
		{
			this.events.add( new eventInfo( eventType.SERVER_THREAD_STOP, e.getMessage() ) );
		}
		
		this.notifyEvent();
	}	
	
	private void notifyEvent()
	{
		synchronized ( this.monitorThread ) 
		{
			if( this.events.size() > 0 )
			{
				this.monitorThread.notify();
			}		
		}
	}
	
	public streamingParameters getSocketParameters()
	{
		return this.socketParameters;
	}
}
