package sudoku_controller;

import sudoku_model.ReasonForError;

public interface SolutionListener {
	public void updateSolutionBoard(SolutionCompleteEvent e);

	public void onErrorInput(ErrorInputEvent errorInputEvent);

}
