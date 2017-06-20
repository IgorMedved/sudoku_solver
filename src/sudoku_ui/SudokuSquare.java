package sudoku_ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import sudoku_conract.SudokuContract;

public class SudokuSquare extends JPanel implements DocumentListener {
	private List<List<JTextField>> mSquare;
	private ActionListener mListener;
	private int mSquareRow;
	private int mSquareCol;

	public SudokuSquare(ActionListener listener, int squareRow, int squareCol)
	{
		setLayout(new GridBagLayout());
		mListener = listener;
		mSquareRow = squareRow;
		mSquareCol = squareCol;
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		GridBagConstraints gc = new GridBagConstraints();
		setPreferredSize (new Dimension (65,65));
		mSquare = new ArrayList<>(3);
		List<JTextField> row = new ArrayList<>(3);
		JTextField temp;
		gc.weightx=1;
		for (int i = 0; i < 3; i++)
		{
			row = new ArrayList<>(3);
			gc.gridy = i;
		
			for (int j= 0; j<3; j++)
			{
				gc.gridx = j;
				temp = new JTextField();
				temp.setMinimumSize(new Dimension (20,20));
				temp.setPreferredSize(new Dimension(20,20));
				if (mListener!= null)
				{
					AbstractDocument d = (AbstractDocument) temp.getDocument();
					d.setDocumentFilter(new TextFilter());
					temp.getDocument().addDocumentListener(this);
					temp.getDocument().putProperty(SudokuContract.ROW, mSquareRow*3+ i);
					temp.getDocument().putProperty(SudokuContract.COLUMN, mSquareCol*3 +j);
			    //temp.addActionListener(listener);
					//temp.setName((i+squareRow*3) + ""+(j+squareCol*3));
				}
				else
					temp.disable();
				add (temp,gc);
				row.add(temp);
			}
			mSquare.add(row);
		}
				
	}

	public void updateSquare(List<List<Integer>> solutions) {
		JTextField currentField;
		for (int i1= 0; i1 < 3; i1++)
			for (int j1= 0; j1<3; j1++)
			{
				int value = solutions.get(mSquareRow*3+i1).get(mSquareCol*3+j1);
				currentField = mSquare.get(i1).get(j1);
				if (value!=0)
				{
					currentField.setDisabledTextColor(Color.GREEN);
					currentField.setText(Integer.toString(value));
				}
				else
				{
					currentField.setDisabledTextColor(Color.BLACK);
					currentField.setText("*");
				}
			}
				
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		notifyMainFrame(e);
		
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		notifyMainFrame(e);
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		notifyMainFrame(e);
		
	}
	
	public void notifyMainFrame(DocumentEvent e)
	{
		if (mListener!=null)
		{
			mListener.actionPerformed(new ActionEvent(e.getDocument(), ActionEvent.ACTION_PERFORMED, ""));
		}
	}

	public void delete(int i, int j) {
		//System.out.println("Removing text from " + i + " " +j);
		//System.out.println("Existing text "+ mSquare.get(i).get(j));
		mSquare.get(i).get(j).setText("");
		
	}

	public void highlight(int i, int j) {
		mSquare.get(i).get(j).setBackground(Color.yellow);
		
	}

	public char getCharacter(int i, int j) {
		// TODO Auto-generated method stub
		if (mSquare.get(i).get(j).getText().length() <1)
			return '*';
		else
			return mSquare.get(i).get(j).getText().charAt(0);
	}

	public void setText(int i, int j, char value) {
		mSquare.get(i).get(j).setText(Character.toString(value));
		
	}
}
