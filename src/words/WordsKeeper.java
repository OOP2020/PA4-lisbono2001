package words;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordsKeeper {
	public String[] keeper;

	public String getRandomNextWord() {
		int random = new Random().nextInt(300);
		return keeper[random];
	}

	public void setKeeper(String[] newwords) {
		keeper = newwords;
	}

	public void setText(String filename) {
		List<String> words = new ArrayList<String>();
		File inputfile = new File("SampleWords.txt");
		try {
			Scanner scanner = new Scanner(inputfile);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Not found");
		}
		keeper = (words.toArray(new String[0]));
	}
}