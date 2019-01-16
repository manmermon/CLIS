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

import Activities.Activity;
import Activities.ActivityInfo;
import Activities.ActivityRegister;
import Activities.activityParameters;
import Activities.factoryActivities;
import Auxiliar.BufferAnswers;
import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.WarningMessage;
import Config.ConfigApp;
import Config.Results.ConfigPropertiesResults;
import Config.Results.ConfigResults;
import GUI.ErrorException;
import GUI.MyComponents.Tuple;
import GUI.guiManager;
import GUI.guiParameters;
import Log.log;
import Sockets.Info.streamInputMessage;
import Sockets.Info.streamSocketInfo;
import Sockets.Info.streamSocketProblem;
import Sockets.Info.streamingOutputMessage;
import Sockets.Info.streamingParameters;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;
import Timers.TimerParameters;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import javax.naming.ConfigurationException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class coreControl extends Thread implements IControlLevel1
{
	public static final String CORE_START_WORKING_FIRST = "CORE_START_WORKING_FIRST";
	public static final String CORE_START_WORKING_NEXT_TB = "CORE_START_WORKING_NEXT_TB";
	
	private final String PRE_TIMER = "pre-timer";
	private final String TEST_TIMER = "test-timer";
	private final String ACTIVITY_TIMERS = "activity-timers";
	private final String POST_TIMER = "post-timer";

	protected final int EVENT_ACTIVITY_TIME_OUT = 0;
	protected final int EVENT_ACTIVITY_ANSWER = 1;
	protected final int EVENT_ACTIVITY_END = 2;
	protected final int EVENT_ACTIVITY_NEW_PHASE = 3;

	private Map timersParameters = null;
	private activityParameters activityPars = null;
	private streamInformations streamPars = null;

	private timerControl ctrlTimer = null;
	private activityControl ctrlActivity = null;
	private streamingControl ctrStream = null;
	private guiManager managerGUI = null;

	private controlNotifiedEvent ctrlNotifiedEvents = null;

	private long answerTime = 0L;
	private long testTime = 0L;

	private BufferAnswers answerBuffer = new BufferAnswers( 3 );

	private boolean isPerfomancePhase = false;
	private boolean isWaitingForStartCommand = false;
	private boolean isTestStart = false;

	private static coreControl core = null;

	private ConfigResults cfgTime = null;

	private log myLog = null;
	private boolean addedLogHeader = false;
	private boolean testEndingRegister = false;

	private WarningMessage warnMsg = null;

	public static long tiempo = 0L;

	private boolean sentFirstNewOperationEvent = false;
	
	private String[] testBenchFiles;
	private int tbIindex = 0;

	private boolean showWarningEvent = true;
	private boolean configCopyDone = false;
	
	//private boolean savingOutData = false;
	private boolean closeWhenDoingNothing = false;
	
	
	//ThreadMXBean bean = ManagementFactory.getThreadMXBean();

	private coreControl() throws Exception
	{
		this.setName( this.getClass().getSimpleName() );

		this.createControlUnits();
	}

	public static coreControl getInstance() throws Exception
	{
		if (core == null)
		{
			core = new coreControl();
		}

		return core;
	}

	private void createControlUnits() throws Exception
	{
		/*
		new Thread()
		{
			public void run() 
			{
				this.setName( "PRINTER THREAD ");
				while( true )
				{
					try {
						Thread.sleep( 2000L );
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("//////////////////");
					Map ts = Thread.getAllStackTraces();
					for( Object t : ts.keySet() )
					{
						String info = "coreControl.createControlUnits() "+bean.getThreadInfo( ((Thread)t).getId() );
						info = info.replace( "\n", "");
						if( !info.contains( "Java" ) && !info.contains( "Finalizer" ) 
								&& !info.contains( "Attach" ) && !info.contains( "TimerQueue" )
								&& !info.contains( "Swing") && !info.contains( "Reference" )
								&& !info.contains( "AWT" ) && !info.contains( "Dispatcher" ) 
								&& !info.contains( "Fetcher") )
						{
							if( info.contains( "BLOCKED" ) )
							{
								Toolkit.getDefaultToolkit().beep();
								System.out
										.println("coreControl.createControlUnits() ->"+ctrlNotifiedEvents.eventRegister);
							}
							System.out.println( info );
						}
					}

				}
			};

		}.start();
		 */

		this.ctrlTimer = timerControl.getInstance();
		this.ctrlTimer.setControlSupervisor(this);
		this.ctrlTimer.start();

		this.ctrlActivity = activityControl.getInstance();
		this.ctrlActivity.setControlSupervisor(this);
		this.ctrlActivity.start();

		this.managerGUI = guiManager.getInstance();

		this.ctrStream = streamingControl.getInstance();
		this.ctrStream.setControlSupervisor(this);
		this.ctrStream.start();

		this.ctrlNotifiedEvents = new controlNotifiedEvent();
		this.ctrlNotifiedEvents.setName(this.ctrlNotifiedEvents.getClass().getName());
		this.ctrlNotifiedEvents.startThread();
	}

	public synchronized void startWorking( Object info )
	{
		try
		{	
			System.gc(); // Para limpiar los restos de memoria

			tiempo = System.nanoTime();
			this.answerBuffer.clearBuffer();

			this.warnMsg = new WarningMessage();

			//NOT CHANGE ORDER -->

			guiParameters guiPars = getGUIParameters();

			//create activity and timers
			//TODO
			this.activityPars = getActivityParameters();
			this.timersParameters = getTimerParameters(this.activityPars);

			this.createTimerActivity();

			this.checkCorrectConfiguration();
			if (this.warnMsg.getWarningType() == WarningMessage.ERROR_MESSAGE )
			{
				throw new Exception( this.warnMsg.getMessage() );
			}

			if (this.warnMsg.getWarningType() == WarningMessage.WARNING_MESSAGE )
			{
				String[] opts = { UIManager.getString( "OptionPane.yesButtonText" ), 
						UIManager.getString( "OptionPane.noButtonText" ) };

				int actionDialog = JOptionPane.showOptionDialog( this.managerGUI.getAppUI(), this.warnMsg.getMessage() + "\nContinue?", 
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
						null, opts, opts[1]);

				if ( actionDialog == JOptionPane.CANCEL_OPTION 
						|| actionDialog == JOptionPane.NO_OPTION 
						|| actionDialog == JOptionPane.CLOSED_OPTION )
				{
					this.isTestStart = false;
					this.managerGUI.cleanTestGUI();
					this.managerGUI.restoreGUI();

					return;
				}
			}

			//Configure GUI
			this.managerGUI.applyGUIParameter( guiPars );

			//Create In-Out sockets
			this.streamPars = getStreamingInformations();
			Map< String, Object > sockets = new HashMap< String, Object >();

			if ( !this.streamPars.getClientsInformation().isEmpty() )
			{
				sockets.put( streamingControl.CLIENT_SOCKET_STREAMING, this.streamPars.getClientsInformation());
			}

			if (this.streamPars.getServerInformation() != null)
			{
				List<streamingParameters> server = new ArrayList< streamingParameters >();
				server.add( this.streamPars.getServerInformation() );

				sockets.put( streamingControl.SERVER_SOCKET_STREAMING, server );
			}

			this.ctrStream.addSubordinates(sockets);

			if( (Boolean)ConfigApp.getProperty( ConfigApp.IS_LOG_ACTIVE ) )
			{
				String file = ConfigApp.getProperty( ConfigApp.OUTPUT_FOLDER ).toString();
				file = file.replace( "\\", "/" );
				if ( !file.endsWith( "/" ) )
				{
					file = file + "/";
				}

				File folder = new File( file );
				if( !folder.exists() )
				{
					if( !folder.mkdir() )
					{
						throw new FileSystemException( "Folder " + folder + " not created." );
					}					
				}
				
				file = file + ConfigApp.getProperty( ConfigApp.LOG_FILE_NAME ).toString();
				this.myLog = new log( file );
				this.myLog.startThread();
			}
			

			//<-- Not change order.

			waitStartCommand();
		}
		catch (Exception e)
		{
			this.isTestStart = false;
			this.managerGUI.cleanTestGUI();
			this.managerGUI.restoreGUI();
			
			this.testBenchFiles = null;
			this.tbIindex = 0;
			
			ErrorException.showErrorException(this.managerGUI.getAppUI(), e.getMessage(), "Error", ErrorException.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void checkCorrectConfiguration() throws ConfigurationException
	{
		WarningMessage timerMsg = this.ctrlTimer.checkParameters();
		WarningMessage actMsg = this.ctrlActivity.checkParameters();
		WarningMessage socketMsg = this.ctrStream.checkParameters();
		
		this.warnMsg.setMessage( "", WarningMessage.OK_MESSAGE );

		this.warnMsg.setMessage( this.warnMsg.getMessage() + timerMsg.getMessage(), timerMsg.getWarningType() );
		this.warnMsg.setMessage( this.warnMsg.getMessage() + actMsg.getMessage(), actMsg.getWarningType() );
		this.warnMsg.setMessage( this.warnMsg.getMessage() + socketMsg.getMessage(), socketMsg.getWarningType() );
						
		if( this.testBenchFiles == null )
		{
			boolean thereAreTBFiles = false;
			
			String tbfiles = ConfigApp.getProperty( ConfigApp.TEST_BENCH_FILES ).toString();
			
			if( !tbfiles.isEmpty() )
			{			
				String[] files = tbfiles.split( ";" );
				
				String errorTBHeader = "Caution in test bench files:\n";
				
				String[] errorTB = new String[ files.length ]; 
				int[] warningType = new int[ files.length ];
				
				ConfigApp.copyConfig();
				
				WarningMessage warnTB = new WarningMessage();
				
				try
				{
					int count = 0;	

					boolean ok = true;
					
					int lastFileErrorIndex = -1;
					int lastTBFileIndex = -1;
					
					for( int iF = 0; iF < files.length; iF++ )
					{
						String f = files[ iF ];
						
						errorTB[ iF ]  = "";
						warningType[ iF ] = WarningMessage.OK_MESSAGE;
						
						if( !f.isEmpty() )
						{	
							lastTBFileIndex = iF;
							
							thereAreTBFiles = true;
							
							if( !ConfigApp.loadConfig( new File( f ) ) )
							{
								ok = false;
								errorTB[ iF ] = f;
								warningType[ iF ] = WarningMessage.ERROR_MESSAGE;
							}
							else
							{
								boolean time = (Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_TEST_ACTIVED );
								boolean rep = (Boolean)ConfigApp.getProperty( ConfigApp.IS_REPETITION_ACTIVE );
								
								if( !time && !rep )
								{
									errorTB[ iF ]= "Test bench file " + f + " is infinitely executed (test time and repetitions are not selected).";
									warningType[ iF ] = WarningMessage.ERROR_MESSAGE;
									lastFileErrorIndex = iF;
								}
							}
							
							count++;
						}
					}
					
					if( lastFileErrorIndex == lastTBFileIndex )
					{
						errorTB[ lastFileErrorIndex ] = "Last test bench file is infinitely executed (test time and repetitions are not selected)."; 
						warningType[ lastFileErrorIndex ] = WarningMessage.WARNING_MESSAGE;
					}
					
					if( ok )
					{
						this.testBenchFiles = new String[ count ];
						
						int iF = 0;
						for( String f : files )
						{	 
							 if( !f.isEmpty() )
							 {
								 this.testBenchFiles[ iF ] = f;
								 iF++;
							 }
						}
					}
				}
				catch( Exception ex )
				{
					warnTB.setMessage( warnTB.getMessage() + ex.getMessage(), WarningMessage.ERROR_MESSAGE );
				}		
				finally
				{
					ConfigApp.restoreCopyConfig();
					ConfigApp.removeCopyConfig();
					
					if( thereAreTBFiles )
					{
						boolean time = (Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_TEST_ACTIVED );
						boolean rep = (Boolean)ConfigApp.getProperty( ConfigApp.IS_REPETITION_ACTIVE );
						
						if( !time && !rep )
						{
							warnTB.setMessage( warnTB.getMessage() + "Test bench files are never applied due to test time and repetitions are not selected.\n", WarningMessage.ERROR_MESSAGE );
						}
					}
				}
				
				for( int iEr = 0; iEr < errorTB.length; iEr++ )
				{
					String msg = errorTB[ iEr ];
					
					if( !msg.isEmpty() )
					{
						warnTB.setMessage( warnTB.getMessage() + msg + "\n", warningType[ iEr ] );
					}
				}
				
				if( warnTB.getWarningType() != WarningMessage.OK_MESSAGE )
				{
					warnTB.setMessage( errorTBHeader + warnTB.getMessage(), warnTB.getWarningType() );
				}
				
				this.warnMsg.setMessage( this.warnMsg.getMessage() + warnTB.getMessage(), warnTB.getWarningType() );
			}
		}
	}
	
	private void createTimerActivity() throws Exception
	{
		//create activity
		Map<String, Object> p = new HashMap< String, Object >();
		p.put(this.activityPars.getActivityType(), this.activityPars);
		this.ctrlActivity.addSubordinates(p);

		//Create timers		
		this.ctrlTimer.addSubordinates( this.timersParameters );
	}

	private synchronized void waitStartCommand() throws Exception
	{
		this.isWaitingForStartCommand = this.streamPars.getInputCommands().values().contains( Commands.COMMAND_START );

		if (this.isWaitingForStartCommand)
		{
			guiManager.getInstance().setAppState( "Wait" );
			
			this.managerGUI.showWaitingStartCommand();
		}
		else
		{
			this.preStartPhase();
		}
	}

	private synchronized void preStartPhase() throws Exception
	{
		guiManager.getInstance().setAppState( "Run" );
		
		this.isTestStart = true;
		this.testTime = System.currentTimeMillis();

		if (this.timersParameters.containsKey( this.PRE_TIMER ))
		{
			Boolean bbg = (Boolean)ConfigApp.getProperty( ConfigApp.PREPOS_BLACK_BACKGROUND );

			if (!bbg.booleanValue())
			{
				this.managerGUI.setPrePostMessage("Wait to start...", bbg.booleanValue());
			}
			else
			{
				this.managerGUI.setPrePostMessage("+", bbg.booleanValue());
			}

			this.ctrlTimer.toWorkSubordinates(new Tuple< String, Integer>( this.PRE_TIMER, 0 ) );
			
			this.ctrStream.toWorkSubordinates(getOutputMessage( Commands.TRIGGERED_EVENT_PRERUN ) );
		}
		else
		{
			this.perfomancePhase();
		}
	}

	private synchronized void perfomancePhase()
	{
		try
		{
			this.ctrlTimer.unnecessaryTimer( this.PRE_TIMER, 0);

			this.isPerfomancePhase = true;

			if (this.timersParameters.containsKey( this.TEST_TIMER ) )
			{
				this.ctrlTimer.toWorkSubordinates(new Tuple< String, Integer>( this.TEST_TIMER, 0 ) );
			}

			this.testTime = System.currentTimeMillis();

			this.createActivity();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private synchronized void createActivity()
	{
		try
		{
			this.ctrlActivity.toWorkSubordinates( activityControl.PREPARE_ACTIVITY );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private synchronized void showActivity(JPanel activityPanel)
	{
		try 
		{
			this.ctrStream.toWorkSubordinates( this.getOutputMessage( Commands.TRIGGERED_EVENT_NEW_OPERATION ) );

			this.sentFirstNewOperationEvent = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.managerGUI.showActity( activityPanel );

		String actReport = "";
		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECT_ACTIVITY_REPORT ) )
		{
			actReport = this.ctrlActivity.getActivityReport();
		}

		this.registerLog(Commands.TRIGGERED_EVENT_NEW_OPERATION, System.currentTimeMillis() - this.testTime, actReport);

		this.startOrContinueActivity();
	}

	private synchronized void startOrContinueActivity()
	{
		this.answerTime = System.currentTimeMillis();

		try
		{
			this.ctrlActivity.setBlockingStartWorking(true);
			this.ctrlActivity.startWorking( activityControl.START_OR_CONTINUE_ACTIVITY );
			this.ctrlActivity.setBlockingStartWorking(false);

			int currentPhase = this.ctrlActivity.getNumberOfCurrentPhase();

			boolean isShowNextActPhase = this.ctrlActivity.isShowActivityPhase();

			if ( (!this.sentFirstNewOperationEvent ) 
					&& ( currentPhase != this.activityPars.getFirstEnablePhase() ) 
					&& ( isShowNextActPhase ) )
			{

				this.ctrStream.toWorkSubordinates( this.getOutputMessage( Commands.TRIGGERED_EVENT_NEW_PHASE ) );

				this.registerLog( Commands.TRIGGERED_EVENT_NEW_PHASE, System.currentTimeMillis() - this.testTime, this.ctrlActivity.getActivityReport() );

			}
			else
			{
				this.sentFirstNewOperationEvent = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{

		}
	}

	private void nextActivityTimer(int indexTimer)
	{

		if (this.timersParameters.containsKey( this.ACTIVITY_TIMERS ) && this.ctrlActivity.isShowActivityPhase() )
		{
			Tuple< String, Integer> t = new Tuple< String, Integer>( this.ACTIVITY_TIMERS, indexTimer );

			try
			{
				this.ctrlTimer.toWorkSubordinates(t);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	private synchronized void finishPhase()
	{
		if (this.timersParameters.containsKey( this.POST_TIMER ) )
		{
			try
			{
				this.testEndingRegister = true;
				this.registerLog( Commands.TRIGGERED_EVENT_TEST_ENDED, System.currentTimeMillis() - this.testTime, "");

				Boolean bbg = (Boolean)ConfigApp.getProperty( ConfigApp.PREPOS_BLACK_BACKGROUND );

				if ( !bbg )
				{
					this.managerGUI.setPrePostMessage("Activity End", bbg.booleanValue());
				}
				else
				{
					this.managerGUI.setPrePostMessage("+", bbg.booleanValue());
				}

				this.ctrStream.toWorkSubordinates( this.getOutputMessage(Commands.TRIGGERED_EVENT_POSRUN));

				this.ctrlTimer.toWorkSubordinates(new Tuple< String, Integer >( this.POST_TIMER, 0 ) );

				this.ctrlTimer.unnecessaryTimer( this.TEST_TIMER, 0);
				this.ctrlTimer.unnecessaryTimer( this.ACTIVITY_TIMERS );
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		else 
		{
			stopWorking( true );
		}
	}
					
	public synchronized void stopWorking( boolean nextTestBenchFile )
	{
		if ( this.isTestStart 
				|| this.isWaitingForStartCommand )
		{
			nextTestBenchFile = nextTestBenchFile && this.testBenchFiles != null && tbIindex < testBenchFiles.length;
			
			this.showWarningEvent = !nextTestBenchFile;
			
			guiManager.getInstance().setAppState( "Stop" );
			
			String stopTriggeredEvent = Commands.TRIGGERED_EVENT_TEST_ENDED;
			
			if( nextTestBenchFile )
			{
				stopTriggeredEvent = Commands.TRIGGERED_EVENT_NEXT_TB;
			}
						
			if ( !this.testEndingRegister )
			{
				this.registerLog( stopTriggeredEvent, System.currentTimeMillis() - this.testTime, "");
			}
		
			if ( !this.isWaitingForStartCommand )
			{
				try
				{
					this.ctrStream.setBlockingStartWorking(true);
					this.ctrStream.toWorkSubordinates( this.getOutputMessage( stopTriggeredEvent ) );
					this.ctrStream.setBlockingStartWorking(false);
				}
				catch (Exception localException1)
				{}
			}
			
			this.isTestStart = false;
			this.isPerfomancePhase = false;
			this.isWaitingForStartCommand = false;

			this.ctrlNotifiedEvents.interruptProcess();
			this.ctrlNotifiedEvents.clearEvent();

			this.stopTimerActivityGUI();

			this.testEndingRegister = false;
			this.addedLogHeader = false;
			
			if ( this.myLog != null )
			{
				this.myLog.stopThread( IStoppableThread.StopWithTaskDone );
				this.myLog = null;
			}
			
			this.ctrStream.deleteSubordinates( IStoppableThread.StopWithTaskDone );

			this.cfgTime = null;
			
			System.gc();
			
			if( nextTestBenchFile )
			{
				try
				{	
					if( !this.configCopyDone )
					{
						this.configCopyDone = true;
						ConfigApp.copyConfig();
					}
					
					ConfigApp.loadConfig( new File( this.testBenchFiles[ this.tbIindex ] ) );
									
					this.tbIindex++;
							
					this.startWorking( coreControl.CORE_START_WORKING_NEXT_TB );
				}
				catch( Exception e )
				{
					ErrorException.showErrorException( this.managerGUI.getAppUI(), e.getMessage(), "Error", ErrorException.ERROR_MESSAGE);
					this.tbIindex = 0;
					this.testBenchFiles = null;
				}
			}
			else
			{
				this.managerGUI.restoreGUI();
				//this.managerGUI.cleanTestGUI();
				
				ConfigApp.restoreCopyConfig();
			
				this.configCopyDone = false;
				this.tbIindex = 0;
				this.testBenchFiles = null;
				
				if( this.closeWhenDoingNothing )
				{
					System.exit( 0 );
				}
			}			
		}
	}
	

	private void resetTest()
	{
		if (this.isPerfomancePhase)
		{
			this.stopTimerActivityGUI();

			try
			{
				this.createTimerActivity();

				this.perfomancePhase();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private synchronized void stopTimerActivityGUI()
	{
		this.ctrlTimer.deleteSubordinates( IStoppableThread.ForcedStop );
		this.ctrlActivity.deleteSubordinates( IStoppableThread.ForcedStop );
		this.managerGUI.cleanTestGUI();

		try
		{
			if ((this.cfgTime != null) 
					&& ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_TRAINING ) ) )
			{
				ConfigPropertiesResults c = this.cfgTime.getConfigProperties( this.activityPars.getActivityType() );
				c.increaseNumberTests(this.activityPars.getDifficultLevel(), System.currentTimeMillis() - this.testTime);
				this.cfgTime.saveConfigResults(ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString());
			}
		}
		catch (Exception e)
		{
			String errorMsg = e.getMessage();
			if ((errorMsg == null) || (errorMsg.isEmpty()))
			{
				errorMsg = "" + e.getCause();
			}

			ErrorException.showErrorException(this.managerGUI.getAppUI(), errorMsg + ":" 
					+ ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString(), 
					"Error", ErrorException.ERROR_MESSAGE);
		}
	}

	private synchronized void preRunTimerFinished()
	{
		perfomancePhase();
	}

	private synchronized void testTimerFinished()
	{
		finishPhase();
	}

	private void registerLog(String type, long time, String activityReport)
	{
		if ( ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_LOG_ACTIVE ) ) 
				&& (this.myLog != null ) )
		{
			if ( !this.addedLogHeader )
			{
				this.addedLogHeader = true;

				this.myLog.append("#" + new Date().toString() + "\n");
				this.myLog.append("#Activity = " + this.activityPars.getActivityType() + "\n");
				this.myLog.append("#Difficulty = " + this.activityPars.getDifficultLevel() + "\n");
				this.myLog.append("#=======================\n");
				this.myLog.append("Time Event ");

				if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECT_ACTIVITY_REPORT ) )
				{
					this.myLog.append(this.ctrlActivity.getActivityReportHeader());
				}

				this.myLog.append("\n");
			}

			this.myLog.append(String.format(Locale.US, "%.4f", new Object[] { time * 1e-3D }) + " " + type + " " + activityReport + "\n");
		}
	}

	private synchronized void eventActivityManager(int type, Object info)
	{
		if (type == EVENT_ACTIVITY_NEW_PHASE)
		{
			if ( (Integer)info != this.activityPars.getFirstEnablePhase() )
			{
				this.ctrlActivity.suspendActivity();
				this.startOrContinueActivity();
			}
		}
		else
		{
			this.ctrlActivity.suspendActivity();

			if (type == EVENT_ACTIVITY_END)
			{
				if (this.ctrlActivity.activityWithAdaptableTimer())
				{
					adjustActivityTimer();
				}

				this.ctrlTimer.stopAllTimers( this.ACTIVITY_TIMERS );

				this.createActivity();

			}
			else
			{
				boolean timerEvent = type == this.EVENT_ACTIVITY_TIME_OUT;

				if (timerEvent)
				{
					if (this.ctrlActivity.isAnswerPhase())
					{						
						this.answerBuffer.addAnswer(-1);

						this.managerGUI.setAnswerIcon( guiManager.ANSWER_ICO_TIME_OUT );

						try
						{
							this.ctrStream.toWorkSubordinates( this.getOutputMessage(Commands.TRIGGERED_EVENT_TIMEOUT));
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}


						String actReport = "";
						if( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECT_ACTIVITY_REPORT ) )
						{
							actReport = this.ctrlActivity.getActivityReport();
						}

						registerLog( Commands.TRIGGERED_EVENT_TIMEOUT, System.currentTimeMillis() - this.testTime, actReport);
					}
				}
				else if (this.ctrlActivity.isAnswerPhase())
				{
					if ( (Boolean)info )
					{
						this.managerGUI.setAnswerIcon( guiManager.ANSWER_ICO_CORRECT );

						if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_TRAINING ) )
						{
							if (this.cfgTime != null)
							{
								int dificult = this.activityPars.getDifficultLevel();
								this.cfgTime.addTimeOutProperties(System.currentTimeMillis() - this.answerTime, dificult, this.activityPars.getActivityType());
							}
						}

						this.managerGUI.sumResulteLevelIndicator(1);

						if (this.managerGUI.isGreenLevelIndicator())
						{
							this.answerBuffer.addAnswer(3);
						}

						this.answerBuffer.addAnswer(1);

						try
						{
							this.ctrStream.toWorkSubordinates( this.getOutputMessage( Commands.TRIGGERED_EVENT_CORRECT_ANSWER));
						}
						catch (Exception localException3) 
						{}

						String actReport = "";
						if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECT_ACTIVITY_REPORT ) )
						{
							actReport = this.ctrlActivity.getActivityReport();
						}

						registerLog( Commands.TRIGGERED_EVENT_CORRECT_ANSWER, System.currentTimeMillis() - this.testTime, actReport);
					}
					else
					{
						this.managerGUI.setAnswerIcon( guiManager.ANSWER_ICO_INCORRECT );

						this.answerBuffer.addAnswer(-1);

						this.managerGUI.sumResulteLevelIndicator( -2 );

						try
						{
							this.ctrStream.toWorkSubordinates( this.getOutputMessage( Commands.TRIGGERED_EVENT_INCORRECT_ANSWER));
						}
						catch (Exception localException5) 
						{}


						String actReport = "";
						if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECT_ACTIVITY_REPORT ) )
						{
							actReport = this.ctrlActivity.getActivityReport();
						}

						this.registerLog( Commands.TRIGGERED_EVENT_INCORRECT_ANSWER, System.currentTimeMillis() - this.testTime, actReport);
					}

					this.ctrlTimer.stopTimer( this.ACTIVITY_TIMERS, this.ctrlActivity.getNumberOfCurrentPhase());
				}

				this.startOrContinueActivity();
			}
		}
	}

	private void adjustActivityTimer()
	{
		if (this.answerBuffer.getSumAnswer() <= -3)
		{
			this.answerBuffer.clearBuffer();

			if (this.timersParameters.get( this.ACTIVITY_TIMERS ) != null)
			{
				this.ctrlTimer.sumRelativeValueToAllTimer( this.ACTIVITY_TIMERS, 1.1F);

			}
		}
		else if (this.answerBuffer.getSumAnswer() >= 3)
		{
			this.answerBuffer.clearBuffer();

			if (this.timersParameters.get( this.ACTIVITY_TIMERS ) != null)
			{
				this.ctrlTimer.sumRelativeValueToAllTimer( this.ACTIVITY_TIMERS, 0.9F);
			}
		}
	}

	private synchronized void postRunTimerFinished()
	{
		stopWorking( true );
	}

	public synchronized void updateClockDisplay(long newValue, String timerString)
	{
		if (this.isTestStart)
		{
			this.managerGUI.setTestClock(newValue, timerString);
		}
	}

	public void eventNotification(IControlLevel2 subordinate, eventInfo event)
	{
		this.ctrlNotifiedEvents.registreNotification( event );

		this.ctrlNotifiedEvents.treatEvent();
	}

	private synchronized void eventTimerManager(String timerName, int timerIndex)
	{
		if( this.PRE_TIMER.equals( timerName ) )
		{
			this.preRunTimerFinished();
		}		
		else if( this.ACTIVITY_TIMERS.equals( timerName ) )
		{			
			//System.out.println((System.nanoTime()-tiempo)+"-coreControl.eventTimerManager() <"+timerName+", "+timerIndex+">");
			this.eventActivityManager( EVENT_ACTIVITY_TIME_OUT, timerIndex );
		}
		else if( this.TEST_TIMER.equals( timerName ) )
		{
			this.testTimerFinished();
		}
		else if( this.POST_TIMER.equals( timerName ) )
		{
			this.postRunTimerFinished();
		}
		else
		{
			throw new IllegalArgumentException( "Unknown timer." );
		}
	}

	private synchronized void eventSocketMessagesManager(List<eventInfo> EVENTS)
	{
		if ((EVENTS != null) && (!EVENTS.isEmpty()))
		{
			for (eventInfo event : EVENTS)
			{
				if (event.getEventType().equals( eventType.STREAMING_INPUT_MSG ))
				{
					streamInputMessage msg = (streamInputMessage)event.getEventInformation();

					String commanType = (String)this.streamPars.getInputCommands().get(msg.getMessage());

					if (commanType != null)
					{
						if (commanType.equals(Commands.COMMAND_STOP))
						{
							stopWorking( false );
						}
						else if(commanType.equals(Commands.COMMAND_NEXT_TB))
						{
							stopWorking( true );
						}
						else if (commanType.equals(Commands.COMMAND_START))
						{
							if ((!this.isTestStart) 
									&&  (this.isWaitingForStartCommand))
							{
								this.isWaitingForStartCommand = false;

								try
								{
									this.preStartPhase();
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
						else if ( commanType.equals( Commands.COMMAND_RESET))
						{
							this.resetTest();
						}
					}
				}
				else if (event.getEventType().equals( eventType.STREAMING_CONNECTION_PROBLEM ))
				{
					streamSocketProblem problem = (streamSocketProblem)event.getEventInformation();

					String msg = problem.getProblemCause().getMessage();
					if (msg.isEmpty())
					{
						msg = problem.getProblemCause().getCause().toString();
					}

					this.ctrStream.removeClientStreamSocket( problem.getSocketAddress() );

					ErrorException.showErrorException( managerGUI.getAppUI(), msg, 
							eventType.STREAMING_CONNECTION_PROBLEM, 
							ErrorException.WARNING_MESSAGE );
				}
				else if (event.getEventType().equals(  eventType.STREAMING_OUTPUT_SOCKET_CLOSES ))
				{
					streamSocketProblem problem = (streamSocketProblem)event.getEventInformation();

					String msg = problem.getProblemCause().getMessage();
					if (msg.isEmpty())
					{
						msg = problem.getProblemCause().getCause().toString();
					}

					ErrorException.showErrorException( managerGUI.getAppUI(), msg, 
							eventType.STREAMING_OUTPUT_SOCKET_CLOSES, 
							ErrorException.WARNING_MESSAGE );
				}
			}
		}
	}
	
	public boolean isDoingSomething()
	{
		boolean doing = this.isTestStart;
				
		return doing;
	}
	
	public void closeWhenDoingNothing( boolean close  )
	{
		this.closeWhenDoingNothing = close;
		
		if( this.closeWhenDoingNothing )
		{
			System.exit( 0 );
		}
	}
	
	private Tuple<String, List<streamingOutputMessage>> getOutputMessage(String outputMsgID)
	{
		List<streamingOutputMessage> msgList = new ArrayList< streamingOutputMessage >();

		Map<String, List<streamSocketInfo>> outMSGs = this.streamPars.getOutputCommands().get( outputMsgID );

		if (outMSGs != null)
		{
			Set< String > msgSet = outMSGs.keySet();
			for (String msg : msgSet)
			{
				List<streamSocketInfo> dests = outMSGs.get( msg );

				msgList.add(new streamingOutputMessage( msg, null, dests));
			}
		}

		return new Tuple<String, List<streamingOutputMessage>>( streamingControl.CLIENT_SOCKET_STREAMING, msgList);
	}

	private guiParameters getGUIParameters() throws ConfigurationException
	{
		guiParameters pars = new guiParameters();

		pars.setFullScreen( (Boolean)ConfigApp.getProperty( ConfigApp.IS_FULLSCREEN ) );
		pars.setVisibleTestClock( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SHOW_TIMER_ACTIVE ) );
		pars.setIdScreen( (Integer)ConfigApp.getProperty( ConfigApp.SELECTED_SCREEN ) );

		if (this.cfgTime == null)
		{
			this.cfgTime = new ConfigResults( ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString() );
		}

		if (this.activityPars == null)
		{
			this.activityPars = getActivityParameters();
		}

		String task = this.activityPars.getActivityType();

		pars.setInitialAnswerLevel( 0 );

		int dif = (Integer)ConfigApp.getProperty( ConfigApp.DIFICULTY_INDEX_SELECTED );
		boolean isClockOn = (Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_TEST_ACTIVED );
		long time = (Long)ConfigApp.getProperty( ConfigApp.VALUE_TEST_TIME ) * 60L * 1000L;
		double target = 0.0D;

		if (ActivityRegister.isChallegeActivity( task ))
		{
			ConfigPropertiesResults c = this.cfgTime.getConfigProperties( task );

			if (c == null)
			{
				this.cfgTime.addTimeOutProperties(30000L, 
						(Integer)ConfigApp.getProperty( ConfigApp.DIFICULTY_INDEX_SELECTED ), 
						task);

				c = this.cfgTime.getConfigProperties(task);
			}

			target = 1.5D * c.getNumberCorrectAnswer(dif);
			if (isClockOn)
			{
				target /= c.getTestTime(dif);
				target *= time;
			}
			else
			{
				target /= c.getNumberTest(dif);
			}
		}

		if (target < 1.0D)
		{
			target = 1.0D;
		}

		pars.setUserAnswerTarget((int)Math.ceil(target));

		pars.setTimeValue( (int)( (Long)ConfigApp.getProperty( ConfigApp.VALUE_TEST_TIME ) * 60L ) );

		return pars;
	}

	private Map<String, List<TimerParameters>> getTimerParameters(activityParameters actPars) throws ConfigurationException
	{
		if (actPars == null)
		{
			throw new IllegalArgumentException("Activity parameters null");
		}

		HashMap<String, List<TimerParameters>> pars = new HashMap<String, List<TimerParameters>>();

		//PRE-RUN
		long t = (Long)ConfigApp.getProperty( ConfigApp.TIME_PRERUN ) * 1000L;
		boolean sound = (Boolean)ConfigApp.getProperty( ConfigApp.TIME_PRERUN_SOUND  );

		List<TimerParameters> par = new ArrayList< TimerParameters >();

		if (t > 0L)
		{
			par.add(new TimerParameters(t, sound, false));
			pars.put( this.PRE_TIMER, par);
		}

		//TEST TIMER
		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_TEST_ACTIVED ) )
		{
			t = (Long)ConfigApp.getProperty( ConfigApp.VALUE_TEST_TIME ) * 1000L * 60L;

			par = new ArrayList< TimerParameters >();
			par.add(new TimerParameters(t, (Boolean)ConfigApp.getProperty( ConfigApp.IS_COUNTDOWN_SOUND_ACTIVE ), true ) );
			pars.put( this.TEST_TIMER, par);
		}

		//ACTIVITY TIMER
		par = new ArrayList< TimerParameters >();

		boolean[] enablePhases = actPars.getEnablePhases();
		ActivityInfo info = actPars.getActivityInfo();

		List<Long> nonAnswerTimersList = (List< Long >)ConfigApp.getProperty( ConfigApp.NON_ANSWER_TIMERS );

		if (nonAnswerTimersList.size() < enablePhases.length - 1)
		{
			throw new ConfigurationException("Length of non-answer-timer list is lower than to number of activity phases. "
					+ "Expected length: " + ( enablePhases.length - 1));
		}

		Iterator<Long> nonAnswerTimers = nonAnswerTimersList.iterator();

		for (int i = 0; i < info.getPhaseNumber(); i++)
		{
			TimerParameters p = null;

			if (i != info.getMainPhase() - 1)
			{
				t = 0L;

				if (nonAnswerTimers.hasNext())
				{
					t = ((Long)nonAnswerTimers.next()).longValue() * 1000L;
				}


				if ( enablePhases[i] )
				{
					p = new TimerParameters(t, false, false);
				}
			}

			par.add(p);
		}

		if ( ((Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_OUT_AUTO_ACTIVE ) ) 
				|| ((Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_OUT_MANUAL_ACTIVE ) ) )
		{
			t = (Long)ConfigApp.getProperty( ConfigApp.VALUE_TIME_OUT_MANUAL ) * 1000L;

			if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_OUT_AUTO_ACTIVE ) )
			{
				if (this.cfgTime == null)
				{
					this.cfgTime = new ConfigResults( ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString() );
				}


				String task = ActivityRegister.getActivityID( (Integer)ConfigApp.getProperty( ConfigApp.TASK_INDEX_SELECTED ) );
				ConfigPropertiesResults c = this.cfgTime.getConfigProperties(task);

				int dif = (Integer)ConfigApp.getProperty( ConfigApp.DIFICULTY_INDEX_SELECTED ) ;
				t = c.getAnswerTime(dif);
				t /= c.getNumberCorrectAnswer(dif);
			}

			TimerParameters p = null;
			if (t > 0L)
			{
				p = new TimerParameters(t, false, false);
			}

			if (par.size() >= info.getMainPhase())
			{
				par.set(info.getMainPhase() - 1, p);
			}
			else
			{
				par.add(info.getMainPhase() - 1, p);
			}
		}

		if (!par.isEmpty())
		{
			pars.put( this.ACTIVITY_TIMERS, par);

			if (enablePhases.length > 1)
			{
				boolean find = false;
				int i = 0;
				while( (i < enablePhases.length) && (!find) )
				{
					find = enablePhases[i];
					if( !find )
					{
						i++;
					}
				}

				if ( (find) && (i < enablePhases.length))
				{
					TimerParameters p = (TimerParameters)par.get(i);
					if (p == null)
					{
						this.warnMsg.setMessage(this.warnMsg.getMessage() + "First timer is infinity\n", 1);
					}
				}
			}
		}

		//POS-RUN
		t = (Long)ConfigApp.getProperty( ConfigApp.TIME_POSRUN ) * 1000L;
		sound = (Boolean)ConfigApp.getProperty( ConfigApp.TIME_POSRUN_SOUND );
		if (t > 0L)
		{
			par = new ArrayList< TimerParameters >();
			par.add(new TimerParameters(t, sound, false));
			pars.put( this.POST_TIMER, par);
		}

		return pars;
	}

	private activityParameters getActivityParameters() throws ConfigurationException
	{
		activityParameters pars = new activityParameters();

		String actType = ActivityRegister.getActivityID( (Integer)ConfigApp.getProperty( ConfigApp.TASK_INDEX_SELECTED ) );

		pars.setActivityType(actType);

		pars.setDifficultLevel( (Integer)ConfigApp.getProperty( ConfigApp.DIFICULTY_INDEX_SELECTED ) );

		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_REPETITION_ACTIVE ) )
		{
			pars.setRepetitions( (Integer)ConfigApp.getProperty( ConfigApp.VALUE_REPETITION ) );
		}

		pars.addExtraParameter(  Activity.ID_PARAMETER_X_AXIS, (Boolean)ConfigApp.getProperty( ConfigApp.TRAJECTORY_INVERTED_XAXIS ));
		pars.addExtraParameter(  Activity.ID_PARAMETER_Y_AXIS, (Boolean)ConfigApp.getProperty( ConfigApp.TRAJECTORY_INVERTED_YAXIS ));

		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_SET_SIZE, (Integer)ConfigApp.getProperty( ConfigApp.SAM_SET_SIZE ) );

		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECTED_SLIDE ) )
		{
			pars.addExtraParameter( Activity.ID_PARAMETER_SLIDES, ConfigApp.getProperty( ConfigApp.PATH_SLIDES ) );
		}

		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECTED_SOUND ) )
		{
			pars.addExtraParameter(  Activity.ID_PARAMETER_SOUND_CLIPS, ConfigApp.getProperty( ConfigApp.PATH_SOUNDS ) );
		}

		pars.addExtraParameter( Activity.ID_PARAMETER_RANDOM_SAMPLES, ConfigApp.getProperty( ConfigApp.RANDOM_AFFECTIVE_SAMPLES_TYPE ) );
		pars.addExtraParameter( Activity.ID_PARAMETER_PRESERVE_SLIDE_SOUND_ORDER, ConfigApp.getProperty( ConfigApp.IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE ));
		pars.addExtraParameter( Activity.ID_PARAMETER_SLIDE_GROUP_MAIN, ConfigApp.getProperty( ConfigApp.IS_SELECTED_SLIDE_MAIN_GROUP ));
		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_DOMINANCE, ConfigApp.getProperty( ConfigApp.IS_SAM_DOMINANCE ) );
		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_BEEP, ConfigApp.getProperty( ConfigApp.SAM_BEEP_ACTIVE ) );

		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_VALENCE_RANGE, ConfigApp.getProperty( ConfigApp.SAM_VALENCE_SCALE ) );
		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_AROUSAL_RANGE, ConfigApp.getProperty( ConfigApp.SAM_AROUSAL_SCALE ) );
		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_DOMINANCE_RANGE, ConfigApp.getProperty( ConfigApp.SAM_DOMINANCE_SCALE ) );
		pars.addExtraParameter( Activity.ID_PARAMETER_SAM_EMOTION_SET, ConfigApp.getProperty( ConfigApp.SAM_EMOTION_SET ) );
		
		pars.addExtraParameter(  Activity.ID_PARAMETER_WAIT_SOUND_END, 
				(Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECTED_TIME_UNTIL_SOUND_END ));

		pars.addExtraParameter(  Activity.ID_ACTIVITY_PANEL_SIZE, guiManager.getInstance().getActivityPanelSize() );


		ActivityInfo info = factoryActivities.getActivity(actType).getActivityInfo();
		pars.setActivityInfo(info, ActivityRegister.isPluginActivity(actType));

		boolean[] enablePhases = new boolean[info.getPhaseNumber()];

		List<Long> nonAnswerTimersList = (List< Long >)ConfigApp.getProperty( ConfigApp.NON_ANSWER_TIMERS );

		if (nonAnswerTimersList.size() < enablePhases.length - 1)
		{
			throw new ConfigurationException("Length of non-answer-timer list is lower than to number of activity phases."
					+ " Expected length: " + ( enablePhases.length - 1));
		}
		
		Iterator<Long> nonAnswerTimers = nonAnswerTimersList.iterator();
		for (int i = 0; i < enablePhases.length; i++)
		{
			enablePhases[i] = true;

			if (i != info.getMainPhase() - 1)
			{
				if (nonAnswerTimers.hasNext())
				{
					enablePhases[i] = nonAnswerTimers.next() != 0L;
				}
				else
				{
					enablePhases[i] = false;
				}
			}
		}

		pars.setEnablePhases(enablePhases);

		return pars;
	}

	private streamInformations getStreamingInformations()
	{
		streamInformations infos = new streamInformations();

		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SOCKET_SERVER_ACTIVE ) )
		{
			Map<String, Object[][]> socketTable = (Map< String, Object[][]>)ConfigApp.getProperty( ConfigApp.SERVER_SOCKET_TABLE );

			Set< String > SOCKETS = socketTable.keySet();
			Object[][] cmdTable;

			for( String socket : SOCKETS )
			{
				//Socket information
				String[] socketInfo = socket.split( ":" );

				String protocol = socketInfo[ 0 ];
				String ip = socketInfo[ 1 ];
				int port = new Integer( socketInfo[ 2 ] );

				int protocol_type = streamSocketInfo.TCP_PROTOCOL;					
				if( protocol.equals( "UDP" ) )
				{
					protocol_type = streamSocketInfo.UDP_PROTOCOL;
				}

				streamSocketInfo info = new streamSocketInfo( protocol_type, ip, port);

				infos.setServerInformation( new streamingParameters( info, streamingParameters.SOCKET_CHANNEL_IN ) );

				//In-out Messages
				cmdTable = socketTable.get( info.toString() );

				for( int i = 0; i < cmdTable.length; i++ )
				{
					if( (Boolean)cmdTable[ i ][ 0 ] )
					{
						String commandType = cmdTable[ i ][ 1 ].toString().toLowerCase();
						String command = this.createMessage( cmdTable[ i ][ 2 ].toString() );

						infos.addInputCommands( command, commandType );
					}
				}
			} 
		} 


		if ( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SOCKET_CLIENT_ACTIVE ) )
		{
			Map<String, Object[][]> socketTable = (Map< String, Object[][]>)ConfigApp.getProperty( ConfigApp.CLIENT_SOCKET_TABLE );

			Set<String> SOCKETS = socketTable.keySet();
			Object[][] cmdTable;
			for( String socket : SOCKETS )
			{
				//Socket information
				String[] socketInfo = socket.split( ":" );

				String protocol = socketInfo[ 0 ];
				String ip = socketInfo[ 1 ];
				int port = new Integer( socketInfo[ 2 ] );

				int protocol_type = streamSocketInfo.TCP_PROTOCOL;					
				if( protocol.equals( "UDP" ) )
				{
					protocol_type = streamSocketInfo.UDP_PROTOCOL;
				}

				streamSocketInfo info = new streamSocketInfo( protocol_type, ip, port);

				infos.addClientInformations( new streamingParameters( info, streamingParameters.SOCKET_CHANNEL_OUT ) );

				//In-out Messages
				cmdTable = socketTable.get( info.toString() );

				for( int i = 0; i < cmdTable.length; i++ )
				{
					if( (Boolean)cmdTable[ i ][ 0 ] )
					{
						String commandType = cmdTable[ i ][ 1 ].toString().toLowerCase();
						String command = this.createMessage( cmdTable[ i ][ 2 ].toString() );

						infos.addOutputCommand( commandType, command, info );
					}
				}
			}
		}

		return infos;
	}

	private String createMessage(String msg)
	{
		if (msg != null)
		{
			String[] special = { "\\t", "\\r", "\\n" };
			String[] speChars = { "\t", "\r", "\n" };
			for (int i = 0; i < special.length; i++)
			{
				String msgAux = "";
				String str = special[i];

				int index = 0;
				int init = 0;
				while (index >= 0)
				{
					index = msg.substring(init).indexOf( "\\" + str);
					if (index < 0)
					{
						msgAux = msgAux + msg.substring(init).replaceAll(new StringBuilder("\\").append(str).toString(), speChars[i]);
					}
					else
					{
						msgAux = msgAux + msg.substring(init, index);
						msgAux = msgAux.replaceAll( new StringBuilder( "\\" ).append(str).toString(), speChars[i] ) + str;
						init = index + str.length() + 1;

						if (init >= msg.length())
						{
							index = -1;
						}
					}
				}

				msg = msgAux;
			}
		}

		return msg;
	}

	public boolean controlNotifiedEventSemBlock()
	{
		return this.ctrlNotifiedEvents.controlNotifySemphore.availablePermits() == 0;
	}


	///////////////////////////////////////
	//
	//

	private class controlNotifiedEvent extends AbstractStoppableThread implements ITaskMonitor
	{
		private LinkedHashMap<String, Object> eventRegister = new LinkedHashMap<String, Object>();
		private boolean treatEvent = true;

		private coreControl.controlNotifiedManager ctrlManager = null;

		private Semaphore controlNotifySemphore = null;
		private Semaphore eventRegisterSemaphore = null;
		private boolean rejectAnswerTimerOverEvent = false;

		public controlNotifiedEvent()
		{
			this.controlNotifySemphore = new Semaphore(1, true);
			this.eventRegisterSemaphore = new Semaphore(1, true);
		}


		protected void preStopThread(int friendliness) throws Exception
		{}

		protected void postStopThread(int friendliness) throws Exception
		{
			try
			{
				this.eventRegisterSemaphore.acquire();
			}
			catch (Exception localException) 
			{}

			this.ctrlManager.stopThread( IStoppableThread.ForcedStop );
			this.ctrlManager = null;
			this.eventRegister.clear();

			if (this.eventRegisterSemaphore.availablePermits() < 1)
			{
				this.eventRegisterSemaphore.release();
			}
		}

		protected void runInLoop() throws Exception
		{
			if (this.eventRegister.size() == 0)
			{
				super.wait();
			}

			try
			{
				this.eventRegisterSemaphore.acquire();
			}
			catch (Exception localException ) 
			{}

			synchronized (this.eventRegister)
			{				
				if (this.ctrlManager == null || this.ctrlManager.getState().equals( State.TERMINATED ) )
				{
					this.rejectAnswerTimerOverEvent = ((this.eventRegister.keySet().contains(  eventType.ACTIVITY_ANSWER ) ) 
							||  (this.eventRegister.keySet().contains( eventType.TIME_OVER ) ) );

					this.ctrlManager = new controlNotifiedManager( this.eventRegister );
					this.ctrlManager.taskMonitor( this );

					this.eventRegister.clear();

					this.ctrlManager.startThread();
				}
			}

			if (this.eventRegisterSemaphore.availablePermits() < 1)
			{
				this.eventRegisterSemaphore.release();
			}
		}

		public void registreNotification(eventInfo event)
		{
			try
			{
				this.controlNotifySemphore.acquire();
			}
			catch (InterruptedException localInterruptedException) 
			{}      


			String event_type = event.getEventType();
			Object event_Info = event.getEventInformation();

			boolean reg = true;

			try
			{
				this.eventRegisterSemaphore.acquire();
			}
			catch (Exception localException) 
			{}

			synchronized (this.eventRegister)
			{

				if (this.eventRegister.size() > 0)
				{
					if ((this.eventRegister.containsKey( eventType.TEST_END )) 
							|| ( (this.eventRegister.containsKey( eventType.TIME_OVER ) ) 
									&& (this.eventRegister.get( eventType.TIME_OVER ).toString().equals( TEST_TIMER ) ) ) )
					{

						reg = false;
					}
					else if ( (event_type.equals( eventType.ACTIVITY_ANSWER ) ) 
							&& (this.eventRegister.containsKey( eventType.TIME_OVER )) 
							&& (this.eventRegister.get( eventType.TIME_OVER ).toString().equals( ACTIVITY_TIMERS )))
					{
						reg = false;
					}
					else if ((event_type.equals( eventType.TIME_OVER )) 
							&& (event_Info.toString().equals( ACTIVITY_TIMERS )) 
							&& (this.eventRegister.containsKey( eventType.ACTIVITY_ANSWER )))
					{
						reg = false;
					}
				}


				if ( reg )
				{
					if (event_type.equals( eventType.STREAMING_EVENTS ) )
					{
						List<eventInfo> storedEvents = (List<eventInfo>)this.eventRegister.get(event_type);
						List<eventInfo> newEvents = (List<eventInfo>)event_Info;
						Set<String> setEvents = new HashSet<String>();

						if (storedEvents != null)
						{
							Iterator<eventInfo> itEvent = storedEvents.iterator();

							while (itEvent.hasNext())
							{
								eventInfo e = (eventInfo)itEvent.next();

								if (setEvents.contains(e.getEventType()))
								{
									itEvent.remove();
								}
								else
								{
									setEvents.add(e.getEventType());
								}
							}
						}

						Iterator<eventInfo> itEvent = newEvents.iterator();
						while (itEvent.hasNext())
						{
							eventInfo e = itEvent.next();

							if (setEvents.contains(e.getEventType()))
							{
								itEvent.remove();
							}
							else
							{
								setEvents.add(e.getEventType());
							}
						}

						if (storedEvents != null)
						{
							newEvents.addAll(storedEvents);
						}

						event_Info = newEvents;
					}

					if ( this.rejectAnswerTimerOverEvent )
					{
						if ( (!event_type.equals( eventType.ACTIVITY_ANSWER ) ) 
								&& (!event_type.equals(  eventType.TIME_OVER  ) ) )
						{
							this.eventRegister.put(event_type, event_Info);
						}

					}
					else 
					{
						this.eventRegister.put(event_type, event_Info);
					}
				}
			}

			if (this.eventRegisterSemaphore.availablePermits() < 1)
			{
				this.eventRegisterSemaphore.release();
			}

			if (this.controlNotifySemphore.availablePermits() < 1)
			{
				this.controlNotifySemphore.release();
			}
		}

		public synchronized void treatEvent()
		{
			System.out.println("coreControl.controlNotifiedEvent.treatEvent() " );
			if (super.getState().equals( Thread.State.WAITING ) )
			{
				this.treatEvent = true;
				super.notify();
			}
		}

		public void interruptProcess()
		{
			this.treatEvent = false;
		}

		public void clearEvent()
		{
			try
			{
				this.eventRegisterSemaphore.acquire();
			}
			catch (Exception localException) 
			{}

			synchronized (this.eventRegister)
			{
				if (this.eventRegister.size() > 0)
				{
					this.eventRegister.clear();

					super.interrupt();
				}
			}

			if (this.eventRegisterSemaphore.availablePermits() < 1)
			{
				this.eventRegisterSemaphore.release();
			}
		}

		protected void runExceptionManager(Exception e)
		{
			if (!(e instanceof InterruptedException))
			{
				e.printStackTrace();
				ErrorException.showErrorException(coreControl.this.managerGUI.getAppUI(), e.getMessage(), 
						"Exception in " + getClass().getSimpleName(), 
						ErrorException.ERROR_MESSAGE);
			}
		}

		public void taskDone(INotificationTask task) throws Exception
		{
			try
			{
				this.eventRegisterSemaphore.acquire();
			}
			catch (Exception localException) 
			{}
			
			//this.ctrlManager = null;			
			this.rejectAnswerTimerOverEvent = false;

			if (this.eventRegisterSemaphore.availablePermits() < 1)
			{
				this.eventRegisterSemaphore.release();
			}
		}
	}


	////////////////////////////////
	//
	//
	private class controlNotifiedManager extends AbstractStoppableThread implements INotificationTask
	{
		private LinkedHashMap<String, Object> eventRegister = new LinkedHashMap<String, Object>();

		private ITaskMonitor monitor;

		public controlNotifiedManager( Map< String, Object > events )
		{
			super.setName( this.getClass().getName() );
			
			if (events != null)
			{
				for (String event : events.keySet())
				{
					this.eventRegister.put( event, events.get( event ) );
				}
			}
		}

		protected void preStart() throws Exception
		{
			super.preStart();
		}


		protected void preStopThread(int friendliness) throws Exception
		{}

		protected void postStopThread(int friendliness)  throws Exception
		{
			this.eventRegister.clear();
			this.eventRegister = null;
		}

		protected void runInLoop() throws Exception
		{
			if (this.eventRegister.size() > 0)
			{
				String event_type = (String)this.eventRegister.keySet().iterator().next();
				final Object eventObject = this.eventRegister.get(event_type);

				this.eventRegister.remove(event_type);

				if (event_type.equals( eventType.TIME_OVER ))
				{
					eventTimerManager( (( Tuple< String, Integer >)eventObject).x.toString()
							, (Integer)((Tuple< String, Integer>)eventObject).y );
				}
				else if (event_type.equals( eventType.ACTIVITY_ANSWER ))
				{
					eventActivityManager( EVENT_ACTIVITY_ANSWER, eventObject);
				}
				else if (event_type.equals( eventType.ACTIVITY_END ))
				{
					eventActivityManager( EVENT_ACTIVITY_END, eventObject);
				}
				else if (event_type.equals( eventType.ACTIVITY_NEW_PHASE ))
				{
					nextActivityTimer( (Integer)eventObject );
				}
				else if (event_type.equals( eventType.ACTIVITY_READY ))
				{
					showActivity( (JPanel)eventObject );
				}
				else if (event_type.equals( eventType.TEST_END ) )
				{
					finishPhase();
				}
				else if (event_type.equals( eventType.ACTIVITY_PROBLEM ))
				{
					stopWorking( false );

					String errorMsg = ((Exception)eventObject).getMessage();
					if (errorMsg.isEmpty())
					{
						errorMsg = errorMsg + ((Exception)eventObject).getCause();
					}

					ErrorException.showErrorException(coreControl.this.managerGUI.getAppUI(), 
							errorMsg, "activity problem", 
							ErrorException.ERROR_MESSAGE);
				}
				else if (event_type.equals( eventType.STREAMING_EVENTS ))
				{
					eventSocketMessagesManager((List< eventInfo> )eventObject);
				}
				else if (event_type.equals( eventType.PROBLEM ) )
				{
					stopWorking( false );

					ErrorException.showErrorException(coreControl.this.managerGUI.getAppUI(), 
									eventObject.toString(), 
									"activity problem", 
									ErrorException.ERROR_MESSAGE);
				}
				else if (event_type.equals( eventType.WARNING ) )
				{
					if( showWarningEvent )
					{
						new Thread()
						{
							public void run()
							{
								ErrorException.showErrorException( managerGUI.getAppUI(), 
										eventObject.toString(), 
										"warning", 
										ErrorException.WARNING_MESSAGE);
							}
						}.start();
					}
				}
			}
		}

		protected void targetDone() throws Exception
		{
			super.targetDone();

			this.stopThread = this.eventRegister.isEmpty();
		}

		protected void runExceptionManager(Exception e)
		{
			if (!(e instanceof InterruptedException))
			{
				e.printStackTrace();
				ErrorException.showErrorException( managerGUI.getAppUI(), e.getMessage(), 
						"Exception in " + getClass().getSimpleName(), 
						ErrorException.ERROR_MESSAGE);
			}
		}

		protected void cleanUp() throws Exception
		{
			super.cleanUp();

			if (this.monitor != null)
			{
				this.monitor.taskDone(this);
			}
		}


		public void taskMonitor(ITaskMonitor monitor)
		{
			this.monitor = monitor;
		}


		public List<eventInfo> getResult()
		{
			return null;
		}

		public void clearResult() 
		{

		}
	}
}