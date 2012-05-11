package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	public static final int MAX_CLIENTS = 4;

	private int port;
	private List<Listener> listeners;

	public Server(int port) {
		this.port = port;
		listeners = new ArrayList<Listener>();
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			while (!isInterrupted() && listeners.size() < MAX_CLIENTS) {
				ServerSocket server = null;
				try {
					server = new ServerSocket(port);
				} catch (IOException e) {
					System.err.println("Server could not listen on port " + port);
				}

				try {
					Socket socket = server.accept();
				} catch (IOException e) {
					System.err.println("An IO error occured when listening for client connections");
				}
			}
			waitForDisconnect();
		}
	}

	public synchronized void disconnectListener(Listener listener) {
		listeners.remove(listener);
		notifyAll();
	}

	private synchronized void waitForDisconnect() {
		while (listeners.size() == MAX_CLIENTS) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
}
