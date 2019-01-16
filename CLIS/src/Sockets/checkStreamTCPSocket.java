package Sockets;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

public class checkStreamTCPSocket extends AbstractStoppableThread 
{
	private InputStream inputStreamSocket;
	private IClosedSocketMonitor monitorSocket;
	
	public checkStreamTCPSocket( Socket s, IClosedSocketMonitor monitor ) throws IOException 
	{
		if( s == null || monitor == null)
		{	
			throw new IllegalArgumentException( "Socket and/or monitor are null." );
		}
		
		this.inputStreamSocket = s.getInputStream();
		this.monitorSocket = monitor;
	}

	@Override
	protected void preStopThread(int friendliness) throws Exception 
	{	
	}

	@Override
	protected void postStopThread(int friendliness) throws Exception 
	{	
	}

	@Override
	protected void runInLoop() throws Exception 
	{
		if( this.inputStreamSocket != null )
		{
			super.wait( 1000L );
			try
			{
				if( this.inputStreamSocket.read() < 0 )
				{
					this.monitorSocket.closedConnection();
				}
			}
			catch( Exception e )
			{
				super.stopThread = true;
				this.monitorSocket.closedConnection();
			}
		}
		else
		{			
			super.stopThread = true;
			throw new IllegalStateException( "InputStream channel from socket is null." );
		}
	}

}
