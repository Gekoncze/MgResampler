package cz.mg.resampler.resamplers.java.stretch;

import cz.mg.resampler.resamplers.java.utilities.ComputationUtilities;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


public class BoxStretch implements Stretch {
    public BoxStretch() {
    }
    
    @Override
    public void compute(Image sourceImage, Image destinationImage, int dx, int dy, ColorRgba result) {
        float nx = ComputationUtilities.boxIntegerToNormalized(dx, destinationImage.getWidth());
        float ny = ComputationUtilities.boxIntegerToNormalized(dy, destinationImage.getHeight());
        int x = ComputationUtilities.normalizedToBoxInteger(nx, sourceImage.getWidth());
        int y = ComputationUtilities.normalizedToBoxInteger(ny, sourceImage.getHeight());
        result.set(sourceImage.getReadColor(x, y));
    }

    @Override
    public String toString() {
        return "Box";
    }
}
