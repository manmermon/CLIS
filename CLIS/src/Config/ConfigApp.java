/*
 * Copyright 2011-2018 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General public static License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General public static License for more details.
 *
 *   You should have received a copy of the GNU General public static License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package Config;

import Auxiliar.AffectiveObject;
import Auxiliar.EmotionParameter;
import Auxiliar.RandomSort;
import Config.Language.Language;
import Controls.Commands;
import Excepciones.DefaultValueException;
import GUI.MyComponents.IPAddressValidator;
import GUI.MyComponents.NumberRange;
import GUI.MyComponents.Tuple;
import Sockets.Info.streamSocketInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import Activities.IActivity;

public class ConfigApp
{
	// Language
	public static final String LANGUAGE = "LANGUAGE";

	public static final String SET_TIME = "SET_TIME";
	public static final String SET_SOCKET = "SET_SOCKET";
	public static final String fullNameApp = "CLIS (Cognitive Load Increases Stress)";
	public static final String shortNameApp = "CLIS";
	public static final Calendar buildDate = new GregorianCalendar( 2018, 12 - 1, 18 );
	
	public static final String version = "Version 4." + ( buildDate.get( Calendar.YEAR ) % 100 ) + "." + ( buildDate.get( Calendar.MONTH ) + 1 );
	
	public static final String appDateRange = "2011-" + buildDate.get( Calendar.YEAR );
	public static final String defaultPathFile = System.getProperty("user.dir") + File.separatorChar;

	public static final String defaultNameFileConfigResults = "configResults.dat";

	public static final String defaultNameLogFile = "log.txt";  

	public static final String defaultNameFileConfigExtension = "cfg";
	
	public static String defaultNameFileConfig = "config." + defaultNameFileConfigExtension;
	
	public static final String defaultNameOutputDataFile = "data.clis";

	public static final String defaultNameActivityReport = "report.txt";

	public static final String TEST_BENCH_FILES = "TEST_BENCH_FILES";
	
	public static final String SAM_SET_SIZE = "SAM_SIZE_SET";

	public static final String IS_TRAINING = "IS_TRAINING";

	public static final String IS_FULLSCREEN = "IS_FULLSCREEN";

	public static final String TASK_INDEX_SELECTED = "TASK_INDEX_SELECTED";

	public static final String DIFICULTY_INDEX_SELECTED = "DIFICULTY_INDEX_SELECTED";

	public static final String OUTPUT_FOLDER = "OUTPUT_FOLDER";

	public static final String IS_LOG_ACTIVE = "IS_LOG_ACTIVE";

	public static final String LOG_FILE_NAME = "LOG_FILE_NAME";

	public static final String IS_SHOW_TIMER_ACTIVE = "IS_SHOW_TIMER_ACTIVE";

	public static final String IS_COUNTDOWN_SOUND_ACTIVE = "IS_COUNTDOWN_SOUND_ACTIVE";

	public static final String IS_SELECTED_SLIDE = "IS_SELECTED_SLIDE";

	public static final String IS_SELECTED_SOUND = "IS_SELECTED_SOUND";

	public static final String IS_SELECTED_TIME_UNTIL_SOUND_END = "IS_SELECTED_TIME_UNTIL_SOUND_END";

	public static final String RANDOM_AFFECTIVE_SAMPLES_TYPE = "RANDOM_AFFECTIVE_SAMPLES_TYPE";

	public static final String RANDOM_ORDER_INTERBLOCKS = "RANDOM_ORDER_INTERBLOCKS";

	public static final String IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE = "IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE";

	public static final String IS_SELECTED_SLIDE_MAIN_GROUP = "IS_SELECTED_SLIDE_MAIN_GROUP";

	public static final String IS_SELECT_ACTIVITY_REPORT = "IS_SELECT_ACTIVITY_REPORT";

	public static final String PATH_SLIDES = "PATH_SLIDES";

	public static final String PATH_SOUNDS = "PATH_SOUNDS";

	public static final String AFFECTIVE_GROUPS = "AFFECTIVE_GROUPS";

	public static final String AFFECTIVE_BLOCKS = "AFFECTIVE_BLOCKS";

	public static final String IS_SAM_DOMINANCE = "IS_SAM_DOMINANCE";

	public static final String SELECTED_SCREEN = "SELECTED_SCREEN";

	public static final String TRAJECTORY_INVERTED_XAXIS = "TRAJECTORY_INVERTED_XAXIS";

	public static final String TRAJECTORY_INVERTED_YAXIS = "TRAJECTORY_INVERTED_YAXIS";

	public static final String IS_REPETITION_ACTIVE = "IS_REPETITION_ACTIVE";

	public static final String VALUE_REPETITION = "VALUE_REPETITION";

	/************
	 * 
	 * Timer parameters
	 * 
	 */

	public static final String TIME_PRERUN_SOUND = "TIME_PRERUN_SOUND";

	public static final String TIME_POSRUN_SOUND = "TIME_POSRUN_SOUND";

	public static final String TIME_PRERUN = "TIME_PRERUN";

	public static final String TIME_POSRUN = "TIME_POSRUN";

	public static final String PREPOS_BLACK_BACKGROUND = "PREPOS_BLACK_BACKGROUND";

	public static final String IS_TIME_TEST_ACTIVED = "IS_TIME_TEST_ACTIVE";

	public static final String VALUE_TEST_TIME = "VALUE_TEST_TIME";

	public static final String IS_TIME_OUT_AUTO_ACTIVE = "IS_TIME_OUT_AUTO_ACTIVE";

	public static final String VALUE_TIME_OUT_AUTO = "VALUE_TIME_OUT_AUTO";

	public static final String IS_TIME_OUT_MANUAL_ACTIVE = "IS_TIME_OUT_MANUAL_ACTIVE";

	public static final String VALUE_TIME_OUT_MANUAL = "VALUE_TIME_OUT_MANUAL";

	public static final String PATH_FILE_TIME_OUT_AUTO = "PATH_FILE_TIME_OUT_AUTO";

	public static final String NON_ANSWER_TIMERS = "NON_ANSWER_TIMERS";


	/***********
	 * 
	 * Socket	
	 *  
	 */
	public static final String IS_SOCKET_SERVER_ACTIVE = "IS_SOCKET_SERVER_ACTIVE";

	public static final String IS_SOCKET_CLIENT_ACTIVE = "IS_SOCKET_CLIENT_ACTIVE";

	public static final String CLIENT_SOCKET_TABLE = "CLIENT_SOCKET_TABLE";

	public static final String SERVER_SOCKET_TABLE = "SERVER_SOCKET_TABLE";

	/****************
	 * 
	 * Lab Streaming Layer
	 * 
	 */

	//TODO DELETED
	/*
	public static final String LSL_ID_DEVICES = "LSL_ID_DEVICES";

	public static final String LSL_OUTPUT_FILE_NAME = "LSL_OUT_FILE_NAME";

	public static final String LSL_OUTPUT_FILE_FORMAT = "LSL_OUT_FILE_FORMAT";
	*/
	/**********
	 *
	 * SAM features
	 * 
	 */
	
	public static final String SAM_VALENCE_SCALE = "SAM_VALENCE_SCALE";
	public static final String SAM_AROUSAL_SCALE = "SAM_AROUSAL_SCALE";
	public static final String SAM_DOMINANCE_SCALE = "SAM_DOMINANCE_SCALE";
	public static final String SAM_EMOTION_SET = "SAM_EMOTION_SCALE";
	public static final String SAM_BEEP_ACTIVE = "SAM_BEEP_ACTIVE";
	
	////////////////////////

	private static Map< String, Object > listConfig = new HashMap< String, Object >();
	private static Map< String, Object > copyListConfig = new HashMap< String, Object >();
	private static Map< String, Class > list_Key_Type = new HashMap< String, Class >();
	private static Map< String, NumberRange > list_Key_RankValues = new HashMap< String, NumberRange >();

	static
	{
		create_Key_Value();
		create_Key_Type_Set();
		create_Key_RankValues();
	}

	private static void create_Key_Value()
	{
		listConfig.clear();

		loadDefaultProperties();
	}

	private static void create_Key_Type_Set()
	{
		list_Key_Type.clear();

		list_Key_Type.put( LANGUAGE, String.class);

		list_Key_Type.put( IS_TRAINING, Boolean.class);
		list_Key_Type.put( IS_FULLSCREEN, Boolean.class);
		list_Key_Type.put( TASK_INDEX_SELECTED, Integer.class);
		list_Key_Type.put( SELECTED_SCREEN, Integer.class);

		list_Key_Type.put( DIFICULTY_INDEX_SELECTED, Integer.class);
		list_Key_Type.put( TIME_PRERUN_SOUND, Boolean.class);
		list_Key_Type.put( TIME_POSRUN_SOUND, Boolean.class);
		list_Key_Type.put( TIME_PRERUN, Long.class);
		list_Key_Type.put( TIME_POSRUN, Long.class);
		list_Key_Type.put( PREPOS_BLACK_BACKGROUND, Boolean.class);

		list_Key_Type.put( IS_TIME_TEST_ACTIVED, Boolean.class);
		list_Key_Type.put( VALUE_TEST_TIME, Long.class);
		list_Key_Type.put( IS_REPETITION_ACTIVE, Boolean.class);
		list_Key_Type.put( VALUE_REPETITION, Integer.class);
		list_Key_Type.put( SAM_SET_SIZE, Integer.class);

		list_Key_Type.put( IS_TIME_OUT_AUTO_ACTIVE, Boolean.class);
		list_Key_Type.put( PATH_FILE_TIME_OUT_AUTO, String.class);
		list_Key_Type.put( IS_TIME_OUT_MANUAL_ACTIVE, Boolean.class);
		list_Key_Type.put( VALUE_TIME_OUT_MANUAL, Long.class);
		list_Key_Type.put( IS_LOG_ACTIVE, Boolean.class);
		list_Key_Type.put( OUTPUT_FOLDER, String.class);
		list_Key_Type.put( LOG_FILE_NAME, String.class);

		list_Key_Type.put( TRAJECTORY_INVERTED_XAXIS, Boolean.class);
		list_Key_Type.put( TRAJECTORY_INVERTED_YAXIS, Boolean.class);

		list_Key_Type.put( IS_SELECT_ACTIVITY_REPORT, Boolean.class);

		list_Key_Type.put( PATH_SLIDES, List.class);
		list_Key_Type.put( PATH_SOUNDS, List.class);
		list_Key_Type.put( AFFECTIVE_GROUPS, Integer.class);
		list_Key_Type.put( AFFECTIVE_BLOCKS, Integer.class);
		list_Key_Type.put( IS_SAM_DOMINANCE, Boolean.class);

		list_Key_Type.put( IS_SELECTED_SLIDE, Boolean.class);
		list_Key_Type.put( IS_SELECTED_SOUND, Boolean.class);
		list_Key_Type.put( IS_SELECTED_TIME_UNTIL_SOUND_END, Boolean.class);
		list_Key_Type.put( RANDOM_AFFECTIVE_SAMPLES_TYPE, Integer.class);
		list_Key_Type.put( IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE, Boolean.class);
		list_Key_Type.put( IS_SELECTED_SLIDE_MAIN_GROUP, Boolean.class);
		list_Key_Type.put( RANDOM_ORDER_INTERBLOCKS, Integer.class);

		list_Key_Type.put( IS_SHOW_TIMER_ACTIVE, Boolean.class);
		list_Key_Type.put( IS_COUNTDOWN_SOUND_ACTIVE, Boolean.class);

		list_Key_Type.put( CLIENT_SOCKET_TABLE, Map.class);
		list_Key_Type.put( SERVER_SOCKET_TABLE, Map.class);

		list_Key_Type.put( IS_SOCKET_CLIENT_ACTIVE, Boolean.class);
		list_Key_Type.put( IS_SOCKET_SERVER_ACTIVE, Boolean.class);


		//TODO DELETED
		/*
		list_Key_Type.put( LSL_ID_DEVICES, HashSet.class);
		list_Key_Type.put( LSL_OUTPUT_FILE_FORMAT, String.class);
		list_Key_Type.put( LSL_OUTPUT_FILE_NAME, String.class);
		*/

		list_Key_Type.put( NON_ANSWER_TIMERS, List.class);
		
		list_Key_Type.put( SAM_VALENCE_SCALE, Integer.class );
		list_Key_Type.put( SAM_AROUSAL_SCALE, Integer.class );
		list_Key_Type.put( SAM_DOMINANCE_SCALE, Integer.class );
		list_Key_Type.put( SAM_EMOTION_SET, List.class );
		list_Key_Type.put( TEST_BENCH_FILES, String.class );
		list_Key_Type.put( SAM_BEEP_ACTIVE, Boolean.class );
	}

	private static void create_Key_RankValues()
	{
		list_Key_RankValues.clear();

		list_Key_RankValues.put( TASK_INDEX_SELECTED, new NumberRange( 0, Integer.MAX_VALUE ) );
		list_Key_RankValues.put( DIFICULTY_INDEX_SELECTED, new NumberRange(0, IActivity.NUMBER_DIFICUL_LEVELS - 1  ) );
		list_Key_RankValues.put( TIME_PRERUN, new NumberRange(0.0D, Double.MAX_VALUE));
		list_Key_RankValues.put( TIME_POSRUN, new NumberRange(0.0D, Double.MAX_VALUE));
		list_Key_RankValues.put( VALUE_TEST_TIME, new NumberRange(0.0D, Double.MAX_VALUE));
		list_Key_RankValues.put( VALUE_REPETITION, new NumberRange(0.0D, Double.MAX_VALUE));
		list_Key_RankValues.put( SELECTED_SCREEN, new NumberRange(0, Integer.MAX_VALUE));

		list_Key_RankValues.put( RANDOM_AFFECTIVE_SAMPLES_TYPE, new NumberRange(0, RandomSort.NUMBER_RANDOM_SAMPLE_TYPE - 1 ) );
		list_Key_RankValues.put( RANDOM_ORDER_INTERBLOCKS, new NumberRange(0, Integer.MAX_VALUE));

		list_Key_RankValues.put( AFFECTIVE_GROUPS, new NumberRange(1, Integer.MAX_VALUE));
		list_Key_RankValues.put( AFFECTIVE_BLOCKS, new NumberRange(1, Integer.MAX_VALUE));

		list_Key_RankValues.put( VALUE_TIME_OUT_MANUAL, new NumberRange(0.0D, Double.MAX_VALUE) );

		list_Key_RankValues.put( NON_ANSWER_TIMERS, new NumberRange( -1.0D, Double.MAX_VALUE) );
		list_Key_RankValues.put( SAM_SET_SIZE, new NumberRange(1, Integer.MAX_VALUE) );
		
		list_Key_RankValues.put( SAM_VALENCE_SCALE, new NumberRange( 1, 9 ) );
		list_Key_RankValues.put( SAM_AROUSAL_SCALE, new NumberRange( 1, 9 ) );
		list_Key_RankValues.put( SAM_DOMINANCE_SCALE, new NumberRange( 1, 9 ) );
	}

	public static void copyConfig()
	{
		copyListConfig.clear();
		
		for( String key : listConfig.keySet() )
		{
			copyListConfig.put( key, listConfig.get( key ) );
		}
	}
	
	public static void restoreCopyConfig()
	{
		if( !copyListConfig.isEmpty() )
		{
			loadDefaultProperties( );
			
			for( String key : copyListConfig.keySet() )
			{
				listConfig.put( key, copyListConfig.get( key ) );
			}
		}
	}
	
	public static void removeCopyConfig()
	{
		copyListConfig.clear();
	}
	
	public static void saveConfig(File f) throws Exception
	{
		defaultNameFileConfig = f.getName();
		
		Properties prop = new Properties();

		Iterator<String> it = listConfig.keySet().iterator();

		while (it.hasNext())
		{
			String key = (String)it.next();

			if ((key.equals( CLIENT_SOCKET_TABLE )) || 
					(key.equals( SERVER_SOCKET_TABLE )))
			{
				Map<String, Object[][]> table = (Map<String, Object[][]>)listConfig.get( key );
		
				if( table.isEmpty() )
				{
					if( key.equals( CLIENT_SOCKET_TABLE ) )
					{
						loadDefaultValueClientSocketTable();
					}
					else
					{
						loadDefaultValueServerSocketTable();
					}
					
					table = (Map<String, Object[][]>)listConfig.get( key );
				}
				
				
				Set< Map.Entry< String, Object[][] > > map = table.entrySet();

				String s = "{";
				for ( Map.Entry< String, Object[][] > ent : map)
				{
					s = s + "{" + (String)ent.getKey() + "=";
					s = s + Arrays.deepToString((Object[])ent.getValue()) + "}";
				}
				s = s + "}";

				prop.setProperty(key, s);
			}
			//TODO DELETED
			/*
			else if( key.equals( LSL_ID_DEVICES ) )
			{
				String p = listConfig.get( key ).toString();
				p = p.replace( ">, <", ">; <" );
				prop.setProperty( key, p );
			}
			*/
			else
			{
				prop.setProperty(key, listConfig.get(key).toString());
			}
		}

		FileOutputStream fOut = new FileOutputStream(f, false);

		prop.store(fOut, "");
		fOut.close();
	}

	public static boolean loadConfig( File f ) throws Exception
	{
		defaultNameFileConfig = f.getName();
		
		Properties prop = new Properties();
		FileInputStream propFileIn = null;
		
		boolean res = true;

		try
		{
			propFileIn = new FileInputStream(f);

			prop.load( propFileIn );
						
			String msg = checkProperties( prop );			
			
			if( !msg.isEmpty() )
			{
				res = false;
				
				throw new DefaultValueException( "Setting error in " + f + "\n" + msg );
			}
		}
		catch (DefaultValueException e)
		{
			res = false;
			
			throw new Exception(e.getMessage());
		}
		catch (Exception e)
		{
			loadDefaultProperties();
			
			res = false;
			
			throw new Exception(e.getMessage() + ": Default Parameters load.");
		}
		
		return res;
	}

	public static boolean setProperty(String propertyID, Object value)
	{
		boolean ok = true;

		if (value.getClass().equals(listConfig.get(propertyID).getClass()))
		{
			listConfig.put(propertyID, value);
		}
		else
		{
			ok = false;
		}

		return ok;
	}

	public static Object getProperty(String propertyID)
	{
		Object ob = listConfig.get(propertyID);
		return ob;
	}

	private static String checkProperties(Properties prop) throws DefaultValueException
	{
		loadDefaultProperties();

		String checkPropErrorMsg = "";
		String defaultMsg = "Error in config file. Some parameters loaded to its default value:\n";
		
		boolean defaultValue = false;

		Iterator<Object> it = prop.keySet().iterator();

		while (it.hasNext())
		{
			String key = (String)it.next();
			Class clase = list_Key_Type.get(key);

			String p = prop.getProperty(key);

			if (clase != null)
			{
				if (clase.isInstance(new Boolean(false)))
				{
					if ((p == null) || ((!p.toLowerCase().equals( "true" )) && 
							(!p.toLowerCase().equals( "false" ))))
					{
						defaultValue = true;
						
						defaultMsg += key + "; ";
					}

					listConfig.put(key, new Boolean(p));
				}
				else if ((clase.isInstance(new Integer(0))) || (clase.isInstance(new Long(0L))))
				{
					try
					{
						double v = new Double(p);

						NumberRange rank = list_Key_RankValues.get(key);

						if (rank != null)
						{
							if (!rank.within( v ) )
							{
								loadDefaultValue(key);
								defaultValue = true;

								defaultMsg += key + "; ";
							}
							else if (clase.isInstance(new Integer(0)))
							{
								listConfig.put(key, new Integer( (int)v ) );
							}
							else
							{
								listConfig.put(key, new Long( (long)v ) );
							}

						}
						else
						{
							loadDefaultValue(key);
							defaultValue = true;
							
							defaultMsg += key + "; ";
						}
					}
					catch (NumberFormatException e)
					{
						loadDefaultValue(key);
						defaultValue = true;
						
						defaultMsg += key + "; ";
					}
				}
				else if (clase.getCanonicalName().equals(Map.class.getCanonicalName()))
				{
					loadDefaultValue(key);
					boolean allOK = true;

					if (p == null)
					{
						allOK = false;
					}
					else
					{
						String[] strings = p.replace( " ", "" ).replace( "}}", "}").split( "}" );

						Map<String, Object[][]> map = new HashMap<String, Object[][]>();
						for (int j = 0; j < strings.length; j++)
						{
							String[] textTable = strings[j].replace( "{", "" ).split( "=" );

							if (textTable.length != 2)
							{
								allOK = false;
							}
							else
							{
								String socketInfo = checkSocket(textTable[0].split( ":" ) );
								if (socketInfo == null)
								{
									allOK = false;
								}
								else
								{
									String[] table = textTable[1].replace( "[[", "").replace( "]]", "").split( "],");

									Object[][] contentTable = getDefaultCommandSocketTable(key);

									int numCols = contentTable.length;

									for (int i = 0; i < table.length; i++)
									{
										String[] strs = table[i].replace( "[", "").replace( "]", "").split( "," );

										if (strs.length > numCols)
										{
											allOK = false;
										}
										else
										{
											try
											{
												boolean b = new Boolean( strs[0] );

												for (int m = 0; m < contentTable.length; m++)
												{
													if (contentTable[m][1].equals(strs[1].toUpperCase()))
													{
														contentTable[m][0] = b;
														for (int k = 1; k < strs.length; k++)
														{
															contentTable[m][k] = strs[k];
														}

														break;
													}
												}
											}
											catch (Exception e)
											{
												allOK = true;
											}
										}
									}

									map.put(socketInfo, contentTable);
								}
							}
						}

						if (!map.isEmpty())
						{
							listConfig.put(key, map);
						}
					}

					if (!allOK)
					{
						defaultValue = true;
						
						defaultMsg += key + "; ";
					}
				}
				else if( clase.getCanonicalName().equals(List.class.getCanonicalName() ) )
				{
					List<String> values = new ArrayList<String>();

					p = p.replaceAll( "\\[", "");
					p = p.replaceAll( "\\]", "");
					p = p.replaceAll( "\\<", "");
					p = p.replaceAll( "\\>", "");

					String[] paths = p.split( "," );

					for (int i = 0; i < paths.length; i++)
					{
						String path = paths[i];
						if (!path.isEmpty())
						{
							values.add(path.trim());
						}
					}

					if ((key.equals( PATH_SLIDES )) || (key.equals( PATH_SOUNDS )))
					{
						int affectType = 0;
						if (key.equals( PATH_SOUNDS ))
						{
							affectType = AffectiveObject.IS_SOUND;
						}

						List<AffectiveObject> filePaths = new ArrayList<AffectiveObject>();

						if (values.size() % 6 != 0)
						{
							defaultValue = true;
							
							defaultMsg += key + "; ";
						}
						else
						{
							int size = 5;
							int iVal = size;
							boolean allOk = true;
							while (iVal < values.size())
							{
								String path = (String)values.get(iVal - size);
								int grp = 1;
								int block = 1;
								boolean fix = false;
								boolean sam = true;
								boolean beep = false;

								try
								{
									grp = new Integer( values.get(iVal - size + 1) );

									NumberRange rank = (NumberRange)list_Key_RankValues.get( AFFECTIVE_GROUPS );
									boolean ok = rank.within(Integer.valueOf(grp));
									if (!ok)
									{
										grp = 1;
										defaultValue = true;
										allOk = false;
									}
								}
								catch (Exception e)
								{
									defaultValue = true;
									allOk = false;
									break;
								}

								try
								{
									block = new Integer( values.get(iVal - size + 2) );

									NumberRange rank = (NumberRange)list_Key_RankValues.get( AFFECTIVE_BLOCKS );
									boolean ok = rank.within(Integer.valueOf(block));
									if (!ok)
									{
										block = 1;
										defaultValue = true;
										
										allOk = false;
									}
								}
								catch (Exception e)
								{
									defaultValue = true;
									allOk = false;
									break;
								}

								try
								{
									fix = new Boolean( values.get(iVal - size + 3));
								}
								catch (Exception e)
								{
									defaultValue = true;
									allOk = false;
									break;
								}

								try
								{
									sam = new Boolean( values.get(iVal - size + 4));
								}
								catch (Exception e)
								{
									defaultValue = true;
									allOk = false;
									break;
								}

								try
								{
									beep = new Boolean( values.get(iVal));
								}
								catch (Exception e)
								{
									defaultValue = true;
									allOk = false;
									break;
								}

								AffectiveObject fs = new AffectiveObject(path, grp, block, fix, sam, beep, affectType);

								filePaths.add(fs);

								iVal += size + 1;
							}
							
							if( !allOk )
							{
								defaultMsg += key + "; ";
							}
						}

						listConfig.put(key, filePaths);
					}
					else if (key.equals( NON_ANSWER_TIMERS ))
					{
						try
						{
							List<Long> nonAnswerTimers = new ArrayList<Long>();

							NumberRange rank = list_Key_RankValues.get(key);

							Iterator<String> itV = values.iterator();

							boolean ok = true;

							while ((itV.hasNext()) && (ok))
							{
								Long t = new Long( itV.next() );
								ok = rank.within(t);
								if (!ok)
								{
									loadDefaultValue(key);
									defaultValue = true;
									
									defaultMsg += key + "; ";
								}
								else
								{
									nonAnswerTimers.add(t);
								}
							}

							listConfig.put(key, nonAnswerTimers);
						}
						catch (NumberFormatException e)
						{
							loadDefaultValue(key);
							defaultValue = true;
							
							defaultMsg += key + "; ";
						}
					}
					else if( key.equals( SAM_EMOTION_SET ) )
					{
						loadDefaultValue( SAM_EMOTION_SET );
						
						if( values.size() % 3 == 0)
						{
							try
							{
								List< EmotionParameter > emPars = (List< EmotionParameter >)getProperty( SAM_EMOTION_SET );
								
								List< Integer > typeEmotions = new ArrayList< Integer >();
								
								for( int type = 1; type <= emPars.size(); type++ )
								{
									typeEmotions.add( type );
								}
								
								boolean allOk = true;
								for( int i = 2; i < values.size(); i += 3 )
								{
									int type = new Integer( values.get( i - 2) );
									String text = values.get( i - 1 );
									boolean sel = new Boolean( values.get( i ) );
											
									Iterator< Integer > itType = typeEmotions.iterator();
									boolean find = false;
									while( itType.hasNext() && !find )
									{
										int t = itType.next();
										
										find  = t == type;
										if( find )
										{
											itType.remove();
										}
									}
									
									if( type > 0 && type <= emPars.size() )
									{
										EmotionParameter em = emPars.get( type - 1 );
										
										em.setSelect( sel );
										em.setText( text );
									}
									else
									{
										defaultValue = true;
										allOk = false;
									}
								}
								
								for( Integer t : typeEmotions )
								{
									EmotionParameter em = emPars.get( t - 1 );
									em.setSelect( false );
								}
								
								if( !allOk )
								{
									defaultMsg += key + "; ";
								}
								
							}
							catch( Exception e )
							{
								defaultValue = true;
								
								defaultMsg += key + "; ";
							}
						}
						else
						{
							defaultValue = true;
							
							defaultMsg += key + "; ";
						}
					}
					else
					{
						listConfig.put(key, values);
					}
				}
				else if (clase.isInstance(new String()))
				{
					if (p == null)
					{
						loadDefaultValue(key);
						defaultValue = true;
						
						defaultMsg += key + "; ";
					}
					else
					{
						listConfig.put(key, p);

					}
				}
				else if (clase.getCanonicalName().equals(Tuple.class.getCanonicalName()))
				{
					if (p.equals(new Tuple(null, null).toString()))
					{
						loadDefaultValue( key );
					}
					else
					{
						p = p.replace( "<", "").replace( ">", "").replace( " ", "");
						String[] values = p.split( ",");

						if (values.length != 2)
						{
							defaultValue = true;
							loadDefaultValue(key);
							
							defaultMsg += key + "; ";
						}
						else
						{
							listConfig.put(key, new Tuple< String, String >(values[0], values[1]));
						}            
					}
				}
				//TODO DELETED
				/*
				else if( clase.getCanonicalName().equals( HashSet.class.getCanonicalName() ) )
				{
					if( key.equals( LSL_ID_DEVICES ) )
					{

						LSL.StreamInfo[] results = LSL.resolve_streams();
						
						HashSet< LSLConfigParameters > lsldevs = new HashSet< LSLConfigParameters >();
						
						p = p.replace("[",  "").replace("]", "");
						if( !p.isEmpty() )
						{
							String[] devices = p.split( ">;");
							
							String msgDevNonFound = "";
													
							int countDevOff = 0;
							boolean allOk = true;
							
							for( String dev : devices )
							{
								dev = dev.replace( " ", "").replace( "<", "" ).replace( ">", "" );
								String[] devInfo = dev.split( "," );
								if( devInfo.length == 7 )
								{
									String sourceID = devInfo[ 0 ];
									String name = devInfo[ 1 ];
									String type = devInfo[ 2 ];
									String info = devInfo[ 3 ];
																		
									boolean select = false;
									
									try 
									{
										select = new Boolean( devInfo[ 4 ] );
									}
									catch (Exception e) 
									{
									}
									
									boolean interleaved = false;
									
									try 
									{
										interleaved = new Boolean( devInfo[ 6 ] );
									}
									catch (Exception e) 
									{
									}
									
									int chunckSize = 1; 
									try
									{
										chunckSize = new Integer( devInfo[ 5 ] );
									}
									catch( Exception e)
									{}
									
									String uid = checkLSL( sourceID, name, type, results );
									
									if( !uid.isEmpty() )
									{
										lsldevs.add( new LSLConfigParameters( uid, name, type, sourceID
																			, info, select, chunckSize, interleaved ) );
									}
									else
									{			
										msgDevNonFound += "<" + sourceID + ", " + name + ", " + type + ">; ";
										countDevOff++;
										
										if( countDevOff > 4 )
										{
											msgDevNonFound += "\n";
											countDevOff = 0;
										}
										
										defaultValue = true;
										allOk = false;
									}
								}
								else
								{
									defaultValue = true;
									allOk = false;
								}
							}
							
							if( !msgDevNonFound.isEmpty() )
							{
								defaultMsg += "LSL devices non found:\n" + msgDevNonFound;
							}
							
							listConfig.put(key, lsldevs );
							
							if( !allOk )
							{
								defaultMsg += key + "; ";
							}
						}
					}
				}
				*/
			}
			else 
			{
				System.out.println(key + ": non-defined parameter.");
			}
		}
		
		if( !prop.keySet().containsAll(list_Key_Type.keySet() ) )
		{
			defaultValue = true;
						
			for( Object k : list_Key_Type.keySet() )
			{
				if( !prop.keySet().contains( k ) )
				{
					defaultMsg += k.toString() + ";" ;
				}
			}
		}
		

		if (defaultValue)
		{
			checkPropErrorMsg = defaultMsg;			
		}
		
		return checkPropErrorMsg;
	}
	
	private static String checkSocket(String[] strs)
	{
		String socketInfo = null;
		if (strs.length == 3)
		{
			Object[] v = new Object[3];

			List<Integer> index = new ArrayList<Integer>();
			index.add( 0 );
			index.add( 1 );
			index.add( 2 );

			byte c = 7;
			for (Integer i : index)
			{
				String ipProp = strs[i.intValue()].replace( " " , "");

				if ((c & 0x4) > 0) /* TCP-UDP */	
				{
					if (ipProp.equals( "UDP" ) )
					{
						v[0] = streamSocketInfo.UDP_PROTOCOL;
						c = (byte)(c & 0x3);
					}
					else if (ipProp.equals( "TCP" ) )
					{
						v[0] = streamSocketInfo.TCP_PROTOCOL;
						c = (byte)(c & 0x3);
					}
				}

				if ((c & 0x2) > 0) /* IP */
				{
					if (IPAddressValidator.validate(ipProp))
					{
						v[1] = ipProp;

						c = (byte)(c & 0x5);
					}
				}

				if ((c & 0x1) > 0)
				{
					try
					{
						v[2] = new Integer(ipProp);

						c = (byte)(c & 0x6);
					}
					catch (Exception localException) {}
				}
			}


			if (c == 0)
			{
				socketInfo = streamSocketInfo.getSocketString( (Integer)v[0], v[1].toString(), (Integer)v[2] );
			}
		}

		return socketInfo;
	}

	private static void loadDefaultValue(String prop)
	{
		if (prop.equals( IS_TRAINING ))
		{
			loadDefaultValueTraining();
		}
		else if (prop.equals( NON_ANSWER_TIMERS ) )
		{
			loadDefaultNonAnswerTimers();
		}
		else if ( prop.equals( SELECTED_SCREEN ) )
		{
			loadDefaultSelectedScree();
		}
		else if (prop.equals( LANGUAGE ) )
		{
			loadDefaultLanguage();
		}
		else if (prop.equals( IS_TIME_TEST_ACTIVED ) )
		{
			loadDefaultValueTimeTestActive();
		}
		else if (prop.equals( IS_LOG_ACTIVE ))
		{
			loadDefaultValueLogActive();
		}
		else if (prop.equals( IS_SHOW_TIMER_ACTIVE ))
		{
			loadDefaultValueShowTimerActive();
		}
		else if (prop.equals( IS_COUNTDOWN_SOUND_ACTIVE ))
		{
			loadDefaultValueCountDownActive();
		}
		else if (prop.equals( IS_SELECT_ACTIVITY_REPORT ))
		{
			loadDefaultIsSelectedActivityReport();
		}
		else if (prop.equals( IS_SELECTED_SLIDE ))
		{
			loadDefaultValueIsSelectedSlide();
		}
		else if (prop.equals( IS_SELECTED_SOUND ))
		{
			loadDefaultValueIsSelectedSound();
		}
		else if (prop.equals( IS_SELECTED_TIME_UNTIL_SOUND_END ))
		{
			loadDefaultValueIsSelectedTimeUntilSoundEnd();
		}
		else if (prop.equals( RANDOM_AFFECTIVE_SAMPLES_TYPE ))
		{
			loadDefaultValueRandomAffectiveSampleType();
		}
		else if (prop.equals( RANDOM_ORDER_INTERBLOCKS ))
		{
			loadDefaultValueRandomOrderInterBlocks();
		}
		else if (prop.equals( IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE ))
		{
			loadDefaultValuePreserveSlideSoundCorrespondence();
		}
		else if (prop.equals( IS_SELECTED_SLIDE_MAIN_GROUP ))
		{
			loadDefaultValueSlideMainGroup();
		}
		else if (prop.equals( OUTPUT_FOLDER ))
		{
			loadDefaultValueOutputFolder();
		}
		else if (prop.equals( LOG_FILE_NAME ))
		{
			loadDefaultValueLogFileName();
		}
		else if (prop.equals( VALUE_TEST_TIME ))
		{
			loadDefaultValueTimeTest();
		}
		else if (prop.equals( TIME_PRERUN_SOUND ))
		{
			loadDefaultValueTimePreRunSound();
		}
		else if (prop.equals( TIME_POSRUN_SOUND ))
		{
			loadDefaultValueTimePosRunSound();
		}
		else if (prop.equals( TIME_PRERUN ))
		{
			loadDefaultValueTimePreRun();
		}
		else if (prop.equals( VALUE_TIME_OUT_MANUAL ))
		{
			loadDefaultValueTimeOutManual();
		}
		else if (prop.equals( IS_TIME_OUT_MANUAL_ACTIVE ))
		{
			loadDefaultValueTimeOutManualActive();
		}
		else if (prop.equals( IS_TIME_OUT_AUTO_ACTIVE ))
		{
			loadDefaultValueTimeOutAutoActive();
		}
		else if (prop.equals( PREPOS_BLACK_BACKGROUND ))
		{
			loadDefaultValuePrePosBlackBackground();
		}
		else if (prop.equals( DIFICULTY_INDEX_SELECTED ))
		{
			loadDefaultValueDificultySelected();
		}
		else if (prop.equals( IS_FULLSCREEN ))
		{
			loadDefaultValueFullScreen();
		}
		else if (prop.equals( PATH_FILE_TIME_OUT_AUTO ))
		{
			loadDefaultValuePathTimeOutAutoFile();
		}
		else if (prop.equals( TIME_POSRUN ))
		{
			loadDefaultValuePosRun();
		}
		else if (prop.equals( IS_REPETITION_ACTIVE ))
		{
			loadDefaultValueRepetitionActive();
		}
		else if (prop.equals( VALUE_REPETITION ))
		{
			loadDefaultValueRepetition();
		}
		else if (prop.equals( SAM_SET_SIZE ))
		{
			loadDefaultValueSamSetSize();
		}
		else if (prop.equals( TASK_INDEX_SELECTED ))
		{
			loadDefaultValueTaskIndexSelected();
		}
		else if (prop.equals( IS_SOCKET_CLIENT_ACTIVE ))
		{
			loadDefaultValueIsClientSocketActive();
		}
		else if (prop.equals( PATH_SLIDES ))
		{
			loadDefaultValueListSlides();
		}
		else if (prop.equals( PATH_SOUNDS ))
		{
			loadDefaultValueListSounds();
		}
		else if (prop.equals( AFFECTIVE_GROUPS ))
		{
			loadDefaultValueAffectiveGroups();
		}
		else if (prop.equals( AFFECTIVE_BLOCKS ))
		{
			loadDefaultValueAffectiveBlocks();
		}
		else if (prop.equals( IS_SAM_DOMINANCE ))
		{
			loadDefaultValueIsSamDominance();
		}
		else if (prop.equals( TRAJECTORY_INVERTED_YAXIS ))
		{
			loadDefaultValueTrajectoryInvertedYAXIS();
		}
		else if (prop.equals( TRAJECTORY_INVERTED_XAXIS ))
		{
			loadDefaultValueTrajectoryInvertedXAXIS();
		}
		else if (prop.equals( CLIENT_SOCKET_TABLE ))
		{
			loadDefaultValueClientSocketTable();
		}
		else if (prop.equals( SERVER_SOCKET_TABLE ))
		{
			loadDefaultValueServerSocketTable();
		}
		//TODO DELETED
		/*
		else if (prop.equals( LSL_ID_DEVICES ))
		{
			loadDefaultLSLDeviceInfo();
		}
		else if (prop.equals( LSL_OUTPUT_FILE_FORMAT ))
		{
			loadDefaultLSLOutputFileFormat();
		}
		else if (prop.equals( LSL_OUTPUT_FILE_NAME ))
		{
			loadDefaultLSLOutputFileName();
		}
		*/
		else if( prop.equals( SAM_VALENCE_SCALE ) )
		{
			loadDefaultValueValenceRange();
		}
		else if( prop.equals( SAM_AROUSAL_SCALE ) )
		{
			loadDefaultValueArousalRange();
		}
		else if( prop.equals( SAM_DOMINANCE_SCALE ) )
		{
			loadDefaultValueDominanceRange();
		}
		else if( prop.equals( SAM_EMOTION_SET ) )
		{
			loadDefaultValueEmotionSet();
		}
		else if( prop.equals( SAM_BEEP_ACTIVE ) )
		{
			loadDefaultSAMBeep();
		}
		else if( prop.equals( TEST_BENCH_FILES ) )
		{
			loadDefaultTestBechFiles();
		}
	}

	private static void loadDefaultProperties()
	{
		loadDefaultLanguage();

		loadDefaultNonAnswerTimers();

		loadDefaultValueTraining();
		loadDefaultSelectedScree();

		loadDefaultValueTimeTestActive();
		loadDefaultValueTimeTest();
		loadDefaultValueTimePreRun();
		loadDefaultValuePrePosBlackBackground();
		loadDefaultValueTimeOutManual();
		loadDefaultValueTimeOutManualActive();
		loadDefaultValueTimeOutAutoActive();

		loadDefaultValueDificultySelected();
		loadDefaultValueFullScreen();
		loadDefaultValuePathTimeOutAutoFile();
		loadDefaultValuePosRun();
		loadDefaultValueRepetitionActive();
		loadDefaultValueRepetition();
		loadDefaultValueTaskIndexSelected();
		loadDefaultValueLogActive();
		loadDefaultValueOutputFolder();
		loadDefaultValueLogFileName();

		loadDefaultValueTimePreRunSound();
		loadDefaultValueTimePosRunSound();

		loadDefaultIsSelectedActivityReport();

		loadDefaultValueIsSelectedSlide();
		loadDefaultValueIsSelectedSound();
		loadDefaultValueIsSelectedTimeUntilSoundEnd();
		loadDefaultValueRandomAffectiveSampleType();
		loadDefaultValuePreserveSlideSoundCorrespondence();
		loadDefaultValueRandomOrderInterBlocks();

		loadDefaultValueCountDownActive();
		loadDefaultValueShowTimerActive();

		loadDefaultValueListSlides();
		loadDefaultValueListSounds();
		loadDefaultValueAffectiveGroups();
		loadDefaultValueAffectiveBlocks();
		loadDefaultValueSamSetSize();
		loadDefaultValueSlideMainGroup();
		loadDefaultValueIsSamDominance();

		loadDefaultValueTrajectoryInvertedXAXIS();
		loadDefaultValueTrajectoryInvertedYAXIS();

		loadDefaultValueClientSocketTable();
		loadDefaultValueServerSocketTable();

		loadDefaultValueIsClientSocketActive();
		loadDefaultValueIsServerSocketActive();
		
		//TODO DELETED
		/*
		loadDefaultLSLDeviceInfo();
		loadDefaultLSLOutputFileName();
		loadDefaultLSLOutputFileFormat();
		*/
		
		loadDefaultValueValenceRange();	
		loadDefaultValueArousalRange();
		loadDefaultValueDominanceRange();
		loadDefaultValueEmotionSet();
		loadDefaultSAMBeep();
		
		loadDefaultTestBechFiles();
	}

	private static void loadDefaultLanguage()
	{
		listConfig.put( LANGUAGE, Language.defaultLanguage );
	}

	private static void loadDefaultValueLogActive()
	{
		listConfig.put( IS_LOG_ACTIVE, false );
	}

	private static void loadDefaultValueOutputFolder()
	{
		listConfig.put( OUTPUT_FOLDER, defaultPathFile);
	}

	private static void loadDefaultValueLogFileName()
	{
		listConfig.put( LOG_FILE_NAME, defaultNameLogFile );
	}

	private static void loadDefaultValueTraining()
	{
		listConfig.put( IS_TRAINING, false );
	}

	private static void loadDefaultSelectedScree()
	{
		listConfig.put( SELECTED_SCREEN, 1 );
	}

	private static void loadDefaultValueIsSelectedSlide()
	{
		listConfig.put( IS_SELECTED_SLIDE, false );
	}

	private static void loadDefaultValueIsSelectedSound() 
	{
		listConfig.put( IS_SELECTED_SOUND, false );
	}

	private static void loadDefaultValueIsSelectedTimeUntilSoundEnd()
	{
		listConfig.put( IS_SELECTED_TIME_UNTIL_SOUND_END, false );
	}

	private static void loadDefaultValueShowTimerActive()
	{
		listConfig.put( IS_SHOW_TIMER_ACTIVE, true );
	}

	private static void loadDefaultValueCountDownActive()
	{
		listConfig.put( IS_COUNTDOWN_SOUND_ACTIVE, true );
	}

	private static void loadDefaultValueFullScreen()
	{
		listConfig.put( IS_FULLSCREEN, false );
	}

	private static void loadDefaultValueTaskIndexSelected()
	{
		listConfig.put( TASK_INDEX_SELECTED, 0);
	}

	private static void loadDefaultValueDificultySelected()
	{
		listConfig.put( DIFICULTY_INDEX_SELECTED, 0);
	}

	private static void loadDefaultValueTimePreRunSound()
	{
		listConfig.put( TIME_PRERUN_SOUND, true );
	}

	private static void loadDefaultValueTimePosRunSound()
	{
		listConfig.put( TIME_POSRUN_SOUND, false );
	}

	private static void loadDefaultValueTimePreRun()
	{
		listConfig.put( TIME_PRERUN, 1L );
	}

	private static void loadDefaultValuePosRun()
	{
		listConfig.put( TIME_POSRUN,  1L );
	}

	private static void loadDefaultValueTimeTestActive()
	{
		listConfig.put( IS_TIME_TEST_ACTIVED, false );
	}

	private static void loadDefaultValueTimeTest()
	{
		listConfig.put( VALUE_TEST_TIME, 5L );
	}

	private static void loadDefaultValueRepetitionActive()
	{
		listConfig.put( IS_REPETITION_ACTIVE, false );
	}

	private static void loadDefaultValueRepetition()
	{
		listConfig.put( VALUE_REPETITION, 10 );
	}

	private static void loadDefaultValueTimeOutAutoActive()
	{
		listConfig.put( IS_TIME_OUT_AUTO_ACTIVE, false );
	}

	private static void loadDefaultValuePathTimeOutAutoFile()
	{
		listConfig.put( PATH_FILE_TIME_OUT_AUTO, defaultPathFile + defaultNameFileConfigResults );
	}

	private static void loadDefaultValueTimeOutManualActive()
	{
		listConfig.put( IS_TIME_OUT_MANUAL_ACTIVE, false );
	}

	private static void loadDefaultValueTimeOutManual()
	{
		listConfig.put( VALUE_TIME_OUT_MANUAL, 30L );
	}

	private static void loadDefaultValueListSounds()
	{
		listConfig.put( PATH_SLIDES, new ArrayList< AffectiveObject >());
	}

	private static void loadDefaultValueListSlides()
	{
		listConfig.put( PATH_SOUNDS, new ArrayList< AffectiveObject >());
	}

	private static void loadDefaultValueAffectiveGroups()
	{
		listConfig.put( AFFECTIVE_GROUPS, 1);
	}

	private static void loadDefaultValueAffectiveBlocks()
	{
		listConfig.put( AFFECTIVE_BLOCKS, 1);
	}

	private static void loadDefaultValueTrajectoryInvertedXAXIS()
	{
		listConfig.put( TRAJECTORY_INVERTED_XAXIS, false );
	}

	private static void loadDefaultValueTrajectoryInvertedYAXIS()
	{
		listConfig.put( TRAJECTORY_INVERTED_YAXIS, false );
	}

	private static void loadDefaultValueClientSocketTable()
	{
		Map<String, Object[][]> table = new LinkedHashMap<String, Object[][]>();

		table.put( defaultSocket(), getDefaultCommandSocketTable( CLIENT_SOCKET_TABLE ));

		listConfig.put( CLIENT_SOCKET_TABLE, table);
	}

	public static Object[][] getDefaultCommandSocketTable(String propertyID)
	{
		String[] events = Commands.getTriggeredEvents();
		if (propertyID.equals( SERVER_SOCKET_TABLE ))
		{
			events = Commands.getCommands();
		}

		Object[][] t = new Object[events.length][3];

		for (int i = 0; i < events.length; i++)
		{
			t[i][0] = Boolean.FALSE;
			t[i][1] = events[i].toUpperCase();
			t[i][2] = events[i];
		}

		return t;
	}

	private static void loadDefaultValueServerSocketTable()
	{
		Map<String, Object[][]> table = new LinkedHashMap<String, Object[][]>();

		table.put(defaultSocket(), getDefaultCommandSocketTable( SERVER_SOCKET_TABLE ));

		listConfig.put( SERVER_SOCKET_TABLE, table);
	}

	private static void loadDefaultValueIsClientSocketActive()
	{
		listConfig.put( IS_SOCKET_CLIENT_ACTIVE, false );
	}

	private static void loadDefaultValueIsServerSocketActive()
	{
		listConfig.put( IS_SOCKET_SERVER_ACTIVE, false );
	}

	private static String defaultSocket()
	{
		int port = 55555;

		ServerSocket localmachine = null;		
		try 
		{
			localmachine = new ServerSocket( 0 );
			localmachine.setReuseAddress( true );
			port = localmachine.getLocalPort();
			localmachine.close();
		} 
		catch (IOException e) 
		{}
		finally
		{
			if( localmachine != null )
			{
				try 
				{
					localmachine.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}

			localmachine = null;
		}

		return streamSocketInfo.getSocketString( streamSocketInfo.TCP_PROTOCOL,
				Inet4Address.getLoopbackAddress().getHostAddress(),
				port );
	}

	//TODO DELETED
	/*
	private static void loadDefaultLSLDeviceInfo()
	{
		//listConfig.put( LSL_ID_DEVICES, new TupleHashSet());
		listConfig.put( LSL_ID_DEVICES, new HashSet< LSLConfigParameters >() );
	}

	private static void loadDefaultLSLOutputFileName()
	{
		listConfig.put( LSL_OUTPUT_FILE_NAME, defaultNameOutputDataFile );
	}

	private static void loadDefaultLSLOutputFileFormat()
	{
		listConfig.put( LSL_OUTPUT_FILE_FORMAT, DataFileFormat.CLIS);
	}
	*/

	private static void loadDefaultIsSelectedActivityReport()
	{
		listConfig.put( IS_SELECT_ACTIVITY_REPORT, false );
	}

	private static void loadDefaultNonAnswerTimers()
	{
		List<Long> l = new ArrayList< Long >();
		l.add( 30L );

		listConfig.put( NON_ANSWER_TIMERS, l);
	}

	private static void loadDefaultValueSamSetSize()
	{
		listConfig.put( SAM_SET_SIZE, 1);
	}

	private static void loadDefaultValueRandomAffectiveSampleType()
	{
		listConfig.put( RANDOM_AFFECTIVE_SAMPLES_TYPE, 0);
	}

	private static void loadDefaultValuePreserveSlideSoundCorrespondence()
	{
		listConfig.put( IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE, false );
	}

	private static void loadDefaultValueRandomOrderInterBlocks()
	{
		listConfig.put( RANDOM_ORDER_INTERBLOCKS, 0);
	}

	private static void loadDefaultValuePrePosBlackBackground()
	{
		listConfig.put( PREPOS_BLACK_BACKGROUND, false );
	}

	private static void loadDefaultValueSlideMainGroup()
	{
		listConfig.put( IS_SELECTED_SLIDE_MAIN_GROUP, true );
	}

	private static void loadDefaultValueIsSamDominance()
	{
		listConfig.put( IS_SAM_DOMINANCE, false );
	}
	
	private static void loadDefaultValueValenceRange()
	{
		listConfig.put( SAM_VALENCE_SCALE, 9 );
	}
	
	private static void loadDefaultValueArousalRange()
	{
		listConfig.put( SAM_AROUSAL_SCALE , 9 );
	}
	
	private static void loadDefaultValueDominanceRange()
	{
		listConfig.put( SAM_DOMINANCE_SCALE, 9 );
	}
	
	private static void loadDefaultValueEmotionSet()
	{
		List< EmotionParameter > emotions = new ArrayList< EmotionParameter >();
		
		for( int i = 1; i < 8; i++ )
		{
			emotions.add( new EmotionParameter( i ) );
		}
		
		listConfig.put( SAM_EMOTION_SET,  emotions );
	}
	
	private static void loadDefaultTestBechFiles()
	{
		listConfig.put( TEST_BENCH_FILES, "" );
	}
	
	private static void loadDefaultSAMBeep()
	{
		listConfig.put( SAM_BEEP_ACTIVE, false );
	}
}
