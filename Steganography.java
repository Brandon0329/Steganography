import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface Steganography {
    public BufferedImage hideMessage(String message, File filePath, String destFile) throws IOException;
    public String revealMessage(File filePath, String destFile) throws IOException;
}