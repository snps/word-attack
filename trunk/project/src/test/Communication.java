package test;


import org.junit.Test;

import server.EnemyMonitor;
import view.Gui;
import client.Client;
import server.Listener;
import enemy.Enemy;

public class Communication {

	@Test
	public void clientSendsDog() {
		EnemyMonitor words = new EnemyMonitor();
		Listener clientListener = new Listener(words);
		clientListener.start();
		
		Gui gui = new DogGui();
		Client client = new Client(gui, "Player 1");	//behövs gui?
		client.update(null, null);
//		assertEquals("dog", words.getWord());
	}
	
	private class DogGui extends Gui {
		
		@Override
		public void createGui() {
			// TODO Auto-generated method stub
			
		}

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

		@Override
		public void addPlayer(String playerName) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void increasePlayerScore(String playerName, int amount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removePlayer(String playerName) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
