import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;;

public class StegController {
	private static final Steganography stegUtil = new LSBSteg();
	
	private StegController() {}
	
	public static BufferedImage hide(String message, File filePath, String destFile) throws IOException {
		return stegUtil.hideMessage(message, filePath, destFile);
	}
	
	public static String reveal(File filePath, String destFile) throws IOException {
		return stegUtil.revealMessage(filePath, destFile);
	}
}
