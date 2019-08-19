package cz.mg.resampler.resamplers.java.greyscale;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


public interface Greyscale {
    public float compute(float r, float g, float b, float a);
    
    public default float compute(ColorRgba color){
        return compute(color.r, color.g, color.b, color.a);
    }
}
