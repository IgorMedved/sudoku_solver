package sudoku_model.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.SynthSeparatorUI;

import sudoku_controller.SolutionStatus;

public class SudokuNewSolver extends Solver{
	

	public SudokuNewSolver()
	{
		super();
	}

	public SolutionSet solve (SolutionSet incomingSolution)
	{
		int count = 1;
		do
		{
			madeProgress = false;
			isSolved = true;
			impossibleSolve = false;
			System.out.println("Running limitSolution for the " + count + " time ");
			limitSolutionsByEliminatingKnownValues(incomingSolution); // The first strategy for solving sudoku is like this
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
			System.out.println("Impossible solve " + impossibleSolve + " is solved " + isSolved + " made progress "+  madeProgress);
			
			if ((!impossibleSolve) &&(!isSolved) &&(!madeProgress)) // the second strategy is more time consuming do not run it
			{													// unless there is no choice
				//printSolutions();
				System.out.println("Running onlychoices for the " + count + " time ");
				incomingSolution.printSolutionSet();
				selectOnlyChoises(incomingSolution); // when we exhausted the possibility of advancement using 
								// the first strategy we can try another strategy:
								// if we look at a solution set in row r, and col c that consists of more than one entry
								// but one of those entries x is not present in any of the solutions set in
								// 1) row = r, but col!=c
								// 2) col = c, but row!=r
								// 3) row!=r && col!= c of a corresponding 3x3 square
								// then the solution for row r and col c must be x
				System.out.println("Impossible solve " + impossibleSolve + " is solved " + isSolved + " made progress "+  madeProgress);
			}
			// the first two strategies I discovered and implemented completely independently
			// while search and naked twin strategies
			// were suggested in the ai_nanodegree preview project
			
			if ((!impossibleSolve) &&(!isSolved) &&(!madeProgress))
			{														// this is another non-guessing strategy for solving sudoku. It was suggested in the AI course
				System.out.println("Running all_twins for the " + count + " time ");
				incomingSolution.printSolutionSet();
				all_twins(incomingSolution);						// at Udacity. In a sense it is just an extension of eliminating known values strategy
																	// it works in the following way:
																	// Imagine we have two boxes among the peers that only have two possible remaining solutions that are the same
																	// i.e (the solution @ box 0,0 can only be 4,7 and solution @ box 1,1 can only be 4,7)
																	// it must follow that all other peers cannot have the same two solutions
																	// i.e( the boxes 0,1; 0,2; 1,0; 1,2; 2,0; 2,1; 2,2 can't have solutions 4,7) and thus we can eliminate
																	// this pair of solutions from the rest of the peers 
																	// same logic applies to 3 boxes of 3 remaining solutions, 4 boxes of 4 solutions and so on)
																	// if there is more than n boxes among the peers with only n solutions remaining it follows that the system
																	// is over-constrained and cannot be solved
				System.out.println("Impossible solve " + impossibleSolve + " is solved " + isSolved + " made progress "+  madeProgress);
			}
			if ((!impossibleSolve) &&(!isSolved) &&(!madeProgress))
			{
				System.out.println("Running multiplechoice for the " + count + " time ");
				selectMultipleChoice(incomingSolution);
				System.out.println("Impossible solve " + impossibleSolve + " is solved " + isSolved + " made progress "+  madeProgress);
			}
			
			if ((!impossibleSolve) &&(!isSolved) &&(!madeProgress))
				onlyRowOrColInSquare(incomingSolution);
			count ++;
		}
		while ((!isSolved || impossibleSolve) && madeProgress);
		long end = System.nanoTime();
		//System.out.println("Time " + (end -start));
		return incomingSolution;
	}
	
	private void onlyRowOrColInSquare (SolutionSet incomingSolution)
	{
		BoxSolution unsolvedForSquare;
		Box firstBox;
		Box lastBox;
		List<Box> thisSquarePeers;
		List<Box> remainingPeers;
		// go over all the squares and find solution values that are still undetermined for each square
		for (int i = 0; i < 9; i+=3)
		{
			for (int j = 0; j< 9; j+=3)
			{
				firstBox = new Box(i,j);
				
				unsolvedForSquare =  BoxSolution.getFullSolution();
				thisSquarePeers = new ArrayList<>(9);
				thisSquarePeers.add(firstBox);
				thisSquarePeers.addAll(Peers.getSquarePeers().get(firstBox));
				for (Box tempBox: thisSquarePeers)
					if (incomingSolution.getSolution(tempBox).isOnlySolution())
						unsolvedForSquare.remove(incomingSolution.getSolution(tempBox).getSolution());
				System.out.println("unsoved for square "+  i +" " + j);
				for (Integer sol: unsolvedForSquare.getSolution())
					System.out.print(sol +" ");
				System.out.println();
				for (Integer individualSolution:unsolvedForSquare.getSolution())
				{
					for (int row = i; row < i+3; row++)
					{
						System.out.println("Row is "+ row + " current solution " + individualSolution);
						if (isOnlyInRowI (incomingSolution, thisSquarePeers, row, individualSolution))
						{
							System.out.println("only in row i " + row);
							lastBox = new Box(row, j+2);
							remainingPeers = Peers.getNonSquareRowPeers().get(lastBox);
							for (Box remainingPeer: remainingPeers)
							{
								if (!incomingSolution.getSolution(remainingPeer).isOnlySolution() &&incomingSolution.getSolution(remainingPeer).remove(individualSolution))
								{
									System.out.println("Removing " + individualSolution + " from " + remainingPeer);
									madeProgress = true;
								}
							}
						}
					}
					for (int col = j; col < j+3; col++)
						if (isOnlyInColJ (incomingSolution, thisSquarePeers, col, individualSolution))
						{
							System.out.println("Only in col " + col);
							lastBox = new Box (i+2, col);
							remainingPeers = Peers.getNonSquareColPeers().get(lastBox);
							for (Box remainingPeer: remainingPeers)
							{
								if (!incomingSolution.getSolution(remainingPeer).isOnlySolution() && incomingSolution.getSolution(remainingPeer).remove(individualSolution))
									madeProgress = true;
							}
						}
				}
				
			}
		}
	}
	
	private boolean isOnlyInColJ(SolutionSet incomingSolution, List<Box> thisSquarePeers, int j,
			Integer individualSolution) {
		int col = 0;
		boolean onlyColJ = true;
		for (Box currentBox: thisSquarePeers)
		{
			if (col!= j && incomingSolution.getSolution(currentBox).contains(individualSolution))
				return false;
			col++;
			if (col>2)
				col = 0;
		}
		return onlyColJ;
	}

	private boolean isOnlyInRowI(SolutionSet incomingSolution, List<Box> thisSquarePeers, int i, Integer individualSolution) {
		int col = 0;
		int row = 0;
		boolean onlyRowI = true;
		for (Box currentBox: thisSquarePeers)
		{
			if (row !=i && incomingSolution.getSolution(currentBox).contains(individualSolution))
				return false;
			col++;
			if (col >2)
			{
				col=0;
				row++;
			}
			
		}
		return onlyRowI;
	}

	private void selectMultipleChoice(SolutionSet incomingSolution) {
		// TODO Auto-generated method stub
		BoxSolution solution1;
		BoxSolution unusedValues;
		//System.out.println("Running multiple choice");
		Map<Box, Boolean> rowMap = MultipleChoiceMaps.getUsedValuesMap();
		Map<Box, Boolean> colMap = MultipleChoiceMaps.getUsedValuesMap();
		Map<Box, Boolean> squareMap = MultipleChoiceMaps.getUsedValuesMap();
		List<Integer> repeatValues;
		for (Box box1: incomingSolution.keySet()) // go over every box
		{
			//System.out.println("processing box " + box1);
			solution1 = incomingSolution.getSolution(box1);
			//System.out.println("Solution is ");
				/*for (int sol: solution1.getSolution())
				{
					System.out.print(sol + " ");
				}
				System.out.println();*/
			if(solution1.getSolution().size()>1) 
			{
				
				//System.out.println("solution size more than 1");
				for (int i = 0; i <solution1.getSolution().size(); i++) // go over every solution set larger than 1
				{
					
					int currentSolutionValue = solution1.getSolution().get(i);
					//System.out.println("Current solution value is " + currentSolutionValue);
					Box temp =new Box (box1.row, currentSolutionValue);
					if (!rowMap.get(temp))
					{
						rowMap.put(temp, true); // update row map
						//System.out.println("used value for row " + temp.row + " is " +  temp.col);
						repeatValues = findAndExcludeRepeats (incomingSolution, solution1, currentSolutionValue, i, box1, rowMap, box1.row,
								Peers.getRowPeers().get(box1), Peers.getRightRowPeers().get(box1), "row");
						
						if (repeatValues != null)
						{
							for (int j = 1; j < repeatValues.size(); j++)
								rowMap.put(new Box(box1.row, repeatValues.get(j)), true);
							i= solution1.getSolution().indexOf(currentSolutionValue);
							/*System.out.println("Repeat values for row: " + box1.row);
							for (int value: repeatValues)
								System.out.print(value + " ");
							System.out.println();*/
						}
						else
						{
							
						}
					}
					temp = new Box (box1.col, currentSolutionValue);
					if (!colMap.get(temp))
					{
						colMap.put(temp, true);
						repeatValues  = findAndExcludeRepeats (incomingSolution, solution1, currentSolutionValue, i, box1, colMap, box1.col,
								Peers.getColPeers().get(box1), Peers.getDownColPeers().get(box1), "col");
						if (repeatValues != null)
						{
							for (int j = 1; j < repeatValues.size(); j++)
								colMap.put(new Box(box1.col, repeatValues.get(j)), true);
							i= solution1.getSolution().indexOf(currentSolutionValue);
							/*System.out.println("Repeat values for col : " + box1.col);
							for (int value: repeatValues)
								System.out.print(value + " ");
							System.out.println();*/
						}
					}
					temp = new Box (MultipleChoiceMaps.squareNumber(box1.row, box1.col), currentSolutionValue);
					if (!squareMap.get(temp))
					{
						squareMap.put(temp, true);
						repeatValues = findAndExcludeRepeats (incomingSolution, solution1, currentSolutionValue, i, box1, squareMap, MultipleChoiceMaps.squareNumber(box1.row, box1.col), 
								Peers.getSquarePeers().get(box1), Peers.getRemainingSquarePeers().get(box1), "square");
						if (repeatValues != null)
						{
							for (int j = 1; j < repeatValues.size(); j++)
								squareMap.put(new Box(MultipleChoiceMaps.squareNumber(box1.row, box1.col), repeatValues.get(j)), true);
							i= solution1.getSolution().indexOf(currentSolutionValue);
/*							System.out.println("Repeat values for square: " + MultipleChoiceMaps.squareNumber(box1.row, box1.col));
							for (int value: repeatValues)
								System.out.print(value + " ");
							System.out.println();*/
						}
					}
				}
			}
		}
	}

	private List<Integer> findAndExcludeRepeats(SolutionSet incomingSolution, BoxSolution solution1, int currentSolutionValue, int i, Box box1, Map<Box, Boolean> usedValues, int location,
			List<Box> allPeers, List<Box> remainingPeers, String message) 
	{
		List<Box> positionRepeats1 = findRepeats(incomingSolution, remainingPeers, currentSolutionValue); // boxes in which value 1 present
		/*System.out.println(message + " position repeats for value " + currentSolutionValue);
		for (Box box: positionRepeats1 )
			System.out.println(box);*/
		List<Integer> values = new ArrayList<>();
		//System.out.println("Current solution is " + currentSolutionValue);
		values.add(currentSolutionValue);
		//System.out.println(message + " positionRepeats size is " + positionRepeats1.size() +  " solution1 size -i is " + (solution1.getSolution().size() -i));
		if (positionRepeats1.size() < solution1.getSolution().size() -i) // it is guaranteed that size should be larger than 0
		{
			int numRepeats = 0;
			for (int j = i+1; j< solution1.getSolution().size(); j++)
			{
				int nextValue = solution1.getSolution().get(j);
				// check that next value was not used before
				if(!usedValues.get(new Box(location, nextValue)))
				{
				//Box temp1 = new Box(box1.row, nextValue);
					List <Box> positionRepeats2 = findRepeats(incomingSolution, remainingPeers, nextValue);
				/*System.out.println(message + " position repeats for value " + nextValue);
				for (Box box: positionRepeats2 )
					System.out.println(box);*/
					if (positionRepeats1.size() == positionRepeats2.size())
					{
					//System.out.println(message + " position repeats are equal for values " + currentSolutionValue + " and "+ nextValue);
						boolean shouldAdd = true;
						for (int k = 0; k < positionRepeats1.size(); k++)
						{
						//System.out.println();
							if (positionRepeats1.get(k)!=positionRepeats2.get(k))
							{
								shouldAdd =false;
								break;
							}
						
						}
						if(shouldAdd)
						{
						//System.out.println("Adding value "+ nextValue);
							values.add(nextValue);
						}
					}
						
				}
			}
			if (values.size() == positionRepeats1.size()+1) 
			{
				/*List<Box> allRowPeers = Peers.getRowPeers().get(box1);
				for (Box peer : allRowPeers)
					if (box1== peer || rowPositionRepeats1.contains(peer))
						for (int l = 0; l< solution1.getSolution().size(); l++)
							if(incomingSolution.removeSolutions(peer, values))
								madeProgress = true;*/
				
				positionRepeats1.add(box1);
				BoxSolution removableValues = BoxSolution.getFullSolution();
				removableValues.remove(values);
				for (Box removable: positionRepeats1)
					if (incomingSolution.removeSolutions(removable, removableValues.getSolution()))
						madeProgress = true;
				return values;
					
			}
		}
		return null;
		
	}

	private List<Box> findRepeats(SolutionSet incomingSolution, List<Box> remainingPeers, int currentSolutionValue) {
		List<Box> repeats = new ArrayList<>(remainingPeers.size());
		for (Box peer: remainingPeers)
		{
			if (incomingSolution.getSolution(peer).contains(currentSolutionValue))
				repeats.add(peer);
		}
		return repeats;
	}

	private void naked_twin(SolutionSet incomingSolution) {
		BoxSolution solution1;
		for (Box twin1: incomingSolution.keySet())
		{
			solution1 = incomingSolution.getSolution(twin1);
			if (solution1.getSolution().size() == 2)
			{
				// check for twins in the same row
				checkForTwinsAmongPeers(incomingSolution, twin1, solution1, Peers.getRightRowPeers().get(twin1), Peers.getRowPeers().get(twin1));
				
				// check for twins in the same col
				checkForTwinsAmongPeers(incomingSolution, twin1, solution1, Peers.getDownColPeers().get(twin1), Peers.getColPeers().get(twin1));
				
				// check for twins in the same square 
				checkForTwinsAmongPeers(incomingSolution, twin1, solution1, Peers.getRemainingSquarePeers().get(twin1), Peers.getSquarePeers().get(twin1));
				
				// if it is diagonal sudoku add check for diagonal peers
			}
		}
	}
	
	private void checkForTwinsAmongPeers (SolutionSet incomingSolution, Box twin1, BoxSolution solution1, List<Box> remainingPeers, List<Box> allCurrentPeers)
	{
		BoxSolution solution2;
		for (Box twin2: remainingPeers)
		{
			solution2 = incomingSolution.getSolution(twin2);
			if (solution2.equals(solution1))
				for (Box box: allCurrentPeers)
					if (!box.equals(twin1) && !box.equals(twin2))
						if (incomingSolution.removeSolutions(box, solution1.getSolution()))
							madeProgress = true;
		}
	}
	
	// eliminate repeating patterns from solution
	private void all_twins(SolutionSet incomingSolution)
	{
		for (Box box1: incomingSolution.keySet()) // go over all boxes in order
		{
			BoxSolution solution1 = incomingSolution.getSolution(box1); // solution at the current box
			/*System.out.println("Find twins for box "+ box1);
			System.out.println("Solution is ");
			for (Integer sol: solution1.getSolution())
				System.out.print(sol + " ");*/
			//System.out.println();
			if (solution1.getSolution().size()>1)
			{
				//System.out.println("Solution greater than 1");
				checkForAllTwinRepeats (incomingSolution, box1, solution1, Peers.getRightRowPeers().get(box1), Peers.getRowPeers().get(box1), "row");
				checkForAllTwinRepeats (incomingSolution, box1, solution1, Peers.getDownColPeers().get(box1), Peers.getColPeers().get(box1), "col");
				checkForAllTwinRepeats (incomingSolution, box1, solution1, Peers.getRemainingSquarePeers().get(box1), Peers.getSquarePeers().get(box1), "square");
			}
		}
	}
	
	private void checkForAllTwinRepeats (SolutionSet incomingSolution, Box box1, BoxSolution solution1, List<Box> remainingPeers, List<Box> allCurrentPeers, String message)
	{
		//List<BoxSolution> repeatSolutions = new ArrayList<>(7);
		int sameSolutionsNumber = 1;
		//System.out.println("Solution1 size: " + solution1.getSolution().size());
		if (solution1.getSolution().size() <= remainingPeers.size()+1 && solution1.getSolution().size()<8)
		{
			for (Box box2: remainingPeers)
			{
				if(incomingSolution.getSolution(box2).equals(solution1))
					sameSolutionsNumber++;
			}
			if (sameSolutionsNumber == solution1.getSolution().size())
			{
				//System.out.println("Found some twins " + message);
				for (Box box: allCurrentPeers)
					if (!incomingSolution.getSolution(box).equals(solution1))
						if(incomingSolution.removeSolutions(box, solution1.getSolution()))
						{
							//System.out.println("Removed solution " + solution1 + " from " + box);
							madeProgress = true;
						}
							
			}
			else if (sameSolutionsNumber >= solution1.getSolution().size())
			{
				//System.out.println("found two many twins " + message);
				impossibleSolve = true;
			}
			/*else
				System.out.println("Did not find enough twins. Only " +sameSolutionsNumber + " in " + message);*/
				
						
		}
	}

	private void selectOnlyChoises(SolutionSet incomingSolution) {
		
		for (Box box: incomingSolution.keySet())
		{
			BoxSolution solution = incomingSolution.getSolution(box);
			if (!solution.isOnlySolution())
				for (int i = 0; i < solution.getSolution().size(); i++)
				{
					if (isUnique(incomingSolution, Peers.getRowPeers().get(box), solution.getSolution().get(i)))
					{
						solution.setUnique(solution.getSolution().get(i));
						madeProgress = true;
						break;
					}
					if (isUnique(incomingSolution, Peers.getColPeers().get(box), solution.getSolution().get(i)))
					{
						solution.setUnique(solution.getSolution().get(i));
						madeProgress = true;
						break;
					}
					if (isUnique(incomingSolution, Peers.getSquarePeers().get(box), solution.getSolution().get(i)))
					{
						solution.setUnique(solution.getSolution().get(i));
						madeProgress = true;
						break;
					}
				}
		}
		
	}

	private void limitSolutionsByEliminatingKnownValues(SolutionSet incomingSolution) {
		for (Box box: incomingSolution.keySet())
		{
			BoxSolution solution = incomingSolution.getSolution(box);
			if (solution.isNonExistant())
			{
				impossibleSolve = true;
				return;
			}
			else if (solution.isOnlySolution())
			{
				List<Box> peers = Peers.getAllPeers().get(box);
				for (Box peer: peers)
					if (incomingSolution.removeSolution(peer, solution.getSolution().get(0)))
						madeProgress = true;
			}
			else // at least one non unique solution;
				isSolved = false;
		}
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
	
	private boolean isUnique (SolutionSet incomingSolution, List<Box> peers, int value)
	{
		for (Box peer :peers)
			if(incomingSolution.getSolution(peer).contains(value))
				return false;
		return true;
	}

	

}
