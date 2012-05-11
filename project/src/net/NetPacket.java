package net;

/**
 * <p>
 * Implementation of a carrier packet which holds XML data.
 * </p>
 * <p>
 * Net packets are used to send data over a network connection.
 * </p>
 * 
 */
public class NetPacket {
	public static enum Type {
		ACKNOWLEDGE, NEW_PLAYER, REMOVE_PLAYER, START_GAME, PLAYER_INPUT_UPDATE, CREATE_ENEMY, DESTROY_ENEMY, MOVE_ENEMIES, GAME_OVER, DISCONNECT_FROM_GAME, UNKNOWN
	};

	public static final String PACKET_TAG = "packet";
	public static final String TYPE_TAG = "type";
	public static final String PLAYER_INPUT_TAG = "input";
	public static final String WORD_TAG = "word";
	public static final String SPEED_TAG = "speed";
	public static final String PLAYER_NAME_TAG = "player";
	public static final String X_POS_TAG = "xpos";

	private static final String PACKET_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><" + NetPacket.PACKET_TAG + ">";
	private static final String PACKET_TAIL = "</" + NetPacket.PACKET_TAG + ">";

	private String payload;

	/**
	 * <p>
	 * <code>public NetPacket(NetPacket.Type type)</code>
	 * </p>
	 * <p>
	 * Constructs an empty {@link NetPacket} object with the specified type.
	 * </p>
	 * 
	 * @param type
	 *            the type of the {@link NetPacket}.
	 */
	public NetPacket(NetPacket.Type type) {
		// Get type text
		String typeText = type.toString();

		// Construct packet
		payload = NetPacket.PACKET_HEAD;
		payload += "<" + NetPacket.TYPE_TAG + ">" + typeText + "</" + NetPacket.TYPE_TAG + ">";
		payload += NetPacket.PACKET_TAIL;
	}

	/**
	 * <p>
	 * <code>public NetPacket(byte[] XMLData)</code>
	 * </p>
	 * <p>
	 * Constructs a {@link NetPacket} around the specified XML data.
	 * </p>
	 * 
	 * @param XMLData
	 *            the XML data
	 */
	public NetPacket(byte[] XMLData) {
		payload = new String(XMLData);
	}

	/**
	 * <p>
	 * <code>public byte[] getBytes()</code>
	 * </p>
	 * <p>
	 * Returns a byte array representation of the {@link NetPacket}
	 * <code>payload</code> data.
	 * </p>
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		return payload.getBytes();
	}

	/**
	 * <p>
	 * <code>public Type getType()</code>
	 * </p>
	 * <p>
	 * Returns the type of the {@link NetPacket}.
	 * </p>
	 * 
	 * @return the type
	 */
	public Type getType() {
		String packetType = getPacketElementContent(NetPacket.TYPE_TAG);
		NetPacket.Type returnType = NetPacket.Type.UNKNOWN;

		for (NetPacket.Type type : NetPacket.Type.values()) {
			if (type.toString().equals(packetType)) {
				returnType = type;
			}
		}

		return returnType;
	}

	/**
	 * <p>
	 * <code>public void addPacketElement(String tagName, String content)</code>
	 * </p>
	 * <p>
	 * Appends a new XML element with the specified tag name around the
	 * specified content text. The XML element is appended within the parent
	 * element specified in <code>PACKET_TAG</code>.
	 * </p>
	 * 
	 * @param tagName
	 *            the name of the element to add.
	 * @param content
	 *            the content text.
	 */
	public void addPacketElement(String tagName, String content) {
		// Get packet content
		String packetContent = NetPacket.getXMLElementContent(NetPacket.PACKET_TAG, payload);

		packetContent += "<" + tagName + ">" + content + "</" + tagName + ">";

		payload = NetPacket.PACKET_HEAD + packetContent + NetPacket.PACKET_TAIL;
	}

	/**
	 * <p>
	 * <code>public String getPacketElementContent(String tagName)</code>
	 * </p>
	 * <p>
	 * Returns the contents of the first occurrence of an element with the
	 * specified tag name from the packet XML data.
	 * </p>
	 * 
	 * @param tagName
	 *            the name of the element.
	 * @return the element contents. Returns null if the element was not found.
	 */
	public String getPacketElementContent(String tagName) {
		// Get packet content
		String packetContent = NetPacket.getXMLElementContent(NetPacket.PACKET_TAG, payload);

		// Return element content
		return NetPacket.getXMLElementContent(tagName, packetContent);
	}

	/**
	 * <p>
	 * <code>private static String getXMLElementContent(String tagName, String xmlData)</code>
	 * </p>
	 * <p>
	 * Returns the contents of the first occurrence of an element with the
	 * specified tag name from the XML data.
	 * </p>
	 * 
	 * @param tagName
	 *            the name of the element.
	 * @param xmlData
	 *            the XML data.
	 * @return the element contents. Returns null if the element was not found.
	 */
	private static String getXMLElementContent(String tagName, String xmlData) {
		// Construct start and end tags
		String startTag = "<" + tagName + ">";
		String endTag = "</" + tagName + ">";

		// Get content start and end positions
		int contentStartPos = xmlData.indexOf(startTag) + startTag.length();
		int contentEndPos = xmlData.indexOf(endTag);

		// Check if tags not found
		if (contentStartPos == -1 || contentEndPos == -1) {
			return null;
		}

		// Return content
		return xmlData.substring(contentStartPos, contentEndPos);
	}
}
