package sudoku_controller;

import java.util.List;

public interface GuiListener {
	public void onTextEntered(int row, int col, int value);
	//public void onSavePuzzlePressed(String input);
	//public void onSaveSolutionPressed(String string);

	public void onLoad(List<List<Integer>> board);
}
