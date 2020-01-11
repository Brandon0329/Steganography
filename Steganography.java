import java.io.IOException;

public interface Steganography {
    public boolean hideMessage(String message, String filePath, String destFile) throws IOException;
    public String revealMessage(String filePath, String destFile) throws IOException;
}