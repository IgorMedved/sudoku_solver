package sudoku_controller;

public enum SolutionStatus {
	SOLVED, UNSOLVED, OVERCONSTRAINED;
	
	
	public String toString()
	{
		switch (this)
		{
		case SOLVED:
			return "Solved";
		case UNSOLVED:
			return "Unsolved";
		default:
			return "Overconstrained";
		
		}
	}
}
