package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.resamplers.java.Progress;


public interface ScaleUp {
    public void scaleUp(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
    public int getWindowHalfSize();
    public int getDeltaAbsoluteResultSize();
    public int getResultWidth(int sourceWidth);
    public int getResultHeight(int sourceHeight);
    public int getProgressStepCount(int sourceWidth, int sourceHeight);
}
