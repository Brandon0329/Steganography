import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;

public final class LSBSteg implements Steganography {
    /* Value that goes at the beginning of steganographic image. */
    private static final int MAGIC_NUM = 0x53544547;
    /* Default amount of bytes needed for preprocessing. */
    private static final int USED_BYTES = 33;
    private static final int ONE_BIT = 8;
    private static final int TWO_BITS = 4;

    /* Ensure that this class cannot be instantiated. */
    private LSBSteg() {}

    // Write magic number using 2 bits per byte, then change two bits to represent
    // how many bits per byte to hide the message in the picture with, then hide message.
    // ignoring the case where the image has a 4th byte in each pixel for the alpha value.
    private static int bitsPerByte(byte[] bytes, String message) {
        int len = message.length();
        /* Can we fit 1 bit per byte? */
        if(len * ONE_BIT <= bytes.length - USED_BYTES)
            return 1;
        /* If not 1, can we fit 2 bits per byte? */
        if(len * TWO_BITS <= bytes.length - USED_BYTES)
            return 2;
        /* Won't fit. */
        return 0;
    }

    private static void writeMessageSize(byte[] bytes, int size) {
        writeInteger(bytes, size, USED_BYTES - 16);
    }

    private static void writeMagic(byte[] bytes) {
        writeInteger(bytes, MAGIC_NUM, 0);
    }

    private static void writeInteger(byte[] bytes, int val, int offset) {
        int bitmask = 0xC0000000;
        for(int i = 1; i <= 16; ++i) {
            byte currByte = bytes[i - 1 + offset];
            bytes[i + offset] = (byte) ((val & bitmask) >>> (32 - 2 * i)) | currByte; // confusing, fix later
            bitmask = bitmask >>> 2;
        }
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
        byte[] byteBuf = buf.getData();
        byte[] bytes = new byte[byteBuf.length];
        System.arraycopy(byteBuf, 0, bytes, 0, byteBuf.length);
        return bytes; 
    }

    // Use ImageIO.write() to create steganographic image from new buffer
    @Override
    public static boolean hideMessage(String message, String srcFile, String destFile) throws IOException {
        BufferedImage img = ImageIO.read(new File(srcFile));
        byte[] bytes = getBytes(img);
        int bits = bitsPerByte(bytes, message);
        if(bits <= 0)
            return false;
        writeMagic(bytes);
        // write bits value here
        writeMessageSize(bytes, message.length());
        return true;
    }

    @Override
    public static String revealMessage(String srcFile, String destFile) throws IOException {
        return "";
    }
}