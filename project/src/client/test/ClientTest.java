package client.test;

import java.io.IOException;

import view.Gui;
import view.PlayBoard;
import client.Client;

public class ClientTest {
	public static void main(String[] args) {
		String name = "Felix";
		String server = "localhost";
		if(args.length == 2) {
			server = args[0];
			name = args[1];
		} else {
			System.out.println("Using standard name " + name + " and defaulting server location to " + server + ".");
		}
		Gui gui = new PlayBoard();
		Client client = new Client(gui, name);

		// ServerStub stub = new ServerStub(4444);
		// stub.setDaemon(true);
		// stub.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			System.err.println("Interruped!");
		}

		try {
			client.connect(server, 4444);
			//client.sendStartRequest();
		} catch (IOException e) {
			System.err.println("Could not connect to server!");
		}
	}
}
