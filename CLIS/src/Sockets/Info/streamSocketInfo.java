package Sockets.Info;

import java.net.InetSocketAddress;

public class streamSocketInfo implements Comparable< streamSocketInfo >
{	
	public static final int TCP_PROTOCOL = 0;
	public static final int UDP_PROTOCOL = 1;
	
	private InetSocketAddress socketAddress;
	private int protocolType = TCP_PROTOCOL;
	
	public streamSocketInfo( int protocol_type, String ipAddress, int port ) 
	{
		this.setProtocolType( protocol_type );
		this.setSocketAddress( ipAddress, port );
	}		
	
	public int getProtocolType()
	{
		return this.protocolType;
	}
	
	public void setProtocolType( int protocol_Type )
	{
		if( protocol_Type != TCP_PROTOCOL 
				&& protocol_Type != UDP_PROTOCOL )
		{
			throw new IllegalArgumentException( "Unsupported protocol." );
		}
		
		this.protocolType = protocol_Type;
	}
	
	public InetSocketAddress getSocketAddress( )
	{
		return this.socketAddress;
	}
	
	public void setSocketAddress( String ipAddress, int port )
	{
		this.socketAddress = new InetSocketAddress( ipAddress, port );		
	}	
	
	public String toString()
	{			
		return getSocketString( this.protocolType, this.socketAddress.getAddress().getHostAddress(), this.socketAddress.getPort() );
	}
	
	public static String getSocketString( int protocol_Type, String ip, int port )
	{
		String str = "TCP";
		if( protocol_Type == UDP_PROTOCOL )
		{
			str = "UDP";
		}
		
		return str + ":" + ip + ":" + port;
	}
	
	/**
	 * True if both objects have the same protocol, ip address, port and strem direction.
	 */
	@Override
	public boolean equals(Object obj) 
	{
		boolean equal = obj instanceof streamSocketInfo;
		
		if( equal )
		{
			streamSocketInfo pars = ( streamSocketInfo )obj;
			
			equal = this.socketAddress.getPort() == pars.getSocketAddress().getPort()
					&& this.protocolType == pars.getProtocolType() 
					&& this.socketAddress.getAddress().getHostAddress().equals( pars.getSocketAddress().getAddress().getHostAddress() );
		}
		
		return equal;
	} 
	
	@Override
	public int hashCode() 
	{
		int hash = this.socketAddress.getAddress().getHostAddress().hashCode() + 
					this.socketAddress.getPort() + this.protocolType;
        return hash;
	}


	@Override
	public int compareTo( streamSocketInfo o ) 
	{
		int p = o.getProtocolType() - this.protocolType;
		
		if( p == 0 )
		{
			p = this.socketAddress.getAddress().getHostAddress().compareTo( o.getSocketAddress().getAddress().getHostAddress() );
			
			if( p == 0 )
			{
				p = o.getSocketAddress().getPort() - this.socketAddress.getPort();
			}
		}
		
		return p;	
	}
}
