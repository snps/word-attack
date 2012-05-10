package net.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketWriter;

public class Test {
	public static void main(String[] args) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Socket socket = new Socket("localhost", 4444);
					NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());

					NetPacket packet = new NetPacket(NetPacket.Type.PLAYER_INPUT_UPDATE);
					packet.addPacketElement("text", "elak");

					String type = packet.getType().toString();
					System.out.println("Sent packet type: " + type);
					String text = packet.getPacketElementContent("text");
					System.out.println("Sent packet text: " + text);

					writer.writePacket(packet);

					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

		try {
			ServerSocket server = new ServerSocket(4444);
			Socket socket = server.accept();
			server.close();

			NetPacketReader reader = new NetPacketReader(socket.getInputStream());

			NetPacket packet = reader.readPacket();

			socket.close();

			System.out.println("Read packet type: " + packet.getType().toString());
			System.out.println("Read packet text: " + packet.getPacketElementContent("text"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
