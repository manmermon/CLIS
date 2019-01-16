package Auxiliar.Tasks;

import java.util.List;

import Controls.eventInfo;

public interface INotificationTask 
{
	/**
	 * Setting task monitor.
	 *  
	 * @param monitor: task monitor
	 */
	public void taskMonitor( ITaskMonitor monitor );
		
	
	/**
	 * To get the task result. 
	 * 
	 * @return task result.
	 */
	public List< eventInfo > getResult();
	
	/**
	 * Erase task result.
	 */
	public void clearResult();

}
