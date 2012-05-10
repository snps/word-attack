package net;

import java.io.OutputStream;
import java.io.IOException;

/**
 * <p>
 * Implementation of a <i>packet writer</i> wrapper class. Works in conjunction
 * with a <i>packet reader</i> class.
 * </p>
 * <p>
 * Always writes the length of the packet payload before writing the actual
 * payload data. The length is written as a two digit number representing the
 * number of digits needed to determine the data length. After the length of the
 * length is written, the actual length number is written followed by the data.
 * </p>
 * <p>This allows for an arbitrary number of bytes to be written and read.
 * The packet size limit becomes a number with 99 digits because of the two byte
 * representation of the length.</p>
 * <p><b>Example:</b><br><code>data = {'<', 't', 'a', 'g', '>'}<br>
 * length = { '5' }<br>lengthOfLenght = { '1' }</code></p>
 * 
 */
public class NetPacketWriter {
	private OutputStream out;

	/**
	 * <p>
	 * <code>public NetPacketWriter(OutputStream out)</code>
	 * </p>
	 * <p>
	 * Constructs a {@link NetPacketWriter} which will write {@link NetPacket}s
	 * to the specified {@link OutputStream}.
	 * </p>
	 * 
	 * @param out
	 *            the output stream to wrap the {@link NetPacketWriter} around.
	 */
	public NetPacketWriter(OutputStream out) {
		this.out = out;
	}

	/**
	 * <p>
	 * <code>public void writePacket(NetPacket packet) throws IOException</code>
	 * </p>
	 * <p>
	 * Writes the specified {@link NetPacket} to the output stream and flushes
	 * it.
	 * </p>
	 * 
	 * @param packet
	 *            the packet to write
	 * @throws IOException
	 *             if a write error occurs
	 */
	public void writePacket(NetPacket packet) throws IOException {
		byte[] data = packet.getBytes();
		byte[] len = Integer.toString(data.length).getBytes();
		byte[] lengthOfLen = Integer.toString(len.length).getBytes();

		if (lengthOfLen.length > 2) {
			throw new IOException("Can't write packet. Packet is too big!");
		}

		// Add leading zero if length of lenght is one.
		if (lengthOfLen.length == 1) {
			lengthOfLen = new byte[] { '0', lengthOfLen[0] };
		}

		out.write(lengthOfLen);
		out.write(len);
		out.write(data);
		out.flush();
	}
}
