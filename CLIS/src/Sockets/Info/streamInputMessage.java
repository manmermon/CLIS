package Sockets.Info;

import java.net.InetSocketAddress;

public class streamInputMessage 
{
	private InetSocketAddress source = null;
	private InetSocketAddress dest = null;
	private String message = "";
	
	public streamInputMessage( String msg, InetSocketAddress origin, InetSocketAddress destination ) 
	{	
		if( origin == null && destination == null )
		{
			throw new IllegalArgumentException( "InetSocketAddress inputs null." );
		}
		
		this.dest = destination;
		this.source = origin;
		this.message = msg;
	}
		
	public InetSocketAddress getOrigin()
	{
		return this.source;
	}
	
	public InetSocketAddress getDestination()
	{
		return this.dest;
	}
	
	public String getMessage( )
	{
		return this.message;
	}
	
	@Override
	public String toString() 
	{	
		return "<" + this.message + ", [" + this.source +", " + this.dest + "]>";
	}

}
