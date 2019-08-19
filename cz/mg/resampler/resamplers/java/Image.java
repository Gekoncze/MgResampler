package cz.mg.resampler.resamplers.java;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.utilities.ImageSizeException;
import cz.mg.resampler.resamplers.java.utilities.IntegerRegion;

/**
 *  Java implementation of resampler raster image.
 */
public class Image implements cz.mg.resampler.Image {
    public static final ColorRgba SINK = new ColorRgba();
    
    private final ColorRgba[][] data;
    private final int width, height;
    private final int x, y, w, h;
    private final IntegerRegion writeableRegion;
    private boolean clampRead = false;
    
    public Image(int width, int height) throws Exception {
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
        this.w = width;
        this.h = height;
        writeableRegion = new IntegerRegion(0, 0, width, height);
        try {
            this.data = new ColorRgba[width][height];
            for(int ix = 0; ix < width; ix++){
                for(int iy = 0; iy < height; iy++){
                    if(Thread.interrupted()) throw new InterruptedException();
                    this.data[ix][iy] = new ColorRgba();
                }
            }
        } catch(OutOfMemoryError e){
            throw new ImageSizeException("Could not create image. Not enough memory for image of size " + width + " x " + height);
        }
    }
    
    private Image(Image image, int x, int y, int w, int h, boolean clampRead) {
        this.data = image.data;
        this.width = image.width;
        this.height = image.height;
        this.x = image.x + x;
        this.y = image.y + y;
        this.w = w;
        this.h = h;
        IntegerRegion nr = new IntegerRegion(this.x, this.y, w, h);
        this.writeableRegion = IntegerRegion.intersection(image.writeableRegion, nr);
        this.clampRead = clampRead;
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    /**
     *  Gets color at given position for reading.
     *  If the color is out of bounds, the returned value will depend on the
     *  current "read image border" object.
     *  @param x: the horizontal position in the raster image
     *  @param y: the vertical position in the raster image
     *  @return color object with optimalizations for reading
     */
    public ColorRgba getReadColor(int x, int y) {
        x = x + this.x;
        y = y + this.y;
        
        if(clampRead){
            if(x < this.x) x = this.x;
            if(y < this.y) y = this.x;
            if(x >= this.x + w) x = this.x + w - 1;
            if(y >= this.y + h) y = this.y + h - 1;
        }
        
        if(x < 0) x = 0;
        if(y < 0) y = 0;
        if(x >= width) x = width - 1;
        if(y >= height) y = height - 1;
        
        return data[x][y];
    }

    /**
     *  Gets color at given position for writing.
     *  If the color is out of bounds, the returned value will depend on the
     *  current "write image border" object.
     *  @param x: the horizontal position in the raster image
     *  @param y: the vertical position in the raster image
     *  @return color object with optimalizations for writing
     */
    public ColorRgba getWriteColor(int x, int y) {
        x = x + this.x;
        y = y + this.y;
        if(!writeableRegion.isInside(x, y)) return SINK;
        return data[x][y];
    }

    @Override
    public void clear() {
        for(int ix = 0; ix < width; ix++){
            for(int iy = 0; iy < height; iy++){
                if(Thread.interrupted()) break;
                this.data[ix][iy].clear();
            }
        }
    }

    @Override
    public Image subimage(int x, int y, int w, int h, boolean clampRead) {
        return new Image(this, x, y, w, h, clampRead);
    }

    @Override
    public Image subimage(int x, int y, int w, int h) {
        return new Image(this, x, y, w, h, false);
    }
}
