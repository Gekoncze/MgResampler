package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


public interface ColorDifference {
    public float compute(ColorRgba a, ColorRgba b);
}
