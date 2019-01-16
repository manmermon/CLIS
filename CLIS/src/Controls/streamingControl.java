/*
 * Copyright 2011-2018 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package Controls;

import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.WarningMessage;
import GUI.MyComponents.Tuple;
import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamSocketProblem;
import Sockets.Info.streamingOutputMessage;
import Sockets.Info.streamingParameters;
import Sockets.Manager.ManagerInOutStreamSocket;
import Sockets.streamClientSocket;
import Sockets.streamServerSocket;
import StoppableThread.IStoppableThread;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class streamingControl extends controlOfSubordinateTemplate implements ITaskMonitor
{
	private static streamingControl ctrStreaming = null;

	public static final String CLIENT_SOCKET_STREAMING = "client socket";

	public static final String SERVER_SOCKET_STREAMING = "server socket";
	private Map<InetSocketAddress, ManagerInOutStreamSocket> clients = null;
	private streamServerSocket server = null;

	private List<eventInfo> events = null;

	private List<INotificationTask> tasks = null;

	private Semaphore taskSem = null;
	private Semaphore sendMsgSemaphore = null;

	private boolean stopRunLoop = false;

	private boolean deletingSubordinates = false;
	
	public static streamingControl getInstance()
	{
		if (ctrStreaming == null)
		{
			ctrStreaming = new streamingControl();
		}

		return ctrStreaming;
	}

	private streamingControl()
	{
		super();

		super.setName( this.getClass().getSimpleName() );

		this.clients = new HashMap< InetSocketAddress, ManagerInOutStreamSocket >();		
		this.events = new ArrayList< eventInfo >();
		this.tasks = new ArrayList< INotificationTask >();

		this.taskSem = new Semaphore(1, true);
		this.sendMsgSemaphore = new Semaphore(1, true);
	}

	/*
	 * (non-Javadoc)
	 * @see Controls.controlTemplate#startWorking(java.lang.Object)
	 */
	@Override
	protected void startWork( Object info ) throws Exception
	{
		try
		{
			this.sendMsgSemaphore.acquire();
		}
		catch (InterruptedException localInterruptedException) 
		{}

		Tuple< String, List< streamingOutputMessage > > in = ( Tuple< String, List< streamingOutputMessage > > )info;
		if( in.x.equals( CLIENT_SOCKET_STREAMING ) )
		{	
			for( streamingOutputMessage msg : in.y )
			{
				List< streamSocketInfo > dests = msg.getDestinations();

				for( streamSocketInfo sID : dests )
				{
					ManagerInOutStreamSocket ioss = this.clients.get( sID.getSocketAddress() );

					if( ioss != null )
					{
						ioss.sendMessage( msg );
					}
				}
			}
		}
		else if( in.x.equals( SERVER_SOCKET_STREAMING ) )
		{
			for( streamingOutputMessage msg : in.y )
			{
				ManagerInOutStreamSocket ioss = this.clients.get( msg.getOrigin().getSocketAddress() );

				if( ioss != null )
				{
					ioss.sendMessage( msg );
				}
			}
		}
		else
		{
			throw new IllegalArgumentException( "Tuple.x is unknown." );
		}

		if( this.sendMsgSemaphore.availablePermits() < 1 )
		{
			this.sendMsgSemaphore.release();
		}
	}

	protected List<IStoppableThread> createSubordinates(Map<String, Object> parameters) throws Exception
	{
		if (parameters == null)
		{
			throw new NullPointerException("Parameters null.");
		}

		this.deletingSubordinates = false;
		
		List<IStoppableThread> clientList = new ArrayList< IStoppableThread >();

		for (String streamingID : parameters.keySet())
		{
			List<streamingParameters> pars = (List< streamingParameters >)parameters.get(streamingID);

			if (streamingID.equals( CLIENT_SOCKET_STREAMING ))
			{
				for (streamingParameters par : pars)
				{
					try
					{
						ManagerInOutStreamSocket client = streamClientSocket.createClientSocket( par );
						client.taskMonitor( this );
						client.startThread();

						this.clients.put( client.getRemoteSocketInfo().getSocketAddress(), client );

						clientList.add( client );
					}
					catch (Exception e)
					{
						streamSocketProblem problem = new streamSocketProblem(par.getSocketInfo().getSocketAddress(), e);
						this.events.add(new eventInfo( eventType.STREAMING_CONNECTION_PROBLEM, problem));
					}

				}
			} else if (streamingID.equals( SERVER_SOCKET_STREAMING ))
			{
				if (!pars.isEmpty())
				{
					streamingParameters par = (streamingParameters)pars.get(0);

					this.server = new streamServerSocket(par);
					this.server.taskMonitor(this);
					this.server.startThread();

					clientList.add(this.server);
				}
			}
		}

		this.checkEvents();

		return clientList;
	}

	public void taskDone(INotificationTask t)
	{
		try
		{
			this.taskSem.acquire();
		}
		catch (InterruptedException localInterruptedException) 
		{}

		this.tasks.add( t );

		if (this.taskSem.availablePermits() < 1)
		{
			this.taskSem.release();
		}
				
		synchronized( this )
		{
			super.notify();
		}
	}

	public void setSubordinates(String subordinateID, Object parameters)    throws Exception
	{

	}

	protected void cleanUpSubordinates() 
	{

	}

	public synchronized void run()
	{
		while (!this.stopRunLoop)
		{
			try
			{
				super.wait();
			}
			catch (InterruptedException localInterruptedException) 
			{}


			try
			{
				this.taskSem.acquire();
			}
			catch (InterruptedException localInterruptedException1) 
			{}
			
			synchronized( this.tasks ) 
			{	
				for( INotificationTask task : this.tasks )
				{
					List< eventInfo > r = new ArrayList< eventInfo >( task.getResult() );          
					task.clearResult();
					
					for( eventInfo e : r )
					{
						if (e.getEventType().equals(  eventType.STREAMING_INOUT_CHANNEL_CREATED ) )
						{

							ManagerInOutStreamSocket c = (ManagerInOutStreamSocket)e.getEventInformation();
							c.taskMonitor(this);

							c.setReportClosedSocketByInterruptExceptionReader(false);
							c.setReportClosedSocketByInterruptExceptionWriter(true);

							try
							{
								c.startThread();

								streamSocketInfo clientInfo = c.getRemoteSocketInfo();

								if (clientInfo == null)
								{
									clientInfo = c.getLocalSocketInfo();
								}

								this.clients.put( clientInfo.getSocketAddress(), c );
								this.subordiateList.add(c);

							}
							catch (Exception ex)
							{
								streamSocketProblem problem = new streamSocketProblem(c.getRemoteSocketInfo().getSocketAddress(), ex);
								this.events.add(new eventInfo( eventType.STREAMING_CONNECTION_PROBLEM, problem));
							}
						}
						else if (e.getEventType().equals( eventType.STREAMING_INPUT_MSG ))
						{
							this.events.add( e );							
						}
						else if (e.getEventType().equals( eventType.STREAMING_CONNECTION_PROBLEM ))
						{
							this.events.add( e );
						}
						else if( e.getEventType().equals( eventType.THREAD_STOP ) )
						{
							if( !this.deletingSubordinates )
							{
								Tuple<streamSocketInfo, streamSocketInfo> c = (Tuple<streamSocketInfo, streamSocketInfo>)e.getEventInformation();
								streamSocketInfo local = (streamSocketInfo)c.x;
								streamSocketInfo remote = (streamSocketInfo)c.y;

								streamSocketInfo id = remote;
								ManagerInOutStreamSocket ioss = null;

								if (id != null)
								{
									ioss = (ManagerInOutStreamSocket)this.clients.get( id.getSocketAddress() );
									if (ioss == null)
									{
										id = local;
										if (id != null)
										{
											ioss = (ManagerInOutStreamSocket)this.clients.get( id.getSocketAddress() );
										}
									}
								}
								else
								{
									id = local;
									ioss = (ManagerInOutStreamSocket)this.clients.get( id.getSocketAddress() );
								}

								if ( ioss != null )
								{
									this.clients.remove( id.getSocketAddress() );

									boolean clientClose = ( local != null ) 
											&& ( this.server != null ) 
											&& ( !this.server.getSocketParameters().getSocketInfo().equals( local ) );

									if( clientClose )
									{
										this.events.add(new eventInfo( eventType.STREAMING_OUTPUT_SOCKET_CLOSES
												, new streamSocketProblem(id.getSocketAddress()
														, new Exception("The output socket " + id + " is closed."))));
									}

								}
							}
						}
						else if (e.getEventType().equals( eventType.SERVER_THREAD_STOP ))
						{
							this.server = null;
						}
					}
				}

				this.checkEvents();

				this.tasks.clear();
			}

			if (this.taskSem.availablePermits() < 1)
			{
				this.taskSem.release();
			}
		}
	}

	@Override
	public synchronized void deleteSubordinates(int friendliness)
	{
		this.deletingSubordinates = true;
	
		super.deleteSubordinates( friendliness );
	}
	
	private void checkEvents()
	{
		if ( !this.events.isEmpty() )
		{
			if ( super.event != null )
			{
				synchronized( super.event )
				{
					this.event = new eventInfo(  eventType.STREAMING_EVENTS, new ArrayList< eventInfo >( this.events ) );
					this.events.clear();
				}
			}
			else
			{
				super.event = new eventInfo( eventType.STREAMING_EVENTS, new ArrayList< eventInfo >( this.events ) );
				this.events.clear();
			}

			this.supervisor.eventNotification( this, super.event );
		}
	}

	public void removeClientStreamSocket(InetSocketAddress socketID)
	{
		ManagerInOutStreamSocket client = (ManagerInOutStreamSocket)this.clients.get(socketID);

		if (client != null)
		{
			if ( client.isFinished() )
			{
				this.clients.remove(socketID);
			}
			else
			{
				client.stopThread(  IStoppableThread.StopInNextLoopInteraction  );
			}
		}
	}

	public WarningMessage checkParameters()
	{
		return new WarningMessage();
	}
}