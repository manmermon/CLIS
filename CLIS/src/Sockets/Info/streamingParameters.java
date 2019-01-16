package Sockets.Info;

public class streamingParameters
{	
	public static final int SOCKET_CHANNEL_IN = 0;
	public static final int SOCKET_CHANNEL_OUT = 1;
	public static final int SOCKET_CHANNEL_INOUT = 2;
	
	private int In_Out = SOCKET_CHANNEL_INOUT;
	private streamSocketInfo socketInfo =  null;
	
	public streamingParameters( streamSocketInfo info, int INOUT ) 
	{
		if( info == null )
		{
			throw new IllegalArgumentException( "StreamSocketInfo null." );
		}
		this.socketInfo = info;
		this.setDirectionSocketConnetion( INOUT );
	}	
	
	public streamSocketInfo getSocketInfo()
	{
		return this.socketInfo;
	}
	
	public int getDirectionSocketConnection()
	{
		return this.In_Out;
	}
	
	/**
	 * Direction of socket connection.	 
	 * 
	 * @param in_out == 0 -> a read socket is created.
	 * 			in_out == 1 -> a write socket is create.
	 * 			in_out == otherwise -> a in-out socket (read and send messages) is created (default).
	 */
	public void setDirectionSocketConnetion( int in_out )
	{
		if( in_out != SOCKET_CHANNEL_IN
				&& in_out != SOCKET_CHANNEL_OUT )
		{
			this.In_Out = SOCKET_CHANNEL_INOUT;
		}		
		else
		{
			this.In_Out = in_out;
		}
	}
		
	/**
	 * True if both objects have the same protocol, ip address, port and strem direction.
	 */
	@Override
	public boolean equals(Object obj) 
	{
		boolean equal = obj instanceof streamingParameters;
		
		if( equal )
		{
			streamingParameters pars = ( streamingParameters )obj;
			
			equal = this.In_Out == pars.getDirectionSocketConnection() && this.socketInfo.equals( pars.getSocketInfo() );
		}
		
		return equal;
	} 
}
