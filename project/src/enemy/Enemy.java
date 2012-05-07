package enemy;

public class Enemy {
	private String word;
	private int speed;
	private int xPos;
	private int yPos;

	public Enemy(String word, int speed, int xPos) {
		this.word = word;
		this.speed = speed;
		this.xPos = xPos;
		yPos = 0;
	}

	public String getWord() {
		return word;
	}

	public int getSpeed() {
		return speed;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void move() {
		yPos += speed;
	}

	public int getWordWidth() {
		return word.length() * 10;
	}

	public boolean equals(Enemy enemy) {
		return enemy.word.equals(this.word);
	}
}
