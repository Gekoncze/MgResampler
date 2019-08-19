package cz.mg.resampler;


public interface Resampler {
    public void resample(Image sourceImage, Image destinationImage, Progress progress) throws Exception;
}
