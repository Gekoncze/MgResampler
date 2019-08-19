package cz.mg.resampler.resamplers.java.scaledown;

import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.resamplers.java.Progress;


public interface ScaleDown {
    public void scaleDown(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
    public int getResultWidth(int sourceWidth);
    public int getResultHeight(int sourceHeight);
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight);
}
