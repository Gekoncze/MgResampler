package cz.mg.resampler.resamplers.java;

import cz.mg.resampler.resamplers.java.stretch.BoxStretch;
import cz.mg.resampler.resamplers.java.stretch.Stretch;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import static cz.mg.resampler.resamplers.java.Image.SINK;
import cz.mg.resampler.Parameter;


public class StretchResampler extends Resampler implements cz.mg.resampler.StretchResampler {
    private Stretch stretch = new BoxStretch();

    @Parameter(order = 0)
    public Stretch getStretch() {
        return stretch;
    }

    @Parameter(order = 0)
    public void setStretch(Stretch stretch) {
        this.stretch = stretch;
    }
    
    @Override
    public void resample(cz.mg.resampler.Image resamplerSourceImage, cz.mg.resampler.Image resamplerDestinationImage, cz.mg.resampler.Progress resamplerProgress) throws Exception {
        Image sourceImage = (Image) resamplerSourceImage;
        Image destinationImage = (Image) resamplerDestinationImage;
        Progress progress = (Progress) resamplerProgress;
        
        for(int y = 0; y < destinationImage.getHeight(); y++){
            for(int x = 0; x < destinationImage.getWidth(); x++){
                if(progress != null) progress.step();
                ColorRgba result = destinationImage.getWriteColor(x, y);
                if(result == SINK) continue;
                stretch.compute(sourceImage, destinationImage, x, y, result);
            }
        }
    }
    
    @Override
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight, int destinationImageWidth, int destinationImageHeight) {
        return destinationImageWidth * destinationImageHeight;
    }

    @Override
    public String toString() {
        return "Stretch resampler";
    }
}
