package net;

import java.io.IOException;

/**
 * <p>
 * Implementation of a timeout exception intended for the
 * {@link NetPacketReader}.
 * </p>
 * 
 */
public class NetPacketReaderTimeoutException extends IOException {
	private static final long serialVersionUID = 8660431700649563044L;

	/**
	 * <p>
	 * <code>public PacketStreamTimeoutException()</code>
	 * </p>
	 * <p>
	 * Constructs a new {@link NetPacketReaderTimeoutException}
	 * </p>
	 */
	public NetPacketReaderTimeoutException() {
		super();
	}
}
