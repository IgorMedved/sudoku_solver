package sudoku_model.ai;

import sudoku_controller.SolutionStatus;

public abstract class Solver {
	protected boolean isSolved;
	protected boolean impossibleSolve;
	protected boolean madeProgress;
	protected transient boolean interrupted = false;
	public static int counter;
	//public static String message;
	public Solver ()
	{
		madeProgress = false;
		isSolved = true;
		impossibleSolve = false;
	}
	
	public abstract SolutionSet solve(SolutionSet incomingSolution);
	
	public void stop()
	{
		interrupted = true;
	}
	
	public SolutionStatus getSolutionStatus()
	{
		SolutionStatus status = SolutionStatus.UNSOLVED;
		if (impossibleSolve)
			status = SolutionStatus.OVERCONSTRAINED;
		else if (isSolved)
			status = SolutionStatus.SOLVED;
		
		return status;
	}
}
