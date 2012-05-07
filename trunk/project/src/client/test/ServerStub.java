package client.test;

import enemy.Enemy;
import view.Gui;

public class ServerStub extends Thread {
	private Gui gui;

	public ServerStub(Gui gui) {
		this.gui = gui;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Nothing.
			}
			
			gui.moveEnemies();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Nothing.
			}
			
			int speed = (int) Math.round(Math.random() * 45 + 5);
			int xPos = (int) Math.round(Math.random() * 700);
			gui.addEnemy(new Enemy("Elaking", speed, xPos));
			
			gui.moveEnemies();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Nothing.
			}
			
			gui.moveEnemies();
		}
	}
}
