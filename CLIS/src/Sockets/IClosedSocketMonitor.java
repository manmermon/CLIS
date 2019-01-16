package Sockets;

public interface IClosedSocketMonitor 
{
	/**
	 * Notify that the TCP socket is closed.
	 */
	public void closedConnection();
}
