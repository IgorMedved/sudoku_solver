package sudoku_ui;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sudoku_model.SudokuBoard;
import sudoku_model.ai.Box;
import sudoku_model.ai.BoxSolution;
import sudoku_model.ai.InteractionPeers;
import sudoku_model.ai.Peers;
import sudoku_model.ai.SolutionSet;
import sudoku_model.ai.Solver;
import sudoku_model.ai.SudokuNewSolver;

public class Ai_test {
	private SudokuBoard testBoard;

	@Before
	public void setUp()
	{
		Peers.initializePeers();
		testBoard = new SudokuBoard();
		
	}
	
	@Test
	public void testDirectionalLists ()
	{
		
		testBoard.set(0, 3, 2);
		testBoard.set(8, 8, 1);
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		List<Box> rowBoxes = solutions.rowTotal(new Box(0,0), 2);
		
		assertTrue ("There should be 9 boxes with value 2 available", solutions.rowTotal(new Box(0,0), 2).size() == 9);
		assertTrue ("There should be 9 boxes with value 2 available", solutions.colTotal(new Box (0,3), 2).size() == 9);
		assertTrue ("There should be 8 boxes with value 3 available in row 0", solutions.rowTotal(new Box(0,5), 3).size() == 8);
		assertTrue ("There should be 4 boxes with value 3 available right of row 4", solutions.right(new Box(0,4), 3).size() == 4);
		assertTrue ("There should be 4 boxes with value 3 available left of row 4", solutions.left(new Box(0,4), 2).size() == 4);
		assertTrue ("There should be 4 boxes with value 3 available right of row 4", solutions.down(new Box(0,4), 3).size() == 8);
		assertTrue ("There should be 4 boxes with value 3 available right of row 4", solutions.up(new Box(0,4), 3).size() == 0);
		//assertTrue
		SudokuNewSolver solver = new SudokuNewSolver();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//System.out.println(solutions.colTotal(new Box (0,3), 2).size());
		assertTrue ("There should be 1 box with value 2 available", solutions.rowTotal(new Box(0,0), 2).size() == 1);
		assertTrue ("There should be 1 box with value 2 available", solutions.colTotal(new Box (0,3), 2).size() == 1);
		assertTrue ("There should be no boxes with value 2 available", solutions.left(new Box(0,3), 2).size() == 0);
		assertTrue ("There should be 4 boxes with value 3 available right of row 4", solutions.right(new Box(8,8), 1).size() == 0);
		assertTrue ("There should be 4 boxes with value 3 available left of row 4", solutions.left(new Box(8,8), 1).size() == 0);
		assertTrue ("There should be 4 boxes with value 3 available right of row 4", solutions.down(new Box(8,8), 1).size() == 0);
		assertTrue ("There should be 4 boxes with value 3 available right of row 4", solutions.up(new Box(8,8), 1).size() == 0);
	}
	
	@Test public void testSwordFishRow()
	{
		testBoard.set(0, 0, 7);
		testBoard.set(0, 1, 5);
		testBoard.set(2, 0, 1);
		testBoard.set(2, 1, 3);
		testBoard.set(7, 0, 2);
		testBoard.set(1, 7, 2);
		testBoard.set(3, 2, 7);
		testBoard.set(4, 2, 5);
		testBoard.set(5, 2, 3);
		testBoard.set(6, 2, 6);
		testBoard.set(3, 4, 3);
		testBoard.set(4, 4, 6);
		testBoard.set(5, 4, 4);
		testBoard.set(6, 4, 5);
		testBoard.set(8, 3, 2);
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		SudokuNewSolver solver = new SudokuNewSolver();
		testBoard.printBoard();
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("swordFish", solver, solutions);
		//solutions.printSolutionSet();
		
	}
	
	@Test
	public void testSearch()
	{
		testBoard.set(0, 1, 6);
		testBoard.set(0, 4, 9);
		testBoard.set(0, 5, 3);
		testBoard.set(0, 6, 4);
		testBoard.set(1, 0, 1);
		testBoard.set(1, 2, 3);
		testBoard.set(1, 3, 8);
		testBoard.set(1, 4, 7);
		testBoard.set(1, 6, 9);
		testBoard.set(2, 1, 4);
		testBoard.set(3, 3, 5);
		testBoard.set(3, 8, 3);
		testBoard.set(4, 1, 7);
		testBoard.set(4, 3, 2);
		testBoard.set(4, 5, 6);
		testBoard.set(4, 7, 9);
		testBoard.set(5, 0, 3);
		testBoard.set(5, 5, 8);
		testBoard.set(6, 7, 2);		
		testBoard.set(7, 2, 6);
		testBoard.set(7, 4, 5);
		testBoard.set(7, 5, 1);
		testBoard.set(7, 6, 8);
		testBoard.set(8, 2, 1);
		testBoard.set(8, 3, 3);
		testBoard.set(8, 4, 8);
		testBoard.set(8, 7, 5);
		testBoard.printBoard();
		InteractionPeers.initializePeers();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		SudokuNewSolver solver = new SudokuNewSolver();
		solver.counter=1;
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		solutions.printSolutionSet();
		SolutionSet finalSolution = (SolutionSet) runSearch("search", solver, solutions);
		if (finalSolution!= null)
		{
			System.out.println("Final solution");
			finalSolution.printSolutionSet();
			
		}
			
		else
			System.out.println("Why the hell is it null???");
	}
	@Test
	public void testSwordFishRowStart()
	{
		testBoard.set(0, 0, 1);
		testBoard.set(6, 1, 2);
		testBoard.set(2, 2, 6);
		testBoard.set(4, 2, 6);
		testBoard.set(5, 2, 5);
		testBoard.set(1, 3, 2);
		testBoard.set(3, 7, 2);
		testBoard.set(6, 6, 1);
		testBoard.set(7, 6, 3);
		testBoard.set(8, 6, 4);
		testBoard.set(6, 8, 5);
		testBoard.set(7, 8, 6);
		testBoard.set(8, 8, 7);
		
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		SudokuNewSolver solver = new SudokuNewSolver();
		testBoard.printBoard();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("swordFish", solver, solutions);
	}
	
	@Test public void testSwordFishCol()
	{
		testBoard.set(0, 0, 7);
		testBoard.set(1, 0, 5);
		testBoard.set(0, 2, 1);
		testBoard.set(1, 2, 3);
		testBoard.set(0, 7, 2);
		testBoard.set(7, 1, 2);
		testBoard.set(2, 3, 7);
		testBoard.set(2, 4, 5);
		testBoard.set(2, 5, 3);
		testBoard.set(2, 6, 6);
		testBoard.set(4, 3, 3);
		testBoard.set(4, 4, 6);
		testBoard.set(4, 5, 4);
		testBoard.set(4, 6, 5);
		testBoard.set(3, 8, 2);
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		SudokuNewSolver solver = new SudokuNewSolver();
		//testBoard.printBoard();
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("swordFish", solver, solutions);
		//solutions.printSolutionSet();
		
	}


	@Test
	public void testLimitSolutions() {
		
		testBoard.set(0, 0, 1);
		testBoard.set(4, 5, 3);
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		Box box1 = new Box (0,0);
		Box box2 = new Box (4,5);
		SudokuNewSolver solver = new SudokuNewSolver();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		// check that 1 and 3 were removed from all the necessary fields
		List<Box> peers00 = Peers.getAllPeers().get(box1);
		for (Box box: peers00)
			assertFalse("No peer of box 0,0 should contain value of 1", (solutions.getSolution(box).contains(1)));
		List<Box> peers45 = Peers.getAllPeers().get(box2);
		for (Box box: peers45)
			assertFalse("No peer of box 4,5 should contain value of 3", (solutions.getSolution(box).contains(3)));
		// check that all the boxes that are not peers of either 1 or 3 or boxes themselves still have full solution
		for (Box box: solutions.keySet())
		{
			if (!box.equals(box1) && (!box.equals(box2)&& (!peers00.contains(box))&& (!peers45.contains(box))))
			{
				assertTrue("All other boxes should still have full solution ", solutions.getSolution(box).equals(BoxSolution.getFullSolution()));
			}
		}
	}
	
	@Test
	public void testSelectMultipleChoice()
	{
		testBoard.set(1, 6, 1);
		testBoard.set(1, 7, 2);
		testBoard.set(1, 8, 3);
		testBoard.set(2, 0, 7);
		testBoard.set(2, 1, 8);
		testBoard.set(2, 2, 9);
		testBoard.set(2, 3, 1);
		testBoard.set(2, 4, 2);
		testBoard.set(2, 5, 3);
		testBoard.set(4, 0, 6);
		testBoard.set(4, 1, 4);
		testBoard.set(4, 2, 5);
		SudokuNewSolver solver = new SudokuNewSolver();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		Box unique = new Box (0,6);
		// eliminate the known solutions
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("selectMultipleChoice",solver, solutions);
		//solutions.printSolutionSet();
	}
	
	@Test
	public void testSelectOnlyChoice()
	{
		// prepare test case
		testBoard.set(0, 0, 1);
		testBoard.set(0, 1, 2);
		testBoard.set(0, 2, 3);
		testBoard.set(1, 0, 7);
		testBoard.set(2, 6, 4);
		testBoard.set(2, 7, 5);
		testBoard.set(2, 8, 6);
		testBoard.set(4, 7, 7);
		testBoard.set(7,8,7);
		SudokuNewSolver solver = new SudokuNewSolver();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		Box unique = new Box (0,6);
		// eliminate the known solutions
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("selectOnlyChoises",solver, solutions);
		assertTrue("There should be a unique solution 7 for box 0,6", solutions.getSolution(unique).isOnlySolution() && solutions.getSolution(unique).contains(7));
	}
	
	@Test
	public void testNakedTwins ()
	{
		testBoard.set(0, 0, 1);
		testBoard.set(0, 1, 2);
		testBoard.set(0, 2, 3);
		testBoard.set(1, 0, 7);
		testBoard.set(2, 6, 4);
		testBoard.set(2, 7, 5);
		testBoard.set(2, 8, 6);
		testBoard.set(4, 7, 7);
		testBoard.set(7, 8, 7);
		SudokuNewSolver solver = new SudokuNewSolver();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		runPrivateMethodOnSolver("naked_twin", solver, solutions);
		//solutions.printSolutionSet();
	}
	@Test
	public void test_all_twins()
	{
		testBoard.set(0, 0, 1);
		testBoard.set(0, 1, 2);
		testBoard.set(0, 2, 3);
		testBoard.set(1, 0, 7);
		testBoard.set(2, 6, 4);
		testBoard.set(2, 7, 5);
		testBoard.set(2, 8, 6);
		testBoard.set(4, 7, 7);
		testBoard.set(7, 8, 7);
		SudokuNewSolver solver = new SudokuNewSolver();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		runPrivateMethodOnSolver("all_twins", solver, solutions);
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		runPrivateMethodOnSolver("all_twins", solver, solutions);
		//solutions.printSolutionSet();
		//solutions.printSolutionSet();
		boolean impossibleSolve = getPrivateField ("impossibleSolve", solutions);
		assertTrue ("In this test there should be no solutions", impossibleSolve);
	}
	
	@Test
	public void testSquareElimination()
	{
		testBoard.set(0, 0, 1);
		testBoard.set(0, 1, 2);
		testBoard.set(0, 2, 3);
		testBoard.set(1, 0, 4);
		testBoard.set(1, 1, 5);
		testBoard.set(1, 2, 6);
		testBoard.set(8, 0, 7);
		SudokuNewSolver solver = new SudokuNewSolver();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		//solutions.printSolutionSet();
		runPrivateMethodOnSolver("onlyRowOrColInSquare", solver, solutions);
		
	}
	
	private boolean getPrivateField(String fieldName, SolutionSet solution)
	{
		boolean myBoolean = false;
		try {
			Field field = solution.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			myBoolean = field.getBoolean(solution);
			return myBoolean;
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private Object runSearch(String methodName, SudokuNewSolver solver, SolutionSet solution){
		Method method=null;
		try {
			method = solver.getClass().getDeclaredMethod(methodName, SolutionSet.class);
			
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		method.setAccessible(true);
		
		try {
			return method.invoke(solver, solution);
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void runPrivateMethodOnSolver(String methodName, SudokuNewSolver solver, SolutionSet solution)
	{
		Method method=null;
		try {
			method = solver.getClass().getDeclaredMethod(methodName, SolutionSet.class);
			
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		method.setAccessible(true);
		
		try {
			method.invoke(solver, solution);
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
