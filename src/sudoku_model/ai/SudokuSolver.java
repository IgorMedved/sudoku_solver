package sudoku_model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import sudoku_controller.SolutionStatus;
import sudoku_model.SudokuBoard;

public class SudokuSolver extends SudokuBoard{
	private List<List<List<Integer>>> mSolutions;
	public static final List<Integer> ALL_SOLUTIONS = initializeSolutionCell();
	private boolean isSolved;
	private boolean impossibleSolve;
	private boolean madeProgress;
	
	public SudokuSolver(SudokuSolver copy)
	{
		super (copy);
		if (copy.mSolutions == null)
		
			mSolutions = createNewSolutions();
			
		else
			mSolutions = copy.mSolutions;
		isSolved = false;
		impossibleSolve = false;
		madeProgress = false;
	}
	
	public SudokuSolver (SudokuBoard board)
	{
		mBoard = new SudokuBoard(board).getBoard();
		mSolutions = createInitialSolutions();
		//printSolutions();
	}
	
	public List<List<List<Integer>>> createInitialSolutions()
	{
		List<List<List<Integer>>> solutions = new ArrayList<>(9);
		List<List<Integer>> rowSolutions;
		List<Integer> solution;
		for (int i = 0; i < 9; i++)
		{
			rowSolutions = new ArrayList<>(9);
			for (int j= 0; j<9; j++)
				if (mBoard.get(i).get(j)==0)
					rowSolutions.add(new ArrayList<>(ALL_SOLUTIONS));
				else
				{
					solution = new ArrayList<>(1);
					solution.add(mBoard.get(i).get(j));
					rowSolutions.add(solution);
				}
			solutions.add(rowSolutions);
		}
						
		
		return solutions;
	}
	
	private static List<Integer> initializeSolutionCell()
	{
		List<Integer> solutions = new ArrayList<>(9);
		
		for (int i = 1; i <=9; i++)
			solutions.add(i);
		return solutions;
	}
	
	private static List<List<List<Integer>>> createNewSolutions()
	{
		List<List<List<Integer>>> allSolutions = new ArrayList<>(9);
		List<List<Integer>> rowSolutions;
		for (int i = 0; i < 9; i++)
		{
			rowSolutions = new ArrayList<>(9);
			for (int j = 0; j <9; j++)
				rowSolutions.add(new ArrayList<>(ALL_SOLUTIONS));
			allSolutions.add(rowSolutions);
		}
		
		return allSolutions;
	}
	
	
	public void solve()
	{
		long start = System.nanoTime();
		
		do
		{
			madeProgress = false;
			isSolved = true;
			impossibleSolve = false;
			limitSolutionsByEliminatingKnownValues(); // The first strategy for solving sudoku is like this
											//if some number n that was either a part of the initial conditions
												// or was found during solution process occurs in a row r and col c 
												//  we know for sure that n cannot be present anywhere else
												//  where
												// 1) row = r, but col != c (in the same row)
												// 2) col = c, but row!=r
												// 3) row!=r || col!= c of a corresponding 3x3 square
												// so we eliminate n from a solution set at these positions
												// if during process of elimination one or more solution sets at r1 and c1
												// were reduced to size 1 we can repeat the process again
												// At the same time if solution sets are reduced to size 0, it means
												// that our problem is overconstrained, and no solution is possible 
												// without changing the initial conditions
			// impossibleSolve = true; // dont go
			// isSolved = true don't go
			// made progress dont go
			if ((!impossibleSolve) &&(!isSolved) &&(!madeProgress)) // the second strategy is more time consuming do not run it
			{													// unless there is no choice
				printSolutions();
				selectOnlyChoises(); // when we exhausted the possibility of advancement using 
								// the first strategy we can try another strategy:
								// if we look at a solution set in row r, and col c that consists of more than one entry
								// but one of those entries x is not present in any of the solutions set in
								// 1) row = r, but col!=c
								// 2) col = c, but row!=r
								// 3) row!=r && col!= c of a corresponding 3x3 square
								// then the solution for row r and col c must be x
			}
			// the first two strategies I discovered and implemented completely independently
			// while search and naked twin strategies
			// were suggested in the ai_nanodegree preview project
			//if ((!impossibleSolve) &&(!isSolved) &&(!madeProgress))
				//naked_twin();
		}
		while ((!isSolved || impossibleSolve) && madeProgress);
		long end = System.nanoTime();
		System.out.println("Time " + (end -start));
	}
	
	public void limitSolutionsByEliminatingKnownValues()
	{
		System.out.println("Start elimianation");
		List<Integer> solution; 
		outer: for (int i = 0; i < 9; i++)
			for (int j=0; j <9; j++)
			{
				int previousSize = 	mSolutions.get(i).get(j).size();
				if (mBoard.get(i).get(j)==0)
					isSolved = false;
				else //
				{
					System.out.println("mBoard value " + mBoard.get(i).get(j));
					solution = new ArrayList<>(1);
					solution.add(mBoard.get(i).get(j));
					mSolutions.get(i).set(j, solution);
				}
				
				
				removeRow(mSolutions.get(i).get(j), i,j);
				removeCol(mSolutions.get(i).get(j), i,j);
				removeSquare(mSolutions.get(i).get(j), i,j);
				
				if (previousSize!= mSolutions.get(i).get(j).size())
					madeProgress = true;
				
				if (mSolutions.get(i).get(j).size()==0)
				{
					impossibleSolve = true;
					break outer;
				}
				else if (mSolutions.get(i).get(j).size()==1)
				{
					if (mBoard.get(i).get(j) != mSolutions.get(i).get(j).get(0))
					{
						mBoard.get(i).set(j, mSolutions.get(i).get(j).get(0));
						madeProgress = true;
					}
					
				}
				
					
			}
	}
	
	public void selectOnlyChoises()
	{
		//System.out.println("Running only choice");
		List <Integer> solution;
		for (int i = 0; i < 9; i++)
			for (int j= 0; j < 9; j++)
			{
				//System.out.println("Running row " + i + " col " + j);
				solution = mSolutions.get(i).get(j);
				if (solution.size()!=1)
					for (int k = 0; k<mSolutions.get(i).get(j).size(); k++)
					{
						//System.out.println("Got into solution ");
						int onlySolution = solution.get(k);
						boolean row = rowUnique(i,j, solution.get(k));
						boolean col = columnUnique(i,j, solution.get(k));
						boolean box = boxUnique(i,j,solution.get(k));
						if (row || col || box)
						{
							//System.out.println("Before progress");
							//printSolutions();
							madeProgress = true;
							mBoard.get(i).set(j, solution.get(k));
							mSolutions.get(i).set(j, new ArrayList<>(1));
							mSolutions.get(i).get(j).add(onlySolution);
							//System.out.println("After progress");
							//printSolutions();
						}
					}
			}
	}
	
	/*public void naked_twin ()
	{
		Map<Pair, List<Integer>> possible_twins = new HashMap<>();
		for (int i = 0; i < 9; i++)
			for (int j=0; j<9; j++)
				if (mSolutions.get(i).get(j).size()==2)
					possible_twins.put(new Pair(i,j), mSolutions.get(i).get(j));
		for (Pair twin1 : possible_twins.keySet())
			for (Pair twin2: possible_twins.keySet())
				if (twin1!=twin2)
					if (possible_twins.get(twin1).get(0)==possible_twins.get(twin2).get(0) &&)
	}*/
	
	private boolean rowUnique(int row, int col, int value)
	{
		for (int i = 0; i <9; i++)
			if (i!=col)
				if (mSolutions.get(row).get(i).contains(value))
					return false;
		return true;
	}
	
	private boolean columnUnique(int row, int col, int value)
	{
		for (int i = 0; i < 9; i++)
			if (i!=row)
				if (mSolutions.get(i).get(col).contains(value))
					return false;
		return true;
	}
	
	private boolean boxUnique(int row, int col, int value)
	{
		int lowerCol = lower(col);
		int upperCol = upper(col);
		int lowerRow = lower(row);
		int upperRow = upper(row);
		for (int i = lowerRow; i<=upperRow; i++)
			for (int j = lowerCol; j<=upperCol; j++)
				if(i!=row || j!=col)
					if (mSolutions.get(i).get(j).contains(value))
						return false;
		return true;
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
	
	
	public void printSolutions()
	{
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
			{
				System.out.println("Solution for " + i + " " + j +": ");
			
				for (int k = 0; k< mSolutions.get(i).get(j).size(); k ++)
					
					System.out.print(mSolutions.get(i).get(j).get(k) +" ");
				System.out.println();
			}
	}
}
