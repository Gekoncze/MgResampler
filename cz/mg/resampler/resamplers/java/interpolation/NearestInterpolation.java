package cz.mg.resampler.resamplers.java.interpolation;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


public class NearestInterpolation implements Interpolation {
    @Override
    public void compute(Image image, float x, float y, ColorRgba result) {
        int ix1 = (int) x;
        int iy1 = (int) y;
        int ix2 = ix1 + 1;
        int iy2 = iy1 + 1;
        float tx = x - ix1;
        float ty = y - iy1;
        int ix = tx < 0.5 ? ix1 : ix2;
        int iy = ty < 0.5 ? iy1 : iy2;
        result.set(image.getReadColor(ix, iy));
    }

    @Override
    public String toString() {
        return "Nearest";
    }
}
