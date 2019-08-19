package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


public class FastRgbColorDifference implements SolidColorDifference {
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        float dr = Math.abs(a.r - b.r);
        float dg = Math.abs(a.g - b.g);
        float db = Math.abs(a.b - b.b);
        return (dr + dg + db) / 3.0f;
    }
    
    @Override
    public String toString() {
        return "Fast RGB";
    }
}
