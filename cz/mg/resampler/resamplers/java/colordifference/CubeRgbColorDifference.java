package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import static java.lang.Math.sqrt;


public class CubeRgbColorDifference implements SolidColorDifference {
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        float maxDistance = (float) sqrt(3.0f);
        return distance(a, b) / maxDistance;
    }
    
    private float distance(ColorRgba a, ColorRgba b){
        float dr = a.r - b.r;
        float dg = a.g - b.g;
        float db = a.b - b.b;
        return (float) sqrt(dr*dr + dg*dg + db*db);
    }
    
    @Override
    public String toString() {
        return "Cube RGB";
    }
}
