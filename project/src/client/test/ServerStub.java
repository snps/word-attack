package client.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import enemy.Enemy;

import net.NetPacket;
import net.NetPacketWriter;

public class ServerStub extends Thread {
	private int port;

	public ServerStub(int port) {
		this.port = port;
	}

	public void run() {
		NetPacketWriter writer = null;

		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			server.close();
			writer = new NetPacketWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Could not create server socket!");
			return;
		}

		try {
			int count = 0;
			while (!isInterrupted()) {
				NetPacket packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);
				writer.writePacket(packet);
				Thread.sleep(500);

				count++;
				String word = "tuffing" + count;
				packet = new NetPacket(NetPacket.Type.CREATE_ENEMY);
				packet.addPacketElement(NetPacket.WORD_TAG, word);
				int speed = (int) Math.round(Math.random() * 45 + 5);
				int xPos = (int) Math.round(Math.random() * (800 - Enemy.getWordWidth(word)));
				packet.addPacketElement(NetPacket.SPEED_TAG, Integer.toString(speed));
				packet.addPacketElement(NetPacket.X_POS_TAG, Integer.toString(xPos));
				writer.writePacket(packet);
				packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);
				writer.writePacket(packet);
				Thread.sleep(500);

				packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);
				writer.writePacket(packet);
				Thread.sleep(500);
			}
		} catch (IOException e) {
			System.err.println("Could not write packet to client!");
		} catch (InterruptedException e) {
			// Nothing
		}
	}
}
