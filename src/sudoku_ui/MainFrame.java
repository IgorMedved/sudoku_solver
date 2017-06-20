package sudoku_ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import sudoku_conract.SudokuContract;
import sudoku_controller.Controller;
import sudoku_controller.ErrorInputEvent;
import sudoku_controller.GuiListener;
import sudoku_controller.SolutionCompleteEvent;
import sudoku_controller.SolutionListener;
import sudoku_controller.SolutionStatus;
import sudoku_model.ReasonForError;



public class MainFrame extends JFrame implements ActionListener,SolutionListener {

		private GuiListener mListener;
	
		private SudokuPanel mEntryPanel;
		private SudokuPanel mSolutionPanel;
		private JLabel mStatusLabel;
		private JLabel mActionLabel;
		private JButton mExportPuzzleButton;
		private JButton mExportSolutionButton;
		private JButton mLoadPuzzleButton;
		
		public MainFrame()
		{
			super ("Sudoku Sovler");
			
			mEntryPanel = new SudokuPanel(this);
			mSolutionPanel = new SudokuPanel(null);
			mExportPuzzleButton = new JButton(SudokuContract.SAVE_PUZZLE_BUTTON_LABEL);
			mExportSolutionButton = new JButton(SudokuContract.SAVE_SOLUTION_BUTTON_LABEL);
			mLoadPuzzleButton = new JButton(SudokuContract.LOAD_PUZZLE_BUTTON_LABEL);
			mStatusLabel = new JLabel(SudokuContract.STATUS_LABEL_ENTER);
			mActionLabel = new JLabel(SudokuContract.ACTION_LABEL_ENTER);
			mExportPuzzleButton.setName(SudokuContract.SAVE_PUZZLE_BUTTON_NAME);
			mExportSolutionButton.setName(SudokuContract.SAVE_SOLUTION_BUTTON_NAME);
			mLoadPuzzleButton.setName(SudokuContract.LOAD_PUZZLE_BUTTON_NAME);
			
			mExportPuzzleButton.addActionListener(this);
			mExportSolutionButton.addActionListener(this);
			mLoadPuzzleButton.addActionListener(this);
			componentLayout();
			setSize(new Dimension(615,490));
			setResizable(false);
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    setVisible(true);
		}
		
		
		public void componentLayout()
		{
			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			
			gc.gridx = 0;
			gc.gridy = 0;
			add (mEntryPanel, gc);
			
			gc.gridx= 1;
			add (mSolutionPanel, gc);
			
			gc.gridx= 0;
			gc.gridy= 1;
			add (mActionLabel, gc);
			
			gc.gridx=1;
			gc.gridy=1;
			add (mStatusLabel, gc);
			
			gc.gridx=0;
			gc.gridy=2;
			add (mExportPuzzleButton,gc);
			
			gc.gridx=1;
			gc.gridy=2;
			add (mExportSolutionButton,gc);
			
			gc.gridx=0;
			gc.gridy=3;
			add(mLoadPuzzleButton, gc);
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Action was performed " + e.getSource());
			
			if( mListener!= null)
			{
				if (e.getSource() instanceof JTextField)
				{
					JTextField source = (JTextField)e.getSource();
					String valueStr = source.getText();
					if (valueStr.length() > 0)
					{
						int value = Integer.parseInt(valueStr);
					
						int row = Character.getNumericValue(source.getName().charAt(0));
						int col = Character.getNumericValue(source.getName().charAt(1));
						mListener.onTextEntered(row, col, value);
					}
				}
				else if (e.getSource() instanceof JButton)
				{
					JButton source = (JButton)e.getSource();
					if(SudokuContract.SAVE_PUZZLE_BUTTON_NAME.equals(source.getName()))
						save("Save Puzzle", mEntryPanel);
					else if (SudokuContract.SAVE_SOLUTION_BUTTON_NAME.equals(source.getName()))
						save("Save Solution", mSolutionPanel);
					else if (SudokuContract.LOAD_PUZZLE_BUTTON_NAME.equals(source.getName()))
						loadPuzzle();
					
				}
				else if (e.getSource() instanceof PlainDocument )
				{
					PlainDocument source = (PlainDocument)e.getSource();
					try {
						//System.out.println("Got to here");
						String valueString = source.getText(0, 1);
						int value;
						if (valueString != null && !valueString.trim().isEmpty())
						{
							System.out.println("\"" + valueString +"\"");
							value = Integer.parseInt(valueString);
						}
						else
							value = 0;
						int row = (int) source.getProperty(SudokuContract.ROW);
						int col = (int) source.getProperty(SudokuContract.COLUMN);
						mListener.onTextEntered(row, col, value);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		}


		private void loadPuzzle() {
			JFileChooser fc = new JFileChooser();
			StringBuilder sb;
			fc.setCurrentDirectory(new File("."));
			int retrival = fc.showOpenDialog(null); // Let user select file from the file system
		    if (retrival == JFileChooser.APPROVE_OPTION) 
		    {
		    	mListener.onLoad();
		    	reloadPanel();
		    	try {
					List<String> input = Files.readAllLines(fc.getSelectedFile().toPath());
					for (int i = 0; i <9; i++)
						for (int j = 0; j < 9; j++)
						{
							mEntryPanel.update(i, j, input.get(i).charAt(j));
							try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}


		public void setListener(GuiListener listener) {
			mListener = listener;
		}


		@Override
		public void updateSolutionBoard(SolutionCompleteEvent e) {
			mSolutionPanel.updateSolutionBoard(e.getSolutionBoard());
			updateLabels(e.getStatus());
			notify();
			
		}
		
		
		private void updateLabels (SolutionStatus status)
		{
			switch (status)
			{
			case SOLVED:
				mActionLabel.setText(SudokuContract.ACTION_LABEL_WIN);
				mStatusLabel.setText(SudokuContract.STATUS_LABEL_WIN);
				break;
			
			case UNSOLVED:
				mActionLabel.setText(SudokuContract.ACTION_LABEL_ENTER);
				mStatusLabel.setText(SudokuContract.STATUS_LABEL_ENTER);
				break;
			case OVERCONSTRAINED:
				mActionLabel.setText(SudokuContract.ACTION_LABEL_REMOVE);
				mStatusLabel.setText(SudokuContract.STATUS_LABEL_REMOVE);
			}
		}


		@Override
		public void onErrorInput(ErrorInputEvent errorInputEvent) {
			//System.out.println(errorInputEvent.getError().getErrorType());
			String errorMessage = "Number " + errorInputEvent.getError().getValue() + " already exists in ";
			switch (errorInputEvent.getError().getErrorType())
			{
			case ROW:
				errorMessage += "row " + (errorInputEvent.getError().getRow()+1);
				break;
			case COLUMN:
				errorMessage += "column " + (errorInputEvent.getError().getCol()+1);
				break;
			case SQUARE:
				errorMessage += "this 3x3 square";
				break;
			}
			
			JOptionPane.showMessageDialog(this, errorMessage);
			mEntryPanel.remove(errorInputEvent.getInputRow(), errorInputEvent.getInputCol());
			//mEntryPanel.highlight(errorInputEvent.getError().getRow(), errorInputEvent.getError().getCol());
		}


		private void save (String message, SudokuPanel source)
		{
			
			JFileChooser fc = new JFileChooser();
			StringBuilder sb;
			fc.setCurrentDirectory(new File("."));
			int retrival = fc.showSaveDialog(this); // Let user select file from the file system
		    if (retrival == JFileChooser.APPROVE_OPTION) {
			        try {
			            FileWriter fw = new FileWriter(fc.getSelectedFile());
			            char c;
			            for (int i =0; i<9; i++)
			            {
			            	sb = new StringBuilder();
			            	for (int j = 0; j<9; j++)
			            	{
			            		c = source.getCharacter(i,j);
			            		if (Character.isDigit(c))
			            			sb.append(c);
			            		else
			            			sb.append('*');
			            	}
			            	fw.write(sb.toString());
			            	fw.write(System.lineSeparator());
			            	
			            }
			            		
			            
			            fw.close();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
			        
			    }
			//File saveFile = fc.getSelectedFile();
			//System.out.println(saveFile);
			//fc.showSaveDialog(this);
		}
		
		private void reloadPanel()
		{
			remove(mEntryPanel);
			mEntryPanel = new SudokuPanel(this);
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			add(mEntryPanel,gc);
		}
}
