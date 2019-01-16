package StoppableThread;

public interface IStoppableThread 
{
	public final int StopWithTaskDone = -1;
	public final int StopInNextLoopInteraction = 0;
	public final int ForcedStop = 1;
	
	/**
     * 
     * @param friendliness:
     * - if friendliness < 0: stop execution when task is done.
     * - if friendliness = 0: stop execution before the next loop interaction.
     * - if friendliness > 0: interrupt immediately task and then execution is stopped.     
     */
    public void stopThread( int friendliness );
    
    /**
     * Start execution.
     */
    public void startThread() throws Exception;    
}
