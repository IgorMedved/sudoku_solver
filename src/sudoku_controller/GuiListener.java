package sudoku_controller;

public interface GuiListener {
	public void onTextEntered(int row, int col, int value);
	//public void onSavePuzzlePressed(String input);
	//public void onSaveSolutionPressed(String string);

	public void onLoad();
}
