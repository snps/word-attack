package client;

import java.io.IOException;
import java.io.InputStream;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketReaderTimeoutException;

public class Listener extends Thread {
	public static final int READ_INTERVAL = 100;

	private Client client;
	private InputStream input;

	public Listener(Client client, InputStream input) {
		this.client = client;
		this.input = input;
	}

	@Override
	public void run() {
		NetPacketReader reader = new NetPacketReader(input, READ_INTERVAL);

		while (!isInterrupted()) {
			NetPacket packet = null;

			// Read next packet from server.
			try {
				packet = reader.readPacket();
			} catch (NetPacketReaderTimeoutException e) {
				continue;
			} catch (IOException e) {
				System.err.println("Client listener could not read packet from server!");
			}

			// Handle packet.
			if (packet != null) {
				handlePacket(packet);
			}
		}
		
		System.out.println("Listener terminated");
	}

	private void handlePacket(NetPacket packet) {
		NetPacket.Type type = packet.getType();

		if (type == NetPacket.Type.START_GAME) {
			client.startGame();
		} else if (type == NetPacket.Type.CREATE_ENEMY) {
			String word = packet.getPacketElementContent(NetPacket.WORD_TAG);
			int speed = Integer.parseInt(packet.getPacketElementContent(NetPacket.SPEED_TAG));
			int xPos = Integer.parseInt(packet.getPacketElementContent(NetPacket.X_POS_TAG));

			client.createEnemy(word, speed, xPos);
		} else if (type == NetPacket.Type.DESTROY_ENEMY) {
			String word = packet.getPacketElementContent(NetPacket.WORD_TAG);
			String playerName = packet.getPacketElementContent(NetPacket.PLAYER_NAME_TAG);

			client.destroyEnemy(word);
			client.increaseScore(playerName);
		} else if (type == NetPacket.Type.MOVE_ENEMIES) {
			client.moveEnemies();
		} else if (type == NetPacket.Type.GAME_OVER) {
			// Disconnect from server.
			try {
				client.disconnect();
			} catch (IOException e) {
				System.err.println("Could not disconnect from server!");
			}

			client.showMessage("Game Over");
		} else if (type == NetPacket.Type.UNKNOWN) {
			client.showMessage("Unknown packet received from server");
		}
	}
}
