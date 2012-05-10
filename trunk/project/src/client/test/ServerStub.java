package client.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import net.NetPacket;
import net.NetPacketWriter;
import wordlist.WordFileReader;
import enemy.Enemy;

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
		
		WordFileReader wfr = null;
		try {
			wfr = new WordFileReader("words/words3.txt");
		} catch (FileNotFoundException e) {
			System.err.println("Word file not found!");
			return;
		}
		
		List<String> wordlist = wfr.readWords();

		try {
			while (!isInterrupted()) {
				NetPacket packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);
				writer.writePacket(packet);
				Thread.sleep(500);

				String word = wordlist.get((int) Math.floor(Math.random() * wordlist.size()));
				packet = new NetPacket(NetPacket.Type.CREATE_ENEMY);
				packet.addPacketElement(NetPacket.WORD_TAG, word);
				int speed = (int) Math.floor(Math.random() * 45 + 5);
				int xPos = (int) Math.floor(Math.random() * (800 - Enemy.getWordWidth(word)));
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
