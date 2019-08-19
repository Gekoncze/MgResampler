package cz.mg.resampler.resamplers.java;

import cz.mg.resampler.resamplers.java.other.ImageUtilities;
import cz.mg.resampler.resamplers.java.scaleup.NonuniformScaleUp;
import cz.mg.resampler.resamplers.java.scaleup.ScaleUp;
import cz.mg.resampler.Parameter;


public class ScaleUpResampler extends Resampler implements cz.mg.resampler.ScaleUpResampler {
    private static final int DEFAULT_SCALE_UP_COUNT = 1;
    private static final int MIN_STEP_COUNT = 0;
    private static final int MAX_STEP_COUNT = 15;
    
    private int horizontalScaleUpCount = DEFAULT_SCALE_UP_COUNT;
    private int verticalScaleUpCount = DEFAULT_SCALE_UP_COUNT;
    private ScaleUp scaleUp = null;
    private boolean fixBorder = true;

    public ScaleUpResampler() {
    }

    @Parameter(order = 0)
    public ScaleUp getScaleUp() {
        return scaleUp;
    }

    @Parameter(order = 0)
    public void setScaleUp(ScaleUp scaleUp) {
        this.scaleUp = scaleUp;
    }
    
    @Override
    @Parameter(order = 1)
    public int getHorizontalScaleUpCount() {
        return horizontalScaleUpCount;
    }
    
    @Override
    @Parameter(order = 1)
    public void setHorizontalScaleUpCount(int stepCount) {
        if(stepCount < MIN_STEP_COUNT) stepCount = MIN_STEP_COUNT;
        if(stepCount > MAX_STEP_COUNT) stepCount = MAX_STEP_COUNT;
        this.horizontalScaleUpCount = stepCount;
    }
    
    @Override
    @Parameter(order = 2)
    public int getVerticalScaleUpCount() {
        return verticalScaleUpCount;
    }
    
    @Override
    @Parameter(order = 2)
    public void setVerticalScaleUpCount(int stepCount) {
        if(stepCount < MIN_STEP_COUNT) stepCount = MIN_STEP_COUNT;
        if(stepCount > MAX_STEP_COUNT) stepCount = MAX_STEP_COUNT;
        this.verticalScaleUpCount = stepCount;
    }

    @Parameter(order = 2)
    public boolean isFixBorder() {
        return fixBorder;
    }

    @Parameter(order = 2)
    public void setFixBorder(boolean fixBorder) {
        this.fixBorder = fixBorder;
    }
    
    @Override
    public final void resample(cz.mg.resampler.Image resamplerSourceImage, cz.mg.resampler.Image resamplerDestinationImage, cz.mg.resampler.Progress resamplerProgress) throws Exception {
        Image sourceImage = (Image) resamplerSourceImage;
        Image destinationImage = (Image) resamplerDestinationImage;
        Progress progress = (Progress) resamplerProgress;

        // fix border for algorithms fetching colors in window
        if(fixBorder){
            int ws = scaleUp.getWindowHalfSize();
            if(ws > 0){
                int ds = scaleUp.getDeltaAbsoluteResultSize();
                sourceImage = sourceImage.subimage(-ws, -ws, sourceImage.getWidth() + 2*ws, sourceImage.getHeight() + 2*ws);
                destinationImage = destinationImage.subimage(-getResultWidth(ws-ds), -getResultHeight(ws-ds), getResultWidth(sourceImage.getWidth()), getResultHeight(sourceImage.getHeight()));
            }
        }
        
        int bufferWidth = getResultWidth(sourceImage.getWidth());
        int bufferHeight = getResultHeight(sourceImage.getHeight());
        
        Image sourceBuffer = new Image(bufferWidth, bufferHeight);
        Image destinationBuffer = new Image(bufferWidth, bufferHeight);
        
        Image sourceBufferPart = null;
        Image destinationBufferPart = createBufferPart(destinationBuffer, sourceImage.getWidth(), sourceImage.getHeight());
        
        ImageUtilities.copy(sourceImage, destinationBuffer);

        // do N scale ups
        Image currentBuffer = destinationBuffer;
        int remainingHorizontalScales = horizontalScaleUpCount;
        int remainingVerticalScales = verticalScaleUpCount;
        while(remainingHorizontalScales > 0 && remainingVerticalScales > 0){
            if(Thread.interrupted()) throw new InterruptedException();
            currentBuffer = swapBuffer(currentBuffer, sourceBuffer, destinationBuffer);
            sourceBufferPart = destinationBufferPart;
            destinationBufferPart = nextPart(currentBuffer, sourceBufferPart);
            scaleUp.scaleUp(sourceBufferPart, destinationBufferPart, progress);
            remainingHorizontalScales--;
            remainingVerticalScales--;
        }
        if(scaleUp instanceof NonuniformScaleUp){
            while(remainingHorizontalScales > 0){
                if(Thread.interrupted()) throw new InterruptedException();
                currentBuffer = swapBuffer(currentBuffer, sourceBuffer, destinationBuffer);
                sourceBufferPart = destinationBufferPart;
                destinationBufferPart = nextPartHorizontal(currentBuffer, sourceBufferPart);
                ((NonuniformScaleUp)scaleUp).scaleUpHorizontally(sourceBufferPart, destinationBufferPart, progress);
                remainingHorizontalScales--;
            }
            while(remainingVerticalScales > 0){
                if(Thread.interrupted()) throw new InterruptedException();
                currentBuffer = swapBuffer(currentBuffer, sourceBuffer, destinationBuffer);
                sourceBufferPart = destinationBufferPart;
                destinationBufferPart = nextPartVertical(currentBuffer, sourceBufferPart);
                ((NonuniformScaleUp)scaleUp).scaleUpVertically(sourceBufferPart, destinationBufferPart, progress);
                remainingVerticalScales--;
            }
        }
        
        ImageUtilities.copy(destinationBufferPart, destinationImage);
    }
    
    private Image nextPart(Image currentBuffer, Image currentPart){
        return createBufferPart(currentBuffer, nextWidth(currentPart.getWidth()), nextHeight(currentPart.getHeight()));
    }
    
    private Image nextPartHorizontal(Image currentBuffer, Image currentPart){
        return createBufferPart(currentBuffer, nextWidth(currentPart.getWidth()), currentPart.getHeight());
    }
    
    private Image nextPartVertical(Image currentBuffer, Image currentPart){
        return createBufferPart(currentBuffer, currentPart.getWidth(), nextHeight(currentPart.getHeight()));
    }
    
    private int nextWidth(int width){
        return scaleUp.getResultWidth(width);
    }
    
    private int nextHeight(int height){
        return scaleUp.getResultHeight(height);
    }
    
    private Image swapBuffer(Image currentBuffer, Image firstBuffer, Image secondBuffer){
        if(currentBuffer == firstBuffer) return secondBuffer;
        if(currentBuffer == secondBuffer) return firstBuffer;
        throw new RuntimeException();
    }
    
    private Image createBufferPart(Image currentBuffer, int width, int height){
        return currentBuffer.subimage(0, 0, width, height, true);
    }
    
    @Override
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight){
        if(fixBorder){
            int ws = scaleUp.getWindowHalfSize();
            if(ws > 0){
                sourceImageWidth += 2*ws;
                sourceImageHeight += 2*ws;
            }
        }
        
        int totalProgressSteps = 0;
        int remainingHorizontalScales = horizontalScaleUpCount;
        int remainingVerticalScales = verticalScaleUpCount;
        while(remainingHorizontalScales > 0 && remainingVerticalScales > 0){
            totalProgressSteps += scaleUp.getProgressStepCount(sourceImageWidth, sourceImageHeight);
            sourceImageWidth = nextWidth(sourceImageWidth);
            sourceImageHeight = nextHeight(sourceImageHeight);
            remainingHorizontalScales--;
            remainingVerticalScales--;
        }
        if(scaleUp instanceof NonuniformScaleUp){
            while(remainingHorizontalScales > 0){
                totalProgressSteps += ((NonuniformScaleUp)scaleUp).getHorizontalProgressStepCount(sourceImageWidth, sourceImageHeight);
                sourceImageWidth = nextWidth(sourceImageWidth);
                remainingHorizontalScales--;
            }
            while(remainingVerticalScales > 0){
                totalProgressSteps += ((NonuniformScaleUp)scaleUp).getVerticalProgressStepCount(sourceImageWidth, sourceImageHeight);
                sourceImageHeight = nextHeight(sourceImageHeight);
                remainingVerticalScales--;
            }
        }
        
        return totalProgressSteps;
    }
    
    @Override
    public int getResultWidth(int sourceWidth) {
        int resultWidth = sourceWidth;
        for(int i = 0; i < horizontalScaleUpCount; i++) resultWidth = scaleUp.getResultWidth(resultWidth);
        return resultWidth;
    }

    @Override
    public int getResultHeight(int sourceHeight) {
        int resultHeight = sourceHeight;
        for(int i = 0; i < verticalScaleUpCount; i++) resultHeight = scaleUp.getResultHeight(resultHeight);
        return resultHeight;
    }

    @Override
    public String toString() {
        return "Scale up resampler";
    }
}
