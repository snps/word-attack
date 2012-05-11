package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.NetPacket;
import enemy.Enemy;

public class ClientMonitor {
	private List<ClientConnection> connections;
	private HashSet<Enemy> enemies;

	public ClientMonitor() {
		connections = new ArrayList<ClientConnection>();
		enemies = new HashSet<Enemy>();
	}

	public synchronized void addClientConnection(ClientConnection connection) {
		connections.add(connection);

		System.out.println(connections.size() + "/" + Server.MAX_CLIENTS + " connection slots used");
	}

	public synchronized void removeClientConnection(ClientConnection connection) {
		connections.remove(connection);
		notifyAll();

		System.out.println(connections.size() + "/" + Server.MAX_CLIENTS + " connection slots used");
	}

	public synchronized int size() {
		return connections.size();
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
				try {
					sendPacketToAllClients(new NetPacket(NetPacket.Type.GAME_OVER));
				} catch (IOException e) {
					System.err.println("Server could not send game over!");
				}
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
