package server;

import java.io.IOException;

import net.NetPacket;
import enemy.Enemy;

public class EnemyGenerator extends Thread {
	public static final int TIME_BETWEEN_MOVES = 1000;
	public static final int TIME_BETWEEN_CREATE_ENEMY = 4000;
	public static final int SPEED_INCREASE_OVER_TIME = 200;
	public static final String WORD_FILE = "words/words3.txt";

	private ClientMonitor clientMonitor;

	public EnemyGenerator(ClientMonitor clientMonitor) {
		this.clientMonitor = clientMonitor;
	}

	public void run() {
		long time = System.currentTimeMillis();
		int timeBetweenCreateEnemy = TIME_BETWEEN_CREATE_ENEMY;

		while (!isInterrupted()) {
			NetPacket packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);

			// Update server state.
			clientMonitor.moveEnemies();

			try {
				// Send move enemies packet to clients.
				clientMonitor.sendPacketToAllClients(packet);

				// Create new enemy.
				if (clientMonitor.hasAvailableWord() && System.currentTimeMillis() > time + timeBetweenCreateEnemy) {
					// Get next word.
					String word = clientMonitor.getNextWord();

					int speed = (int) Math.floor(Math.random() * 45 + 5);
					int xPos = (int) Math.floor(Math.random() * (800 - Enemy.getWordWidth(word)));

					clientMonitor.addEnemy(new Enemy(word, speed, xPos));

					packet = new NetPacket(NetPacket.Type.CREATE_ENEMY);
					packet.addPacketElement(NetPacket.WORD_TAG, word);
					packet.addPacketElement(NetPacket.SPEED_TAG, Integer.toString(speed));
					packet.addPacketElement(NetPacket.X_POS_TAG, Integer.toString(xPos));
					clientMonitor.sendPacketToAllClients(packet);
					
					// Decrease time between enemy creates over time.
					timeBetweenCreateEnemy = (timeBetweenCreateEnemy < 0) ? 0 : timeBetweenCreateEnemy - SPEED_INCREASE_OVER_TIME;

					time = System.currentTimeMillis();
				}
			} catch (IOException e) {
				System.err.println("Enemy generator could not distribute packets to clients!");
			}

			try {
				Thread.sleep(TIME_BETWEEN_MOVES);
			} catch (InterruptedException e) {
				interrupt();
			}
		}

		System.out.println("Enemy generator terminated");
	}
}
