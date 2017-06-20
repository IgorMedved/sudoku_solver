package sudoku_model;

public class ReasonForError {
	
	private int row;
	private int col;
	private ErrorType mError;
	private int mValue;
	
	public ReasonForError ()
	{
		mError=ErrorType.NOERROR;
	}
	
	public ReasonForError(int row, int col, int value, ErrorType error)
	{
		mError = error;
		this.row = row;
		this.col = col;
		mValue = value;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public ErrorType getErrorType() {
		return mError;
	}

	public int getValue() {
		return mValue;
	}
	
	
}
