package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import net.NetPacket;
import net.NetPacketWriter;
import view.Gui;
import view.PlayBoard;
import enemy.Enemy;

public class Client implements Observer {
	public static final int SCORE_PER_WORD = 100;

	private Gui gui;
	private Socket socket;
	private Listener listener;
	
	private String name;
	private int score;

	public Client(String name) {
		PlayBoard pb = new PlayBoard();
		pb.addObserver(this);
		gui = pb;
		
		this.name = name;
		score = 0;
	}

	public void connect(String host, int port) throws IOException {
		// Connect to host.
		socket = new Socket(host, port);

		// Create and start server listener.
		listener = new Listener(this, socket.getInputStream());
		listener.setDaemon(true);
		listener.start();
	}

	public void disconnect() throws IOException {
		// Stop listener.
		listener.interrupt();

		// Close connection.
		socket.close();
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
	
	public String getName() {
		return name;
	}

	public void increaseScore(String playerName) {
		// FIXME add score to correct player.
		
		score += SCORE_PER_WORD;
	}
	
	public void showMessage(String message) {
		gui.showMessage(message);
	}

	@Override
	public void update(Observable obs, Object o) {
		String input = gui.getPlayerInput();

		// Create packet and append player input.
		NetPacket packet = new NetPacket(NetPacket.Type.PLAYER_INPUT_UPDATE);
		packet.addPacketElement(NetPacket.PLAYER_INPUT_TAG, input);

		// Create packet writer and send packet.
		try {
			NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
			writer.writePacket(packet);
		} catch (IOException e) {
			System.err.println("Client could not write packet to server!");
		}
	}

}
