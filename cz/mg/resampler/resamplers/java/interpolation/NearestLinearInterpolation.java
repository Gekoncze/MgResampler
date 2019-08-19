package cz.mg.resampler.resamplers.java.interpolation;

import cz.mg.resampler.resamplers.java.colordifference.ColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.FastRgbColorDifference;
import cz.mg.resampler.resamplers.java.other.SmoothStep;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.Parameter;


public class NearestLinearInterpolation implements Interpolation {
    private ColorDifference colorDifference = new FastRgbColorDifference();

    public NearestLinearInterpolation() {
    }

    public NearestLinearInterpolation(ColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }
    
    @Parameter(order = 0)
    public ColorDifference getColorDifference() {
        return colorDifference;
    }

    @Parameter(order = 0)
    public void setColorDifference(ColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }
    
    @Override
    public void compute(Image image, float x, float y, ColorRgba result) {
        int ix1 = (int) x;
        int iy1 = (int) y;
        int ix2 = ix1 + 1;
        int iy2 = iy1 + 1;

        result.set(mix2D(
            image.getReadColor(ix1, iy1), image.getReadColor(ix2, iy1),
            image.getReadColor(ix1, iy2), image.getReadColor(ix2, iy2),
            x - ix1, y - iy1
        ));
    }

    private ColorRgba mix2D(ColorRgba topLeft, ColorRgba topRight, ColorRgba bottomLeft, ColorRgba bottomRight, float tx, float ty){
        ColorRgba top = mix1D(topLeft, topRight, tx);
        ColorRgba bottom = mix1D(bottomLeft, bottomRight, tx);
        return mix1D(top, bottom, ty);
    }
    
    private ColorRgba mix1D(ColorRgba a, ColorRgba b, float t){
        float difference = colorDifference.compute(a, b);
        return mix(a, b, mixChange(t, difference));
    }
    
    private static ColorRgba mix(ColorRgba a, ColorRgba b, float t){
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
    
    private float mixChange(float t, float difference){
        return SmoothStep.compute(t, 1.0f - difference);
    }

    @Override
    public String toString() {
        return "Nearest linear";
    }
}
