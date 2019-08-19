package cz.mg.resampler.gui.utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;


public class ImageUtilities {
    public static BufferedImage load(Class location, String name) throws IOException {
        InputStream stream = location.getResourceAsStream(name);
        if(stream == null) throw new IOException("Could not find image '" + name + "' in " + location.getPackage().getName());
        BufferedImage image = ImageIO.read(stream);
        if(image == null) throw new IOException("Invalid image file.");
        return image;
    }
    
    public static BufferedImage load(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        if(image == null) throw new IOException("Invalid image file.");
        return image;
    }
    
    public static BufferedImage loadWithRuntimeException(Class location, String name) {
        try {
            return load(location, name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void save(OutputStream stream, BufferedImage image, ImageFileFormat format) throws IOException {
        ImageIO.write(image, format.toString(), stream);
    }
    
    public static enum ImageFileFormat {
        PNG,
        JPG,
        BMP;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
        
        public static ImageFileFormat fromString(String string){
            string = string.toUpperCase();
            switch(string){
                case "PNG": return PNG;
                case "JPG": return JPG;
                case "BMP": return BMP;
                default: throw new RuntimeException();
            }
        }
    }
}
