package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import net.NetPacket;
import wordlist.WordChooser;
import wordlist.WordFileReader;
import enemy.Enemy;

public class ClientMonitor {
	public static final String WORD_FILE = "words/words3.txt";

	private Vector<ClientConnection> connections;
	private HashSet<Enemy> enemies;
	private EnemyGenerator generator;
	private WordChooser wordChooser;
	private boolean isRunningGame;

	public ClientMonitor() {
		connections = new Vector<ClientConnection>();
		enemies = new HashSet<Enemy>();
		isRunningGame = false;
	}

	public synchronized void addClientConnection(ClientConnection connection) {
		connections.add(connection);

		System.out.println("Server: " + connections.size() + "/" + Server.MAX_CLIENTS + " connection slots used");
	}

	public synchronized void removeClientConnection(ClientConnection connection) {
		connections.remove(connection);
		notifyAll();

		if (connections.isEmpty()) {
			if (generator != null && generator.isAlive()) {
				generator.interrupt();
			}
			enemies.clear();
			isRunningGame = false;
		}

		System.out.println("Server: " + connections.size() + "/" + Server.MAX_CLIENTS + " connection slots used");
	}

	public synchronized int size() {
		return connections.size();
	}

	public synchronized boolean hasAvailableWord() {
		return wordChooser.hasAvailableWord();
	}

	public synchronized String getNextWord() {
		return wordChooser.getNextWord();
	}

	public synchronized void giveBackWord(String word) {
		wordChooser.giveBackWord(word);
	}

	public synchronized boolean isRunningGame() {
		return isRunningGame;
	}

	public synchronized void startEnemyGenerator() {
		// Try to create the word chooser.
		try {
			wordChooser = new WordChooser(new WordFileReader(WORD_FILE).readWords());
		} catch (FileNotFoundException e) {
			System.err.println("Server: could not read word file " + WORD_FILE);
			NetPacket packet = new NetPacket(NetPacket.Type.ERROR);
			packet.addPacketElement(NetPacket.MESSAGE_TAG, "Server could not read word file");
			try {
				sendPacketToAllClients(packet);
			} catch (IOException e1) {
				System.err.println("Server: could not distribute error packets");
			}
			return;
		}

		// Terminate previously running generator.
		if (generator != null && generator.isAlive()) {
			generator.interrupt();
			try {
				generator.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		// Start the generator.
		generator = new EnemyGenerator(this);
		generator.start();
		isRunningGame = true;
	}

	public synchronized void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}

	public synchronized void removeEnemy(Enemy enemy) {
		enemies.remove(enemy);
	}

	public synchronized void moveEnemies() {
		Iterator<Enemy> itr = enemies.iterator();
		while (itr.hasNext()) {
			Enemy enemy = itr.next();
			enemy.move();

			if (enemy.getYPos() >= 485) {
				generator.interrupt();

				try {
					sendPacketToAllClients(new NetPacket(NetPacket.Type.GAME_OVER));
				} catch (IOException e) {
					System.err.println("Server: could not send game over!");
				}

				break;
			}
		}
	}

	public synchronized boolean enemyWordExists(String word) {
		return enemies.contains(new Enemy(word, 0, 0));
	}

	public synchronized void sendPacketToAllClients(NetPacket packet) throws IOException {
		for (ClientConnection conn : connections) {
			conn.sendPacket(packet);
		}
	}

	public synchronized void waitForDisconnect() {
		while (connections.size() == Server.MAX_CLIENTS) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
}
