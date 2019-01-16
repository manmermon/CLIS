package Sockets.Manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.LinkedList;
import java.util.List;

import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamingOutputMessage;
import Controls.eventInfo;
import Controls.eventType;

public class writeSocketThread extends TemplateReadWriteSocketThread
{	
	private List< streamingOutputMessage > msgList = null;
	
	private boolean isSendingMsg = false;
	
	public writeSocketThread( AbstractSelectableChannel channel ) throws Exception  
	{
		super( channel );
		
		this.msgList = new LinkedList< streamingOutputMessage >();
	}
	
	@Override
	protected boolean checkIsWait() 
	{	
		return this.getState().equals( State.WAITING ) && !this.isSendingMsg;		
	}
	
	/**
	 * Send message to remote socket. 
	 * @param msg: String data to send.
	 * @param destinations: remote socket list (only for UDP).
	 */
	public void sendMessage( final streamingOutputMessage msg )
	{		
		if( msg != null )
		{
			this.isSendingMsg = true;
			
			final Thread notifiedThread = this;
			new Thread()
			{
				public void run() 
				{
					try
					{
						synchronized ( msgList ) 
						{
							msgList.add( msg );
						}
									
						synchronized ( notifiedThread )
						{
							notifiedThread.notify();
						}
					}
					catch( Exception e )
					{
						
					}
				};
				
			}.start();
		}
	}

	@Override
	protected void runInLoop() throws InterruptedException, IOException 
	{		
		super.wait();
		
		synchronized ( this.msgList )
		{
			for( streamingOutputMessage msg : this.msgList )
			{	
				String message = msg.getMessage();
				ByteBuffer buf = ByteBuffer.allocate( message.length() );					
				buf.clear();
				buf.put( message.getBytes() );
						
				buf.flip();
				
				if( this.socket instanceof SocketChannel )
				{						
					while( buf.hasRemaining() ) 
					{
					    ( ( SocketChannel )this.socket ).write( buf );
					}
				}
				else
				{
					List< streamSocketInfo > dests = msg.getDestinations();
					
					if( dests != null )
					{
						for( streamSocketInfo d : dests )
						{
							DatagramPacket data = new DatagramPacket( buf.array(), 
																		buf.array().length, 
																		d.getSocketAddress() );
							
							( ( DatagramChannel ) this.socket ).socket().send( data );
						}
					}	
				}
				
				buf.clear();
			}
			
			this.msgList.clear();
			
			synchronized ( this.events )
			{
				this.events.add( new eventInfo( eventType.STREAMING_OUTPUT_MSG_OK, true ) );
			}
		}	
		
		this.isSendingMsg = false;
		
		synchronized ( this.monitorThread )
		{
			if( this.events.size() > 0 )
			{
				this.monitorThread.notify();
			}
		}
	}

	@Override
	public void targetDone() throws Exception 
	{
		synchronized( this.msgList )
		{
			if( super.stopWhenTaskDone )
			{
				super.stopThread = this.msgList.size() == 0;				
			}
		}		
	}	
}
