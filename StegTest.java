import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;

public class StegTest {
    public static void main(String[] args) throws IOException {
        BufferedImage bi = ImageIO.read(new File("JPGTest.jpeg"));
        ImageIO.write(bi, "png", new File("TestImage-1.png"));
        // BufferedImage bi = ImageIO.read(new File("TestImage.png"));
        // System.out.printf("%d %d\n", bi.getWidth(), bi.getHeight());
        // WritableRaster wr = bi.getRaster();
        // DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
        // System.out.println(dbb.getData().length); // = width * height * 4
        // for(int i = 0; i < 10; i++)
        //     System.out.print((dbb.getData()[i]) + " ");
        // System.out.println();
        // for(int i = 0; i < 10; i++)
        //     System.out.print((dbb.getData()[bi.getWidth() * (bi.getHeight() / 2) + i]) + " ");
    }
}