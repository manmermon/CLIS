package Sockets.Manager;

import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayList;
import java.util.List;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.Tasks.NotifierThread;
import Controls.eventInfo;
import Controls.eventType;
import Sockets.IClosedSocketMonitor;
import Sockets.checkStreamTCPSocket;
import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamSocketProblem;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public abstract class TemplateReadWriteSocketThread  extends AbstractStoppableThread implements INotificationTask, IClosedSocketMonitor
{
	private ITaskMonitor monitor = null;
	protected NotifierThread monitorThread = null;
	
	protected List< eventInfo > events = null;
	
	protected AbstractSelectableChannel socket = null;
	
	protected streamSocketInfo localSocketInfo = null;
	protected streamSocketInfo remoteSocketInfo = null;

	protected checkStreamTCPSocket checkTCPConnectionState = null;
	
	protected int numEvents = 0;
	
	private boolean reportClosedByInterruptexception = true;
		
	public TemplateReadWriteSocketThread( AbstractSelectableChannel channel )  throws Exception 
	{
		if( channel == null )
		{
			throw new IllegalArgumentException( "Socket null." );
		}
				
		this.socket = channel;
		
		String localIP, remoteIP;
		int localPort, remotePort;
		int protocol = streamSocketInfo.TCP_PROTOCOL;
		
		if( channel instanceof SocketChannel )
		{
			localIP = ((SocketChannel) channel).socket().getLocalAddress().getHostAddress();
			remoteIP = ((SocketChannel) channel).socket().getInetAddress().getHostAddress();
			
			localPort = ((SocketChannel) channel).socket().getLocalPort();
			remotePort = ((SocketChannel) channel).socket().getPort();			
			
			this.checkTCPConnectionState = new checkStreamTCPSocket( ((SocketChannel)this.socket).socket(), this );
			
			this.remoteSocketInfo = new streamSocketInfo( protocol, remoteIP, remotePort );
		}
		else //if( channel instanceof DatagramChannel )
		{
			protocol = streamSocketInfo.UDP_PROTOCOL;
			localPort = ((DatagramChannel) channel).socket().getLocalPort();						
			localIP = ((DatagramChannel) channel).socket().getLocalAddress().getHostAddress();
			
			try
			{
				remotePort = ((DatagramChannel) channel).socket().getPort();
				remoteIP = ((DatagramChannel) channel).socket().getInetAddress().getHostAddress();
				
				this.remoteSocketInfo = new streamSocketInfo( protocol, remoteIP, remotePort );
			}
			catch( Exception e )
			{				
			}
		}
		
		this.localSocketInfo = new streamSocketInfo( protocol, localIP, localPort );		
		
		super.setName( this.localSocketInfo + "-" + this.remoteSocketInfo );
		
		this.events = new ArrayList<eventInfo>();
	}
		
	
	public streamSocketInfo getLocalSocketInfo()
	{
		return this.localSocketInfo;
	}
	
	public streamSocketInfo getRemoteSocketInfo()
	{
		return this.remoteSocketInfo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.INotificationTask#taskMonitor(Auxiliar.Tasks.ITaskMonitor)
	 */
	@Override
	public void taskMonitor( ITaskMonitor m ) 
	{
		if( this.monitorThread != null )
		{
			throw new IllegalThreadStateException( "Thread is working." );
		}
			
		this.monitor = m;
	}
	
	public void setReportClosedSocketByInterruptException( boolean flag )
	{
		this.reportClosedByInterruptexception = flag;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.INotificationTask#getResult()
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

	/*
	 * (non-Javadoc)
	 * @see Auxiliar.Tasks.INotificationTask#clearResult()
	 */
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
			
			if( super.stopThread )
			{
				this.monitorThread.stopThread( IStoppableThread.StopInNextLoopInteraction );
			}
		}
	}
	
	@Override
	protected void preStart() throws Exception 
	{
		String suffix = "-" + this.getClass().getCanonicalName() + ": " + this.getName();
		if( this.checkTCPConnectionState != null )
		{
			this.checkTCPConnectionState.setName( this.checkTCPConnectionState.getClass().getCanonicalName() + suffix);
			this.checkTCPConnectionState.startThread();
		}
		
		this.monitorThread = new NotifierThread( this.monitor, this );
		this.monitorThread.setName( this.monitorThread.getClass().getCanonicalName() + suffix );
		this.monitorThread.startThread();
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
				&& checkIsWait() )
		{
			try
			{
				super.interrupt();
			}
			catch( SecurityException e )
			{		
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void cleanUp() throws Exception 
	{
		synchronized ( this.events )
		{
			this.events.add( new eventInfo( eventType.THREAD_STOP, null ) );
		}
				
		synchronized ( this.monitorThread )
		{	
			if( this.events.size() > 0 )
			{
				this.monitorThread.notify();		
			}
		}
		
		//this.monitorThread.stopThread( IStoppableThread.StopInNextLoopInteraction );
		
		if( this.checkTCPConnectionState != null )
		{
			this.checkTCPConnectionState.stopThread( IStoppableThread.StopInNextLoopInteraction );
		}
		
		//this.socket.close();
		
		//this.monitorThread = null;
		this.checkTCPConnectionState = null;
		this.socket = null;
	}
	
	@Override
	public void closedConnection() 
	{
		this.stopThread( IStoppableThread.ForcedStop );
	}
	
	@Override
	protected void runExceptionManager(Exception e) 
	{	
		this.stopThread = true;
		
		boolean report = !(e instanceof InterruptedException ) 
							&&( !( e instanceof ClosedByInterruptException)
									|| this.reportClosedByInterruptexception );
		
		if( report )
		{			
			e.printStackTrace();
			synchronized ( this.events )
			{
				streamSocketProblem problem = new streamSocketProblem( null, e );
				if( this.remoteSocketInfo != null )
				{
					problem = new streamSocketProblem( this.remoteSocketInfo.getSocketAddress(), e );
				}

				this.events.add( new eventInfo( eventType.STREAMING_CONNECTION_PROBLEM, problem ) );
			}
			
			/* It is not necessary. CleanUp() will report to monitor
			synchronized ( this.monitorThread )
			{
				if( this.events.size() > 0 )
				{
					this.monitorThread.notify();
				}
			}
			*/
		}	
	}
	
	protected abstract boolean checkIsWait();
	
	protected abstract void runInLoop() throws Exception;
}
