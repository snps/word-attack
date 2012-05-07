package test;


import static org.junit.Assert.*;
import org.junit.Test;

import server.*;
import view.*;
import client.*;
import enemy.Enemy;

public class Communication {

	@Test
	public void clientSendsDog() {
		WordMonitor words = new WordMonitor();
		ClientListener clientListener = new ClientListener(words);
		clientListener.start();
		
		Gui gui = new DogGui();
		Client client = new Client(gui);	//behövs gui?
		client.update(null, null);
//		assertEquals("dog", words.getWord());
	}
	
	private class DogGui implements Gui {

		@Override
		public String getPlayerInput() {
			// TODO Auto-generated method stub
			return "dog";
		}

		@Override
		public void addEnemy(Enemy enemy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void moveEnemies() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeEnemy(Enemy enemy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void showMessage(String text) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
