package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;


public class WeightRgbColorDifference implements SolidColorDifference {
    private float red;
    private float green;
    private float blue;

    public WeightRgbColorDifference() {
        red = 1;
        green = 1;
        blue = 1;
    }

    public WeightRgbColorDifference(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Parameter(order = 1)
    public float getRed() {
        return red;
    }

    @Parameter(order = 1)
    public void setRed(float red) {
        if(red > 1) red = 1;
        if(red < 0) red = 0;
        this.red = red;
    }

    @Parameter(order = 2)
    public float getGreen() {
        return green;
    }

    @Parameter(order = 2)
    public void setGreen(float green) {
        if(green > 1) green = 1;
        if(green < 0) green = 0;
        this.green = green;
    }

    @Parameter(order = 3)
    public float getBlue() {
        return blue;
    }

    @Parameter(order = 3)
    public void setBlue(float blue) {
        if(blue > 1) blue = 1;
        if(blue < 0) blue = 0;
        this.blue = blue;
    }
    
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        float dr = Math.abs(a.r - b.r) * red;
        float dg = Math.abs(a.g - b.g) * green;
        float db = Math.abs(a.b - b.b) * blue;
        float max = red + green + blue;
        return (dr + dg + db) / max;
    }
    
    @Override
    public String toString() {
        return "Weight RGB";
    }
}
