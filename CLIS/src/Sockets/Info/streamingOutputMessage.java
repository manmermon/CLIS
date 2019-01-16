package Sockets.Info;

import java.util.ArrayList;
import java.util.List;

public class streamingOutputMessage 
{
	private streamSocketInfo source = null;
	private List< streamSocketInfo > destination = new ArrayList< streamSocketInfo >();
	private String message = "";
	
	public streamingOutputMessage( String msg, streamSocketInfo origin, streamSocketInfo destination ) 
	{
		this.setOrigin( origin );
		
		this.addDestination( destination );
		
		this.message = msg;
	}
	
	public streamingOutputMessage( String msg, streamSocketInfo origin, List< streamSocketInfo > destination ) 
	{
		this.source = origin;
		
		this.addDestination( destination );
		
		this.message = msg;
	}
	
	public void addDestination( streamSocketInfo d )
	{
		if( d == null )
		{
			throw new IllegalArgumentException( "Streaming paramters null." );
		}
		
		this.destination.add( d );
	}
	
	public void addDestination( List< streamSocketInfo > d )
	{
		if( d == null )
		{
			throw new IllegalArgumentException( "Streaming paramters null." );
		}
		
		this.destination.addAll( d );
	}
	
	public List< streamSocketInfo > getDestinations()
	{
		return this.destination;
	}
	
	public void setDestination( int pos, streamSocketInfo d )
	{
		this.destination.set( pos, d );
	}
	
	public streamSocketInfo getOrigin()
	{
		return this.source;
	}
	
	public void setOrigin( streamSocketInfo origin )
	{
		if( origin == null )
		{
			throw new IllegalArgumentException( "Origin streamSocketInfo null." );
		}
		
		this.source = origin;
	}
	
	public void clearDestination()
	{
		this.destination.clear();
	}

	public String getMessage( )
	{
		return this.message;
	}
	
	public void setMessage( String msg )
	{
		this.message = msg;
	}
	
	@Override
	public String toString() 
	{	
		return "<" + this.message + ", [" + this.source + ", "+ this.destination +"]>";
	}
}
