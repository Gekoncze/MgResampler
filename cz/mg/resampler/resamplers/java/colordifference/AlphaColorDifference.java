package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;


public class AlphaColorDifference implements SimpleColorDifference {
    private SolidColorDifference colorDifference;

    public AlphaColorDifference() {
        this.colorDifference = new FastRgbColorDifference();
    }

    public AlphaColorDifference(SolidColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }

    @Parameter(order = 1)
    public SolidColorDifference getColorDifference() {
        return colorDifference;
    }

    @Parameter(order = 1)
    public void setColorDifference(SolidColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }
    
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        float da = a.a - b.a;
        float aa = (a.a + b.a) / 2;
        float d = colorDifference.compute(a, b) * aa;
        float dd = (float) Math.sqrt(d*d + da*da);
        if(dd > 1) dd = 1;
        return dd;
    }

    @Override
    public String toString() {
        return "Alpha";
    }
}
