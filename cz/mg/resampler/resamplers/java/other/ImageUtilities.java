package cz.mg.resampler.resamplers.java.other;

import cz.mg.resampler.resamplers.java.Image;


public class ImageUtilities {
    public static void copy(Image src, Image dst) throws InterruptedException {
        int w = Math.min(src.getWidth(), dst.getWidth());
        int h = Math.min(src.getHeight(), dst.getHeight());
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                if(Thread.interrupted()) throw new InterruptedException();
                dst.getWriteColor(x, y).set(src.getReadColor(x, y));
            }
        }
    }
}
