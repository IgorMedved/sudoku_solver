package sudoku_controller;

import java.util.EventObject;
import java.util.List;

import sudoku_model.ai.SudokuSolver;

public class SolutionCompleteEvent extends EventObject {
	private List<List<Integer>> mSolutionBoard;
	private SolutionStatus mStatus;


	public SolutionCompleteEvent(Object source, SudokuSolver solver) {
		super(source);
		mSolutionBoard = solver.getBoard();
		mStatus = solver.getSolutionStatus();
	}


	public List<List<Integer>> getSolutionBoard() {
		return mSolutionBoard;
	}


	public SolutionStatus getStatus() {
		return mStatus;
	}
}
