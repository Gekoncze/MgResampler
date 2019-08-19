package cz.mg.resampler;


public interface StretchResampler extends Resampler {
    public int getProgressStepCount(int sourceWidth, int sourceHeight, int destinationWidth, int destinationHeight);
}
