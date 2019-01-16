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

import java.util.LinkedHashMap;
import java.util.Map;

public class Commands 
{
	public static String COMMAND_RESET = "reset";
	public static String COMMAND_STOP = "stop";
	public static String COMMAND_START = "start";
	public static String COMMAND_NEXT_TB = "next";
	
	public static String TRIGGERED_EVENT_PRERUN = "prerun";
	public static String TRIGGERED_EVENT_POSRUN = "posrun";
	public static String TRIGGERED_EVENT_NEW_OPERATION = "new_operation";
	public static String TRIGGERED_EVENT_CORRECT_ANSWER = "correct_answer";
	public static String TRIGGERED_EVENT_INCORRECT_ANSWER = "incorrect_answer";
	public static String TRIGGERED_EVENT_TIMEOUT = "time_out";
	public static String TRIGGERED_EVENT_TEST_ENDED = "test_ended";
	public static String TRIGGERED_EVENT_NEW_PHASE = "new_phase";
	public static String TRIGGERED_EVENT_NEXT_TB = "next_tb";
	
	private static String SPECIAL_STRING = "Special characters";
		
	private static Map< String, String > commandlegends = new LinkedHashMap<String, String>();
	private static Map< String, String > eventlegends = new LinkedHashMap<String, String>();
	
	private static Map< String, Integer > outputDataFileMark = new LinkedHashMap< String, Integer >();
	
	static
	{
		commandlegends.put( SPECIAL_STRING, "The character \\ is a escape sequence in these cases: \\n -> newline; \\r -> carriage return; \\t -> tab."
												+ "Using double \\\\ to avoid this.");
		commandlegends.put( COMMAND_START, "The trials are started. If it is activated, the application waits to receive this command." );
		commandlegends.put( COMMAND_STOP, "The trials are stopped." );
		commandlegends.put( COMMAND_RESET, "The trials are begun again." );
		commandlegends.put( COMMAND_NEXT_TB, "Next test-bench file is started." );
		
		eventlegends.put( SPECIAL_STRING, "The character \\ is a escape sequence in these cases: \\n -> newline; \\r -> carriage return; \\t -> tab."
											+ "Using double \\\\ to avoid this.");
		eventlegends.put( TRIGGERED_EVENT_PRERUN, "A message is sent when pre-run phase is started." );
		eventlegends.put( TRIGGERED_EVENT_POSRUN, "A message is sent when pos-run phase is started." );
		eventlegends.put( TRIGGERED_EVENT_NEW_OPERATION, "A message is sent each new operation." );
		eventlegends.put( TRIGGERED_EVENT_CORRECT_ANSWER, "A message is sent when the user answer is correct." );
		eventlegends.put( TRIGGERED_EVENT_INCORRECT_ANSWER, "A message is sent when the user answer is not correct." );
		eventlegends.put( TRIGGERED_EVENT_TIMEOUT, "A message is sent when the timer is over." );
		eventlegends.put( TRIGGERED_EVENT_TEST_ENDED, "A message is sent when the trials finish." );
		eventlegends.put( TRIGGERED_EVENT_NEW_PHASE, "A message is sent for each operation phase." );
		eventlegends.put( TRIGGERED_EVENT_NEXT_TB, "A message is sent when a new test bench file is loaded." );
				
		int mark = 1;
		String[] cmd = getTriggeredEvents();
		for( int i = 0; i < cmd.length; i++ )
		{
			outputDataFileMark.put( cmd[ i ], mark );
			mark = mark << 1;
		}
	}
	
	
	public static String[] getCommands()
	{
		return new String[]{ COMMAND_START, COMMAND_STOP, COMMAND_RESET, COMMAND_NEXT_TB };
	}
	
	public static String[] getTriggeredEvents( )
	{
		return new String[]{ TRIGGERED_EVENT_PRERUN
							, TRIGGERED_EVENT_POSRUN 
							, TRIGGERED_EVENT_NEW_OPERATION
							, TRIGGERED_EVENT_TEST_ENDED
							, TRIGGERED_EVENT_CORRECT_ANSWER
							, TRIGGERED_EVENT_INCORRECT_ANSWER 
							, TRIGGERED_EVENT_TIMEOUT
							, TRIGGERED_EVENT_NEW_PHASE
							, TRIGGERED_EVENT_NEXT_TB
							};
	}
	
	public static Map< String, String > getCommandLengeds( )
	{
		return commandlegends;
	}
	
	public static Map< String, String > getTriggeredEventLengeds( )
	{
		return eventlegends;
	}
	
	public static Map< String, Integer > getOutputDataFileMark()
	{
		return outputDataFileMark;
	}
	
	public static boolean isCommand( String cmd )
	{
		String aux = cmd.toLowerCase();
		
		return COMMAND_RESET.toLowerCase().equals( aux ) 
				|| COMMAND_START.toLowerCase().equals( aux )
				|| COMMAND_STOP.toLowerCase().equals( aux )
				|| COMMAND_NEXT_TB.toLowerCase().equals( aux );
	}
	
	public static boolean isTriggeredEvent( String event )
	{
		String aux = event.toLowerCase();
		return TRIGGERED_EVENT_CORRECT_ANSWER.toLowerCase().equals( aux ) 
				|| TRIGGERED_EVENT_INCORRECT_ANSWER.toLowerCase().equals( aux )
				|| TRIGGERED_EVENT_NEW_OPERATION.toLowerCase().equals( aux ) 
				|| TRIGGERED_EVENT_TEST_ENDED.toLowerCase().equals( aux )
				|| TRIGGERED_EVENT_TIMEOUT.toLowerCase().equals( aux )
				|| TRIGGERED_EVENT_PRERUN.toLowerCase().equals( aux )
				|| TRIGGERED_EVENT_PRERUN.toLowerCase().equals( aux )
				|| TRIGGERED_EVENT_NEW_PHASE.toLowerCase().equals( aux )
				|| TRIGGERED_EVENT_NEXT_TB.toLowerCase().equals( aux );
	}	
}
