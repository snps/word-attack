package client.test;

import java.io.IOException;

import view.Gui;
import view.PlayBoard;
import client.Client;

public class ClientTest {
	public static void main(String[] args) {
		Gui gui = new PlayBoard();
		Client client = new Client(gui, "Player 1");
		
//		ServerStub stub = new ServerStub(4444);
//		stub.setDaemon(true);
//		stub.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			System.err.println("Interruped!");
		}

		try {
			client.connect("localhost", 4444);
		} catch (IOException e) {
			System.err.println("Could not connect to server!");
		}
		
		client.sendStartRequest();
	}
}
