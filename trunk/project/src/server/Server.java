package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.NetPacket;

public class Server extends Thread {
	public static final int MAX_CLIENTS = 4;

	private int port;
	private int nbrOfPlayers;
	private ClientMonitor clientMonitor;

	public Server(int port, int nbrOfPlayers) {
		this.port = port;
		this.nbrOfPlayers = nbrOfPlayers;
		clientMonitor = new ClientMonitor();
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			while (!isInterrupted() && clientMonitor.size() < MAX_CLIENTS) {
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

					ClientConnection connection = new ClientConnection(clientMonitor, socket);
					clientMonitor.addClientConnection(connection);

					// Send start game when enough players have joined.
					if (clientMonitor.size() >= nbrOfPlayers && !clientMonitor.isRunningGame()) {
						clientMonitor.sendPacketToAllClients(new NetPacket(NetPacket.Type.START_GAME));
					}

					System.out.println("Client connected from " + socket.getInetAddress().toString());
				} catch (IOException e) {
					System.err.println("Server could not open connection to client!");
				}
			}
			clientMonitor.waitForDisconnect();
		}
	}
}
