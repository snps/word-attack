package server;

import java.io.IOException;
import java.net.Socket;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketWriter;

public class ClientConnection {
	private ClientMonitor clientMonitor;
	private Socket socket;

	public ClientConnection(ClientMonitor clientMonitor, Socket socket) throws IOException {
		this.clientMonitor = clientMonitor;
		this.socket = socket;

		// Acknowledge client.
		acknowledgeClient();

		// Start client listener.
		Listener listener = new Listener(socket.getInputStream(), this, clientMonitor);
		listener.start();
	}

	public void disconnect() throws IOException {
		clientMonitor.removeClientConnection(this);

		// Give listener some time to terminate.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Close connection.
		socket.close();
	}

	public void sendPacket(NetPacket packet) throws IOException {
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		writer.writePacket(packet);
	}

	private void acknowledgeClient() throws IOException {
		// XXX Add security check: game version.
		// Exchange acknowledgments.
		NetPacketReader reader = new NetPacketReader(socket.getInputStream());
		NetPacket packet = reader.readPacket();
		if (packet.getType() != NetPacket.Type.ACKNOWLEDGE) {
			System.err.println("Failed to acknowledge client!");
			throw new IOException();
		}
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		writer.writePacket(packet);
	}
}
