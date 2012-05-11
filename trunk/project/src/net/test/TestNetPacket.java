package net.test;

import static org.junit.Assert.assertEquals;
import net.NetPacket;

import org.junit.Test;

public class TestNetPacket {
	@Test
	public void testCreateNetPacket() {
		NetPacket packet = new NetPacket(NetPacket.Type.MOVE_ENEMIES);

		assertEquals(NetPacket.Type.MOVE_ENEMIES, packet.getType());
	}

	@Test
	public void testAddPacketElement() {
		NetPacket packet = new NetPacket(NetPacket.Type.DESTROY_ENEMY);
		packet.addPacketElement(NetPacket.WORD_TAG, "groda");
		packet.addPacketElement(NetPacket.PLAYER_NAME_TAG, "Player 1");

		assertEquals("Element content was not correct", "groda", packet.getPacketElementContent(NetPacket.WORD_TAG));
		assertEquals("Element content was not correct", "Player 1", packet.getPacketElementContent(NetPacket.PLAYER_NAME_TAG));
	}

	@Test
	public void testGetBytes() {
		NetPacket packet = new NetPacket(NetPacket.Type.DESTROY_ENEMY);
		packet.addPacketElement(NetPacket.WORD_TAG, "groda");
		packet.addPacketElement(NetPacket.PLAYER_NAME_TAG, "Player 1");

		String correctXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><packet><type>DESTROY_ENEMY</type><word>groda</word><scoring-player>Player 1</scoring-player></packet>";

		assertEquals("Packet payload content was not correct", correctXml, new String(packet.getBytes()));
	}
}
