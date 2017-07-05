package sudoku_model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


// This class has maps that map each box cell in sudoku grid on a list of related cells that add constraints 
// on a possible solutions in a given cell
// all the members are static as the constraining cells are known beforehand
public class Peers {
	private static Map <Box, List<Box>> rowPeers; // Row peers are all boxes in the same row except for the key box
	private static Map <Box, List<Box>> rightRowPeers; // row peers only to the right of key box considerably reduces computation for naked twins, triplets, etc
	private static Map <Box, List<Box>> colPeers; // Col peers are all boxes in the same column except for the key box
	private static Map <Box, List<Box>> downColPeers;  //Col peers only down of the key box
	private static Map <Box, List<Box>> squarePeers; // square peers are all the peers in the same 3x3 square except for the key Box
	private static Map <Box, List<Box>> remainingSquarePeers; // remaining square peers for box @0,0 are same as regular square peers and for box @1,1 are @1,2, @2,0, @2,1, @2,2
	private static Map <Box, List<Box>> allPeers;
	private static Map <Box, List<Box>> diagonalPeers;
	private static Map <Box, List<Box>> nonSquareRowPeers; // same as row peers, but without peers in the same square as the key
	private static Map <Box, List<Box>> nonSquareColPeers; // same as col peers, but without peers in the same square as the key
	
	
	public Peers()
	{
		initializePeers();
	}
	
	public static void initializePeers()
	{
		Box box;
		rowPeers = new LinkedHashMap<>();
		rightRowPeers = new LinkedHashMap<>();
		colPeers = new LinkedHashMap<>();
		downColPeers = new LinkedHashMap<>();
		squarePeers = new LinkedHashMap<>();
		remainingSquarePeers = new LinkedHashMap<>();
		diagonalPeers = new LinkedHashMap<>();
		allPeers = new LinkedHashMap<>();
		nonSquareColPeers = new LinkedHashMap<>();
		nonSquareRowPeers = new LinkedHashMap<>();
		//List<Box> current_peers;
		for (int i = 0; i< 9; i++)
			for (int j = 0; j<9; j++)
			{
				box = new Box(i, j);
				rowPeers.put(box, initializeRowPeers(i,j));
				rightRowPeers.put(box, initializeRightRowPeers(i,j));
				nonSquareRowPeers.put(box, initializeSquareRowPeers(i,j));
				
				colPeers.put(box, initializeColPeers(i,j));
				downColPeers.put(box, initializeDownColPeers(i,j));
				nonSquareColPeers.put(box, initializeSquareColPeers(i,j));
				squarePeers.put(box, initializeSquarePeers(i,j));
				remainingSquarePeers.put(box, initializeRemainingSquarePeers(i,j));
				allPeers.put(box, initializeAllPeers(i,j));
				if (i==j && i==4) //special middle square
					diagonalPeers.put(box, initializeCenter());
				else if (i==j)
					diagonalPeers.put(box, initializeLeftTopDiagonal(i));
				else if (i == 8-j)
					diagonalPeers.put(box, initializeRightTopDiagonal(i));
			}
		
				
	}
	
	private static List<Box> initializeSquareColPeers(int i, int j) {
		List<Box> peers = new ArrayList<>(6);
		int lowerBoundExclusionRegion = i/3*3;
		int upperBoundExclusionRegion = lowerBoundExclusionRegion +2;
		for (int row = 0; row < 9; row++)
			if (row<lowerBoundExclusionRegion || row>upperBoundExclusionRegion)
				peers.add(new Box(row, j));
		return peers;
	}
	
	public static Map<Box, List<Box>> getNonSquareRowPeers() {
		return nonSquareRowPeers;
	}

	public static Map<Box, List<Box>> getNonSquareColPeers() {
		return nonSquareColPeers;
	}

	private static List<Box> initializeSquareRowPeers(int i, int j) {
		List<Box> peers = new ArrayList<>(6);
		int lowerBoundExclusionRegion = j/3*3;
		int upperBoundExclusionRegion = lowerBoundExclusionRegion +2;
		for (int col = 0; col < 9; col++)
			if (col<lowerBoundExclusionRegion || col>upperBoundExclusionRegion)
				peers.add(new Box(i, col));
		return Collections.unmodifiableList(peers);
	}

	private static List<Box> initializeRemainingSquarePeers(int i, int j) {
		List<Box> peers = new ArrayList<>(8);
		int upperRow = upperSquareBound(i);
		int upperCol = upperSquareBound(j);
		int lowerCol = lowerSquareBound(j);
		for (int row = i; row<= upperRow; row++)
			for (int col = lowerCol; col <=upperCol; col++)
				if (row!=i || col >j)
					peers.add(new Box(row,col));
		return Collections.unmodifiableList(peers);
	}

	private static List<Box> initializeDownColPeers(int i, int j) {
		if (i!=8)
		{
			List<Box> peers = new ArrayList<>(8-i);
			for (int row = i+1; row < 9; row++)
				peers.add(new Box (row, j));
			return Collections.unmodifiableList(peers);
		}
		return Collections.unmodifiableList(new ArrayList<>());
	}


	private static List<Box> initializeRightTopDiagonal(int i) {
		List<Box> peers = new ArrayList<>(8);
		for (int j = 0; j <9; j++)
			if (j!=i)
				peers.add(new Box(8-j, j));
		return Collections.unmodifiableList(peers);
	}

	private static List<Box> initializeLeftTopDiagonal(int i) {
		List<Box> peers = new ArrayList<>(8);
		for (int j = 0; j <9; j++)
			if (j!=i)
				peers.add(new Box(j, j));
				
		return Collections.unmodifiableList(peers);
	}

	private static List<Box> initializeRowPeers(int i, int j)
	{
		List<Box> peers = new ArrayList<>(8);
		
		for (int col = 0; col < 9; col ++)
			if (col!= j)
				peers.add(new Box(i, col));
		return Collections.unmodifiableList(peers);
	}
	
	private static List<Box> initializeRightRowPeers(int i, int j)
	{
		if (j!=8)
		{
			List<Box> peers = new ArrayList<>(8-j);
			for (int col = j+1; col < 9; col++)
				peers.add(new Box (i, col));
			return Collections.unmodifiableList(peers);
		}
		return Collections.unmodifiableList(new ArrayList<>());
	}
	
	private static List<Box> initializeColPeers (int i, int j)
	{
		List<Box> peers = new ArrayList<>(8);
		for (int row = 0; row <9; row ++)
			if (row!=i)
				peers.add(new Box (row, j));
		return Collections.unmodifiableList(peers);
	}
	
	private static List<Box> initializeSquarePeers (int i, int j)
	{
		List<Box> peers = new ArrayList<>(8);
		int upperRow = upperSquareBound(i);
		int upperCol = upperSquareBound(j);
		int lowerCol = lowerSquareBound(j);
		for (int row = lowerSquareBound(i); row <=upperRow; row++ )
			for (int col = lowerCol; col <= upperCol; col ++)
			{
				if (row!=i || col!= j)
					peers.add(new Box(row,col));
			}
		return Collections.unmodifiableList(peers);
	}
	
	private static List<Box> initializeAllPeers (int i, int j)
	{
		List<Box> peers = new ArrayList<>(initializeRowPeers(i,j));
		peers.addAll(initializeColPeers(i,j));
		int upperRow = upperSquareBound(i);
		int upperCol = upperSquareBound(j);
		int lowerCol = lowerSquareBound(j);
		for (int row = lowerSquareBound(i); row <= upperRow; row ++)
			for (int col = lowerCol; col <= upperCol; col++)
				if (row!=i && col!=j) // different from square peers condition as the boxes in the same row and col
										// were already added
					peers.add(new Box(row,col));
		return Collections.unmodifiableList(peers);
	}
	
	private static List<Box> initializeCenter ()
	{
		List<Box> peers = new ArrayList<>(16);
		for (int k = 0; k<9; k++)
		{
			if (k!=4)
			{
				peers.add(new Box (k,k));
				peers.add(new Box(8-k, k));
			}
		}
		return Collections.unmodifiableList(peers);
	}
	
	
	public static int lowerSquareBound (int a)
	{
		return a-a%3;
	}
	
	public static int upperSquareBound (int a)
	{
		return lowerSquareBound(a)+2;
	}
	
	public static void printPeers (Box box)
	{
		List<Box> peers = rowPeers.get(box);
		System.out.println("Box " + box.row + " " + box.col + " peers are.");
		if (peers!= null)
		{
			System.out.println("row peers are: ");
			for (Box peer: peers)
			{
				System.out.println(peer);
			}
			
			System.out.println("\n\n\nColumn peers are: ");
			peers = colPeers.get(box);
			for (Box peer: peers)
			{
				System.out.println(peer);
			}
			
			System.out.println("\n\n\nSquare peers are: ");
			peers = squarePeers.get(box);
			for (Box peer: peers)
				System.out.println(peer);
			
			System.out.println("\n\n\nAll peers are: ");
			peers = allPeers.get(box);
			for (Box peer: peers)
				System.out.println(peer);
			
			System.out.println("\n\n\nnonSquareRowPeers are: ");
			peers = nonSquareRowPeers.get(box);
			for (Box peer: peers)
			{
				System.out.println(peer);
			}
			
			System.out.println("\n\n\nnonSquareColPeers are: ");
			peers = nonSquareColPeers.get(box);
			for (Box peer: peers)
			{
				System.out.println(peer);
			}
			
			peers = diagonalPeers.get(box);
			if (peers!= null)
			{
				System.out.println("\n\n\nDiagonal peers are: ");
				
				for (Box peer: peers)
					System.out.println(peer);
			}
			else
			{
				System.out.println("No diagonal peers for this box");
			}
			
			
			peers = rightRowPeers.get(box);
			if (!peers.isEmpty())
			{
				System.out.println("\n\n\nPeers to the right are: ");
				
				for (Box peer: peers)
					System.out.println(peer);
			}
			else
				System.out.println("\nNo peers to the right");
			
			peers = downColPeers.get(box);
			if (!peers.isEmpty())
			{
				System.out.println("\n\n\nPeers down are: ");
				
				for (Box peer: peers)
					System.out.println(peer);
			}
			else
				System.out.println("\nNo peers down");
			
			peers = remainingSquarePeers.get(box);
			if (!peers.isEmpty())
			{
				System.out.println("\n\n\nRemaining square peers: ");
				
				for (Box peer: peers)
					System.out.println(peer);
			}
			else
				System.out.println("\nNo peers remaining in square");
		}
		else
		{
			System.out.println("No peers for this box");
		}
	}

	public static Map<Box, List<Box>> getRowPeers() {
		return Collections.unmodifiableMap(rowPeers);
		}

	public static Map<Box, List<Box>> getColPeers() {
		return Collections.unmodifiableMap(colPeers);
	}

	public static Map<Box, List<Box>> getSquarePeers() {
		return Collections.unmodifiableMap(squarePeers);
	}

	public static Map<Box, List<Box>> getAllPeers() {
		return Collections.unmodifiableMap(allPeers);
	}

	public static Map<Box, List<Box>> getDiagonalPeers() {
		return Collections.unmodifiableMap(diagonalPeers);
	}
	
	public static Map<Box, List<Box>> getRightRowPeers() {
		return Collections.unmodifiableMap(rightRowPeers);
	}

	public static Map<Box, List<Box>> getDownColPeers() {
		return Collections.unmodifiableMap(downColPeers);
	}

	public static Map<Box, List<Box>> getRemainingSquarePeers() {
		return Collections.unmodifiableMap(remainingSquarePeers);
	}
	

}
