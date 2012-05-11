package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import enemy.Enemy;

import wordlist.WordFileReader;

import net.NetPacket;

public class EnemyGenerator extends Thread {
	public static final int TIME_BETWEEN_MOVES = 500;
	public static final int TIME_BETWEEN_CREATE_ENEMY = 3000;

	private ClientMonitor clientMonitor;

	public EnemyGenerator(ClientMonitor clientMonitor) {
		this.clientMonitor = clientMonitor;
	}

	public void run() {
		long time = System.currentTimeMillis();

		WordFileReader wfr = null;
		try {
			wfr = new WordFileReader("words/words3.txt");
		} catch (FileNotFoundException e) {
			System.err.println("Word file not found!");
			return;
		}

		List<String> wordlist = wfr.readWords();
		while (!isInterrupted()) {
			NetPacket packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);

			// Update server state.
			clientMonitor.moveEnemies();

			try {
				// Send move enemies packet to clients.
				clientMonitor.sendPacketToAllClients(packet);

				// Create new enemy.
				if (System.currentTimeMillis() > time + TIME_BETWEEN_CREATE_ENEMY) {
					String word = wordlist.get((int) Math.floor(Math.random() * wordlist.size()));
					int speed = (int) Math.floor(Math.random() * 45 + 5);
					int xPos = (int) Math.floor(Math.random() * (800 - Enemy.getWordWidth(word)));

					clientMonitor.addEnemy(new Enemy(word, speed, xPos));

					packet = new NetPacket(NetPacket.Type.CREATE_ENEMY);
					packet.addPacketElement(NetPacket.WORD_TAG, word);
					packet.addPacketElement(NetPacket.SPEED_TAG, Integer.toString(speed));
					packet.addPacketElement(NetPacket.X_POS_TAG, Integer.toString(xPos));
					clientMonitor.sendPacketToAllClients(packet);

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
