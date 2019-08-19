package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.other.ColorHsla;
import cz.mg.resampler.resamplers.java.other.RgbaToHsla;
import cz.mg.resampler.resamplers.java.other.Vector3f;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import static java.lang.Math.PI;


public class ConeHslColorDifference implements SolidColorDifference {
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        return Vector3f.distance(
                hslToConePosition(RgbaToHsla.rgbaToHsla(a)),
                hslToConePosition(RgbaToHsla.rgbaToHsla(b))
        );
    }
    
    private Vector3f hslToConePosition(ColorHsla c){
        float cone = 1.0f - Math.abs(1.0f - 2.0f*c.l);
        return new Vector3f(
            cone * c.s * (float)Math.cos(c.h*PI*2.0f) / 2.0f,
            cone * c.s * (float)Math.sin(c.h*PI*2.0f) / 2.0f,
            c.l
        );
    }

    @Override
    public String toString() {
        return "Cone HSL";
    }
}
