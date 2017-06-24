/**
 * Created by noyz on 6/23/17.
 */

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.URL;

public class ImageFunctions {
    public static byte[] bufferedImageToByteArray(
            BufferedImage data) {
        byte[] bytes=null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(data, "jpg", baos);
            bytes = baos.toByteArray();
            return bytes;
            //oos.writeObject(bytes);
        } catch (IOException e) {
            System.out.println("problem while converting image to byte array00");
            e.printStackTrace();
        }
        return bytes;
    }

    public static BufferedImage ByteArrayToBufferedImage(
            byte[] byteArray)  {
        if(byteArray==null) return null;
        BufferedImage image=null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    byteArray);
             image = ImageIO.read(bais);
            return image;
        }catch (IOException e){
            System.out.println("problem while converting byte array to buffered Image");
            e.printStackTrace();
        }
        return image;
    }
}
