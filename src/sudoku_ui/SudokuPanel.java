package sudoku_ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import sudoku_controller.SolutionCompleteEvent;

public class SudokuPanel extends JPanel {
	private List<List<SudokuSquare>> mSudokuBoard;
	
	SudokuPanel (ActionListener listener)
	{
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		GridBagConstraints gc = new GridBagConstraints();
		mSudokuBoard = new ArrayList<>(9);
		List<SudokuSquare> row;
		
		SudokuSquare square;
		setPreferredSize (new Dimension(200, 200));
		//setLayout(new FlowLayout());
		//add(square);
		gc.weightx=1;
		for (int i = 0; i < 3; i++)
		{
			row = new ArrayList<>(3);
			gc.gridy = i;
		
			for (int j= 0; j<3; j++)
			{
				gc.gridx = j;
				square = new SudokuSquare(listener, i, j);
				add(square, gc);
				row.add(square);
			}
			mSudokuBoard.add(row);
		}
	}

	

	public void updateSolutionBoard(List<List<Integer>> solutions) {
		for (int i = 0; i < 3; i++)
		{
			for (int j= 0; j<3; j++)
			{
				mSudokuBoard.get(i).get(j).updateSquare (solutions);
			}
		}
		
	}
	
	public void updateWholeBoard(List<List<Integer>> board) {
		for (int i = 0; i < 3; i++)
		{
			for (int j= 0; j<3; j++)
			{
				mSudokuBoard.get(i).get(j).resetSquare (board);
			}
		}
		
	}

	public synchronized void remove(int inputRow, int inputCol) {
		//System.out.println("Removing text in square: " +(inputRow/3*3+inputCol/3));
		mSudokuBoard.get(inputRow/3).get(inputCol/3).
					delete(inputRow%3, inputCol%3);
		
	}
	
	public synchronized void update (int row, int col, char value)
	{
		if (value == '*')
			remove(row,col);
		else
			mSudokuBoard.get(row/3).get(col/3).setText(row%3, col%3, value);
	}

	public void highlight(int row, int col) {
		mSudokuBoard.get(row/3).get(col/3).
				highlight(row%3, col%3);
		
	}

	public char getCharacter(int i, int j) {
		return mSudokuBoard.get(i/3).get(j/3).getCharacter(i%3, j%3);
	}



	
}
