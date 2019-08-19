package cz.mg.resampler.resamplers.java.stretch;

import cz.mg.resampler.resamplers.java.interpolation.Interpolation;
import cz.mg.resampler.resamplers.java.interpolation.NearestInterpolation;
import cz.mg.resampler.resamplers.java.utilities.ComputationUtilities;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.Parameter;


public class InterpolationStretch implements Stretch {
    private Interpolation interpolation = new NearestInterpolation();

    public InterpolationStretch() {
    }

    public InterpolationStretch(Interpolation interpolation) {
        this.interpolation = interpolation;
    }
    
    @Parameter(order = 0)
    public Interpolation getInterpolation() {
        return interpolation;
    }

    @Parameter(order = 0)
    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    @Override
    public void compute(Image sourceImage, Image destinationImage, int dx, int dy, ColorRgba result) {
        float nx = ComputationUtilities.boxIntegerToNormalized(dx, destinationImage.getWidth());
        float ny = ComputationUtilities.boxIntegerToNormalized(dy, destinationImage.getHeight());
        float x = ComputationUtilities.normalizedToGridFloat(nx, sourceImage.getWidth());
        float y = ComputationUtilities.normalizedToGridFloat(ny, sourceImage.getHeight());
        interpolation.compute(sourceImage, x, y, result);
    }

    @Override
    public String toString() {
        return "Interpolation";
    }
}
