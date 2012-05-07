package client;

import java.io.InputStream;

public class ServerListener extends Thread {
	private Client client;
	private InputStream input;
	
	public ServerListener(Client client, InputStream input) {
		this.client = client;
		this.input = input;
	}
	
	@Override
	public void run() {
		// TODO listen on input stream.
	}
}
