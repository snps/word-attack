package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketWriter;
import view.Gui;
import enemy.Enemy;

public class Client implements Observer {
	public static final int CONNECT_TIMEOUT = 2000;
	public static final int SCORE_PER_WORD = 100;

	private Gui gui;
	private Socket socket;
	private Listener listener;

	private String playerName;

	public Client(Gui gui, String playerName) {
		this.gui = gui;
		this.gui.addObserver(this);
		this.gui.addPlayer(playerName);
		this.playerName = playerName;
	}

	public void connect(String host, int port) throws IOException {
		System.out.println("Connecting to server " + host + " on port " + port);

		// Connect to host.
		socket = new Socket(host, port);

		// Exchange acknowledgments (to ensure connection to the correct
		// server).
		acknowledgeServer();

		System.out.println("Client is connected to server. Starting listener...");

		// Create and start server listener.
		listener = new Listener(this, socket.getInputStream());
		listener.start();

		System.out.println("Listener started.");

		// Send player name.
		NetPacket packet = new NetPacket(NetPacket.Type.NEW_PLAYER);
		packet.addPacketElement(NetPacket.PLAYER_NAME_TAG, playerName);
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		writer.writePacket(packet);
	}

	private void acknowledgeServer() throws IOException {
		// XXX Add security check: game version.
		// Exchange acknowledgments
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		NetPacketReader reader = new NetPacketReader(socket.getInputStream(), CONNECT_TIMEOUT);
		writer.writePacket(new NetPacket(NetPacket.Type.ACKNOWLEDGE));
		NetPacket packet = reader.readPacket();
		if (packet.getType() != NetPacket.Type.ACKNOWLEDGE) {
			throw new IOException();
		}
	}

	public synchronized void disconnect() throws IOException {
		if (socket.isClosed()) {
			return;
		}

		System.out.println("Disconnecting client...");

		// Send disconnect packet to server.
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		writer.writePacket(new NetPacket(NetPacket.Type.DISCONNECT_FROM_GAME));

		// Stop listener.
		listener.interrupt();

		// Give listener some time to terminate.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Close connection.
		socket.close();
	}

	public void sendStartRequest() {
		NetPacket packet = new NetPacket(NetPacket.Type.START_GAME);
		try {
			NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
			writer.writePacket(packet);
		} catch (IOException e) {
			System.err.println("Client could not write packet to server!");
		}
	}

	public void displayGame() {
		gui.createGui();
	}

	public void addCoPlayer(String playerName) {
		if (!playerName.equals(this.playerName)) {
			gui.addPlayer(playerName);
		}
	}

	public void removeCoPlayer(String playerName) {
		gui.removePlayer(playerName);
	}

	public void createEnemy(String word, int speed, int xPos) {
		Enemy enemy = new Enemy(word, speed, xPos);
		gui.addEnemy(enemy);
	}

	public void destroyEnemy(String word) {
		Enemy enemy = new Enemy(word, 0, 0);
		gui.removeEnemy(enemy);
	}

	public void moveEnemies() {
		gui.moveEnemies();
	}

	public void increaseScore(String playerName) {
		gui.increasePlayerScore(playerName, SCORE_PER_WORD);
	}

	public void showMessage(String message) {
		gui.showMessage(message);
	}

	@Override
	public void update(Observable obs, Object o) {
		// Check for disconnect message.
		if (o != null && o.equals("disconnect")) {
			try {
				disconnect();
			} catch (IOException e) {
				System.err.println("Failed to disconnect!");
			}

			return;
		}

		// Get player input.
		String input = gui.getPlayerInput();

		// Create packet and append player input.
		NetPacket packet = new NetPacket(NetPacket.Type.PLAYER_INPUT_UPDATE);
		packet.addPacketElement(NetPacket.PLAYER_INPUT_TAG, input);
		packet.addPacketElement(NetPacket.PLAYER_NAME_TAG, playerName);

		// Create packet writer and send packet.
		try {
			NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
			writer.writePacket(packet);
		} catch (IOException e) {
			System.err.println("Client could not write packet to server!");
			gui.showMessage("Could not send data to server");
		}
	}

}
