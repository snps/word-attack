package view;

import java.util.Observable;

import enemy.Enemy;

/**
 * <p>
 * Abstract superclass for the <i>graphical user interface</i>. All Gui
 * implementations must inherit from this.
 * </p>
 */
public abstract class Gui extends Observable {
	/**
	 * <p>
	 * <code>public void createGui()</code>
	 * </p>
	 * <p>
	 * Creates and displays the Gui on screen.
	 * </p>
	 */
	public abstract void createGui();

	/**
	 * <p>
	 * <code>public String getPlayerInput()</code>
	 * </p>
	 * <p>
	 * Get current player input from Gui.
	 * </p>
	 * 
	 * @return the player input text as a String.
	 */
	public abstract String getPlayerInput();

	/**
	 * <p>
	 * <code>public void moveEnemies()</code>
	 * </p>
	 * <p>
	 * Move all enemies known to Gui.
	 * </p>
	 */
	public abstract void moveEnemies();

	/**
	 * <p>
	 * <code>public void addEnemy(Enemy enemy)</code>
	 * </p>
	 * Add the specified enemy to Gui.
	 * 
	 * @param enemy
	 *            the enemy to add.
	 */
	public abstract void addEnemy(Enemy enemy);

	/**
	 * <p>
	 * <code>public void removeEnemy(Enemy enemy)</code>
	 * </p>
	 * Remove the specified enemy from Gui.
	 * 
	 * @param enemy
	 *            the enemy to remove.
	 */
	public abstract void removeEnemy(Enemy enemy);

	/**
	 * <p>
	 * <code>public void showMessage(String text)</code>
	 * </p>
	 * Show a message dialog in the Gui with the specified text.
	 * 
	 * @param message
	 *            the message to show.
	 */
	public abstract void showMessage(String message);
}
