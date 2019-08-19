package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.other.ColorHsla;
import cz.mg.resampler.resamplers.java.other.RgbaToHsla;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;


public class WeightHslColorDifference implements SolidColorDifference {
    private float hue;
    private float saturation;
    private float lightness;

    public WeightHslColorDifference() {
        hue = 1;
        saturation = 1;
        lightness = 1;
    }

    public WeightHslColorDifference(float hue, float saturation, float lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    @Parameter(order = 1)
    public float getHue() {
        return hue;
    }

    @Parameter(order = 1)
    public void setHue(float hue) {
        if(hue > 1) hue = 1;
        if(hue < 0) hue = 0;
        this.hue = hue;
    }

    @Parameter(order = 2)
    public float getSaturation() {
        return saturation;
    }

    @Parameter(order = 2)
    public void setSaturation(float saturation) {
        if(saturation > 1) saturation = 1;
        if(saturation < 0) saturation = 0;
        this.saturation = saturation;
    }

    @Parameter(order = 3)
    public float getLightness() {
        return lightness;
    }

    @Parameter(order = 3)
    public void setLightness(float lightness) {
        if(lightness > 1) lightness = 1;
        if(lightness < 0) lightness = 0;
        this.lightness = lightness;
    }
    
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        ColorHsla hslA = RgbaToHsla.rgbaToHsla(a);
        ColorHsla hslB = RgbaToHsla.rgbaToHsla(b);
        float dh = Math.abs(hslA.h - hslB.h); if(dh > 0.5f) dh = 1.0f - dh; dh *= 2; dh *= hue;
        float ds = Math.abs(hslA.s - hslB.s) * saturation;
        float dl = Math.abs(hslA.l - hslB.l) * lightness;
        float max = hue + saturation + lightness;
        return (dh + ds + dl) / max;
    }
    
    @Override
    public String toString() {
        return "Weight HSL";
    }
}
