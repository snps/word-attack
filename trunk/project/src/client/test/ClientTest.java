package client.test;

import client.Client;

public class ClientTest {
	public static void main(String[] args) {
		Client client = new Client("Player 1");
		
		ServerStub stub = new ServerStub(client);
		stub.isDaemon();
		stub.start();
	}
}
