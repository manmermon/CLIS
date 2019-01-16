package Timers;

public interface ITimerMonitor 
{
	/**
	 * Notify that timer is over.
	 */
	public void timeOver( String timerName );
	
	/**
	 * Report time state. 
	 * 
	 * @param time
	 */
	public void reportClockTime( long time );	
}
