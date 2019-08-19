package cz.mg.resampler.gui.utilities;

import cz.mg.resampler.Resampler;
import cz.mg.resampler.resamplers.java.utilities.ImageSizeException;
import java.awt.image.BufferedImage;


public class ImageState {
    private final java.awt.image.BufferedImage swingImage;
    private int[] integerData = null;
    private cz.mg.resampler.resamplers.java.Image javaImage = null;

    public ImageState(BufferedImage implImage) throws Exception {
        this.swingImage = implImage;
        generateJavaImage();
    }
    
    public ImageState(int width, int height) throws Exception {
        try {
            swingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        } catch(OutOfMemoryError e){
            String message = "Could not create buffered image. Not enough memory for image of size " + width + " x " + height;
            throw new ImageSizeException(message);
        }
        generateJavaImage();
    }
    
    private void generateJavaImage() throws Exception {
        if(integerData == null) generateIntegerData();
        javaImage = new cz.mg.resampler.resamplers.java.Image(getWidth(), getHeight());
        for(int i = 0; i < integerData.length; i++){
            int x = i % javaImage.getWidth();
            int y = i / javaImage.getWidth();
            ColorUtilities.set(javaImage.getWriteColor(x, y), integerData[i]);
        }
    }
    
    public cz.mg.resampler.resamplers.java.Image getJavaImage() {
        return javaImage;
    }
    
    public cz.mg.resampler.Image getResamplerImage(Resampler resampler) {
        if(resampler instanceof cz.mg.resampler.resamplers.java.Resampler){
            return javaImage;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public int getWidth() {
        return swingImage.getWidth();
    }

    public int getHeight() {
        return swingImage.getHeight();
    }

    public BufferedImage getSwingImage() {
        return swingImage;
    }

    public void updateFromIntegerData() {
        if(integerData == null) generateIntegerData();
        swingImage.setRGB(0, 0, swingImage.getWidth(), swingImage.getHeight(), integerData, 0, swingImage.getWidth());
    }
    
    public void updateFromJavaImage() {
        for(int i = 0; i < integerData.length; i++){
            int x = i % javaImage.getWidth();
            int y = i / javaImage.getWidth();
            integerData[i] = ColorUtilities.getARGB(javaImage.getReadColor(x, y));
        }
        updateFromIntegerData();
    }
    
    public void updateFromOpenglImage() {
        throw new UnsupportedOperationException();
    }
    
    public void updateFromResamplerImage(Resampler resampler) {
        if(resampler instanceof cz.mg.resampler.resamplers.java.Resampler){
            updateFromJavaImage();
        }
    }
    
    private void generateIntegerData(){
        integerData = new int[swingImage.getWidth() * swingImage.getHeight()];
        swingImage.getRGB(0, 0, swingImage.getWidth(), swingImage.getHeight(), integerData, 0, swingImage.getWidth());
    }
}
