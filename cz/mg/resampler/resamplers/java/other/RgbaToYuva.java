package cz.mg.resampler.resamplers.java.other;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


public class RgbaToYuva {
    // source: https://code.google.com/archive/p/hqx/source/default/source in init.c
    public static ColorYuva rgbaToYuva(ColorRgba c){
        return new ColorYuva(
                (+0.299f * c.r + 0.587f * c.g + 0.114f * c.b),
                (-0.169f * c.r - 0.331f * c.g + 0.500f * c.b) + 0.5f,
                (+0.500f * c.r - 0.419f * c.g - 0.081f * c.b) + 0.5f,
                c.a
        );
    }
}
