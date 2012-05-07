package client.test;

import view.Gui;

public class ServerStub extends Thread {
	private Gui gui;

	public ServerStub(Gui gui) {
		this.gui = gui;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Nothing.
			}

			gui.moveEnemies();
		}
	}
}
