package sudoku_model;

import java.util.ArrayList;
import java.util.List;

import sudoku_model.ai.Box;
import sudoku_model.ai.BoxSolution;
import sudoku_model.ai.SolutionSet;

public class SudokuBoard {
	protected List<List<Integer>> mBoard;
	private ReasonForError mError;
	
	public SudokuBoard ()
	{
		mBoard = new ArrayList<>(9);
		mError = new ReasonForError();
		List<Integer> row;
		
		for (int i = 0; i < 9; i++)
		{
			row = new ArrayList<>(9);
			for (int j = 0; j < 9; j++)
				row.add(0);
			mBoard.add(row);
		}
		
	}
	
	public SudokuBoard(SudokuBoard original)
	{
		mBoard = new ArrayList<>(9);
		for (int i = 0; i <9; i++)
			mBoard.add(new ArrayList<>(original.getRow(i)));
	}
	
	public SudokuBoard (List<List<Integer>> original)
	{
		mBoard = original;
	}
	
	public synchronized boolean  set (int row, int col, int value)
	{
		//TODO: change set so that I know where the easy error is coming from
		mError = new ReasonForError();
		//System.out.println("The error is " + mError.getErrorType());
		if (value!= 0 && !checkInput(row,col,value))
			return false;
		mBoard.get(row).set(col, value);
		return true;
		
	}
	
	public boolean checkInput (int row, int col, int value)
	{
		//System.out.println("Running check Input");
		if(!checkRow(row,col, value))
			return false;
		if (!checkCol(row,col, value))
			return false;
		if (!checkBox(row,col, value))
			return false;
		return true;
	}
	
	public boolean checkRow (int row, int col, int value)
	{
		for (int i = 0; i < 9; i++)
			if (col!=i && mBoard.get(row).get(i) == value)
			{
				mError = new ReasonForError (row, i, value, ErrorType.ROW);
				return false;
			}
		return true;
	}
	
	public boolean checkCol (int row, int col, int value)
	{
		for (int i = 0; i < 9; i++)
			if (row!=i && mBoard.get(i).get(col) == value)
			{
				mError = new ReasonForError (i, col, value, ErrorType.COLUMN);
				return false;
			}
		return true;
	}
	
	public boolean checkBox (int row, int col, int value)
	{
		int lowerCol = lower(col);
		int upperCol = upper(col);
		int lowerRow = lower(row);
		int upperRow = upper(row);
		for (int i = lowerRow; i<=upperRow; i++)
			for (int j = lowerCol; j<=upperCol; j++)
				if(i!=row && j!=col && mBoard.get(i).get(j)==value)
				{
					mError = new ReasonForError (i, j, value, ErrorType.SQUARE);
					return false;
				}
		return true;
	}
	
	public void removeRow (List<Integer> solutions, int row, int col)
	{
		for (int i = 0; i <9; i++)
			if (i!= col && mBoard.get(row).get(i)!=0)
				solutions.remove((Integer)mBoard.get(row).get(i));
	}
	
	public void removeCol (List<Integer> solutions, int row, int col)
	{
		for (int i = 0; i < 9; i++)
			if (i!= row && mBoard.get(i).get(col)!=0)
				solutions.remove((Integer)mBoard.get(i).get(col));
	}
	
	public void removeSquare (List<Integer> solutions, int row, int col)
	{
		int lowerCol = lower(col);
		int upperCol = upper(col);
		int lowerRow = lower(row);
		int upperRow = upper(row);
		for (int i = lowerRow; i<=upperRow; i++)
			for (int j = lowerCol; j<=upperCol; j++)
				if(i!=row && j!=col && mBoard.get(i).get(j)!=0)
					solutions.remove((Integer)mBoard.get(i).get(j));
	}

	
	public int lower (int col)
	{
		return col-col%3;
	}
	
	public int upper (int col)
	{
		return lower(col)+2;
	}
	
	public List <Integer> getRow(int rowIndex)
	{
		return mBoard.get(rowIndex);
	}
	
	public List<List<Integer>> getBoard()
	{
		return mBoard;
	}
	
	public void printBoard()
	{
		
		System.out.println();
		for (int i = 0; i < mBoard.size(); i++)
		{
			//
			for (int j = 0; j < mBoard.get(i).size(); j++)
				System.out.print(mBoard.get(i).get(j) + " ");
			System.out.println();
		}
	}

	public ReasonForError getError() {
		return mError;
	}
	
	public static SudokuBoard initializeBoardFromSolution(SolutionSet solutions)
	{
		List <List<Integer>> board = new ArrayList<>(9);
		List<Integer> row =new ArrayList<>(9);
		int currentCol = 0;
		for (BoxSolution boxSolution: solutions.iterator())
		{
			//System.out.println("Box: row " + box.row + " col " + box.col);
			if (currentCol ==9)
			{
				currentCol = 0;
				if (row != null)
					board.add(row);
				row = new ArrayList<>(9);
			}
			row.add(boxSolution.isOnlySolution()? boxSolution.getSolution().get(0):0);
			currentCol++;
		}
		board.add(row);
		return new SudokuBoard(board);
	}
	
}
