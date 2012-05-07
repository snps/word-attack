package client.test;

import view.Gui;
import view.PlayBoard;
import enemy.Enemy;

public class ClientTest {
	public static void main(String[] args) {
		Gui gui = new PlayBoard();
		gui.addEnemy(new Enemy("Elaking", 10, 70));
		gui.addEnemy(new Enemy("Tuffing", 20, 150));
		gui.addEnemy(new Enemy("Cowboy", 5, 290));
		gui.addEnemy(new Enemy("Scarface", 30, 510));
		
		ServerStub stub = new ServerStub(gui);
		stub.isDaemon();
		stub.start();
	}
}
