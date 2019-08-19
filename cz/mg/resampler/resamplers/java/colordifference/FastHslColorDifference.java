package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.other.ColorHsla;
import cz.mg.resampler.resamplers.java.other.RgbaToHsla;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


public class FastHslColorDifference implements SolidColorDifference {
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        ColorHsla hslA = RgbaToHsla.rgbaToHsla(a);
        ColorHsla hslB = RgbaToHsla.rgbaToHsla(b);
        float dh = Math.abs(hslA.h - hslB.h); if(dh > 0.5f) dh = 1.0f - dh; dh *= 2;
        float ds = Math.abs(hslA.s - hslB.s);
        float dl = Math.abs(hslA.l - hslB.l);
        return (dh + ds + dl) / 3.0f;
    }
    
    @Override
    public String toString() {
        return "Fast HSL";
    }
}
