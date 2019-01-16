package Activities;

import java.util.HashMap;
import java.util.Map;


public class activityParameters
{
	public static final int INFINITY_REPETITIONS = -1;
	private int repetitions = INFINITY_REPETITIONS;

	private String activityType = "";

	private int difficultLevel = 0;

	private Map<String, Object> activityExtraPars = null;

	private boolean[] enablePhases = { true };

	private ActivityInfo info = null;

	private boolean isPlugin = false;

	private int firstEnablePhase = 0;

	public activityParameters()
	{
		this.activityExtraPars = new HashMap< String, Object >();
	}

	public void setEnablePhases(boolean[] enable)
	{
		if (enable == null)
		{
			throw new IllegalArgumentException("Input boolean array null");
		}
		this.enablePhases = enable;

		for( this.firstEnablePhase = 0; 
				this.firstEnablePhase < this.enablePhases.length && !this.enablePhases[ this.firstEnablePhase ]; 
				this.firstEnablePhase++ );

		if (this.firstEnablePhase >= this.enablePhases.length)
		{
			this.firstEnablePhase = -1;
		}
	}

	public boolean[] getEnablePhases()
	{
		return this.enablePhases;
	}

	public int getFirstEnablePhase()
	{
		return this.firstEnablePhase;
	}

	public void setRepetitions(int rep)
	{
		this.repetitions = rep;
	}

	public int getRepetitions()
	{
		return this.repetitions;
	}

	public void setDifficultLevel(int level)
	{
		this.difficultLevel = level;
	}

	public int getDifficultLevel()
	{
		return this.difficultLevel;
	}

	public void setActivityType(String type)
	{
		this.activityType = type;
	}

	public String getActivityType()
	{
		return this.activityType;
	}

	public void addExtraParameter(String parameterID, Object par)
	{
		this.activityExtraPars.put(parameterID, par);
	}

	public Map<String, Object> getExtraParamters()
	{
		return this.activityExtraPars;
	}

	public void setActivityInfo(ActivityInfo actInfo, boolean isActivityPlugin)
	{
		if (actInfo == null)
		{
			throw new IllegalArgumentException("ActivityInfo null");
		}

		this.info = actInfo;

		this.isPlugin = isActivityPlugin;
	}

	public ActivityInfo getActivityInfo()
	{
		return this.info;
	}

	public boolean isPlugin()
	{
		return this.isPlugin;
	}
}