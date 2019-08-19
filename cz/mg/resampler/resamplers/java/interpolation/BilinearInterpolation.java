package cz.mg.resampler.resamplers.java.interpolation;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


public class BilinearInterpolation implements Interpolation {
    @Override
    public void compute(Image image, float x, float y, ColorRgba result) {
        int ix1 = (int) x;
        int iy1 = (int) y;
        int ix2 = ix1 + 1;
        int iy2 = iy1 + 1;
        float tx = x - ix1;
        float ty = y - iy1;
        result.set(mix2D(
                image.getReadColor(ix1, iy1), image.getReadColor(ix2, iy1),
                image.getReadColor(ix1, iy2), image.getReadColor(ix2, iy2),
                tx, ty
        ));
    }
    
    private static ColorRgba mix2D(ColorRgba topLeft, ColorRgba topRight, ColorRgba bottomLeft, ColorRgba bottomRight, float tx, float ty){
        return mix1D(
                mix1D(topLeft, topRight, tx),
                mix1D(bottomLeft, bottomRight, tx),
                ty
        );
    }
    
    private static ColorRgba mix1D(ColorRgba a, ColorRgba b, float t){
        return new ColorRgba(
                mix(a.r, b.r, t),
                mix(a.g, b.g, t),
                mix(a.b, b.b, t),
                mix(a.a, b.a, t)
        );
    }
    
    private static float mix(float a, float b, float t) {
        return a*(1-t) + b*t;
    }

    @Override
    public String toString() {
        return "Bilinear";
    }
}
