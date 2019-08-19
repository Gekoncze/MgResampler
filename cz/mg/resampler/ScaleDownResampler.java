package cz.mg.resampler;


public interface ScaleDownResampler extends Resampler {
    public int getResultWidth(int sourceImageWidth);
    public int getResultHeight(int sourceImageHeight);
    public int getHorizontalScaleDownCount();
    public void setHorizontalScaleDownCount(int stepCount);
    public int getVerticalScaleDownCount();
    public void setVerticalScaleDownCount(int stepCount);
    public int getProgressStepCount(int sourceWidth, int sourceHeight);
}
