package cz.mg.resampler.resamplers.java.greyscale;

import cz.mg.resampler.resamplers.java.other.RgbaToHsla;


public class LightnessGreyscale implements Greyscale {
    @Override
    public float compute(float r, float g, float b, float a) {
        return RgbaToHsla.rgbaToHsla(r, g, b, a).l;
    }

    @Override
    public String toString() {
        return "Lightness";
    }
}
