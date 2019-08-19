package cz.mg.resampler.resamplers.java.interpolation;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


public interface Interpolation {
    public void compute(Image image, float x, float y, ColorRgba result);
}
