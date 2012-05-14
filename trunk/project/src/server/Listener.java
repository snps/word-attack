package server;

import java.io.IOException;
import java.io.InputStream;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketReaderTimeoutException;
import enemy.Enemy;

/**
 * Server listener
 * 
 */
public class Listener extends Thread {
	public static final int READ_INTERVAL = 100;

	private InputStream input;
	private ClientConnection connection;
	private ClientMonitor clientMonitor;

	public Listener(InputStream input, ClientConnection connection, ClientMonitor clientMonitor) {
		this.input = input;
		this.connection = connection;
		this.clientMonitor = clientMonitor;
	}

	@Override
	public void run() {
		NetPacketReader reader = new NetPacketReader(input, READ_INTERVAL);

		while (!isInterrupted()) {
			NetPacket packet = null;

			// Read next packet from client.
			try {
				packet = reader.readPacket();
			} catch (NetPacketReaderTimeoutException e) {
				continue;
			} catch (IOException e) {
				System.err.println("Server: listener could not read packet from client!");
			}

			// Handle packet.
			if (packet != null) {
				handlePacket(packet);
			}
		}

		System.out.println("Server: listener terminated");
	}

	private void handlePacket(NetPacket packet) {
		NetPacket.Type type = packet.getType();

		try {
			if (type == NetPacket.Type.PLAYER_INPUT_UPDATE) {
				String word = packet.getPacketElementContent(NetPacket.PLAYER_INPUT_TAG);
				String playerName = packet.getPacketElementContent(NetPacket.PLAYER_NAME_TAG);
				if (clientMonitor.enemyWordExists(word)) {
					clientMonitor.removeEnemy(new Enemy(word, 0, 0));
					clientMonitor.giveBackWord(word);
					packet = new NetPacket(NetPacket.Type.DESTROY_ENEMY);
					packet.addPacketElement(NetPacket.WORD_TAG, word);
					packet.addPacketElement(NetPacket.PLAYER_NAME_TAG, playerName);
					clientMonitor.sendPacketToAllClients(packet);
				}
			} else if (type == NetPacket.Type.NEW_PLAYER) {
				clientMonitor.sendPacketToAllClients(packet);
			} else if (type == NetPacket.Type.START_GAME) {
				clientMonitor.startEnemyGenerator();
				clientMonitor.sendPacketToAllClients(packet);
			} else if (type == NetPacket.Type.DISCONNECT_FROM_GAME) {
				// Send remove player to clients.
				String playerName = packet.getPacketElementContent(NetPacket.PLAYER_NAME_TAG);
				packet = new NetPacket(NetPacket.Type.REMOVE_PLAYER);
				packet.addPacketElement(NetPacket.PLAYER_NAME_TAG, playerName);
				clientMonitor.sendPacketToAllClients(packet);
				
				// Interrupt self.
				interrupt();

				try {
					connection.disconnect();
				} catch (IOException e) {
					System.err.println("Server: could not disconnect client!");
				}
			} else {
				System.err.println("Server: listener received unknown package!");
			}
		} catch (IOException e) {
			System.err.println("Server: failed to distribute packets!");
		}
	}
}
