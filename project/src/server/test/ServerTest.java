package server.test;

import server.Server;

public class ServerTest {
	public static void main(String[] args) {
		Server server = new Server(4444);
		server.start();
	}
}
