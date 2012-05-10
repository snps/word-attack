package enemy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import enemy.Enemy;

public class TestEnemy {
	@Test
	public void testCreateEnemy() {
		Enemy enemy = new Enemy("Enemy", 10, 100);

		assertEquals("Enemy", enemy.getWord());
		assertEquals(10, enemy.getSpeed());
		assertEquals(100, enemy.getXPos());
		assertEquals(0, enemy.getYPos());
		assertEquals(50, enemy.getWordWidth());
	}

	@Test
	public void testMoveEnemy() {
		Enemy enemy = new Enemy("Enemy", 10, 100);

		assertEquals(0, enemy.getYPos());
		enemy.move();
		assertEquals(10, enemy.getYPos());
	}

	@Test
	public void testEnemyEquals() {
		Enemy enemy1 = new Enemy("Enemy", 10, 100);
		Enemy enemy2 = new Enemy("Enemy", 5, 50);
		Enemy enemy3 = new Enemy("Friend", 10, 100);

		assertTrue(enemy1.equals(enemy2));
		assertFalse(enemy1.equals(enemy3));
	}
	
	@Test
	public void testEnemyHashCode() {
		Enemy enemy1 = new Enemy("Enemy", 10, 100);
		int hashCode = "Enemy".hashCode();
		
		assertEquals("Wrong hash code", hashCode, enemy1.hashCode());
	}
}
