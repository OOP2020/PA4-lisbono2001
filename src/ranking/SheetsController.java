package ranking;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * SheetsController class use to control data of Google SpreadSheets by using
 * Google API services and client
 * 
 * @author theetouchkasemarnontana
 *
 */
public class SheetsController {
	// initial Sheets object
	private static Sheets sheetsService;
	// set application name
	private static String sheetname = "Typing Game";
	// set sheets id
	private static String SPREADSHEET_ID = "1-j8qRJTLG_u9n3AWTVQ5B-N2fLM3Kz1xL0wvH88Pfv4";

	private static Credential authorize() throws IOException, GeneralSecurityException {
		// get google resources from credentials.json file
		InputStream in = SheetsController.class.getResourceAsStream("credentials.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
				new InputStreamReader(in));

		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets,
				scopes).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("token")))
						.setAccessType("offline").build();
		// put all resource to Crendential object and return it
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}

	/**
	 * 
	 * @return sheets service for controls the sheet
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		Credential credential = authorize();
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
				credential).setApplicationName(sheetname).build();
	}

	/**
	 * Read the sheet data to PlayerKeeper static variable
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static void read() throws IOException, GeneralSecurityException {
		sheetsService = getSheetsService();
		String range = "A2:D";

		// get resource from the specific spreadsheets ID and create a list to store its
		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();

		List<List<Object>> values = response.getValues();

		if (values == null || response.isEmpty()) {
			System.out.println("No data found.");
		} else {
			for (List row : values) {
				String name = (String) row.get(0);
				int score = Integer.parseInt((String) row.get(1));
				double accuracy = Double.parseDouble((String) row.get(2));

				PlayersKeeper.add(new Player(name, score, accuracy));
			}

		}
	}

	/**
	 * Append Player data to the latest row of the sheet
	 * 
	 * @param player
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static void append(Player player) throws IOException, GeneralSecurityException {
		sheetsService = getSheetsService();
		String range = "A2:E";

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String strDate = formatter.format(date);

		ValueRange append = new ValueRange().setValues(
				Arrays.asList(Arrays.asList(player.getName(), player.getScore(), player.getAccuracy(), strDate)));

		AppendValuesResponse apendResult = sheetsService.spreadsheets().values().append(SPREADSHEET_ID, range, append)
				.setValueInputOption("USER_ENTERED").setInsertDataOption("INSERT_ROWS").setIncludeValuesInResponse(true)
				.execute();
	}
}
