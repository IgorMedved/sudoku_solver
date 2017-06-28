package sudoku_model.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sudoku_model.SudokuBoard;

public class SolutionSet{
	private Map<Box, BoxSolution> mSolutionSet;

	public SolutionSet()
	{
		mSolutionSet = new LinkedHashMap<>();
		for (int i = 0; i < 9; i++)
			for (int j=0; j<9; j++)
				mSolutionSet.put(new Box(i,j), BoxSolution.getFullSolution());
	}
	
	private SolutionSet(SudokuBoard board)
	{
		mSolutionSet = new LinkedHashMap<>();
		for (int i = 0; i < 9; i++)
			for (int j=0; j<9; j++)
				if ( board.getBoard().get(i).get(j) <=9 && board.getBoard().get(i).get(j) >0)
					mSolutionSet.put(new Box(i,j), BoxSolution.getUniqueSolution(board.getBoard().get(i).get(j)));
				else
					mSolutionSet.put(new Box(i,j), BoxSolution.getFullSolution());
	}
	
	
	private SolutionSet(SolutionSet original)
	{
		mSolutionSet = new LinkedHashMap<>();
		for (Box box: original.mSolutionSet.keySet())// there is no need to create new object for the box, because it's not modified
			mSolutionSet.put(box, BoxSolution.copySolution(original.mSolutionSet.get(box)));
	}
	
	public static SolutionSet getCopy(SolutionSet original)
	{
		return new SolutionSet(original);
	}
	
	public static SolutionSet getInitialConditionsFromBoard (SudokuBoard board)
	{
		return new SolutionSet(board);
	}
	
	public boolean removeSolution (int i, int j, int value)
	{
		if (BoxSolution.inBounds(i,j))
			return removeSolution(new Box(i,j), value);
		return false;
	}
	
	public boolean removeSolution (Box box, int value)
	{
		BoxSolution solution = mSolutionSet.get(box);
		boolean removed = false;
		if (solution!=null)
		{
			removed = solution.remove(value);
		}
		return removed;
	}
	
	public boolean removeSolutions (int i, int j, List<Integer> values)
	{
		Box box = new Box (i,j);
		return removeSolutions(box, values);
	}
	
	public boolean removeSolutions(Box box, List<Integer> values)
	{
		boolean removed = false;
		BoxSolution solution = mSolutionSet.get(box);
		if (solution!= null)
			for (int k = 0; k < values.size(); k++)
			{
			    if (removeSolution (box, values.get(k)))
			    	removed = true;
			}
		return removed;
	}
	
	public boolean contains (Box box)
	{
		return mSolutionSet!=null && mSolutionSet.containsKey(box);
	}

	public BoxSolution getSolution(Box box1) {
		// TODO Auto-generated method stub
		
		return mSolutionSet == null? null: mSolutionSet.get(box1)== null? BoxSolution.copySolution(new ArrayList<>()): mSolutionSet.get(box1);
	}
	
	public BoxSolution getSolution (int i, int j){
		return getSolution(new Box(i,j));
	}

	
	public Collection<BoxSolution> iterator() {
		// TODO Auto-generated method stub
		return  mSolutionSet.values();
		
	}
	
	public Set<Box> keySet()
	{
		return mSolutionSet.keySet();
	}
	
	public void printSolutionSet()
	{
		for (Box box: mSolutionSet.keySet())
		{
			System.out.println("Solution for box " + box.row + " " + box.col);
			mSolutionSet.get(box).printSolution();
		}
	}
}
