package net.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketWriter;

import org.junit.Before;
import org.junit.Test;

public class TestNetPacketWriterAndReader {
	private static final String TAG = "text";
	private static final String VALUE = "groda";

	private Runnable writer;

	@Before
	public void setUp() {
		writer = new Runnable() {
			public void run() {
				boolean errorFlag = false;
				try {
					Socket socket = new Socket("localhost", 4444);
					NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());

					NetPacket packet = new NetPacket(NetPacket.Type.PLAYER_INPUT_UPDATE);
					packet.addPacketElement(TAG, VALUE);

					String type = packet.getType().toString();
					System.out.println("Sent packet type: " + type);
					String text = packet.getPacketElementContent(TAG);
					System.out.println("Sent packet text: " + text);

					writer.writePacket(packet);

					socket.close();
				} catch (IOException e) {
					errorFlag = true;
				}

				assertFalse("Could not write packet", errorFlag);
			}
		};
	}

	@Test
	public void testWriteAndReadNetPacket() {
		Thread t = new Thread(writer);
		t.start();

		boolean errorFlag = false;

		try {
			ServerSocket server = new ServerSocket(4444);
			Socket socket = server.accept();
			server.close();

			NetPacketReader reader = new NetPacketReader(socket.getInputStream());

			NetPacket packet = reader.readPacket();

			socket.close();

			String type = packet.getType().toString();
			assertEquals("Received packet type was not correct", NetPacket.Type.PLAYER_INPUT_UPDATE.toString(), type);

			String value = packet.getPacketElementContent(TAG);
			assertEquals("Received packet element value was not correct", VALUE, value);

			System.out.println("Read packet type: " + type);
			System.out.println("Read packet text: " + value);
		} catch (IOException e) {
			errorFlag = true;
		}

		assertFalse("Could not write packet", errorFlag);
	}
}
