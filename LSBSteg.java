// Maybe make this class instantiable...
// And stop using "magic" numbers

import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public final class LSBSteg implements Steganography {
    /* Value that goes at the beginning of steganographic image. */
    private static final int MAGIC_NUM = 0x53544547;
    /* Default amount of bytes needed for preprocessing. */
    private static final int USED_BYTES = 33;
    private static final int ONE_BIT = 8;
    private static final int TWO_BITS = 4;

    public LSBSteg() {}

    // Write magic number using 2 bits per byte, then change two bits to represent how many bits
    // per byte to hide the message in the picture with, then write message size, then hide message.
    // ignoring the case where the image has a 4th byte in each pixel for the alpha value.
    private int bitsPerByte(byte[] bytes, String message) {
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

    // Maybe get rid of this and the next method. Unnecessary
    private void writeMessageSize(byte[] bytes, int size) {
        writeInteger(bytes, size, USED_BYTES - 16);
    }

    private void writeMagic(byte[] bytes) {
        writeInteger(bytes, MAGIC_NUM, 0);
    }

    // Optimize later
    private void writeInteger(byte[] bytes, int val, int offset) {
        for(int i = 1; i <= 16; ++i) {
            byte currByte = bytes[i - 1 + offset];
            bytes[i - 1 + offset] = (byte) ((currByte & 0xFC) | (val >>> 30));
            val <<= 2;
        }
    }

    // Optimize later
    private void writeMessage(byte[] bytes, String message, int offset, int bitsPerByte) {
        int bitmask = bitsPerByte == 2 ? 0xFC : 0xFE;
        for(byte b: message.getBytes()) {
            for(int i = 8; i > 0; i -= bitsPerByte) {
                byte currByte = bytes[offset];
                bytes[offset] =  (byte) ((currByte & bitmask) | (b >>> (8 - bitsPerByte)));
                ++offset;
                b <<= bitsPerByte;
            }
        }
    }

    private boolean verifyMagicNumber(byte[] bytes) {
        return readInteger(bytes, 0) == MAGIC_NUM;
    }

    private int readInteger(byte[] bytes, int offset) {
        int num = 0;
        for(int i = 1; i <= 16; ++i) {
            int twoBits = bytes[i - 1 + offset] & 0x3;
            num |= twoBits << (32 - i * 2);
        }
        return num;
    }

    private String readMessage(byte[] bytes, int offset, int bitsPerByte, int messageSize) {
        StringBuilder sb = new StringBuilder();
        int bitmask = bitsPerByte == 2 ? 0x3 : 0x1;
        for(int i = 0; i < messageSize; ++i) {
            int letter = 0;
            for(int j = 8; j > 0; j -= bitsPerByte) {
                int bits = bytes[offset] & bitmask;
                letter |= bits << (j - bitsPerByte);
                ++offset;
            }
            sb.append((char) letter);
        }
        return sb.toString();
    }

    private byte[] getBytes(BufferedImage img) {
        WritableRaster raster = img.getRaster();
        DataBufferByte buf = (DataBufferByte) raster.getDataBuffer();
        byte[] byteBuf = buf.getData();
        byte[] bytes = new byte[byteBuf.length];
        System.arraycopy(byteBuf, 0, bytes, 0, byteBuf.length);
        return bytes; 
    }

    // Might need to throw something here or use a try/catch block
    // Should work for now. Maybe generate random number instead of using STEG suffix
    // Also do some error checking
    private boolean createPNG(byte[] bytes, String srcFile, String destFile) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(bis);
        if(destFile == null || destFile.trim().length() == 0)
            destFile = srcFile.substring(0, srcFile.lastIndexOf('.')) + "STEG.png";
        ImageIO.write(img, "png", new File(destFile));
        return true;
    }

    @Override
    public boolean hideMessage(String message, String srcFile, String destFile) throws IOException {
    	if(message.length() == 0 || srcFile.length() == 0)
    		return false;
        BufferedImage img = ImageIO.read(new File(srcFile));
        byte[] bytes = getBytes(img);
        int bits = bitsPerByte(bytes, message);
        if(bits <= 0)
            return false;
        writeMagic(bytes);
        bytes[16] = (byte) ((bytes[16] & 0xFC) | bits);
        writeMessageSize(bytes, message.length());
        writeMessage(bytes, message, USED_BYTES, bits);
        if(!createPNG(bytes, srcFile, destFile))
            return false;
        return true;
    }

    @Override
    public String revealMessage(String srcFile, String destFile) throws IOException {
        BufferedImage img = ImageIO.read(new File(srcFile));
        byte[] bytes = getBytes(img);
        if(!verifyMagicNumber(bytes))
            return "";
        int bitsPerByte = bytes[16] & 0x3;
        int messageSize = readInteger(bytes, USED_BYTES - 16);
        String message = readMessage(bytes, USED_BYTES, bitsPerByte, messageSize);
        if(destFile != null && destFile.trim().length() > 0 && message.length() > 0) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destFile));
            bw.write(message);
            bw.close();
        }
        return message;
    }
}