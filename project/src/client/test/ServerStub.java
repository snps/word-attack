package client.test;

import client.Client;

public class ServerStub extends Thread {
	private Client client;

	public ServerStub(Client client) {
		this.client = client;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Nothing.
			}
			
			client.moveEnemies();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Nothing.
			}
			
			int speed = (int) Math.round(Math.random() * 45 + 5);
			int xPos = (int) Math.round(Math.random() * 700);
			client.createEnemy("Elaking", speed, xPos);
			
			client.moveEnemies();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Nothing.
			}
			
			client.moveEnemies();
		}
	}
}
