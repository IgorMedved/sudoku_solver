package sudoku_model.ai;

import java.util.ArrayList;
import java.util.List;

// this class is an addition to the original design based on ideas contained in ai nanodegree udacity cours
public class BoxSolution {
	private static final List<Integer> FULL_SOLUTION = initializeFull();
	private List<Integer> mSolution;
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mSolution == null) ? 0 : mSolution.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoxSolution other = (BoxSolution) obj;
		if (mSolution == null) {
			if (other.mSolution != null)
				return false;
		} else if ( mSolution.size()!= other.mSolution.size())
			return false;
		else
			for (int i = 0; i<mSolution.size(); i++)
				if (mSolution.get(i)!=other.mSolution.get(i))
					return false;
		return true;
	}

	public List<Integer> getSolution() {
		return mSolution;
	}

	
	private static List<Integer> initializeFull()
	{
		List<Integer> solution = new ArrayList<>(9);
		for (int i = 1; i <= 9; i++)
			solution.add(i);
		return solution;
	}
	
	private BoxSolution ()
	{
		mSolution = new ArrayList<>(FULL_SOLUTION);
	}
	
	private BoxSolution (List<Integer> solution)
	{
		mSolution = new ArrayList<>(solution);
	}
	
	private BoxSolution (int value)
	{
		mSolution = new ArrayList<>(1);
		mSolution.add(value);
	}
	
	public static BoxSolution getFullSolution()
	{
		return new BoxSolution();
	}
	
	public static BoxSolution copySolution(List<Integer> original)
	{
		return new BoxSolution(original);
	}
	
	public static BoxSolution copySolution (BoxSolution original)
	{
		return new BoxSolution (original.mSolution);
	}
	
	public static BoxSolution getUniqueSolution(int value)
	{
		return new BoxSolution(value);
	}
	
	public boolean isNonExistant()
	{
		return mSolution.size() == 0;
	}
	
	public boolean isOnlySolution()
	{
		return mSolution.size() ==1;
	}
	
	public boolean remove (Integer entry)
	{
		if (entry <0 || entry >9)
			return false;
		else
			return mSolution.remove((Integer)entry);
	}
	
	public boolean remove (List<Integer> entries)
	{
		boolean removed = false;
		for (int i = 0; i <entries.size(); i++)
			if (mSolution.remove(entries.get(i)))
				removed = true;
		return removed;
	}
	
	public void setUnique(int value)
	{
		if (isValidValue(value))
		{
			mSolution = new ArrayList<>(1);
			mSolution.add(value);
		}
	}
	
	public static boolean inBounds (int i, int j)
	{
		return (inBounds (i) && inBounds(j));
	}
	
	private static boolean inBounds (int a)
	{
		return (a>=0 && a <9);
	}
	
	private static boolean isValidValue(int a)
	{
		return (a>0 && a <10);
	}

	public boolean contains(int i) {
		// TODO Auto-generated method stub
		return mSolution != null && mSolution.contains(i);
	}
	
	public void printSolution()
	{
		System.out.println("The solution is ");
		for (int i = 0; i < mSolution.size(); i++)
			System.out.print(mSolution.get(i) + " ");
		System.out.println();
	}
}
