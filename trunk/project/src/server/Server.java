package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import net.NetPacket;

public class Server extends Thread {
	public static final int MAX_CLIENTS = 4;
	public static final int SERVER_ACCEPT_TIMEOUT = 1000;

	private int port;
	private int nbrOfPlayers;
	private ClientMonitor clientMonitor;

	public Server(int port, int nbrOfPlayers) throws IOException {
		// Test if port is available.
		ServerSocket server = new ServerSocket(port);
		server.close();

		this.port = port;
		this.nbrOfPlayers = nbrOfPlayers;
		clientMonitor = new ClientMonitor();
	}

	@Override
	public void run() {
		boolean hasStarted = false;

		ServerSocket server = null;
		outer: while (!isInterrupted()) {
			System.out.println("Server listening for connections on port " + port);
			
			while (!isInterrupted() && clientMonitor.size() < MAX_CLIENTS) {
				// Interrupt when no clients connections left.
				if (hasStarted && clientMonitor.size() == 0) {
					break outer;
				}

				try {
					server = new ServerSocket(port);
				} catch (IOException e) {
					System.err.println("Server could not listen on port " + port);
					break outer;
				}

				try {
					server.setSoTimeout(SERVER_ACCEPT_TIMEOUT);
				} catch (SocketException e) {
					System.err.println("Could not set server socket timeout");
				}

				try {
					Socket socket = null;
					try {
						socket = server.accept();
					} catch (SocketTimeoutException e) {
						continue;
					} finally {
						server.close();
					}

					ClientConnection connection = new ClientConnection(clientMonitor, socket);
					clientMonitor.addClientConnection(connection);

					// Send start game when enough players have joined.
					if (clientMonitor.size() >= nbrOfPlayers && !clientMonitor.isRunningGame()) {
						clientMonitor.sendPacketToAllClients(new NetPacket(NetPacket.Type.START_GAME));
						clientMonitor.startEnemyGenerator();
					}

					System.out.println("Client connected from " + socket.getInetAddress().toString());
				} catch (IOException e) {
					System.err.println("Server could not open connection to client!");
				}

				hasStarted = true;
			}
			clientMonitor.waitForDisconnect();
		}

		System.out.println("Server shutting down");
	}
}
