package application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ranking.Connection;
import ranking.Player;
import ranking.PlayersKeeper;
import ranking.SheetsController;
import words.WordBlock;
import words.WordsKeeper;

public class GamePage extends Application {
	// initial List of dropping words
	static List<WordBlock> droppingwords = new ArrayList<WordBlock>();
	// initial an AnimationTimer
	static AnimationTimer aniTimer;
	static // initail a Pane
	Pane root = new Pane();
	Scene scene;
	// initial WordsKeeper
	static WordsKeeper keeper = new WordsKeeper();

	// initial startTime and usedtime
	static double startTime = System.nanoTime();
	static double usedtime;
	public static int currentscore;

	// set speed
	static double speed = 2;
	static double falling = 500d;

	// initial current Node that using
	private static WordBlock currentNode;

	// initial lables
	public static Label accuracy;
	public static Label missed;
	public static Label score;
	public static Label highscore;
	public static Timeline timeline;

	static boolean connection = Connection.checker();

	/**
	 * 
	 */
	@Override
	public void start(Stage primaryStage) {
		scene = startgame();
		// set title name
		primaryStage.setTitle("Typing game");
		// set scene
		primaryStage.setScene(scene);
		// fix window size
		primaryStage.setResizable(false);
		// show the stage
		primaryStage.show();
	}

	public static Scene getScene() {
		return startgame();
	}

	public static Scene startgame() {
		accuracy = new Label("Accuracy : 0.00");
		missed = new Label("Missed : 0/10");
		score = new Label("Score : 0");

		// Create new Timeline for creating Nodes of WordBlock over specific time
		timeline = new Timeline(new KeyFrame(Duration.millis(2500), generateWords -> {
			speed += falling / 30000;
			droppingwords.add(word());
			Node justAddedword = (Node) droppingwords.get(droppingwords.size() - 1);
			root.getChildren().add(justAddedword);
		}));
		// set cycle times
		timeline.setCycleCount(1000);
		// start the timeline
		timeline.play();

		// set layout of accuracy and missed Label
		accuracy.setLayoutX(10);
		accuracy.setLayoutY(10);
		missed.setLayoutX(10);
		missed.setLayoutY(40);
		score.setLayoutX(10);
		score.setLayoutY(70);

		// add accuracy and missed to the root(Line: 26)
		root.getChildren().addAll(accuracy, missed, score);
		if (connection == true)
			root.getChildren().add(highscore());

		// set value of the WordsKeeper to words of a text file
		keeper.setText("SampleWords.txt");
		// create new AnimationTimer and Override the handle method to updateScreen
		try {
			aniTimer = new AnimationTimer() {
				public void handle(long arg0) {
					updateScreen();
				}
			};
			aniTimer.start();
			// catch exception
		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root, 600, 400);
		// set event when any key pressed on the scene
		scene.setOnKeyPressed(e -> inputKey(e.getCode().toString()));

		return scene;
	}

	public static Label highscore() {
		highscore = new Label();
		highscore.setText("HighScore : " + PlayersKeeper.getHighscore());
		highscore.setLayoutX(10);
		highscore.setLayoutY(100);
		return highscore;
	}

	public static void updateScreen() {

		for (int i = 0; i < droppingwords.size(); i++) {
			// set current Node to first element of droppingwords List (oldest WordBlock
			// alive)
			// for makeing more safe I put currentNode and update label to this for loop
			currentNode = droppingwords.get(0);

			if (Counter.correct > 0 || Counter.incorrect > 0) {
				double acc = ((double) Counter.correct / ((double) Counter.correct + (double) Counter.incorrect));
				accuracy.setText(String.format("Accuracy : %.2f %%", acc * 100));
			}
			missed.setText(String.format("Missed : %d/10", Counter.miss));
			currentscore = Counter.correct * 10 + Counter.complete * 100;
			score.setText(String.format("Score : %d", currentscore));

			((WordBlock) droppingwords.get(i)).setLayoutY(((WordBlock) droppingwords.get(i)).getLayoutY() + speed
					+ ((WordBlock) droppingwords.get(i)).getLayoutY() / 1000);

			// when end the screen
			if (((WordBlock) droppingwords.get(i)).getLayoutY() >= 400) {
				// remove from scene
				root.getChildren().remove(droppingwords.get(i));
				// remove from list
				droppingwords.remove(droppingwords.get(i));
				Counter.miss++;
				if (Counter.miss >= 10)
					gameover();

			}
		}
	}

	public static void gameover() {

		Thread thread1 = new Thread() {
			public void start() {
				timeline.stop();
				aniTimer.stop();
				root.getChildren().clear();
				root.getChildren().add(gameoverComponent());
				Player.currentPlayer.setScore(currentscore);
				Player.currentPlayer.setAccuracy(
						(double) Counter.correct / ((double) Counter.correct + (double) Counter.incorrect));
			}
		};

		Thread thread2 = new Thread() {
			public void start() {
				if (connection == true) {
					try {
						SheetsController.append(Player.currentPlayer);
						SheetsController.read();
					} catch (IOException | GeneralSecurityException e) {
						System.out.println("Append error occured");
					}
				}
			}
		};
		
		thread1.start();
		thread2.start();
		
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			System.out.println("Threads interrupted");
		}
	}

	public static Pane gameoverComponent() {
		HBox x = new HBox();
		Text letter = new Text("Game Over");
		letter.setFont(Font.font(108));
		letter.setFill(Color.RED);
		x.getChildren().add(letter);
		x.setAlignment(Pos.CENTER);
		return x;
	}

	public static WordBlock word() {
		WordBlock word = new WordBlock(keeper.getRandomNextWord());
		word.setLayoutX(rand(50, 350));
		word.setLayoutY(30);
		return word;
	}

	/**
	 * @param min minimum
	 * @param max maximum
	 * @return a random number
	 */
	public static int rand(int min, int max) {
		return (int) (Math.random() * max + min);
	}

	/**
	 * method tha use when any key pressed on scene controls game mechanism
	 * 
	 * @param letter : input key that pressed on scene
	 */
	private static void inputKey(String letter) {
		// go to method for Wordblock
		try {
			currentNode.handleKeyPress(letter);
		} catch (Exception e) {
			// make sure that currentNode is right (just in case)
			currentNode = droppingwords.get(0);
		}

		// when complete type a word
		if (currentNode.isFinished()) {
			// and a
			Counter.complete++;
			// when complete remove the current Node from droppingwords List
			droppingwords.remove(currentNode);
		}
	}
}
