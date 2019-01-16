package Sockets;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamingParameters;
import Sockets.Manager.ManagerInOutStreamSocket;

public class streamClientSocket   
{
	public static ManagerInOutStreamSocket createClientSocket( streamingParameters socketPars ) throws Exception
	{
		if( socketPars == null )
		{
			throw new IllegalArgumentException( "Null parameters." );
		}
		
		ManagerInOutStreamSocket ioss = null;
		
		AbstractSelectableChannel channel;
		if( socketPars.getSocketInfo().getProtocolType() == streamSocketInfo.TCP_PROTOCOL )
		{
			channel = SocketChannel.open();
			channel.configureBlocking( true );
			((SocketChannel) channel).socket().setReuseAddress( true );
			((SocketChannel) channel).connect( socketPars.getSocketInfo().getSocketAddress() );
		}
		else //if( socketInfo.getProtocolType() == streamingParameters.UDP_PROTOCOL )
		{
			channel = DatagramChannel.open();
			channel.configureBlocking( true );
			((DatagramChannel) channel).socket().setReuseAddress( true );
			
			((DatagramChannel) channel).connect( socketPars.getSocketInfo().getSocketAddress() );				
		}
		
		if( channel != null )
		{	
			ioss = new ManagerInOutStreamSocket( channel, socketPars.getDirectionSocketConnection() );
		}
		
		return ioss;
	}
}
