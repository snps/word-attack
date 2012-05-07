package client.test;

import view.PlayBoard;
import client.Client;

public class ClientTest {
	public static void main(String[] args) {
		PlayBoard gui = new PlayBoard();
		Client client = new Client(gui);
		
		gui.addObserver(client);
		
		ServerStub stub = new ServerStub(gui);
		stub.isDaemon();
		stub.start();
	}
}
