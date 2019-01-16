package Config.Language;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.zip.DataFormatException;

public class Language
{
	public static final String defaultLanguage = "DefaultLanguage";
	
	public static final String LANGUAGE = "LANGUAGE";
	
	public static final String ACTION_PLAY = "ACTION_PLAY";
	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_CONFIG = "ACTION_CONFIG";
	public static final String ACTION_ABOUT = "ACTION_ABOUT";
	public static final String ACTION_GNU_GPL = "ACTION_GNU_GPL";
	public static final String LOAD_TEXT = "LOAD_TEXT";
	public static final String SETTING_MENU_FILE = "SETTING_MENU_FILE";
	public static final String SETTING_MENU_LOAD = "SETTING_MENU_LOAD";
	public static final String SETTING_MENU_SAVE = "SETTING_MENU_SAVE";
	public static final String SETTING_TASK = "SETTING_TASK";
	public static final String SETTING_TASK_DIFICULTY = "SETTING_TASK_DIFICULTY";
	public static final String SETTING_TASK_MATHEMATIC = "SETTING_TASK_MATHEMATIC";
	public static final String SETTING_TASK_MEMORY = "SETTING_TASK_MEMORY";
	public static final String SETTING_TASK_SEARCHCOUNT = "SETTING_TASK_SEARCHCOUNT";
	public static final String SETTING_TASK_SORTED_DOTS = "SETTING_TASK_SORTED_DOTS";
	public static final String SETTING_TASK_STROOP_TEST = "SETTING_TASK_STROOP_TEST";
	public static final String SETTING_TASK_TRAJECTORY = "SETTING_TASK_TRAJECTORY";
	public static final String SETTING_TASK_AFFECTIVE = "SETTING_TASK_AFFECTIVE";
	public static final String SETTING_TASK_TRAJECTORY_PARS_PANEL = "SETTING_TASK_TRAJECTORY_PARS";
	public static final String SETTING_TASK_TRAJECTORY_XAXIS = "SETTING_TASK_TRAJECTORY_XAXIS";
	public static final String SETTING_TASK_TRAJECTORY_YAXIS = "SETTING_TASK_TRAJECTORY_YAXIS";
	public static final String SETTING_TASK_AFFECTIVE_PARS_PANEL = "SETTING_TASK_AFFECTIVE_PARS";
	public static final String SETTING_TASK_AFFECTIVE_SLIDES = "SETTING_TASK_AFFECTIVE_SLIDES";
	public static final String SETTING_TASK_AFFECTIVE_SOUNDS = "SETTING_TASK_AFFECTIVE_SOUNDS";
	public static final String SETTING_TASK_AFFECTIVE_SLIDE_UNTIL_END_SOUND = "SETTING_TASK_AFFECTIVE_SLIDE_UNTIL_END_SOUND";
	public static final String SETTING_TASK_AFFECTIVE_SAM_SIZE = "SETTING_TASK_AFFECTIVE_SAM_SIZE";
	public static final String SETTING_TASK_AFFECTIVE_SELECT_DOMINANCE = "SETTING_TASK_AFFECTIVE_SELECT_DOMINANCE";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG = "SETTING_TASK_AFFECTIVE_CONFIG";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_MOVE_UP = "SETTING_TASK_AFFECTIVE_CONFIG_MOVE_UP";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_MOVE_DOWN = "SETTING_TASK_AFFECTIVE_CONFIG_MOVE_DOWN";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_DELETE = "SETTING_TASK_AFFECTIVE_CONFIG_DELETE";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_NONE = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_NONE";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_SAMPLES = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_SAMPLES";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_GROUPS = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_GROUPS";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_BLOCKS = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_BLOCKS";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_PRESERVE_LINK = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_PRESERVE_LINK";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_SLIDE_MAIN = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_SLIDE_MAIN";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_HELP = "SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_HELP";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_GROUPS = "SETTING_TASK_AFFECTIVE_CONFIG_GROUPS";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_BLOCKS = "SETTING_TASK_AFFECTIVE_CONFIG_BLOCKS";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_COUNT = "SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_COUNT";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_SOUND_COUNT = "SETTING_TASK_AFFECTIVE_CONFIG_SOUND_COUNT";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_SECTION = "SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_SECTION";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_SOUND_SECTION = "SETTING_TASK_AFFECTIVE_CONFIG_SOUND_SECTION";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FIX = "SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FIX";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FILE = "SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FILE_PATH";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_TABLE_GROUP = "SETTING_TASK_AFFECTIVE_CONFIG_TABLE_GROUP";
	public static final String SETTING_TASK_AFFECTIVE_CONFIG_TABLE_BLOCK = "SETTING_TASK_AFFECTIVE_CONFIG_TABLE_BLOCK";
	public static final String SETTING_TASK_TIMER_TEST = "SETTING_TASK_TIMER_TEST";
	public static final String SETTING_TASK_REPETITIONS = "SETTING_TASK_REPETITIONS";
	public static final String SETTING_TASK_TIMER_PREPOS_PANEL = "SETTING_TASK_TIMER_PREPOS_PANEL";
	public static final String SETTING_TASK_TIMER_PRETIMER = "SETTING_TASK_TIMER_PRETIMER";
	public static final String SETTING_TASK_TIMER_POSTIMER = "SETTING_TASK_TIMER_POSTIMER";
	public static final String SETTING_TASK_TIMER_PREPOS_BLACK_BACKGROUND = "SETTING_TASK_TIMER_PREPOS_BLACK_BACKGROUND";
	public static final String SETTING_TASK_TIMER_PREPOS_SOUND = "SETTING_TASK_TIMER_PREPOS_SOUND";
	public static final String SETTING_TASK_TIMER_TIMEOUT_PANEL = "SETTING_TASK_TIMER_TIMEOUT_PANEL";
	public static final String SETTING_TASK_TIMER_TIMEOUT_AUTO = "SETTING_TASK_TIMER_TIMEOUT_AUTO";
	public static final String SETTING_TASK_TIMER_TIMEOUT_MANUAL = "SETTING_TASK_TIMER_TIMEOUT_MANUAL";
	public static final String SETTING_TASK_TIMER_NONMAIN_TIMEOUTS = "SETTING_TASK_TIMER_NONMAIN_TIMEOUTS";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_EDIT = "SETTING_TASK_TIMER_AUTOTIMEOUT_EDIT";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_LEVEL = "SETTING_TASK_TIMER_AUTOTIMEOUT_LEVEL";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_FIELD = "SETTING_TASK_TIMER_AUTOTIMEOUT_FIELD";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_NUMBER_TEST = "SETTING_TASK_TIMER_AUTOTIMEOUT_NUMBER_TEST";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_TEST_TIMERS = "SETTING_TASK_TIMER_AUTOTIMEOUT_TEST_TIMER";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_CORRECT_ANSWERS = "SETTING_TASK_TIMER_AUTOTIMEOUT_CORRECT_ANSWERS";
	public static final String SETTING_TASK_TIMER_AUTOTIMEOUT_ANSWERSTIMERS = "SETTING_TASK_TIMER_AUTOTIMEOUT_ANSWER_TIMERS";
	public static final String SETTING_TASK_LOG = "SETTING_TASK_LOG";
	public static final String SETTING_TASK_TRAINING = "SETTING_TASK_TRAINING";
	public static final String SETTING_TASK_FULLSCREEN = "SETTING_TASK_FULLSCREEN";
	public static final String SETTING_TASK_SHOW_TRAIL_TIMER = "SETTING_TASK_SHOW_TRIAL_TIMER";
	public static final String SETTING_TASK_COUNTDOWN_SOUND = "SETTING_TASK_COUNTDOWN_SOUND";
	public static final String SETTING_TASK_ACTIVITY_REPORT = "SETTING_TASK_ACTIVITY_REPORT";
	public static final String SETTING_TASK_SOCKET_TAB = "SETTING_TASK_SOCKET_TAB";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_PANEL = "SETTING_TASK_SOCKET_TAB_INPUT_PANEL";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_ACTIVE = "SETTING_TASK_SOCKET_TAB_INPUT_ACTIVE";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_ADDRESS = "SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_ADDRESS";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_PORT = "SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_PORT";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_ENABLE = "SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_ENABLE";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_EVENT = "SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_EVENT";
	public static final String SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_MESSAGE = "SETTING_TASK_SOCKET_TAB_INPUT_MESSAGE";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_PANEL = "SETTING_TASK_SOCKET_TAB_OUTPUT_PANEL";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_ACTIVE = "SETTING_TASK_SOCKET_TAB_OUTPUT_ACTIVE";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_ADDRESS = "SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_ADDRESS";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_PORT = "SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_PORT";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_ENABLE = "SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_ENABLE";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_EVENT = "SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_EVENT";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_MESSAGE = "SETTING_TASK_SOCKET_TAB_OUTPUT_MESSAGE";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_NEW_SOCKET = "SETTING_TASK_SOCKET_TAB_OUTPUT_NEW_SOCKET";
	public static final String SETTING_TASK_SOCKET_TAB_OUTPUT_DELETE_SOCKET = "SETTING_TASK_SOCKET_TAB_OUTPUT_DELETE_SOCKET";
	
	public static final String DIALOG_SAVE = "DIALOG_SAVE";
	public static final String DIALOG_ERROR = "DIALOG_ERROR";
	public static final String DIALOG_LOAD = "DIALOG_LOAD";
	public static final String DIALOG_REPLACE_FILE_TITLE = "DIALOG_REPLACE_FILE_TITLE";
	public static final String DIALOG_REPLACE_FILE_MESSAGE = "DIALOG_REPLACE_FILE_MESSAGE";
	
	public static final String MSG_WINDOW_CLOSE = "MSG_WINDOW_CLOSE";
	public static final String MSG_WINDOW_LOAD_CONFIG = "MSG_WINDOW_LOAD_CONFIG_MSG";
	public static final String MSG_INTERRUPT = "MSG_INTERRUPT";	
	public static final String MSG_APP_STATE = "MSG_APP_STATE";
	public static final String MSG_WARNING = "MSG_WARNING";
	
	public static final String INFO_STATE_LABEL = "INFO_STATE_LABEL";
	
	public static final String ABOUT_WEB_LABEL = "ABOUT_WEB_LABEL";
	public static final String ABOUT_AUTHOR_LABEL = "ABOUT_AUTHOR_LABEL";
	public static final String ABOUT_EMAIL_LABEL = "ABOUT_EMAIL_LABEL";
	public static final String ABOUT_SOURCE_CODE_LABEL = "ABOUT_SOURCE_CODE_LABEL";
	
	private static Map<String, Caption> captions = new HashMap<String, Caption>();

	static
	{
		captions.put( MSG_WINDOW_CLOSE, new Caption( MSG_WINDOW_CLOSE , defaultLanguage, "Processes are closing. Wait to exit..." ) );
		captions.put( MSG_WINDOW_LOAD_CONFIG, new Caption( MSG_WINDOW_LOAD_CONFIG, defaultLanguage, "Load setting..." ) );
		captions.put( MSG_INTERRUPT, new Caption( MSG_INTERRUPT, defaultLanguage, "Interrupt" ) );
		captions.put( MSG_APP_STATE, new Caption( MSG_APP_STATE, defaultLanguage, "App's state is " ) );
		captions.put( MSG_WARNING,  new Caption( MSG_WARNING, defaultLanguage, "Warning" ) );
		
		captions.put( INFO_STATE_LABEL, new Caption( INFO_STATE_LABEL, defaultLanguage, "State" ) );
		
		captions.put( ABOUT_WEB_LABEL, new Caption( ABOUT_WEB_LABEL, defaultLanguage, "Web" ) );
		captions.put( ABOUT_AUTHOR_LABEL, new Caption( ABOUT_AUTHOR_LABEL, defaultLanguage, "Author" ) );
		captions.put( ABOUT_EMAIL_LABEL, new Caption( ABOUT_EMAIL_LABEL, defaultLanguage, "Email" ) );
		captions.put( ABOUT_SOURCE_CODE_LABEL, new Caption( ABOUT_SOURCE_CODE_LABEL, defaultLanguage, "Source code available on" ) );
		
		captions.put( DIALOG_SAVE, new Caption( DIALOG_SAVE, defaultLanguage, "Save" ) );
		captions.put( DIALOG_ERROR, new Caption( DIALOG_ERROR, defaultLanguage, "Error" ) );
		captions.put( DIALOG_LOAD, new Caption( DIALOG_LOAD, defaultLanguage, "Load" ) );
		captions.put( DIALOG_REPLACE_FILE_TITLE, new Caption( DIALOG_REPLACE_FILE_TITLE, defaultLanguage, "Select an option" ) );
		captions.put( DIALOG_REPLACE_FILE_MESSAGE, new Caption( DIALOG_REPLACE_FILE_MESSAGE, defaultLanguage, "Replace existing file?" ) );

		captions.put( ACTION_PLAY, new Caption( ACTION_PLAY, defaultLanguage, "Play" ) );
		captions.put( ACTION_STOP, new Caption( ACTION_STOP, defaultLanguage, "Stop" ) );
		captions.put( ACTION_CONFIG, new Caption( ACTION_CONFIG, defaultLanguage, "Config" ) );
		captions.put( ACTION_ABOUT, new Caption( ACTION_ABOUT, defaultLanguage, "About" ) );
		captions.put( ACTION_GNU_GPL, new Caption( ACTION_GNU_GPL, defaultLanguage, "GNU GLP" ) );

		captions.put( SETTING_MENU_FILE, new Caption( SETTING_MENU_FILE, defaultLanguage, "File" ) );
		captions.put( SETTING_MENU_LOAD, new Caption( SETTING_MENU_LOAD, defaultLanguage, "Load setting" ) );
		captions.put( SETTING_MENU_SAVE, new Caption( SETTING_MENU_SAVE, defaultLanguage, "Save setting" ) );

		captions.put( LOAD_TEXT, new Caption( LOAD_TEXT, defaultLanguage, "Load" ) );

		captions.put( SETTING_TASK, new Caption( SETTING_TASK, defaultLanguage, "Task" ) );
		captions.put( SETTING_TASK_DIFICULTY, new Caption( SETTING_TASK_DIFICULTY, defaultLanguage, "Dificulty" ) );

		captions.put( SETTING_TASK_MATHEMATIC, new Caption( SETTING_TASK_MATHEMATIC, defaultLanguage, "Mathematic" ) );
		captions.put( SETTING_TASK_MEMORY, new Caption( SETTING_TASK_MEMORY, defaultLanguage, "Memory" ) );
		captions.put( SETTING_TASK_SEARCHCOUNT, new Caption( SETTING_TASK_SEARCHCOUNT, defaultLanguage, "Search-count" ) );
		captions.put( SETTING_TASK_SORTED_DOTS, new Caption( SETTING_TASK_SORTED_DOTS, defaultLanguage, "Sorted dots" ) );
		captions.put( SETTING_TASK_STROOP_TEST, new Caption( SETTING_TASK_STROOP_TEST, defaultLanguage, "Stroop Test" ) );
		captions.put( SETTING_TASK_TRAJECTORY, new Caption( SETTING_TASK_TRAJECTORY, defaultLanguage, "Trajectory" ) );
		captions.put( SETTING_TASK_AFFECTIVE, new Caption( SETTING_TASK_AFFECTIVE, defaultLanguage, "Affective" ) );

		captions.put( SETTING_TASK_TRAJECTORY_PARS_PANEL, new Caption( SETTING_TASK_TRAJECTORY_PARS_PANEL, defaultLanguage, "Trajectory: inverted axes" ) );
		captions.put( SETTING_TASK_TRAJECTORY_XAXIS, new Caption( SETTING_TASK_TRAJECTORY_XAXIS, defaultLanguage, "X-axis" ) );
		captions.put( SETTING_TASK_TRAJECTORY_YAXIS, new Caption( SETTING_TASK_TRAJECTORY_YAXIS, defaultLanguage, "Y-axis" ) );

		captions.put( SETTING_TASK_AFFECTIVE_PARS_PANEL, new Caption( SETTING_TASK_AFFECTIVE_PARS_PANEL, defaultLanguage, "Affective Task" ) );
		captions.put( SETTING_TASK_AFFECTIVE_SLIDES, new Caption( SETTING_TASK_AFFECTIVE_SLIDES, defaultLanguage, "Slides" ) );
		captions.put( SETTING_TASK_AFFECTIVE_SOUNDS, new Caption( SETTING_TASK_AFFECTIVE_SOUNDS, defaultLanguage, "Sounds" ) );
		captions.put( SETTING_TASK_AFFECTIVE_SLIDE_UNTIL_END_SOUND, new Caption( SETTING_TASK_AFFECTIVE_SLIDE_UNTIL_END_SOUND, defaultLanguage, "Slide until sound is finished" ) );
		captions.put( SETTING_TASK_AFFECTIVE_SAM_SIZE, new Caption( SETTING_TASK_AFFECTIVE_SAM_SIZE, defaultLanguage, "S.A.M. set size" ) );
		captions.put( SETTING_TASK_AFFECTIVE_SELECT_DOMINANCE, new Caption( SETTING_TASK_AFFECTIVE_SELECT_DOMINANCE, defaultLanguage, "" ) );

		captions.put( SETTING_TASK_AFFECTIVE_CONFIG, new Caption( SETTING_TASK_AFFECTIVE_CONFIG, defaultLanguage, "Affective task config" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_MOVE_UP, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_MOVE_UP, defaultLanguage, "Up" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_MOVE_DOWN, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_MOVE_DOWN, defaultLanguage, "Down" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_DELETE, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_DELETE, defaultLanguage, "Delete" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT, defaultLanguage, "Random sort" ) );

		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE, defaultLanguage, "Type" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_NONE, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_NONE, defaultLanguage, "None" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_SAMPLES, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_SAMPLES, defaultLanguage, "Random samples" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_GROUPS, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_GROUPS, defaultLanguage, "Random samples & groups" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_BLOCKS, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_TYPE_BLOCKS, defaultLanguage, "Random samples & groups by block" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_PRESERVE_LINK, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_PRESERVE_LINK, defaultLanguage, "Preserve slide-sound link" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_SLIDE_MAIN, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_SLIDE_MAIN, defaultLanguage, "Slide's groups and blocks as main" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_HELP, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_RANDOM_SORT_HELP, defaultLanguage, "Help" ) );

		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_GROUPS, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_GROUPS, defaultLanguage, "No. Groups" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_BLOCKS, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_BLOCKS, defaultLanguage, "No. Blocks" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_COUNT, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_COUNT, defaultLanguage, "No. imgs" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_SOUND_COUNT, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_SOUND_COUNT, defaultLanguage, "No. sounds" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_SECTION, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_SLIDE_SECTION, defaultLanguage, "Pictures" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_SOUND_SECTION, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_SOUND_SECTION, defaultLanguage, "Sounds" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FIX, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FIX, defaultLanguage, "Fix" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FILE, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_FILE, defaultLanguage, "File path" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_GROUP, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_GROUP, defaultLanguage, "Group" ) );
		captions.put( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_BLOCK, new Caption( SETTING_TASK_AFFECTIVE_CONFIG_TABLE_BLOCK, defaultLanguage, "Block" ) );

		captions.put( SETTING_TASK_TIMER_TEST, new Caption( SETTING_TASK_TIMER_TEST, defaultLanguage, "Test time (minuts)" ) );
		captions.put( SETTING_TASK_REPETITIONS, new Caption( SETTING_TASK_REPETITIONS, defaultLanguage, "Repetitions" ) );
		captions.put( SETTING_TASK_TIMER_PREPOS_PANEL, new Caption( SETTING_TASK_TIMER_PREPOS_PANEL, defaultLanguage, "Pre/Pos-Run Time" ) );
		captions.put( SETTING_TASK_TIMER_PRETIMER, new Caption( SETTING_TASK_TIMER_PRETIMER, defaultLanguage, "PreRun Time (s)" ) );
		captions.put( SETTING_TASK_TIMER_POSTIMER, new Caption( SETTING_TASK_TIMER_POSTIMER, defaultLanguage, "PosRun Time (s)" ) );
		captions.put( SETTING_TASK_TIMER_PREPOS_BLACK_BACKGROUND, new Caption( SETTING_TASK_TIMER_PREPOS_BLACK_BACKGROUND, defaultLanguage, "Back background" ) );
		captions.put( SETTING_TASK_TIMER_PREPOS_SOUND, new Caption( SETTING_TASK_TIMER_PREPOS_SOUND, defaultLanguage, "Sound" ) );
		captions.put( SETTING_TASK_TIMER_TIMEOUT_PANEL, new Caption( SETTING_TASK_TIMER_TIMEOUT_PANEL, defaultLanguage, "Timers" ) );
		captions.put( SETTING_TASK_TIMER_TIMEOUT_AUTO, new Caption( SETTING_TASK_TIMER_TIMEOUT_AUTO, defaultLanguage, "Auto" ) );
		captions.put( SETTING_TASK_TIMER_TIMEOUT_MANUAL, new Caption( SETTING_TASK_TIMER_TIMEOUT_MANUAL, defaultLanguage, "Manual (s)" ) );
		captions.put( SETTING_TASK_TIMER_NONMAIN_TIMEOUTS, new Caption( SETTING_TASK_TIMER_NONMAIN_TIMEOUTS, defaultLanguage, "Non-main-task list (s)" ) );

		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_EDIT, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_EDIT, defaultLanguage, "Edit" ) );
		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_LEVEL, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_LEVEL, defaultLanguage, "Level" ) );
		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_FIELD, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_FIELD, defaultLanguage, "Field" ) );
		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_NUMBER_TEST, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_NUMBER_TEST, defaultLanguage, "Number of test" ) );
		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_TEST_TIMERS, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_TEST_TIMERS, defaultLanguage, "Test timer" ) );
		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_CORRECT_ANSWERS, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_CORRECT_ANSWERS, defaultLanguage, "Correct answers" ) );
		captions.put( SETTING_TASK_TIMER_AUTOTIMEOUT_ANSWERSTIMERS, new Caption( SETTING_TASK_TIMER_AUTOTIMEOUT_ANSWERSTIMERS, defaultLanguage, "Answer time" ) );

		captions.put( SETTING_TASK_LOG, new Caption( SETTING_TASK_LOG, defaultLanguage, "Log" ) );
		captions.put( SETTING_TASK_TRAINING, new Caption( SETTING_TASK_TRAINING, defaultLanguage, "Training" ) );
		captions.put( SETTING_TASK_FULLSCREEN, new Caption( SETTING_TASK_FULLSCREEN, defaultLanguage, "Fullscreen" ) );
		captions.put( SETTING_TASK_SHOW_TRAIL_TIMER, new Caption( SETTING_TASK_SHOW_TRAIL_TIMER, defaultLanguage, "Show trial timer" ) );
		captions.put( SETTING_TASK_COUNTDOWN_SOUND, new Caption( SETTING_TASK_COUNTDOWN_SOUND, defaultLanguage, "Countdown sound" ) );
		captions.put( SETTING_TASK_ACTIVITY_REPORT, new Caption( SETTING_TASK_ACTIVITY_REPORT, defaultLanguage, "Activity report" ) );

		captions.put( SETTING_TASK_SOCKET_TAB, new Caption( SETTING_TASK_SOCKET_TAB, defaultLanguage, "Socket" ) );

		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_PANEL, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_PANEL, defaultLanguage, "Input messages" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_ACTIVE, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_ACTIVE, defaultLanguage, "Active" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_ADDRESS, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_ADDRESS, defaultLanguage, "IP address" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_PORT, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_IP_TABLE_PORT, defaultLanguage, "Port" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_ENABLE, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_ENABLE, defaultLanguage, "Enable" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_EVENT, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_EVENT, defaultLanguage, "Event" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_MESSAGE, new Caption( SETTING_TASK_SOCKET_TAB_INPUT_COMMAND_MESSAGE, defaultLanguage, "Message" ) );

		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_PANEL, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_PANEL, defaultLanguage, "Output messages" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_ACTIVE, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_ACTIVE, defaultLanguage, "Active" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_ADDRESS, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_ADDRESS, defaultLanguage, "Ip address" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_PORT, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_IP_TABLE_PORT, defaultLanguage, "Port" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_ENABLE, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_ENABLE, defaultLanguage, "Enable" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_EVENT, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_EVENT, defaultLanguage, "Event" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_MESSAGE, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_COMMAND_MESSAGE, defaultLanguage, "Message" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_NEW_SOCKET, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_NEW_SOCKET, defaultLanguage, "New" ) );
		captions.put( SETTING_TASK_SOCKET_TAB_OUTPUT_DELETE_SOCKET, new Caption( SETTING_TASK_SOCKET_TAB_OUTPUT_DELETE_SOCKET, defaultLanguage, "Delete" ) );
	}

	public static void loadConfig( File f ) throws Exception
	{
		Properties prop = new Properties();
		FileInputStream propFileIn = null;

		try
		{
			propFileIn = new FileInputStream( f );

			prop.load(propFileIn);

			checkProperties(prop);
		}
		catch (Exception e)
		{
			throw new Exception(e.getMessage() + ": Default Parameters load.");
		}
	}

	private static void checkProperties( Properties prop ) throws Exception
	{

		String lang = prop.get( LANGUAGE ).toString();

		if( lang == null )
		{
			throw new DataFormatException( "Language undefined. Label " + LANGUAGE + " is missing." );
		}
		else
		{
			boolean ok = false;
			for( String l : Locale.getISOLanguages() )
			{
				if( l.equals( lang ) )
				{
					ok = true;
					break;
				}			
			}

			if( !ok )
			{
				throw new DataFormatException( "Unknow language's id. Must be in java.util.Locale.getISOLanguages()." );	
			}
		}

		Iterator< Object > it = prop.keySet().iterator();
		while( it.hasNext() )
		{
			String captionID = it.next().toString();
			String txt = prop.getProperty( captionID );

			Caption cap = captions.get( captionID );

			if( cap == null )
			{
				cap = new Caption( captionID, lang, txt );
			}

			cap.setCaption( lang, txt );
		}
	}

	public static String getCaption( String captionID, String lang )
	{
		String txt = "";

		Caption cap = captions.get( captionID );

		if( cap != null )
		{
			txt = cap.getCaption( lang );

			if( txt == null )
			{
				txt = cap.getCaption( defaultLanguage );
			}
		}

		return txt;
	}
}