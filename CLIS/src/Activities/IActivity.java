package Activities;

import Auxiliar.Tasks.ITask;
import Auxiliar.WarningMessage;

public abstract interface IActivity extends ITask
{
	public static final int NUMBER_DIFICUL_LEVELS = 5;
	public static final int ACTIVITY_STATE_MAKING = -3;
	public static final int ACTIVITY_STATE_WAIT_ANSWER = -2;
	public static final int ACTIVITY_STATE_WAIT_START = -1;
	public static final int ACTIVITY_STATE_END = 0;
	public static final int ACTIVITY_STATE_ANSWER = 1;
	public static final int ACTIVITY_STATE_READY = 2;
	public static final int ACTIVITY_STATE_DONE = 3;

	public abstract void stopActivity();

	public abstract void suspendActivity();

	public abstract boolean isActivitySupended();

	public abstract void resumeActivity();

	public abstract boolean isAdaptableTimer();

	public abstract boolean hasReport();

	public abstract String getReport();

	public abstract String getReportHeader();

	public abstract int getNumberOfCurrentPhase();

	public abstract int getNumberOfPhases();

	public abstract boolean isFinished();

	public abstract boolean isMainPhase();

	public abstract WarningMessage checkParameters();
}