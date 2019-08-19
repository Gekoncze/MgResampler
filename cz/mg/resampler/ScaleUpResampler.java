package cz.mg.resampler;


public interface ScaleUpResampler extends Resampler {
    public int getResultWidth(int sourceImageWidth);
    public int getResultHeight(int sourceImageHeight);
    public int getHorizontalScaleUpCount();
    public void setHorizontalScaleUpCount(int stepCount);
    public int getVerticalScaleUpCount();
    public void setVerticalScaleUpCount(int stepCount);
    public int getProgressStepCount(int sourceWidth, int sourceHeight);
}
