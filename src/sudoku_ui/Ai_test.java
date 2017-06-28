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
		testBoard.set(7,8,7);
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
		testBoard.set(7,8,7);
		SudokuNewSolver solver = new SudokuNewSolver();
		SolutionSet solutions = SolutionSet.getInitialConditionsFromBoard(testBoard);
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		runPrivateMethodOnSolver("all_twins", solver, solutions);
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		runPrivateMethodOnSolver("all_twins", solver, solutions);
		solutions.printSolutionSet();
		//solutions.printSolutionSet();
		boolean impossibleSolve = getPrivateField ("impossibleSolve", solver);
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
		solutions.printSolutionSet();
		runPrivateMethodOnSolver("limitSolutionsByEliminatingKnownValues", solver, solutions);
		solutions.printSolutionSet();
		runPrivateMethodOnSolver("onlyRowOrColInSquare", solver, solutions);
		
	}
	
	private boolean getPrivateField(String fieldName, SudokuNewSolver solver)
	{
		boolean myBoolean = false;
		try {
			Field field = solver.getClass().getSuperclass().getDeclaredField(fieldName);
			field.setAccessible(true);
			myBoolean = field.getBoolean(solver);
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
