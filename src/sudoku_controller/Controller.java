package sudoku_controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sudoku_model.SudokuBoard;
import sudoku_model.ai.SudokuSolver;

public class Controller implements GuiListener {

	private SolutionListener mListener;
	
	SudokuBoard mBoard;
	SudokuSolver mSolver;
	ExecutorService executor;
	
	public Controller()
	{
		mBoard = new SudokuBoard();
		//mSolver = new SudokuSolver(mBoard);
		executor = Executors.newCachedThreadPool();
	}
	
	@Override
	public synchronized void  onTextEntered(int row, int col, int value) {
		if (mBoard.set(row, col, value))
		{
			System.out.println("Text Entered. Row " + row + " col " + col + " value " + value );
			//mBoard.printBoard();
			mSolver = new SudokuSolver(mBoard);
			mBoard.printBoard();
			executor.submit(new Runnable (){

				@Override
				public void run() {
					mSolver.solve();
					onSolutionComplete();
				}
			
			});
		}
		else
		{
			executor.submit (new Runnable(){

				@Override
				public void run() {
					mListener.onErrorInput (new ErrorInputEvent (mBoard.getError(), row, col));
					
				}
				
			});
		}
	}
	
	public void onSolutionComplete()
	{
		//System.out.println("Solution is ready!" + mSolver.getSolutionStatus());
		//mSolver.printBoard();
		//mSolver.printSolutions();
		if (mListener!= null)
		{
			//SolutionCompleteEvent ev = new SolutionCompleteEvent(this, mSolver); 
			mListener.updateSolutionBoard(new SolutionCompleteEvent(this, mSolver));
		}
	}

	public void setListener(SolutionListener listener) {
		mListener = listener;
	}

	@Override
	public void onLoad() {
		mBoard = new SudokuBoard();
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
