package Sockets.Info;

import java.net.InetSocketAddress;

public class streamSocketProblem 
{
	private InetSocketAddress socketAddress = null;
	private Exception exc = null;
	
	public streamSocketProblem( InetSocketAddress address, Exception ex ) throws IllegalArgumentException 
	{
		this.socketAddress = address;
		this.exc = ex; 
	}
	
	public InetSocketAddress getSocketAddress()
	{
		return this.socketAddress;
	}
	
	public Exception getProblemCause()
	{
		return this.exc;
	}
	
	@Override
	public String toString() 
	{
		return "<"+socketAddress + "-Problem=" + this.exc + ">";
	}
}
