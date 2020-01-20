import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

public final class LSBSteg implements Steganography {
    private static final int USED_BYTES = 33;
    private static final int MAGIC_NUM  = 0x53544547;
    private static final int[] BITMASKS = {0xFFFCFFFF, 0xFFFFFCFF, 0xFFFFFFFC};

    private int bitsPerByte(BufferedImage img, int len) {
    	final int bytesAvailable = img.getWidth() * img.getHeight() * 3;
    	if(len * 8 <= bytesAvailable - USED_BYTES)
    		return 1;
    	if(len * 4 <= bytesAvailable - USED_BYTES)
    		return 2;
    	return 0;
    }
     
    private int getX(BufferedImage img, int offset) {
    	return (offset % (img.getWidth() * 3)) / 3;
    }
    
    private int getY(BufferedImage img, int offset) {
    	return offset / (img.getWidth() * 3);
    }
    
    private void writeInteger(BufferedImage img, int num, int offset) {
    	int x = getX(img, offset);
    	int y = getY(img, offset);
    	int pixel = img.getRGB(x, y);
    	for(int i = 30; i <= 0; i -= 2) {
    		int bits = (num >>> i) & 0x3;
    		int index = offset % 3;
    		pixel = (pixel & BITMASKS[index]) | (bits << ((2 - index) * 8));
    		++offset;
    		if(offset % 3 == 0) {
    			img.setRGB(x, y, pixel);
    			x = getX(img, offset);
    			y = getY(img, offset);
    			pixel = img.getRGB(x, y);
    		}
    	}
    	img.setRGB(x, y, pixel);
    }
    
    private void writeMessage(BufferedImage img, byte[] message, int offset) {
    	return;
    }
    
    @Override
    public BufferedImage hideMessage(String message, File srcFile, String destFile) throws IOException {
    	BufferedImage img = ImageIO.read(srcFile);
    	if(img == null)
    		return null;
    	int bitsPerByte = bitsPerByte(img, message.length());
    	if(bitsPerByte == 0)
    		return null;
    	writeInteger(img, MAGIC_NUM, 0);
    	// write bitsPerByte value here
    	writeInteger(img, message.length(), USED_BYTES - 16);
    	writeMessage(img, message.getBytes(), USED_BYTES);
    	ImageIO.write(img, "png", new File(destFile));
    	return img;
    }

    @Override
    public String revealMessage(File srcFile, String destFile) throws IOException {
    	return "";
    }
}