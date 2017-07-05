package sudoku_model.ai;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InteractionPeers {
	// maps for rowSquare colSquare and squareSquare interactions functions
	// for more details see
	// https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php
	private static Map<Box, List<Box>> rowSquareSolutionPresentPeer; // the boxes where the value should be present for technique to work
	private static Map<Box, List<Box>> rowSquareSolutionAbsentPeer; // the boxes where value should be absent for technique to work
	private static Map<Box, List<Box>> rowSquareSolutionRemovePeer; // the boxes where value can be removed if conditions 1) and 2) are met

	private static Map<Box, List<Box>> colSquareSolutionPresentPeer; // same as above only for colSquare interaction
	private static Map<Box, List<Box>> colSquareSolutionAbsentPeer;
	private static Map<Box, List<Box>> colSquareSolutionRemovePeer;

	private static Map<Box, List<Box>> squareSquareRowSolutionPresentPeer; // same as above only for SquareSquareRow interaction
	private static Map<Box, List<Box>> squareSquareRowSolutionAbsentPeer;
	private static Map<Box, List<Box>> squareSquareRowSolutionRemovePeer;

	private static Map<Box, List<Box>> squareSquareColSolutionPresentPeer; // same as above only for SquareSquareCol interaction
	private static Map<Box, List<Box>> squareSquareColSolutionAbsentPeer;
	private static Map<Box, List<Box>> squareSquareColSolutionRemovePeer;

	public static void initializePeers() {
		rowSquareSolutionPresentPeer = new LinkedHashMap<>();
		rowSquareSolutionAbsentPeer = new LinkedHashMap<>();
		rowSquareSolutionRemovePeer = new LinkedHashMap<>();

		colSquareSolutionPresentPeer = new LinkedHashMap<>();
		colSquareSolutionAbsentPeer = new LinkedHashMap<>();
		colSquareSolutionRemovePeer = new LinkedHashMap<>();

		squareSquareRowSolutionPresentPeer = new LinkedHashMap<>();
		squareSquareRowSolutionAbsentPeer = new LinkedHashMap<>();
		squareSquareRowSolutionRemovePeer = new LinkedHashMap<>();

		squareSquareColSolutionPresentPeer = new LinkedHashMap<>();
		squareSquareColSolutionAbsentPeer = new LinkedHashMap<>();
		squareSquareColSolutionRemovePeer = new LinkedHashMap<>();

		Peers.initializePeers();
		Box rowBox;
		Box colBox;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j += 3) {
				rowBox = new Box(i, j);
				rowSquareSolutionPresentPeer.put(rowBox, initializeRowSquarePresent(i, j));
				rowSquareSolutionAbsentPeer.put(rowBox, initializeRowSquareAbsent(rowBox)); // this should go after initializeRowSquarePresent
				rowSquareSolutionRemovePeer.put(rowBox, initializeRowSquareRemove(rowBox));
				squareSquareRowSolutionPresentPeer.put(rowBox, initializeSquarePresent(i, j));
				squareSquareRowSolutionAbsentPeer.put(rowBox, initializeSquareAbsent(i, j));
				squareSquareRowSolutionRemovePeer.put(rowBox, initializeSquareRemove(i, j));

			}
		for (int j = 0; j < 9; j++)
			for (int i = 0; i < 9; i += 3) {
				colBox = new Box(i, j);
				colSquareSolutionPresentPeer.put(colBox, initializeColSquarePresent(i, j));
				colSquareSolutionAbsentPeer.put(colBox, initializeColSquareAbsent(colBox)); // this should go after initializeColSquarePresent
				colSquareSolutionRemovePeer.put(colBox, initializeColSquareRemove(colBox));
				squareSquareColSolutionPresentPeer.put(colBox, initializeSquareColPresent(i, j));
				squareSquareColSolutionAbsentPeer.put(colBox, initializeSquareColAbsent(i, j));
				squareSquareColSolutionRemovePeer.put(colBox, initializeSquareColRemove(i, j));

			}

	}

	private static List<Box> initializeSquareColRemove(int i, int j) {
		List<Box> squarePeerRemove = new ArrayList<>(6);
		int lowerBound = convertSquareIndex(i + 6);
		int upperBound = convertSquareIndex(i + 8);
		int j2 = convertLineIndex(j, 1);
		for (int k = lowerBound; k <= upperBound; k++) {
			squarePeerRemove.add(new Box(k, j));
			squarePeerRemove.add(new Box(k, j2));
		}
		return squarePeerRemove;
	}

	private static List<Box> initializeSquareRemove(int i, int j) {
		List<Box> squarePeerRemove = new ArrayList<>(6);
		int lowerBound = convertSquareIndex(j + 6);
		int upperBound = convertSquareIndex(j + 8);
		int i2 = convertLineIndex(i, 1);
		for (int k = lowerBound; k <= upperBound; k++) {
			squarePeerRemove.add(new Box(i, k));
			squarePeerRemove.add(new Box(i2, k));
		}
		return squarePeerRemove;
	}

	private static List<Box> initializeSquareAbsent(int i, int j) {
		List<Box> squarePeerAbsent = new ArrayList<>(6);
		int firstLowerBound = j;
		int firstUpperBound = j + 2;
		int secondLowerBound = convertSquareIndex(j + 3);
		int secondUpeerBound = convertSquareIndex(j + 5);
		int i2 = convertLineIndex(i, 2);
		for (int k = firstLowerBound; k <= firstUpperBound; k++) {

			squarePeerAbsent.add(new Box(i2, k));
		}
		for (int k = secondLowerBound; k <= secondUpeerBound; k++) {

			squarePeerAbsent.add(new Box(i2, k));
		}
		return squarePeerAbsent;
	}

	private static List<Box> initializeSquareColAbsent(int i, int j) {
		List<Box> squarePeerAbsent = new ArrayList<>(6);
		int firstLowerBound = i;
		int firstUpperBound = i + 2;
		int secondLowerBound = convertSquareIndex(i + 3);
		int secondUpeerBound = convertSquareIndex(i + 5);
		int j2 = convertLineIndex(j, 2);
		for (int k = firstLowerBound; k <= firstUpperBound; k++) {

			squarePeerAbsent.add(new Box(k, j2));
		}
		for (int k = secondLowerBound; k <= secondUpeerBound; k++) {

			squarePeerAbsent.add(new Box(k, j2));
		}
		return squarePeerAbsent;
	}

	private static List<Box> initializeSquarePresent(int i, int j) {
		List<Box> squarePeerPresent = new ArrayList<>(12);
		int firstLowerBound = j;
		int firstUpperBound = j + 2;
		int secondLowerBound = convertSquareIndex(j + 3);
		int secondUpeerBound = convertSquareIndex(j + 5);
		int i2 = convertLineIndex(i, 1);
		for (int k = firstLowerBound; k <= firstUpperBound; k++) {
			squarePeerPresent.add(new Box(i, k));
			squarePeerPresent.add(new Box(i2, k));
		}
		for (int k = secondLowerBound; k <= secondUpeerBound; k++) {
			squarePeerPresent.add(new Box(i, k));
			squarePeerPresent.add(new Box(i2, k));
		}

		return squarePeerPresent;
	}

	private static List<Box> initializeSquareColPresent(int i, int j) {
		List<Box> squarePeerPresent = new ArrayList<>(12);
		int firstLowerBound = i;
		int firstUpperBound = i + 2;
		int secondLowerBound = convertSquareIndex(i + 3);
		int secondUpeerBound = convertSquareIndex(i + 5);
		int j2 = convertLineIndex(j, 1);
		for (int k = firstLowerBound; k <= firstUpperBound; k++) {
			squarePeerPresent.add(new Box(k, j));
			squarePeerPresent.add(new Box(k, j2));
		}
		for (int k = secondLowerBound; k <= secondUpeerBound; k++) {
			squarePeerPresent.add(new Box(k, j));
			squarePeerPresent.add(new Box(k, j2));
		}
		return squarePeerPresent;
	}

	private static List<Box> initializeColSquareRemove(Box colBox) {
		List<Box> colSquareRemove = new ArrayList<>(Peers.getColPeers().get(colBox));
		colSquareRemove.removeAll(colSquareSolutionPresentPeer.get(colBox));
		return colSquareRemove;
	}

	private static List<Box> initializeColSquareAbsent(Box colBox) {
		List<Box> colSquareAbsent = new ArrayList<>(Peers.getSquarePeers().get(colBox));
		colSquareAbsent.removeAll(colSquareSolutionPresentPeer.get(colBox));
		return colSquareAbsent;
	}

	private static List<Box> initializeColSquarePresent(int i, int j) {
		List<Box> presentColSqure = new ArrayList<>(3);
		for (int k = i; k <= i + 2; k++)
			presentColSqure.add(new Box(k, j));
		return presentColSqure;
	}

	private static int convertLineIndex(int a, int increase) {
		return a / 3 * 3 + (a + increase) % 3;
	}

	private static int convertSquareIndex(int a) {
		// TODO Auto-generated method stub
		return a % 9;
	}

	private static List<Box> initializeRowSquareRemove(Box rowBox) {
		List<Box> peerRemove = new ArrayList<>(Peers.getRowPeers().get(rowBox));
		peerRemove.removeAll(rowSquareSolutionPresentPeer.get(rowBox));
		return peerRemove;
	}

	private static List<Box> initializeRowSquareAbsent(Box rowBox) {
		List<Box> peersAbsent = new ArrayList<>(Peers.getSquarePeers().get(rowBox));
		peersAbsent.removeAll(rowSquareSolutionPresentPeer.get(rowBox));
		return peersAbsent;
	}

	private static List<Box> initializeRowSquarePresent(int i, int j) {
		List<Box> peersPresent = new ArrayList<>(3);
		for (int k = j; k <= j + 2; k++)
			peersPresent.add(new Box(i, k));

		return peersPresent;
	}

	public static void printBoxPeers(Box box) {
		System.out.println("Printing " + box);
		if (box.isValid()) {
			if (box.col % 3 != 0) {
				System.out.println("No row peers");

			} else {
				System.out.println("Row - Square interaction peers solution present");
				for (Box printingBox : rowSquareSolutionPresentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Row - Square interaction peers solution absent");
				for (Box printingBox : rowSquareSolutionAbsentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Row - Square interaction peers solution might be removed");
				for (Box printingBox : rowSquareSolutionRemovePeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Square - Square - Row interaction peers solution present");
				for (Box printingBox : squareSquareRowSolutionPresentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Square - Square - Row interaction peers solution absent");
				for (Box printingBox : squareSquareRowSolutionAbsentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Square - Square - Row interaction peers solution might be removed");
				for (Box printingBox : squareSquareRowSolutionRemovePeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");
			}

			if (box.row % 3 != 0) {
				System.out.println("No col peers");

			} else {
				System.out.println("Col - Square interaction peers solution present");
				for (Box printingBox : colSquareSolutionPresentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Col - Square interaction peers solution absent");
				for (Box printingBox : colSquareSolutionAbsentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Col - Square interaction peers solution might be removed");
				for (Box printingBox : colSquareSolutionRemovePeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Square - Square - Col interaction peers solution present");
				for (Box printingBox : squareSquareColSolutionPresentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Square - Square - Col interaction peers solution absent");
				for (Box printingBox : squareSquareColSolutionAbsentPeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");

				System.out.println("Square - Square - Col interaction peers solution might be removed");
				for (Box printingBox : squareSquareColSolutionRemovePeer.get(box))
					System.out.println(printingBox);
				System.out.println("\n\n");
			}
		} else
			System.out.println("Box out of bounds");
	}

	public static BoxSolution rowSquareSolutionPresent(SolutionSet solution, Box box) {
		return present (solution, box, rowSquareSolutionPresentPeer.get(box));
	}
	
	public static BoxSolution colSquareSolutionPresent(SolutionSet solution, Box box) {
		return present (solution, box, colSquareSolutionPresentPeer.get(box));
	}
	
	
	public static BoxSolution squareSquareRowSolutionPresent(SolutionSet solution, Box box) {
		return present (solution, box, squareSquareRowSolutionPresentPeer.get(box));
	}
	
	
	public static BoxSolution squareSquareColSolutionPresent(SolutionSet solution, Box box) {
		return present (solution, box, squareSquareColSolutionPresentPeer.get(box));
	}
	
	
	private static BoxSolution present (SolutionSet solution, Box box, List<Box> boxesToCheck)
	{
		if (boxesToCheck == null)
		{
			System.out.println("Is solution empty?");
			return BoxSolution.copySolution(new ArrayList<>()); // return empty
																// solution
			
		}
		
		List<Integer> solutionsAtSquare = new ArrayList<>(9);
		for (Box boxCheck : boxesToCheck) {
			if (!solution.getSolution(boxCheck).isOnlySolution())
				for (Integer current : solution.getSolution(boxCheck).getSolution()) {
					if (!solutionsAtSquare.contains(current))
						solutionsAtSquare.add(current);
				}
		}
		return BoxSolution.copySolution(solutionsAtSquare);
	}

	public static boolean rowSquareSolutionAbsent(SolutionSet solution, Box box, int value) {
		return absent(solution, box, value, rowSquareSolutionAbsentPeer.get(box));
	}
	
	public static boolean colSquareSolutionAbsent(SolutionSet solution, Box box, int value) {
		return absent(solution, box, value, colSquareSolutionAbsentPeer.get(box));
	}
	
	public static boolean squareSquareRowSolutionAbsent(SolutionSet solution, Box box, int value) {
		return absent(solution, box, value, squareSquareRowSolutionAbsentPeer.get(box));
	}
	
	public static boolean squareSquareColSolutionAbsent(SolutionSet solution, Box box, int value) {
		return absent(solution, box, value, squareSquareColSolutionAbsentPeer.get(box));
	}
	
	
	
	private static boolean absent (SolutionSet solution, Box box, int value, List<Box> boxesToCheck)
	{
		if (boxesToCheck == null)
			return false; // return false for invalid box
		for (Box boxCheck : boxesToCheck)
			if (solution.getSolution(boxCheck).contains(value))
				return false;
		return true;
	}

	
	public static boolean rowSquareSolutionRemove(SolutionSet solution, Box box, int value) {
		return remove(solution, box, value, rowSquareSolutionRemovePeer.get(box));
		
	}
	
	public static boolean colSquareSolutionRemove (SolutionSet solution, Box box, int value)
	{
		return remove(solution, box, value, colSquareSolutionRemovePeer.get(box));
	}

	
	public static boolean squareSquareRowSolutionRemove (SolutionSet solution, Box box, int value)
	{
		return remove(solution, box, value, squareSquareRowSolutionRemovePeer.get(box));
	}
	
	public static boolean squareSquareColSolutionRemove (SolutionSet solution, Box box, int value)
	{
		return remove(solution, box, value, squareSquareColSolutionRemovePeer.get(box));
	}


	
	private static boolean remove(SolutionSet solution, Box box, int value, List<Box> boxesToCheck) {
		if (boxesToCheck == null)
			return false;
		boolean removed = false;
		for (Box boxCheck : boxesToCheck)
			if (solution.getSolution(boxCheck).remove(value))
				removed = true;
		return removed;
	}
}
