package Auxiliar.Tasks;

public interface ITask extends INotificationTask
{
	/**
	 * Add a parameter. 
	 * 
	 * @param parameterID: parameter id. 
	 * @param parameter: parameter value
	 */
	public void addParameter( short parameterID, Object parameter ) throws Exception;
	
	/**
	 * Set a parameter. 
	 * 
	 * @param parameterID: parameter id. 
	 * @param parameter: parameter value
	 */
	public void setParameter( short parameterID, Object parameter ) throws Exception;
	
	/**
	 * Clear a parameter. 
	 * 
	 */
	public void clearParameters( );
		
	/**
	 * Create the task.
	 * 
	 *  @throws Exception
	 */
	public void createTask( ) throws Exception;
	
	/**
	 * Doing the task. When this one is done, a report is sent to the monitor.
	 * The monitor can get the result using getResult() function.
	 * 
	 * @throws Exception 
	 */
	public void startTask( ) throws Exception ;	
}
