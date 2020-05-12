package ranking;

import ranking.PlayersKeeper;

public class Player {
	public static Player currentPlayer;

	private String name;
	private int score;
	private double accuracy;

	public Player(String name, int score, double accuracy) {
		this.name = name;
		this.setScore(score);
		this.accuracy = accuracy;
	}

	public String getName() {
		return this.name;
	}

	public int getScore() {
		return this.score;
	}

	public double getAccuracy() {
		return this.accuracy;
	}

	public void setScore(int highscore) {
		this.score = highscore;
	}

	public void setAccuracy(double acc) {
		this.accuracy = acc;
	}

	public int getHighscore() {
		return PlayersKeeper.getPlayerhighscore(this.name);
	}
}
