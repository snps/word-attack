package server;

import java.io.IOException;
import java.net.Socket;

import net.NetPacket;
import net.NetPacketReader;
import net.NetPacketWriter;

public class ClientConnection {
	private ClientMonitor clientMonitor;
	private Socket socket;
	private Listener listener;

	public ClientConnection(ClientMonitor clientMonitor, Socket socket) throws IOException {
		this.clientMonitor = clientMonitor;
		this.socket = socket;

		// Acknowledge client.
		acknowledgeClient();

		// Start client listener.
		listener = new Listener(socket.getInputStream(), this, clientMonitor);
		listener.start();

		// Send start game if game is already running.
		if (this.clientMonitor.isRunningGame()) {
			sendPacket(new NetPacket(NetPacket.Type.START_GAME));
		}
	}

	public synchronized void disconnect() throws IOException {
		clientMonitor.removeClientConnection(this);

		// Send disconnect response to client.
		sendPacket(new NetPacket(NetPacket.Type.DISCONNECT_FROM_GAME));

		// Close connection.
		socket.close();
	}

	public synchronized void sendPacket(NetPacket packet) throws IOException {
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		writer.writePacket(packet);
	}

	private void acknowledgeClient() throws IOException {
		// XXX Add security check: game version.
		// Exchange acknowledgments.
		NetPacketReader reader = new NetPacketReader(socket.getInputStream());
		NetPacket packet = reader.readPacket();
		if (packet.getType() != NetPacket.Type.ACKNOWLEDGE) {
			System.err.println("Server: failed to acknowledge client!");
			throw new IOException();
		}
		NetPacketWriter writer = new NetPacketWriter(socket.getOutputStream());
		writer.writePacket(packet);
	}
}
