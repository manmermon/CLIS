package Activities;

public class ActivityInfo
{
	private int phaseNumber = 1;
	private int answerPhase = 1;
	private int mainPhase = 1;
	private boolean withAnswer = true;
	private boolean withAdaptableTimer = true;

	public ActivityInfo(int PhaseNumber, int MainPhase, int AnswerPhase, boolean WithAnswer, boolean adaptableTimer)
	{
		if ((PhaseNumber < 1) || 
				(AnswerPhase < 1) || 
				(MainPhase < 1))
		{
			throw new IllegalArgumentException("Number of phases, then main phase and the answer phase must be > 0");
		}

		this.phaseNumber = PhaseNumber;
		this.mainPhase = MainPhase;
		this.answerPhase = AnswerPhase;
		this.withAnswer = WithAnswer;
		this.withAdaptableTimer = adaptableTimer;
	}

	public int getPhaseNumber()
	{
		return this.phaseNumber;
	}

	public int getMainPhase()
	{
		return this.mainPhase;
	}

	public int getAnswerPhase()
	{
		return this.answerPhase;
	}

	public boolean hasAnswer()
	{
		return this.withAnswer;
	}

	public boolean isAdaptableTimer()
	{
		return this.withAdaptableTimer;
	}
}