package cz.mg.resampler.resamplers.java;

import cz.mg.resampler.resamplers.java.other.ImageUtilities;
import cz.mg.resampler.resamplers.java.scaledown.NonuniformScaleDown;
import cz.mg.resampler.resamplers.java.scaledown.ScaleDown;
import cz.mg.resampler.Parameter;


public class ScaleDownResampler extends Resampler implements cz.mg.resampler.ScaleDownResampler {
    private static final int DEFAULT_SCALE_DOWN_COUNT = 1;
    private static final int MIN_STEP_COUNT = 0;
    private static final int MAX_STEP_COUNT = 15;
    
    private int horizontalScaleDownCount = DEFAULT_SCALE_DOWN_COUNT;
    private int verticalScaleDownCount = DEFAULT_SCALE_DOWN_COUNT;
    private ScaleDown scaleDown = null;

    public ScaleDownResampler() {
    }

    @Parameter(order = 0)
    public ScaleDown getScaleDown() {
        return scaleDown;
    }

    @Parameter(order = 0)
    public void setScaleDown(ScaleDown scaleUp) {
        this.scaleDown = scaleUp;
    }
    
    @Override
    @Parameter(order = 1)
    public int getHorizontalScaleDownCount() {
        return horizontalScaleDownCount;
    }
    
    @Override
    @Parameter(order = 1)
    public void setHorizontalScaleDownCount(int stepCount) {
        if(stepCount < MIN_STEP_COUNT) stepCount = MIN_STEP_COUNT;
        if(stepCount > MAX_STEP_COUNT) stepCount = MAX_STEP_COUNT;
        this.horizontalScaleDownCount = stepCount;
    }
    
    @Override
    @Parameter(order = 2)
    public int getVerticalScaleDownCount() {
        return verticalScaleDownCount;
    }
    
    @Override
    @Parameter(order = 2)
    public void setVerticalScaleDownCount(int stepCount) {
        if(stepCount < MIN_STEP_COUNT) stepCount = MIN_STEP_COUNT;
        if(stepCount > MAX_STEP_COUNT) stepCount = MAX_STEP_COUNT;
        this.verticalScaleDownCount = stepCount;
    }
    
    @Override
    public final void resample(cz.mg.resampler.Image resamplerSourceImage, cz.mg.resampler.Image resamplerDestinationImage, cz.mg.resampler.Progress resamplerProgress) throws Exception {
        Image sourceImage = (Image) resamplerSourceImage;
        Image destinationImage = (Image) resamplerDestinationImage;
        Progress progress = (Progress) resamplerProgress;
        
        int bufferWidth = sourceImage.getWidth();
        int bufferHeight = sourceImage.getHeight();
        
        Image sourceBuffer = new Image(bufferWidth, bufferHeight);
        Image destinationBuffer = new Image(bufferWidth, bufferHeight);
        
        Image sourceBufferPart = null;
        Image destinationBufferPart = createBufferPart(destinationBuffer, sourceImage.getWidth(), sourceImage.getHeight());
        
        ImageUtilities.copy(sourceImage, destinationBuffer);
        
        // do N scale ups
        Image currentBuffer = destinationBuffer;
        int remainingHorizontalScales = horizontalScaleDownCount;
        int remainingVerticalScales = verticalScaleDownCount;
        while(remainingHorizontalScales > 0 && remainingVerticalScales > 0){
            if(Thread.interrupted()) throw new InterruptedException();
            currentBuffer = swapBuffer(currentBuffer, sourceBuffer, destinationBuffer);
            sourceBufferPart = destinationBufferPart;
            destinationBufferPart = nextPart(currentBuffer, sourceBufferPart);
            scaleDown.scaleDown(sourceBufferPart, destinationBufferPart, progress);
            remainingHorizontalScales--;
            remainingVerticalScales--;
        }
        if(scaleDown instanceof NonuniformScaleDown){
            while(remainingHorizontalScales > 0){
                if(Thread.interrupted()) throw new InterruptedException();
                currentBuffer = swapBuffer(currentBuffer, sourceBuffer, destinationBuffer);
                sourceBufferPart = destinationBufferPart;
                destinationBufferPart = nextPartHorizontal(currentBuffer, sourceBufferPart);
                ((NonuniformScaleDown)scaleDown).scaleDownHorizontally(sourceBufferPart, destinationBufferPart, progress);
                remainingHorizontalScales--;
            }
            while(remainingVerticalScales > 0){
                if(Thread.interrupted()) throw new InterruptedException();
                currentBuffer = swapBuffer(currentBuffer, sourceBuffer, destinationBuffer);
                sourceBufferPart = destinationBufferPart;
                destinationBufferPart = nextPartVertical(currentBuffer, sourceBufferPart);
                ((NonuniformScaleDown)scaleDown).scaleDownVertically(sourceBufferPart, destinationBufferPart, progress);
                remainingVerticalScales--;
            }
        }
        
        ImageUtilities.copy(destinationBufferPart, destinationImage);
    }
    
    private Image swapBuffer(Image currentBuffer, Image firstBuffer, Image secondBuffer){
        if(currentBuffer == firstBuffer) return secondBuffer;
        if(currentBuffer == secondBuffer) return firstBuffer;
        throw new RuntimeException();
    }
    
    private Image createBufferPart(Image currentBuffer, int width, int height){
        return currentBuffer.subimage(0, 0, width, height, true);
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
        return scaleDown.getResultWidth(width);
    }
    
    private int nextHeight(int height){
        return scaleDown.getResultHeight(height);
    }
    
    @Override
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight){
        int totalProgressSteps = 0;
        int remainingHorizontalScales = horizontalScaleDownCount;
        int remainingVerticalScales = verticalScaleDownCount;
        while(remainingHorizontalScales > 0 && remainingVerticalScales > 0){
            totalProgressSteps += scaleDown.getProgressStepCount(sourceImageWidth, sourceImageHeight);
            sourceImageWidth = nextWidth(sourceImageWidth);
            sourceImageHeight = nextHeight(sourceImageHeight);
            remainingHorizontalScales--;
            remainingVerticalScales--;
        }
        if(scaleDown instanceof NonuniformScaleDown){
            while(remainingHorizontalScales > 0){
                totalProgressSteps += ((NonuniformScaleDown)scaleDown).getHorizontalProgressStepCount(sourceImageWidth, sourceImageHeight);
                sourceImageWidth = nextWidth(sourceImageWidth);
                remainingHorizontalScales--;
            }
            while(remainingVerticalScales > 0){
                totalProgressSteps += ((NonuniformScaleDown)scaleDown).getVerticalProgressStepCount(sourceImageWidth, sourceImageHeight);
                sourceImageHeight = nextHeight(sourceImageHeight);
                remainingVerticalScales--;
            }
        }
        
        return totalProgressSteps;
    }
    
    @Override
    public int getResultWidth(int sourceWidth) {
        int resultWidth = sourceWidth;
        for(int i = 0; i < horizontalScaleDownCount; i++) resultWidth = scaleDown.getResultWidth(resultWidth);
        return resultWidth;
    }

    @Override
    public int getResultHeight(int sourceHeight) {
        int resultHeight = sourceHeight;
        for(int i = 0; i < verticalScaleDownCount; i++) resultHeight = scaleDown.getResultHeight(resultHeight);
        return resultHeight;
    }

    @Override
    public String toString() {
        return "Scale down resampler";
    }
}
