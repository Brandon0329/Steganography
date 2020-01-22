import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

public final class LSBSteg implements Steganography {
    private static final int[] ONE_BIT_MASKS = {0xFFFEFFFF, 0xFFFFFEFF, 0xFFFFFFFE};
    private static final int[] TWO_BIT_MASKS = {0xFFFCFFFF, 0xFFFFFCFF, 0xFFFFFFFC};
    private static final int USED_BYTES	     = 33;
    private static final int MAGIC_NUM       = 0x53544547;

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
    
    private byte[] getByteArrayFromInt(int num) {
	byte[] arr = new byte[4];
	int bitmask = 0xFF;
	arr[0] = (byte) ((num >>> 24) & bitmask);
	arr[1] = (byte) ((num >>> 16) & bitmask);
	arr[2] = (byte) ((num >>> 8)  & bitmask);
	arr[3] = (byte) ( num		  & bitmask);
	return arr;
    }

    private int writeByte(BufferedImage img, byte b, int bitsPerByte, int offset) {
	int x = getX(img, offset);
	int y = getY(img, offset);
	int pixel = img.getRGB(x, y);
	int bitmask = bitsPerByte == 1 ? 0x1 : 0x3;
	int[] pixelmasks = bitsPerByte == 1 ? ONE_BIT_MASKS : TWO_BIT_MASKS;
	for(int i = 8 - bitsPerByte; i >= 0; i -= bitsPerByte) {
		int bits = (b >>> i) & bitmask;
		int index = offset % 3;
		pixel = (pixel & pixelmasks[index]) | (bits << ((2 - index) * 8));
		if(index == 2) {
			img.setRGB(x, y, pixel);
			x = getX(img, offset + 1);
			y = getY(img, offset + 1);
			pixel = img.getRGB(x, y);
		}
		++offset;
	}
	img.setRGB(x, y, pixel);
	return offset;
    }
    
    private void writeInteger(BufferedImage img, int num, int offset) {
	for(byte b: getByteArrayFromInt(num))
		offset = writeByte(img, b, 2, offset);
    }
    
    private void writeBitsPerByteVal(BufferedImage img, int bitsPerByte, int offset) {
    	int pixel = img.getRGB(getX(img, offset), getY(img, offset));
    	int index = offset % 3;
    	pixel = (pixel & TWO_BIT_MASKS[index]) | (bitsPerByte << ((2 - index) * 8));
    	img.setRGB(getX(img, offset), getY(img, offset), pixel);
    }
    
    private void writeMessage(BufferedImage img, byte[] message, int bitsPerByte, int offset) {
	for(byte b: message)
		offset = writeByte(img, b, bitsPerByte, offset);
    }
    
    private int readByte(BufferedImage img, int bitsPerByte, int offset) {
    	int bite = 0;
    	int x = getX(img, offset);
    	int y = getY(img, offset);
    	int pixel = img.getRGB(x, y);
    	int[] pixelmasks = bitsPerByte == 1 ? ONE_BIT_MASKS : TWO_BIT_MASKS;
    	for(int shift = 8 - bitsPerByte; shift >= 0; shift -= bitsPerByte) {
    		int index = offset % 3;
    		int bits = (pixel & ~pixelmasks[index]) >>> ((2 - index) * 8);
			bite |= bits << shift;
			if(index == 2) {
				x = getX(img, offset + 1);
				y = getY(img, offset + 1);
				pixel = img.getRGB(x, y);
			}
			++offset;
    	}
    	return bite;
    }
    
    private int readInteger(BufferedImage img, int offset) {
    	int num = 0;
    	for(int i = 24; i >= 0; i -= 8) {
    		num |= readByte(img, 2, offset) << i;
    		offset += 4;
    	}
    	return num;
    }
    
    private int readBitsPerByte(BufferedImage img, int offset) {
    	int pixel = img.getRGB(getX(img, offset), getY(img, offset));
    	int index = offset % 3;
    	return (pixel & ~TWO_BIT_MASKS[index]) >>> ((2 - index) * 8);
    }
    
    private String readMessage(BufferedImage img, int len, int bitsPerByte, int offset) {
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < len; ++i) {
    		sb.append((char) readByte(img, bitsPerByte, offset));
    		offset += 8 / bitsPerByte;
    	}
    	return sb.toString();
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
    	writeBitsPerByteVal(img, bitsPerByte, 16);
    	writeInteger(img, message.length(), USED_BYTES - 16);
    	writeMessage(img, message.getBytes(), bitsPerByte, USED_BYTES);
    	ImageIO.write(img, "png", new File(destFile));
    	return img;
    }

    @Override
    public String revealMessage(File srcFile, String destFile) throws IOException {
    	BufferedImage img = ImageIO.read(srcFile);
    	if(img == null)
    		return "";
    	if(readInteger(img, 0) != MAGIC_NUM)
    		return "";
    	int bitsPerByte = readBitsPerByte(img, 16);
    	int messageLength = readInteger(img, USED_BYTES - 16);
    	return readMessage(img, messageLength, bitsPerByte, USED_BYTES);
    }
}
