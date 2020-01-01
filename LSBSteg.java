import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;

public final class LSBSteg implements Steganography {
    /* Value that goes at the beginning of steganographic image. */
    private static final int MAGIC_NUM = 0x53544547;
    private static final int USED_BYTES = 33;

    /* Ensure that this class cannot be instantiated. */
    private LSBSteg() {}

    // Write magic number using 2 bits per byte, then change two bits to represent
    // how many bits per byte to hide the message in the picture with, then hide message.
    // ignoring the case where the image has a 4th byte in each pixel for the alpha value.
    private static int bitsPerByte(byte[] bytes, String message) {
        int len = message.length();
        /* Can we fit 1 bit per byte? */
        if(len * 8 <= bytes.length - USED_BYTES)
            return 1;
        /* If not 1, can we fit 2 bits per byte? */
        if(len * 4 <= bytes.length - USED_BYTES)
            return 2;
        /* Won't fit. */
        return 0;
    }

    private static boolean encodeMessageSize(byte[] bytes, int size) {
        return false;
    }

    private static boolean verifyMagicNumber(byte[] bytes) {
        return readInteger(bytes, 0) == MAGIC_NUM;
    }

    private static int readInteger(byte[] bytes, int offset) {
        return 0;
    }

    private static byte[] getBytes(BufferedImage img) {
        WritableRaster raster = img.getRaster();
        DataBufferByte buf = (DataBufferByte) raster.getDataBuffer();
        return buf.getData(); 
    }

    @Override
    public static boolean hideMessage(String message, String srcFile) throws IOException {
        BufferedImage img = ImageIO.read(new File(srcFile));
        byte[] bytes = getBytes(img);
        int bits = bitsPerByte(bytes, message);
        if(bits <= 0)
            return false;
        return true;
    }

    @Override
    public static String revealMessage(String srcFile, String destFile) throws IOException {
        return "";
    }
}