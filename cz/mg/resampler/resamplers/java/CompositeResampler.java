package cz.mg.resampler.resamplers.java;

import cz.mg.resampler.resamplers.java.scaledown.ScaleDown;
import cz.mg.resampler.resamplers.java.scaleup.ScaleUp;
import cz.mg.resampler.Progress;
import cz.mg.resampler.Parameter;
import cz.mg.resampler.resamplers.java.stretch.Stretch;


public class CompositeResampler extends Resampler implements cz.mg.resampler.StretchResampler {
    private final StretchResampler stretchResampler = new StretchResampler();
    private final ScaleUpResampler scaleUpResampler = new ScaleUpResampler();
    private final ScaleDownResampler scaleDownResampler = new ScaleDownResampler();
    private int scaleUpResultWidth;
    private int scaleUpResultHeight;
    private int scaleDownResultWidth;
    private int scaleDownResultHeight;

    public CompositeResampler() {
    }
    
    @Parameter(order = 0)
    public Stretch getStretch() {
        return stretchResampler.getStretch();
    }

    @Parameter(order = 0)
    public void setStretch(Stretch stretch) {
        stretchResampler.setStretch(stretch);
    }
    
    @Parameter(order = 1)
    public ScaleUp getScaleUp() {
        return scaleUpResampler.getScaleUp();
    }

    @Parameter(order = 1)
    public void setScaleUp(ScaleUp scaleUp) {
        scaleUpResampler.setScaleUp(scaleUp);
    }
    
    @Parameter(order = 2)
    public ScaleDown getScaleDown() {
        return scaleDownResampler.getScaleDown();
    }

    @Parameter(order = 2)
    public void setScaleDown(ScaleDown scaleDown) {
        scaleDownResampler.setScaleDown(scaleDown);
    }
    
    @Parameter(order = 3)
    public boolean isFixBorder() {
        return scaleUpResampler.isFixBorder();
    }

    @Parameter(order = 3)
    public void setFixBorder(boolean fixBorder) {
        scaleUpResampler.setFixBorder(fixBorder);
    }
    
    @Override
    public void resample(cz.mg.resampler.Image resamplerSourceImage, cz.mg.resampler.Image resamplerDestinationImage, cz.mg.resampler.Progress resamplerProgress) throws Exception {
        Image sourceImage = (Image) resamplerSourceImage;
        Image destinationImage = (Image) resamplerDestinationImage;
        Progress progress = (Progress) resamplerProgress;
        
        if(getScaleUp() != null){
            adjustScaleUp(sourceImage.getWidth(), sourceImage.getHeight(), destinationImage.getWidth(), destinationImage.getHeight());
            sourceImage = scaleUp(sourceImage, progress);
        }
        
        if(getScaleDown() != null){
            adjustScaleDown(sourceImage.getWidth(), sourceImage.getHeight(), destinationImage.getWidth(), destinationImage.getHeight());
            sourceImage = scaleDown(sourceImage, progress);
        }
        
        stretchResampler.resample(sourceImage, destinationImage, progress);
    }
    
    private void adjustScaleUp(int sourceImageWidth, int sourceImageHeight, int destinationImageWidth, int destinationImageHeight){
        adjustHorizontalScaleUp(sourceImageWidth, destinationImageWidth);
        adjustVerticalScaleUp(sourceImageHeight, destinationImageHeight);
    }
    
    private void adjustHorizontalScaleUp(int sourceImageWidth, int destinationImageWidth){
        // sets optimal scale up count
        int horizontalScaleUpCount = 0;
        updateHorizontalScaleUpCount(horizontalScaleUpCount, sourceImageWidth);
        if(sourceImageWidth == 0) return;
        while(scaleUpResultWidth < destinationImageWidth){
            horizontalScaleUpCount++;
            updateHorizontalScaleUpCount(horizontalScaleUpCount, sourceImageWidth);
            if(scaleUpResampler.getHorizontalScaleUpCount() < horizontalScaleUpCount) break; // stops if reached max
        }
    }
    
    private void adjustVerticalScaleUp(int sourceImageHeight, int destinationImageHeight){
        // sets optimal scale up count
        int verticalScaleUpCount = 0;
        updateVerticalScaleUpCount(verticalScaleUpCount, sourceImageHeight);
        if(sourceImageHeight == 0) return;
        while(scaleUpResultHeight < destinationImageHeight){
            verticalScaleUpCount++;
            updateVerticalScaleUpCount(verticalScaleUpCount, sourceImageHeight);
            if(scaleUpResampler.getVerticalScaleUpCount() < verticalScaleUpCount) break; // stops if reached max
        }
    }
    
    private void updateHorizontalScaleUpCount(int scaleUpCount, int sourceImageWidth){
        scaleUpResampler.setHorizontalScaleUpCount(scaleUpCount);
        scaleUpResultWidth = scaleUpResampler.getResultWidth(sourceImageWidth);
    }
    
    private void updateVerticalScaleUpCount(int scaleUpCount, int sourceImageHeight){
        scaleUpResampler.setVerticalScaleUpCount(scaleUpCount);
        scaleUpResultHeight = scaleUpResampler.getResultHeight(sourceImageHeight);
    }
    
    private Image scaleUp(Image sourceImage, Progress progress) throws Exception {
        if(scaleUpResampler.getHorizontalScaleUpCount() <= 0 && scaleUpResampler.getVerticalScaleUpCount() <= 0) return sourceImage;
        Image destinationImage = new Image(scaleUpResultWidth, scaleUpResultHeight);
        scaleUpResampler.resample(sourceImage, destinationImage, progress);
        return destinationImage;
    }
    
    private void adjustScaleDown(int sourceImageWidth, int sourceImageHeight, int destinationImageWidth, int destinationImageHeight){
        adjustHorizontalScaleDown(sourceImageWidth, destinationImageWidth);
        adjustVerticalScaleDown(sourceImageHeight, destinationImageHeight);
    }
    
    private void adjustHorizontalScaleDown(int sourceImageWidth, int destinationImageWidth){
        // sets optimal horizontal scale down count
        int horizontalScaleDownCount = 0;
        updateHorizontalScaleDownCount(horizontalScaleDownCount, sourceImageWidth);
        if(sourceImageWidth == 0) return;
        while(scaleDownResultWidth > destinationImageWidth){
            horizontalScaleDownCount++;
            updateHorizontalScaleDownCount(horizontalScaleDownCount, sourceImageWidth);
            if(scaleDownResampler.getHorizontalScaleDownCount() < horizontalScaleDownCount) break; // stops if reached max
        }
        horizontalScaleDownCount--; // prefers the larger version to not loose more information
        updateHorizontalScaleDownCount(horizontalScaleDownCount, sourceImageWidth);
    }
    
    private void adjustVerticalScaleDown(int sourceImageHeight, int destinationImageHeight){
        // sets optimal vertical scale down count
        int verticalScaleDownCount = 0;
        updateVerticalScaleDownCount(verticalScaleDownCount, sourceImageHeight);
        if(sourceImageHeight == 0) return;
        while(scaleDownResultHeight > destinationImageHeight){
            verticalScaleDownCount++;
            updateVerticalScaleDownCount(verticalScaleDownCount, sourceImageHeight);
            if(scaleDownResampler.getVerticalScaleDownCount() < verticalScaleDownCount) break; // stops if reached max
        }
        verticalScaleDownCount--; // prefers the larger version to not loose more information
        updateVerticalScaleDownCount(verticalScaleDownCount, sourceImageHeight);
    }
    
    private void updateHorizontalScaleDownCount(int scaleDownCount, int sourceImageWidth){
        scaleDownResampler.setHorizontalScaleDownCount(scaleDownCount);
        scaleDownResultWidth = scaleDownResampler.getResultWidth(sourceImageWidth);
    }
    
    private void updateVerticalScaleDownCount(int scaleDownCount, int sourceImageHeight){
        scaleDownResampler.setVerticalScaleDownCount(scaleDownCount);
        scaleDownResultHeight = scaleDownResampler.getResultHeight(sourceImageHeight);
    }
    
    private Image scaleDown(Image sourceImage, Progress progress) throws Exception {
        if(scaleDownResampler.getHorizontalScaleDownCount() <= 0 && scaleDownResampler.getVerticalScaleDownCount() <= 0) return sourceImage;
        Image destinationImage = new Image(scaleDownResultWidth, scaleDownResultHeight);
        scaleDownResampler.resample(sourceImage, destinationImage, progress);
        return destinationImage;
    }

    @Override
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight, int destinationImageWidth, int destinationImageHeight) {
        int progress = 0;
        
        if(getScaleUp() != null){
            adjustScaleUp(sourceImageWidth, sourceImageHeight, destinationImageWidth, destinationImageHeight);
            progress += scaleUpResampler.getProgressStepCount(sourceImageWidth, sourceImageHeight);
        }
        
        if(getScaleDown() != null){
            adjustScaleDown(sourceImageWidth, sourceImageHeight, destinationImageWidth, destinationImageHeight);
            progress += scaleDownResampler.getProgressStepCount(sourceImageWidth, sourceImageHeight);
        }
        
        progress += stretchResampler.getProgressStepCount(sourceImageWidth, sourceImageHeight, destinationImageWidth, destinationImageHeight);
        return progress;
    }

    @Override
    public String toString() {
        return "Composite resampler";
    }
}
