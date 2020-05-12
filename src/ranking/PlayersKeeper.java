package ranking;

import java.util.ArrayList;

public class PlayersKeeper {
	public static ArrayList<Player> players = new ArrayList<Player>();

	public static void add(Player player) {
		players.add(player);
	}
	
	public static int getHighscore() {
		int highscore = 0;
		for (Player player : players) {
			if (player.getScore()>highscore) {
				highscore = player.getScore();
			}
		}
		return highscore;
	}
	
	public static int getPlayerhighscore(String name) {
		int highscore = 0;
		for (Player player : players) {
			if (player.getName().equals(name)) {
				if (player.getScore()>highscore) {
					highscore = player.getScore();
				}
			}
		}
		return highscore;
	}
}
