package cz.mg.resampler.resamplers.java.stretch;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


public interface Stretch {
    public void compute(Image sourceImage, Image destinationImage, int dx, int dy, ColorRgba result);
}
