import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class StegController {
	private static final Steganography theModel = new LSBSteg();
	
	private StegController() {}
	
	public static boolean hide(String message, String filePath, String destFile) throws IOException {
		return theModel.hideMessage(message, filePath, destFile);
	}
	
	public static String reveal(String filePath, String destFile) throws IOException {
		return theModel.revealMessage(filePath, destFile);
	}
}
