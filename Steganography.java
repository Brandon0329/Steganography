
public interface Steganography {
    public static boolean hideMessage(String message, String filePath, String destFile);
    public static String revealMessage(String filePath, String destFile);
}