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

public class eventType
{
  public static final String THREAD_STOP = "thread stop";
  public static final String SERVER_THREAD_STOP = "server stop";
  
  public static final String ACTIVITY_READY = "activity ready";
  public static final String ACTIVITY_ANSWER = "activity answer";
  public static final String ACTIVITY_END = "activity end";
  public static final String ACTIVITY_NEW_PHASE = "new activity phase";
  public static final String ACTIVITY_PROBLEM = "activity problem";
  public static final String ACTIVITY_SHOWED = "showed activity";
  
  public static final String TEST_END = "test end";
  
  public static final String TIME_OVER = "time_over";
  
  public static final String STREAMING_CHANNEL_CLOSE = "channel close";
  public static final String STREAMING_EVENTS = "stream events";
  public static final String STREAMING_INPUT_MSG = "input message";
  public static final String STREAMING_OUTPUT_MSG_OK = "output message ok";
  public static final String STREAMING_CONNECTION_PROBLEM = "connection problem";
  public static final String STREAMING_OUTPUT_SOCKET_CLOSES = "Output socket closes";
  public static final String STREAMING_INOUT_CHANNEL_CREATED = "socket channel create";
  
  public static final String PROBLEM = "problem";
  public static final String OUTPUT_TEMPORAL_FILE_READY = "output temporal file ready";
  public static final String WARNING = "warning";
  
}