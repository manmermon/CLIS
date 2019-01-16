package Activities;

import Config.Language.Caption;

public class ActivityRegInfo extends Caption
{
	private Class ActivityClass = null;
	private boolean challengeActivity = false;

	/**
	 * Create a activity information to register
	 * 
	 * @param id:  identifier. This is used as default caption.
	 * @param activityClass: class type
	 * @param isPlugin: class type
	 * @throws IllegalArgumentException: 
	 * 			if id == null || id.trim().isempty() 
	 * 				|| activityClass == null || !(activityClass instanceof IActivity )
	 */  
	public ActivityRegInfo(String id, Class activityClass, boolean challenge) throws IllegalArgumentException
	{
		super(id, Config.Language.Language.defaultLanguage, id);

		this.setActivityClass(activityClass);

		this.challengeActivity = challenge;
	}

	public void setActivityClass(Class activityClass) throws IllegalArgumentException
	{
		if ((activityClass == null) || 
				(!IActivity.class.isAssignableFrom(activityClass)))
		{
			throw new IllegalArgumentException("Activity class must be instance of IActivity");
		}

		this.ActivityClass = activityClass;
	}

	/**
	 * 
	 * @return parameter value
	 */
	public Class getActivityClass()
	{
		return this.ActivityClass;
	}

	public boolean ischallengeActivity()
	{
		return this.challengeActivity;
	}
}