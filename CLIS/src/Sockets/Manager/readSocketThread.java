package Sockets.Manager;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import Controls.eventInfo;
import Controls.eventType;
import GUI.MyComponents.Tuple;
import Sockets.Info.streamInputMessage;

public class readSocketThread extends TemplateReadWriteSocketThread
{	
	private boolean waitingData = true;		

	public readSocketThread( AbstractSelectableChannel channel ) throws Exception 
	{
		super( channel );
	}

	@Override
	protected boolean checkIsWait() 
	{
		return this.waitingData;
	}

	@Override
	protected void runInLoop() throws Exception 
	{
		int bufferSize = 65507; 
		ByteBuffer buf = ByteBuffer.allocate( bufferSize );
				
		String msg = "";
		int nbytesRead = -1;
		
		SocketAddress client = null;
		if( super.socket instanceof SocketChannel )
		{
			client = ((SocketChannel) super.socket).getRemoteAddress();
		}		
		
		this.waitingData = true;
		
		if( super.socket instanceof SocketChannel )
		{
			( ( SocketChannel )super.socket).read( buf );
		}
		else
		{
			client = ( ( DatagramChannel )super.socket).receive( buf );
		}		
		
		this.waitingData = false;
		
		buf.flip();
		if( client != null )
		{	
			nbytesRead = buf.limit();
		}
		
		if( nbytesRead > 0 )
		{	
			msg += new String( buf.array(), 0, buf.limit() );
					
			while( nbytesRead == bufferSize 
					&& !(super.socket instanceof DatagramChannel) )
			{
				buf.clear();
				if( super.socket instanceof SocketChannel )
				{
					nbytesRead = ( ( SocketChannel )super.socket).read( buf );
				}
				else
				{
					nbytesRead = ( ( DatagramChannel )super.socket).read( buf );
				}
				
				if( nbytesRead > -1 )
				{
					msg += new String( buf.array(), 0, nbytesRead );
					nbytesRead += nbytesRead;
				}								
			}
			
			streamInputMessage in  = new streamInputMessage( msg , (InetSocketAddress)client, this.localSocketInfo.getSocketAddress() );
			
			synchronized ( this.events )
			{
				this.events.add( new eventInfo( eventType.STREAMING_INPUT_MSG, in ) );
			}
		}
		
		synchronized ( super.monitorThread )
		{
			if( this.events.size() > 0 )
			{
				super.monitorThread.notify();
			}			
		}		
	}
}
