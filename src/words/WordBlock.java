package words;

import application.Counter;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WordBlock extends HBox {
	// initial letters array for WordBlock class
	private char[] letters;
	// initial correctedLetters counter
	private int correctedLetters = 0;

	// constructor
	public WordBlock(String word) {
		// change string to char array
		letters = word.toUpperCase().toCharArray();

		// add Text object to WordBlock
		for (char c : letters) {
			// create new Text
			Text letter = new Text(c + "");
			// set font size
			letter.setFont(Font.font(16));
			// add the Text to HBox (WordBlock)
			getChildren().add(letter);
		}
		// set to center
		setAlignment(Pos.CENTER);
	}

	public void handleKeyPress(String inputletter) {
		// set char c to the letter want to check
		char checkingLetter = letters[correctedLetters];

		// if input char is equal to checkingLetter
		if (inputletter.charAt(0) == checkingLetter) {
			// when typed correctly set the lettet to invisible
			getChildren().get(correctedLetters).setVisible(false);
			// count correct letters typed for this WordBlock
			correctedLetters++;
			// count correct letter typed
			Counter.correct++;
		} else {
			// count incorrect letter typed
			Counter.incorrect++;
		}
	}

	public boolean isFinished() {
		return correctedLetters == letters.length;
	}
}