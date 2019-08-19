package cz.mg.resampler.resamplers.java.stretch;

import cz.mg.resampler.resamplers.java.utilities.ComputationUtilities;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;


public class BoxAverageStretch implements Stretch {
    @Override
    public void compute(Image sourceImage, Image destinationImage, int dx, int dy, ColorRgba result) {
        float kx = (float)sourceImage.getWidth() / (float)destinationImage.getWidth();
        float ky = (float)sourceImage.getHeight() / (float)destinationImage.getHeight();
        float px = dx + 0.5f;
        float py = dy + 0.5f;
        float pw = 1.0f;
        float ph = 1.0f;
        float nx = ComputationUtilities.boxFloatToNormalized(px, destinationImage.getWidth());
        float ny = ComputationUtilities.boxFloatToNormalized(py, destinationImage.getHeight());
        float ax = ComputationUtilities.normalizedToBoxFloat(nx, sourceImage.getWidth());
        float ay = ComputationUtilities.normalizedToBoxFloat(ny, sourceImage.getHeight());
        float aw = pw * kx;
        float ah = ph * ky;
        float mx = ax - 0.5f;
        float my = ay - 0.5f;
        int lx0 = Math.round(mx - aw / 2);
        int lx1 = Math.round(mx + aw / 2);
        int ly0 = Math.round(my - ah / 2);
        int ly1 = Math.round(my + ah / 2);
        
        ColorRgba sum = new ColorRgba();
        int count = 0;
        for(int lx = lx0; lx <= lx1; lx++){
            for(int ly = ly0; ly <= ly1; ly++){
                count++;
                ColorRgba c = sourceImage.getReadColor(lx, ly);
                sum.r += c.r;
                sum.g += c.g;
                sum.b += c.b;
                sum.a += c.a;
            }
        }
        result.set(
                sum.r / count,
                sum.g / count,
                sum.b / count,
                sum.a / count
        );
    }
    
    @Override
    public String toString() {
        return "Box average";
    }
}
