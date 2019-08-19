package cz.mg.resampler;

/**
 *  An interface for raster images that can be used in resamplers.
 */
public interface Image {
    /**
     *  Gets the horizontal resolution of the raster image.
     *  @return the horizontal resolution of the image
     */
    public int getWidth();
    
    /**
     *  Gets the vertical resolution of the raster image.
     *  @return the vertical resolution of the image
     */
    public int getHeight();
    
    /**
     *  Clears all color objects in the image.
     */
    public void clear();

    /**
     *  Creates a new sub-image object.
     *  The underlying image data are shared.
     *  The image region can be out of original image bounds.
     *  @param x: the x coordinate in the original image
     *  @param y: the y coordinate in the original image
     *  @param w: the width of the new sub-image
     *  @param h: the height of the new sub-image
     *  @param clampRead: specifies while retrieving color data if the coordinates should be clamped to sub-image region (default is false)
     *  @return the new sub-image object
     */
    public Image subimage(int x, int y, int w, int h, boolean clampRead);
    
    public default Image subimage(int x, int y, int w, int h){
        return subimage(x, y, w, h, false);
    }
}