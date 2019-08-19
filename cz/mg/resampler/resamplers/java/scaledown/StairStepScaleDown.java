package cz.mg.resampler.resamplers.java.scaledown;

import cz.mg.resampler.resamplers.java.stretch.BoxStretch;
import cz.mg.resampler.resamplers.java.stretch.Stretch;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;
import static cz.mg.resampler.resamplers.java.Image.SINK;
import cz.mg.resampler.Parameter;
import cz.mg.resampler.resamplers.java.Progress;


public class StairStepScaleDown implements NonuniformScaleDown {
    private float stepScale = 1.5f;
    private Stretch stretch = new BoxStretch();

    public StairStepScaleDown() {
    }
    
    public StairStepScaleDown(float stepScale, Stretch stretch) {
        this.stepScale = stepScale;
        this.stretch = stretch;
    }
    
    @Parameter(order = 0)
    public float getStepScale() {
        return stepScale;
    }

    @Parameter(order = 0)
    public void setStepScale(float stepScale) {
        if(stepScale < 1.1f) stepScale = 1.1f;
        if(stepScale > 2.0f) stepScale = 2.0f;
        this.stepScale = stepScale;
    }

    @Parameter(order = 1)
    public Stretch getStretch() {
        return stretch;
    }

    @Parameter(order = 1)
    public void setStretch(Stretch stretch) {
        this.stretch = stretch;
    }
    
    @Override
    public void scaleDown(Image sourceImage, Image destinationImage, Progress progress) throws Exception {
        if(destinationImage.getWidth() != getResultWidth(sourceImage.getWidth())) throw new Exception("Invalid destination width for stair step scale down. Expected " + getResultWidth(sourceImage.getWidth()) + ", got " + destinationImage.getWidth() + ".");
        if(destinationImage.getHeight() != getResultHeight(sourceImage.getHeight())) throw new Exception("Invalid destination height for stair step scale down. Expected " + getResultHeight(sourceImage.getHeight()) + ", got " + destinationImage.getHeight() + ".");
        scale(sourceImage, destinationImage, progress);
    }
    
    @Override
    public void scaleDownHorizontally(Image sourceImage, Image destinationImage, Progress progress) throws Exception {
        if(destinationImage.getWidth() != getResultWidth(sourceImage.getWidth())) throw new Exception("Invalid destination width for stair step scale down. Expected " + getResultWidth(sourceImage.getWidth()) + ", got " + destinationImage.getWidth() + ".");
        if(destinationImage.getHeight() != sourceImage.getHeight()) throw new Exception("Invalid destination height for stair step scale down. Expected " + sourceImage.getHeight() + ", got " + destinationImage.getHeight() + ".");
        scale(sourceImage, destinationImage, progress);
    }

    @Override
    public void scaleDownVertically(Image sourceImage, Image destinationImage, Progress progress) throws Exception {
        if(destinationImage.getWidth() != sourceImage.getWidth()) throw new Exception("Invalid destination width for stair step scale down. Expected " + sourceImage.getWidth() + ", got " + destinationImage.getWidth() + ".");
        if(destinationImage.getHeight() != getResultHeight(sourceImage.getHeight())) throw new Exception("Invalid destination height for stair step scale down. Expected " + getResultHeight(sourceImage.getHeight()) + ", got " + destinationImage.getHeight() + ".");
        scale(sourceImage, destinationImage, progress);
    }
    
    private void scale(Image sourceImage, Image destinationImage, Progress progress) throws Exception {
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
    public int getResultWidth(int sourceWidth) {
        int result = (int) (sourceWidth / stepScale);
        if(result == sourceWidth) result--;
        return result;
    }

    @Override
    public int getResultHeight(int sourceHeight) {
        int result = (int) (sourceHeight / stepScale);
        if(result == sourceHeight) result--;
        return result;
    }
    
    @Override
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight) {
        int width = getResultWidth(sourceImageWidth);
        int height = getResultHeight(sourceImageHeight);
        return width * height;
    }

    @Override
    public int getHorizontalProgressStepCount(int sourceImageWidth, int sourceImageHeight) {
        int width = getResultWidth(sourceImageWidth);
        int height = sourceImageHeight;
        return width * height;
    }

    @Override
    public int getVerticalProgressStepCount(int sourceImageWidth, int sourceImageHeight) {
        int width = sourceImageWidth;
        int height = getResultHeight(sourceImageHeight);
        return width * height;
    }

    @Override
    public String toString() {
        return "Stair step";
    }
}
