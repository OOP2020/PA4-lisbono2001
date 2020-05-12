package application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ranking.Connection;
import ranking.Player;
import ranking.SheetsController;

public class MenuPage extends Application {
	public static Stage stage;
	public Player currentplayer;
	AnimationTimer aniTimer;
	double starttime;
	Timeline timeline;
	int usetime;
	Text starting = new Text("game start in 3 seconds");
	Pane root;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		root = initialComponents();
		Scene scene = new Scene(root, 600, 400);
		stage.setTitle("Typing Game");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public Pane initialComponents() {
		FlowPane pane = new FlowPane();
		Label name = new Label("Enter your name");
		Label label = new Label();
		Button button = new Button("Submit");
		TextField text = new TextField();
		text.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER && !text.getText().isEmpty()) {
				label.setText("The name you entered is " + text.getText());
				Player.currentPlayer = new Player(text.getText(), 0, 0);
				checkconection();
			}
		});
		button.setOnAction(e -> {
			if (text.getText().isEmpty())
				label.setText("Please enter your name");
			else {
				label.setText("The name you entered is " + text.getText());
				Player.currentPlayer = new Player(text.getText(), 0, 0);
					checkconection();
			}
		});
		pane.getChildren().addAll(name, text, button, label);
		pane.setPadding(new Insets(20));
		pane.setVgap(10);
		pane.setHgap(10);
		return pane;
	}

	public void checkconection() {
		if (!Connection.checker())
			noconnection();
		else {
			haveconnection();
		}
	}

	public void noconnection() {
		root.getChildren().clear();
		VBox newroot = new VBox();
		Text letter = new Text("No internet connection");
		letter.setFont(Font.font(50));
		Text letter2 = new Text("playing single player mode");
		letter2.setFont(Font.font(48));
		starting.setFont(Font.font(48));

		updateScreen();

		newroot.getChildren().addAll(letter, letter2, starting);
		newroot.setAlignment(Pos.CENTER);
		newroot.setPrefWidth(400);
		root.getChildren().add(newroot);
	}

	public void haveconnection() {
		root.getChildren().clear();
		HBox newroot = new HBox();
		starting.setFont(Font.font(50));
		starttime = System.nanoTime();

		Thread thread1 = new Thread() {
			public void run() {
				updateScreen();
			}
		};

		Thread thread2 = new Thread() {
			public void run() {
				try {
					SheetsController.read();
				} catch (IOException | GeneralSecurityException e) {
					System.out.println("Error with Java IO or GoogleSecurity");
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

		newroot.getChildren().add(starting);
		newroot.setAlignment(Pos.CENTER);
		newroot.setPrefWidth(400);
		root.getChildren().add(newroot);
	}

	public void updateScreen() {
		KeyFrame keyframe = new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				usetime = (int) ((System.nanoTime() - starttime) / 1000000000.0);
				if (usetime <= 3) {
					starting.setText(String.format("game start in %d seconds", (3 - usetime)));
				} else {
					timeline.stop();
					stage.setScene(GamePage.getScene());
				}
			}
		});

		timeline = new Timeline(keyframe, new KeyFrame(Duration.seconds(1)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public static void main(String[] args) {
		launch(args);
	}
}