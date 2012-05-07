package client;

import java.util.Observable;
import java.util.Observer;

import view.Gui;

public class Client implements Observer {
	private Gui gui;
	
	public Client(Gui gui) {
		this.gui = gui;
	}

	@Override
	public void update(Observable obs, Object o) {
		String word = gui.getPlayerInput();
		
		// FIXME
		gui.showMessage(word);
	}

}
