package sudoku_controller;

import sudoku_model.ReasonForError;

public class ErrorInputEvent {
	private ReasonForError mError;
	private int mInputRow;
	private int mInputCol;
	
	
	public ErrorInputEvent (ReasonForError error, int row, int col)
	{
		mError = error;
		mInputRow = row;
		mInputCol = col;
	}


	public ReasonForError getError() {
		return mError;
	}


	public int getInputRow() {
		return mInputRow;
	}


	public int getInputCol() {
		return mInputCol;
	}
}
