package test;


import static org.junit.Assert.*;
import org.junit.Test;

import server.*;
import view.*;
import client.*;

public class Communication {

	@Test
	public void clientSendsDog() {
		WordMonitor words = new WordMonitor();
		ClientListener clientListener = new ClientListener(words);
		clientListener.start();
		
		IGui gui = new DogGui();
		Client client = new Client(gui);	//behövs gui?
		client.update(null, null);
//		assertEquals("dog", words.getWord());
	}
	
	private class DogGui implements IGui {

		@Override
		public String getPlayerInput() {
			// TODO Auto-generated method stub
			return "dog";
		}
		
	}
}
