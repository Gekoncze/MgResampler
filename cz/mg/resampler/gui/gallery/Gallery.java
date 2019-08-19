package cz.mg.resampler.gui.gallery;

import cz.mg.resampler.gui.utilities.ImageUtilities;
import java.awt.image.BufferedImage;


public class Gallery {
    public static final BufferedImage[] IMAGES = new BufferedImage[]{
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Cube.png"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Heart.png"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Dragon.png"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "PixelArt.gif"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Text.png"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Flower.gif"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Colors.png"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Transparency.png"),
        ImageUtilities.loadWithRuntimeException(Gallery.class, "Wall.png"),
    };
}
