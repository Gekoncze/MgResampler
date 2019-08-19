package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.other.ColorHsla;
import cz.mg.resampler.resamplers.java.other.RgbaToHsla;
import cz.mg.resampler.resamplers.java.other.Vector3f;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import static java.lang.Math.PI;


public class SphereHslColorDifference implements SolidColorDifference {
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        return Vector3f.distance(
                hslToSpherePosition(RgbaToHsla.rgbaToHsla(a)),
                hslToSpherePosition(RgbaToHsla.rgbaToHsla(b))
        );
    }
    
    private Vector3f hslToSpherePosition(ColorHsla c){
        float x = c.l * 2 - 1;
        float sphere = (float) Math.sqrt(Math.abs(1.0f-x*x));
        return new Vector3f(
            sphere * c.s * (float)Math.cos(c.h*PI*2.0f) / 2.0f,
            sphere * c.s * (float)Math.sin(c.h*PI*2.0f) / 2.0f,
            c.l
        );
    }
    
    private float acos(float a){
        if(a < -1) a = -1;
        if(a > 1) a = 1;
        return (float) Math.acos(a);
    }
    
    @Override
    public String toString() {
        return "Sphere HSL";
    }
}
