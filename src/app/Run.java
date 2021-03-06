package app;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import sudoku_controller.Controller;
import sudoku_model.ai.InteractionPeers;
import sudoku_model.ai.Peers;
//import tetris_controller.Game;
import sudoku_ui.MainFrame;

public class Run {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		
		InteractionPeers.initializePeers(); // prepare sudoku peer maps for use by the program
		GuiRunnable guiThread = new GuiRunnable(); // runnable for initializing GUI
		SwingUtilities.invokeAndWait(guiThread); // initialize GUI
		Controller controller = new Controller(); // Initialize main game controller
		MainFrame gameFrame = guiThread.getFrameHandle(); // User interface Main Frame
		gameFrame.setListener(controller); // game controller listens and responds to button and keyboard clicks
		controller.setListener(gameFrame); // mainFrame listens to updates in the model through the gameController
	}

}
