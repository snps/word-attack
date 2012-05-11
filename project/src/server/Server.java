package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	public static final int MAX_CLIENTS = 4;

	private int port;
	private ClientMonitor clients;

	public Server(int port) {
		this.port = port;
		clients = new ClientMonitor();
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			while (!isInterrupted() && clients.size() < MAX_CLIENTS) {
				ServerSocket server = null;
				try {
					server = new ServerSocket(port);
				} catch (IOException e) {
					System.err.println("Server could not listen on port " + port);
				}

				System.out.println("Server listening for connections on port " + port);

				try {
					Socket socket = server.accept();
					server.close();

					ClientConnection connection = new ClientConnection(clients, socket);
					clients.addClientConnection(connection);

					System.out.println("Client connected from " + socket.getInetAddress().toString());
				} catch (IOException e) {
					System.err.println("Server could not open connection to client!");
				}
			}
			clients.waitForDisconnect();
		}
	}
}
