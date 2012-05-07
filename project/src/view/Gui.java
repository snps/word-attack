package view;

import enemy.Enemy;

public interface Gui {
	// Get current player input from Gui.
	public String getPlayerInput();

	// Move all enemies known to Gui.
	public void moveEnemies();

	// Add the specified enemy to Gui.
	public void addEnemy(Enemy enemy);

	// Remove the specified enemy from Gui.
	public void removeEnemy(Enemy enemy);

	// Show a message dialog in the Gui with the specified text.
	public void showMessage(String text);
}
