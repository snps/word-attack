package view;

import enemy.Enemy;

/**
 * <p>
 * Interface for the <i>graphical user interface</i>. All Gui implementations
 * must implement this.
 * </p>
 */
public interface Gui {
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
	public String getPlayerInput();

	/**
	 * <p>
	 * <code>public void moveEnemies()</code>
	 * </p>
	 * <p>
	 * Move all enemies known to Gui.
	 * </p>
	 */
	public void moveEnemies();

	/**
	 * <p>
	 * <code>public void addEnemy(Enemy enemy)</code>
	 * </p>
	 * Add the specified enemy to Gui.
	 * 
	 * @param enemy
	 *            the enemy to add.
	 */
	public void addEnemy(Enemy enemy);

	/**
	 * <p>
	 * <code>public void removeEnemy(Enemy enemy)</code>
	 * </p>
	 * Remove the specified enemy from Gui.
	 * 
	 * @param enemy
	 *            the enemy to remove.
	 */
	public void removeEnemy(Enemy enemy);

	/**
	 * <p>
	 * <code>public void showMessage(String text)</code>
	 * </p>
	 * Show a message dialog in the Gui with the specified text.
	 * 
	 * @param message
	 *            the message to show.
	 */
	public void showMessage(String message);
}
