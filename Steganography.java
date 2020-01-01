
public interface Steganography {
    public static boolean hideMessage(String message, String filePath);
    public static String revealMessage(String filePath);
}