package client;

import java.util.Observable;
import java.util.Observer;

import view.IGui;

public class Client implements Observer {
	private IGui gui;
	
	public Client(IGui gui) {
		this.gui = gui;
	}

	@Override
	public void update(Observable obs, Object o) {
		String word = gui.getPlayerInput();
		
		// TODO
	}

}
