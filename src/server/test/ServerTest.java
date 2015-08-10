package server.test;

import java.io.IOException;

import server.Server;

public class ServerTest {
	public static void main(String[] args) {
		Server server = null;
		try {
			server = new Server(4444, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.start();
	}
}
