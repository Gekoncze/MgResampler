package cz.mg.resampler.resamplers.java.colordifference;

import cz.mg.resampler.resamplers.java.other.SmoothStep;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;


public class ModifiedColorDifference implements ColorDifference {
    private SimpleColorDifference colorDifference;
    private float steepness = 0.205f;
    private float position = 0.178f;

    public ModifiedColorDifference() {
        this.colorDifference = new FastRgbColorDifference();
    }

    public ModifiedColorDifference(SimpleColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }
    
    @Parameter(order = 1)
    public SimpleColorDifference getColorDifference() {
        return colorDifference;
    }

    @Parameter(order = 1)
    public void setColorDifference(SimpleColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }
    
    @Parameter(order = 2)
    public float getSteepness() {
        return steepness;
    }

    @Parameter(order = 2)
    public void setSteepness(float steepness) {
        if(steepness < 0.001f) steepness = 0.001f;
        if(steepness > 1) steepness = 1;
        this.steepness = steepness;
    }

    @Parameter(order = 3)
    public float getMixPosition() {
        return position;
    }

    @Parameter(order = 3)
    public void setMixPosition(float position) {
        if(position < 0.001f) position = 0.001f;
        if(position > 0.999f) position = 0.999f;
        this.position = position;
    }
    
    @Override
    public float compute(ColorRgba a, ColorRgba b) {
        float difference = colorDifference.compute(a, b);
        return SmoothStep.compute(difference, steepness, position);
    }

    @Override
    public String toString() {
        return "Modified";
    }
}
