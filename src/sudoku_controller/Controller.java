package sudoku_controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sudoku_model.SudokuBoard;
import sudoku_model.ai.SolutionSet;
import sudoku_model.ai.Solver;
import sudoku_model.ai.SudokuNewSolver;
import sudoku_model.ai.SudokuSolver;

public class Controller implements GuiListener {

	private SolutionListener mListener;
	
	SudokuBoard mInputBoard;
	Solver mSolver;
	ExecutorService executor;
	
	public Controller()
	{
		mInputBoard = new SudokuBoard();
		//mSolver = new SudokuSolver(mBoard);
		executor = Executors.newCachedThreadPool();
	}
	
	@Override
	public synchronized void  onTextEntered(int row, int col, int value) {
		if (mInputBoard.set(row, col, value))
		{
			System.out.println("Text Entered. Row " + row + " col " + col + " value " + value );
			//mBoard.printBoard();
			mSolver = new SudokuNewSolver();
			mInputBoard.printBoard();
			SolutionSet solution = SolutionSet.getInitialConditionsFromBoard(mInputBoard);
			System.out.println("\n\n\n\n\nIncoming Solution set");
			solution.printSolutionSet();
			executor.submit(new Runnable (){

				@Override
				public void run() {
					SolutionSet solution1 =mSolver.solve(solution);
					System.out.println("\n\n\n\n\nsolved Solution!!!");
					solution1.printSolutionSet();
					onSolutionComplete(SudokuBoard.initializeBoardFromSolution(solution1));
				}
			
			});
		}
		else
		{
			executor.submit (new Runnable(){

				@Override
				public void run() {
					mListener.onErrorInput (new ErrorInputEvent (mInputBoard.getError(), row, col));
					
				}
				
			});
		}
	}
	
	public void onSolutionComplete(SudokuBoard outputBoard)
	{
		//System.out.println("Solution is ready!" + mSolver.getSolutionStatus());
		//mSolver.printBoard();
		//mSolver.printSolutions();
		if (mListener!= null)
		{
			//SolutionCompleteEvent ev = new SolutionCompleteEvent(this, mSolver); 
			mListener.updateSolutionBoard(new SolutionCompleteEvent(this, outputBoard.getBoard(), mSolver.getSolutionStatus()));
		}
	}

	public void setListener(SolutionListener listener) {
		mListener = listener;
	}

	@Override
	public void onLoad() {
		mInputBoard = new SudokuBoard();
		onTextEntered(0,0,0);
		
	}

	//@Override
	//public void onSavePuzzlePressed(String inputString) {
		// TODO Auto-generated method stub
		
	//}

	

	//@Override
	//public void onSaveSolutionPressed(String string) {
		// TODO Auto-generated method stub
		
	//}

}
