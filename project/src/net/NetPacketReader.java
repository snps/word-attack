package net;

import java.io.InputStream;
import java.io.IOException;

/**
 * <p>
 * Implementation of a <i>packet reader</i> wrapper class. Works in conjunction
 * with a <i>packet writer</i> class.
 * </p>
 * <p>
 * Always reads the length of the packet payload before reading the actual
 * payload data. The length is read as a two digit number representing the
 * number of digits needed to determine the data length. After the length of the
 * length is read, the actual length number is read followed by the data.
 * </p>
 * <p>This allows for an arbitrary number of bytes to be written and read.
 * The packet size limit becomes a number with 99 digits because of the two byte
 * representation of the length.</p>
 * <p><b>Example:</b><br><code>data = {'<', 't', 'a', 'g', '>'}<br>
 * length = { '5' }<br>lengthOfLenght = { '1' }</code></p>
 * 
 */
public class NetPacketReader {
	private InputStream in;
	private int timeout;

	/**
	 * <p>
	 * <code>public NetPacketReader(InputStream in)</code>
	 * </p>
	 * <p>
	 * Constructs a {@link NetPacketReader} which will read {@link NetPacket}s
	 * from the specified {@link InputStream}.
	 * </p>
	 * 
	 * @param in
	 *            the input stream to wrap the {@link NetPacketWriter} around.
	 */
	public NetPacketReader(InputStream in) {
		this.in = in;
		this.timeout = 0;
	}

	/**
	 * <p>
	 * <code>public NetPacketReader(InputStream in)</code>
	 * </p>
	 * <p>
	 * Constructs a {@link NetPacketReader} which will read {@link NetPacket}s
	 * from the specified {@link InputStream}.
	 * </p>
	 * <p>
	 * The reader will block on read operation until a {@link NetPacket} becomes
	 * available or the specified timeout is reached. If the timeout is reached,
	 * then a {@link NetPacketReaderTimeoutException} is generated. A timeout set
	 * to 0 will cause read operations to block indefinitely.
	 * </p>
	 * 
	 * @param in
	 *            the input stream to wrap the {@link NetPacketWriter} around.
	 * @param timeout
	 *            a read operation will block until the timeout is reached.
	 */
	public NetPacketReader(InputStream in, int timeout) {
		this.in = in;
		this.timeout = timeout;
	}

	/**
	 * <p>
	 * <code>public NetPacket readPacket() throws PacketStreamTimeoutException, IOException</code>
	 * </p>
	 * <p>
	 * Reads the next {@link NetPacket} that becomes available on the input
	 * stream.
	 * <p>
	 * Blocks until a complete {@link NetPacket} has been read or the timeout
	 * has been reached. If the timeout is reached, then a
	 * {@link NetPacketReaderTimeoutException} is generated. However, if the
	 * timeout has been set to 0 the method will block indefinitely.
	 * </p>
	 * </p>
	 * 
	 * @return a {@link NetPacket}
	 * @throws NetPacketReaderTimeoutException
	 *             if the timeout is reached.
	 * @throws NetPacketReaderTimeoutException
	 *             if the timeout is reached.
	 * @throws IOException
	 *             if a read error occurs.
	 * 
	 */
	public NetPacket readPacket() throws NetPacketReaderTimeoutException, IOException {
		// Wait for data to become available
		if (timeout > 0) {
			long startTime = System.currentTimeMillis();
			while (in.available() == 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					throw new IOException();
				}
				if (System.currentTimeMillis() > startTime + timeout) {
					throw new NetPacketReaderTimeoutException();
				}
			}
		}

		// Read length of length bytes.
		byte[] lenBytes1 = readBytesFromStream(2);
		// Get integer length of length.
		int lengthOfLength = Integer.parseInt(new String(lenBytes1));

		// Read length bytes.
		byte[] lenBytes2 = readBytesFromStream(lengthOfLength);
		// Get integer length.
		int length = Integer.parseInt(new String(lenBytes2));

		// Read data
		byte[] data = readBytesFromStream(length);

		return new NetPacket(data);
	}

	/**
	 * <p>
	 * <code>private byte[] readBytesFromStream(int nbrOfBytes) throws IOException</code>
	 * </p>
	 * <p>
	 * Reads a specified number of bytes from the wrapped input stream.
	 * </p>
	 * 
	 * @param nbrOfBytes
	 *            the number of bytes to read from stream.
	 * @return a byte array containing the bytes read.
	 * @throws IOException
	 *             if a read error occurs.
	 */
	private byte[] readBytesFromStream(int nbrOfBytes) throws IOException {
		int bytesRead = 0;
		byte[] bytes = new byte[nbrOfBytes];
		while (bytesRead < nbrOfBytes) {
			bytesRead += in.read(bytes, bytesRead, nbrOfBytes - bytesRead);
		}

		return bytes;
	}
}
