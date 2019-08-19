package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.resamplers.java.Progress;


public interface NonuniformScaleUp extends ScaleUp {
    public void scaleUpHorizontally(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
    public void scaleUpVertically(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
    public int getHorizontalProgressStepCount(int sourceImageWidth, int sourceImageHeight);
    public int getVerticalProgressStepCount(int sourceImageWidth, int sourceImageHeight);
}
