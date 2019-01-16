package Timers;

public class TimerParameters 
{
	private long time = 0L;
	
	private boolean activeBeep = false;
	
	private boolean activeTimeReport = false;

	/**
	 * Set timer properties.
	 * 
	 * @param t
	 * 	time value. If t < 0 then infinity timer.
	 * 
	 * @param beep
	 * 	Active beep sound.
	 * 
	 * @param report
	 * 	Active the report of time.
	 */
	public TimerParameters( long t, boolean beep, boolean report )
	{			
		this.time = t;
		this.activeBeep = beep;
		this.activeTimeReport = report;
	}
	
	public void setTime( long t )
	{
		this.time = t;
	}
		
	public long getTime( )
	{
		return this.time;
	}
	
	public void setEnableBeep( boolean beep )
	{
		this.activeBeep = beep;
	}
	
	public boolean isEnabledBeep()
	{
		return this.activeBeep;
	}
	
	public void setEnableTimeReport( boolean report )
	{
		this.activeTimeReport = report;
	}
	
	public boolean isEnableTimeReport()
	{
		return this.activeTimeReport;
	}
	
	@Override
	public String toString() 
	{
		return "Timer parameters: is beep active? " + this.activeBeep +
				", is time report active? " + this.activeTimeReport +
				", time = " + this.time;
	}
}
