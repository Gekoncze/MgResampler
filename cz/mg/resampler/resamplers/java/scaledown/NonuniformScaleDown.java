package cz.mg.resampler.resamplers.java.scaledown;

import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.resamplers.java.Progress;


public interface NonuniformScaleDown extends ScaleDown {
    public void scaleDownHorizontally(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
    public void scaleDownVertically(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
    public int getHorizontalProgressStepCount(int sourceImageWidth, int sourceImageHeight);
    public int getVerticalProgressStepCount(int sourceImageWidth, int sourceImageHeight);
}
