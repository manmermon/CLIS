package Activities;

import Config.ConfigApp;
import Plugin.ClisPlugin;
import Plugin.PluginConfiguration.PluginConfig;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.LinkedHashMap;
import java.util.ServiceLoader;

public class ActivityRegister
{
	private static ActivityRegister reg = null;

	private static LinkedHashMap<String, ActivityRegInfo> activitiesID = new LinkedHashMap<String, ActivityRegInfo >();
	private static LinkedHashMap<String, Boolean> isPlugin = new LinkedHashMap<String, Boolean>();
	private static LinkedHashMap<String, PluginConfig> pluginCfg = new LinkedHashMap<String, PluginConfig>();

	private static final String fileSeparator = FileSystems.getDefault().getSeparator();
	private static final String pluginDirName = "Plugin" + fileSeparator;

	public static ActivityRegister getInstance()
	{
		if (reg == null)
		{
			reg = new ActivityRegister();
		}

		return reg;
	}

	/**
	 * Register predefined activities
	 */
	public static void LoadPredeterminateActivities()
	{
		AritmeticActivity.registerActivity();
		MemoryTask.registerActivity();
		SearchCountTask.registerActivity();
		SortedDotsTask.registerActivity();
		StroopActivity.registerActivity();
		TrajectoryTask.registerActivity();
		AffectiveActivity.registerActivity();

		for (String id : activitiesID.keySet())
		{
			isPlugin.put(id, false );
		}
	}
	/**
	 * Search activity plugin 
	 */
	public static void searchPlugin()
	{
		try
		{
			String plgPath = ActivityRegister.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

			if (!plgPath.endsWith("/"))
			{
				plgPath = plgPath + fileSeparator;
			}

			plgPath = plgPath + pluginDirName;



			File dir = new File(plgPath);

			if ((dir.exists()) && (dir.isDirectory()))
			{
				File[] pluginList = dir.listFiles(new FilenameFilter()
				{

					public boolean accept(File dir, String name)
					{
						return name.toLowerCase().endsWith(".jar");
					}

				});
				URL[] urls = new URL[pluginList.length];
				for (int i = 0; i < pluginList.length; i++)
				{
					urls[i] = pluginList[i].toURI().toURL();
				}
				URLClassLoader ucl = new URLClassLoader(urls);

				ServiceLoader<ClisPlugin> sl = ServiceLoader.load(ClisPlugin.class, ucl);

				for (ClisPlugin c : sl)
				{
					System.out.println("ActivityRegister.searchPlugin() " + c.getActivityRegister().getID());
				}

			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param activityID: Parameter with activity identifier, caption and class type
	 * @throws IllegalArgumentException: if input is null. 
	 */
	public static void registerActivity(ActivityRegInfo activityID) throws IllegalArgumentException
	{
		if (activityID == null)
		{
			throw new IllegalArgumentException("Input is null.");
		}

		activitiesID.put(activityID.getID(), activityID);
	}

	/**
	 * 
	 * @return number of registered activities
	 */
	public static int getNumberOfActivities()
	{
		return activitiesID.size();
	}

	/**
	 * 
	 * @return All registered activity captions 
	 */
	public String[] getActivityCaptionList()
	{
		String[] caps = new String[activitiesID.size()];

		int i = 0;
		for (ActivityRegInfo cap : activitiesID.values())
		{
			caps[i] = cap.getCaption(ConfigApp.getProperty("LANGUAGE").toString());
			i++;
		}

		return caps;
	}

	/**
	 * 
	 * @return All activity identifiers
	 */
	public static String[] getActivitiesID()
	{
		String[] acts = new String[activitiesID.size()];

		int i = 0;
		for (ActivityRegInfo act : activitiesID.values())
		{
			acts[i] = act.getID();
			i++;
		}

		return acts;
	}

	public static String getActivityID(int index) throws IndexOutOfBoundsException
	{
		if ((index < 0) || 
				(index >= activitiesID.size()))
		{
			throw new IndexOutOfBoundsException("Index out of bounds.");
		}

		return activitiesID.keySet().toArray()[index].toString();
	}

	public static boolean isPluginActivity(String id)
	{
		Boolean is = isPlugin.get(id);

		if (is == null)
		{
			is = false;
		}

		return is.booleanValue();
	}

	public static boolean isChallegeActivity(String id)
	{
		return activitiesID.get(id).ischallengeActivity();
	}

	public static PluginConfig getActivityConfig(String id)
	{
		return pluginCfg.get(id);
	}
	/**
	 * 
	 * @param id: activity identifier
	 * @return activity class
	 * @throws IllegalArgumentException: if activity id is not found.
	 */
	public static Class getActivityClass(String id)
			throws IllegalArgumentException
	{
		Object cl = activitiesID.get(id);

		if (cl == null)
		{
			throw new IllegalArgumentException("Activity ID not found.");
		}

		return ((ActivityRegInfo)cl).getActivityClass();
	}
}