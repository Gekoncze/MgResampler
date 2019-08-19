package cz.mg.resampler.resamplers.java.stretch;

import cz.mg.resampler.resamplers.java.convolutionkernel.ConvolutionKernel;
import cz.mg.resampler.resamplers.java.utilities.ComputationUtilities;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;
import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.resamplers.java.convolutionkernel.NearestConvolutionKernel;


public class ConvolutionStretch implements Stretch {
    private ConvolutionKernel convolutionKernel = new NearestConvolutionKernel();
    private int windowSize = 3;
    private boolean normalize = true;

    public ConvolutionStretch() {
    }
    
    @Parameter(order = 0)
    public ConvolutionKernel getConvolutionKernel() {
        return convolutionKernel;
    }

    @Parameter(order = 0)
    public void setConvolutionKernel(ConvolutionKernel convolutionKernel) {
        this.convolutionKernel = convolutionKernel;
    }

    @Parameter(order = 2)
    public int getWindowSize() {
        return windowSize;
    }

    @Parameter(order = 2)
    public void setWindowSize(int windowSize) {
        if(windowSize < 1) windowSize = 1;
        if(windowSize > 16) windowSize = 16;
        this.windowSize = windowSize;
    }

    @Parameter(order = 1)
    public boolean isNormalize() {
        return normalize;
    }

    @Parameter(order = 1)
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    @Override
    public void compute(Image sourceImage, Image destinationImage, int dx, int dy, ColorRgba result) {
        float nx = ComputationUtilities.boxIntegerToNormalized(dx, destinationImage.getWidth());
        float ny = ComputationUtilities.boxIntegerToNormalized(dy, destinationImage.getHeight());
        float x = ComputationUtilities.normalizedToGridFloat(nx, sourceImage.getWidth());
        float y = ComputationUtilities.normalizedToGridFloat(ny, sourceImage.getHeight());
        compute(sourceImage, x, y, result);
    }
    
    public void compute(Image image, float x, float y, ColorRgba result) {
        int ix0 = (int) x;
        int iy0 = (int) y;
        
        float sum = 0.0f;
        ColorRgba sumColor = new ColorRgba();
        for(int iy = iy0 - windowSize + 1; iy <= iy0 + windowSize; iy++)
        {
            for(int ix = ix0 - windowSize + 1; ix <= ix0 + windowSize; ix++)
            {
                ColorRgba ci = image.getReadColor(ix, iy);
                float kx = convolutionKernel.compute(x - ix);
                float ky = convolutionKernel.compute(y - iy);
                float k = kx*ky;
                
                sumColor.r += ci.r * k;
                sumColor.g += ci.g * k;
                sumColor.b += ci.b * k;
                sumColor.a += ci.a * k;
                
                sum += k;
            }
        }
                
        if(normalize){
            sumColor.r /= sum;
            sumColor.g /= sum;
            sumColor.b /= sum;
            sumColor.a /= sum;
        }
        
        result.set(sumColor);
    }

    @Override
    public String toString() {
        return "Convolution";
    }
}
