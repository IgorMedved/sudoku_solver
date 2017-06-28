package sudoku_model.ai;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sudoku_model.SudokuBoard;

public class ModelTest {

	@Test
	public void testMultipleChoiceMaps()
	{
		assertTrue ("box @0,0 should belong to square 0", MultipleChoiceMaps.squareNumber(0, 0) == 0);
		assertTrue ("box @6,6 should beonlong to square 8", MultipleChoiceMaps.squareNumber(6, 6) == 8);
		assertTrue ("box @7,7 should beonlong to square 8", MultipleChoiceMaps.squareNumber(7, 7) == 8);
	}
	
	
	
	@Test
	public void testPeersValid() {
		Peers.initializePeers();
		Box box1 = new Box(0,0);
		Box invalidBox = new Box (9,3);
		Box box2 = new Box(8,8);
		Box box3 = new Box(4,4);
		Box box4 = new Box (0,3);
		Box box5 = new Box (0,6);
		
		//Peers.printPeers(box1);
		Peers.printPeers(invalidBox);
		Peers.printPeers(box1);
		Peers.printPeers(box2);
		Peers.printPeers(box3);
		Peers.printPeers(box4);
		Peers.printPeers(box5);
		
		assertSame("Invalid box 9,3 can't have peers", Peers.getRowPeers().get(invalidBox), null);
		assertTrue("Box 0, 0 should have diagonal peers", Peers.getDiagonalPeers().get(box1)!=null);
		assertTrue("Box 4,4 shoud have peer @ 7,4", Peers.getAllPeers().get(box3).contains(new Box(7,4)));
		assertFalse("Box 4,4 shoul nothave peer @7,5", Peers.getAllPeers().get(box3).contains(new Box(7,5)));
		
	}
	@Test
	public void testSquarePeers()
	{
		Peers.initializePeers();
		Box box1 = new Box(0,0);
		Box invalidBox = new Box (9,3);
		Box box2 = new Box(8,8);
		Box box3 = new Box(4,4);
		Box box4 = new Box (0,3);
	}
	
	@Test
	public void testFullSolution()
	{
		BoxSolution fullSolution = BoxSolution.getFullSolution();
		assertFalse("Critical error getFullSolution() returns null", fullSolution == null );
		for (int i = 1; i<=9; i++)
			assertTrue ("full solution is missing " + i, i== fullSolution.getSolution().get(i-1));
		
		assertTrue("The value 7 is not removed", fullSolution.remove(7));
		assertFalse("The value 7 was not removed", fullSolution.getSolution().contains(7));
		
		List<Integer> removables = new ArrayList<>(3);
		removables.add(1);
		removables.add(4);
		removables.add(7);
		assertTrue("The value 1 and 4 are not removed", fullSolution.remove(removables));
		assertFalse("The value 4 is still in the list of solutions", fullSolution.getSolution().contains(4));
		
		BoxSolution newSolution = BoxSolution.copySolution(fullSolution);
		assertTrue ("Copy is not equal to original", fullSolution.equals(newSolution));
		newSolution.remove(2);
		assertFalse("the solutions should not be equal", fullSolution.equals(newSolution));
	}
	
	@Test
	public void testUniqueSolution()
	{
		BoxSolution unique = BoxSolution.getUniqueSolution(2);
		assertNotEquals ("unique solution should not be null", null, unique);
		for (int i = 1; i <=9; i++)
			if (i!=2)
				assertFalse ("Unique solution should not have any values beside 2", unique.getSolution().contains(i));
			else
				assertTrue("Unique solution should have value of 2", unique.getSolution().contains(i));
		assertFalse("The remove function should return false when not removing values", unique.remove(4));
		List<Integer> removables = new ArrayList<>(3);
		removables.add(3);
		removables.add(4);
		removables.add(5);
		assertFalse("The remove function should return false when not removing values", unique.remove(removables));
		removables.add(2);
		assertTrue("The remove function should return true when removing at least one value", unique.remove(removables));
	}
	
	@Test
	public void testSolutionSet ()
	{
		SolutionSet mySolutions = new SolutionSet();
		Box box1 = new Box(0,0);
		Box invalidBox = new Box (9,3);
		Box box2 = new Box(8,8);
		Box box3 = new Box(4,4);
		Box box4 = new Box (0,3);
		
		assertTrue ("The box 0,0 should be a key", mySolutions.contains(box1));
		assertFalse ("The box 9,3 should not be a key", mySolutions.contains(invalidBox));
		assertTrue ("The box 0,0 should have a full solution", mySolutions.getSolution(box1).equals(BoxSolution.getFullSolution()));
		assertTrue ("The box 8,8 should have a full solution", mySolutions.getSolution(box2).equals(BoxSolution.getFullSolution()));
		assertTrue ("There should be now solutions for boxes outside grid", mySolutions.getSolution(invalidBox).isNonExistant());
		System.out.println("removing solution 3 from box 2 @ 8,8");
		mySolutions.removeSolution(box2, 3);
		assertFalse("There should be no 3 in solution set at 8,8", mySolutions.getSolution(box2).contains(3));
		
		SolutionSet copy = SolutionSet.getCopy(mySolutions);
		
		assertFalse ("There should be no 3 in solution set copy at 8,8", copy.getSolution(box2).contains(3));
		copy.removeSolution(box3, 4);
		assertFalse ("There should be no 3 in solution set copy at 4,4", copy.getSolution(box3).contains(4));
		assertTrue ("The original solution set should not be affected ", mySolutions.getSolution(box3).contains(4));
	}
	
	@Test
	public void testSolutionSetInteractionWithBoard()
	{
		SudokuBoard board = new SudokuBoard();
		board.set(1, 5, 2);
		board.set(5, 3, 9);
		board.printBoard();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(board);
		assertTrue ("Box @ 1,5 should have unique value 2", solutions.getSolution(new Box(1,5)).equals(BoxSolution.getUniqueSolution(2)));
		assertTrue ("Box @ 5,3 should have unique value 9", solutions.getSolution(new Box(5,3)).equals(BoxSolution.getUniqueSolution(9)));
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if ((i!=1 || j!=5) && (i!=5 || j!=3))
				{
					System.out.println("Assessing box " + i + " " + j);
					assertTrue ("All other boxes should have full solution", solutions.getSolution(new Box(i,j)).equals(BoxSolution.getFullSolution()));
				}
		System.out.println("Trying to initialize board");
		SudokuBoard modifiedBoard = SudokuBoard.initializeBoardFromSolution(solutions);
		assertTrue("value @ 1,3 is 0", modifiedBoard.getBoard().get(1).get(3) == 0);
		modifiedBoard.printBoard();
		assertTrue("value @ 5,3 is not 9", modifiedBoard.getBoard().get(5).get(3) ==9);
		assertTrue("value @ 1,5 is not 2", modifiedBoard.getBoard().get(1).get(5) ==2);
		
	}
	
	@Test
	public void testSolutionSetKeysInOrder()
	{
		SudokuBoard board = new SudokuBoard();
		board.set(1, 5, 2);
		board.set(5, 3, 9);
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(board);
		int i = 0;
		int j = 0;
		for (Box box: solutions.keySet())
		{
			assertTrue ("Key rows are not in correct order", box.row == i);
			assertTrue ("Key columns are not in correct order", box.col== j);
			j++;
			if (j >8)
			{
				i++;
				j = 0;
			}
		}
			
	}

}
