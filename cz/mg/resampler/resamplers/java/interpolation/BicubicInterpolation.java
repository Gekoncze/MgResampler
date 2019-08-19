package cz.mg.resampler.resamplers.java.interpolation;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


/**
 *  Source of equation (no code): https://en.wikipedia.org/wiki/Bicubic_interpolation
 */
public class BicubicInterpolation implements Interpolation {
    @Override
    public void compute(Image image, float x, float y, ColorRgba result) {
        int ix1 = (int) x;
        int iy1 = (int) y;
        int ix2 = ix1 + 1;
        int iy2 = iy1 + 1;

        int cx0 = ix1 - 1;
        int cx1 = ix1;
        int cx2 = ix2;
        int cx3 = ix2 + 1;
        int cy0 = iy1 - 1;
        int cy1 = iy1;
        int cy2 = iy2;
        int cy3 = iy2 + 1;
        
        float tx = x - ix1;
        float ty = y - iy1;

        result.set(mix2D(
                image.getReadColor(cx0, cy0), image.getReadColor(cx1, cy0), image.getReadColor(cx2, cy0), image.getReadColor(cx3, cy0),
                image.getReadColor(cx0, cy1), image.getReadColor(cx1, cy1), image.getReadColor(cx2, cy1), image.getReadColor(cx3, cy1),
                image.getReadColor(cx0, cy2), image.getReadColor(cx1, cy2), image.getReadColor(cx2, cy2), image.getReadColor(cx3, cy2),
                image.getReadColor(cx0, cy3), image.getReadColor(cx1, cy3), image.getReadColor(cx2, cy3), image.getReadColor(cx3, cy3),
                tx, ty
        ));
    }
    
    private ColorRgba mix2D(
            ColorRgba c00, ColorRgba c10, ColorRgba c20, ColorRgba c30,
            ColorRgba c01, ColorRgba c11, ColorRgba c21, ColorRgba c31,
            ColorRgba c02, ColorRgba c12, ColorRgba c22, ColorRgba c32,
            ColorRgba c03, ColorRgba c13, ColorRgba c23, ColorRgba c33,
            float tx, float ty
    ) {
        return mix1D(
                mix1D(c00, c10, c20, c30, tx),
                mix1D(c01, c11, c21, c31, tx),
                mix1D(c02, c12, c22, c32, tx),
                mix1D(c03, c13, c23, c33, tx),
                ty
        );
    }
    
    private ColorRgba mix1D(ColorRgba pa, ColorRgba a, ColorRgba b, ColorRgba pb, float t) {
        return new ColorRgba(
                mix1V(pa.r, a.r, b.r, pb.r, t),
                mix1V(pa.g, a.g, b.g, pb.g, t),
                mix1V(pa.b, a.b, b.b, pb.b, t),
                mix1V(pa.a, a.a, b.a, pb.a, t)
        );
    }
    
    private float mix1V(float pa, float a, float b, float pb, float t) {
        return a + 0.5f*t*(b - pa + t*(2.0f*pa - 5.0f*a + 4.0f*b - pb + t*(3.0f*(a - b) + pb - pa)));
    }

    @Override
    public String toString() {
        return "Bicubic";
    }
}
