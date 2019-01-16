package Auxiliar.Tasks;

public interface ITaskMonitor 
{
	/**
	 * The task reports to its monitor that the result is available.
	 * 
	 * @param IActivity source
	 */
	public void taskDone( INotificationTask task ) throws Exception;
}
