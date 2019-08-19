package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;
import cz.mg.resampler.Parameter;
import cz.mg.resampler.resamplers.java.Progress;


public abstract class GapFillScaleUp implements UniformScaleUp {
    protected Image sourceImage = null;
    protected Image destinationImage = null;
    private boolean fixBorders = true;
    
    @Parameter(order = -1)
    public boolean isFixBorders() {
        return fixBorders;
    }

    @Parameter(order = -1)
    public void setFixBorders(boolean fixBorders) {
        this.fixBorders = fixBorders;
    }
    
    @Override
    public void scaleUp(Image sourceImage, Image destinationImage, Progress progress) throws InterruptedException {
        if(destinationImage.getWidth() != getResultWidth(sourceImage.getWidth())) throw new RuntimeException("Invalid destination image width for resampler. Expected " + getResultWidth(sourceImage.getWidth()) + " got " + destinationImage.getWidth() + ".");
        if(destinationImage.getHeight() != getResultHeight(sourceImage.getHeight())) throw new RuntimeException("Invalid destination image height for resampler. Expected " + getResultHeight(sourceImage.getHeight()) + " got " + destinationImage.getHeight()+ ".");
        try {
            this.sourceImage = sourceImage;
            this.destinationImage = destinationImage;
            init();
            resolveKnownPixels(progress);
            resolveDiagonalPixels(progress);
            resolveAxialPixels(progress);
        } finally {
            this.sourceImage = null;
            this.destinationImage = null;
        }
    }
    
    @Override
    public int getResultWidth(int sourceWidth) {
        return sourceWidth * 2 - 1;
    }

    @Override
    public int getResultHeight(int sourceHeight) {
        return sourceHeight * 2 - 1;
    }
    
    @Override
    public int getProgressStepCount(int sourceImageWidth, int sourceImageHeight) {
        return getResultWidth(sourceImageWidth) * getResultHeight(sourceImageHeight);
    }
    
    @Override
    public int getDeltaAbsoluteResultSize() {
        return -1;
    }
    
    private void resolveKnownPixels(Progress progress) throws InterruptedException {
        for(int y = 0; y < destinationImage.getHeight(); y += 2){
            for(int x = 0; x < destinationImage.getWidth(); x += 2){
                if(progress != null) progress.step();
                destinationImage.getWriteColor(x, y).set(sourceImage.getReadColor(x/2, y/2));
            }
        }
    }

    private void resolveDiagonalPixels(Progress progress) throws InterruptedException {
        for(int y = 1; y < destinationImage.getHeight(); y += 2){
            for(int x = 1; x < destinationImage.getWidth(); x += 2){
                if(progress != null) progress.step();
                ColorRgba dst = destinationImage.getWriteColor(x, y);
                resolveDiagonalPixel(x, y, dst);
            }
        }
    }

    private void resolveAxialPixels(Progress progress) throws InterruptedException {
        for(int y = 0; y < destinationImage.getHeight(); y += 2){
            for(int x = 1; x < destinationImage.getWidth(); x += 2){
                if(progress != null) progress.step();
                ColorRgba dst = destinationImage.getWriteColor(x, y);
                if(fixBorders) if(x == 0 || x == destinationImage.getWidth()-1) resolveHorizontallyUnsolveableAxialPixel(x, y, dst);
                if(fixBorders) if(y == 0 || y == destinationImage.getHeight()-1) resolveVerticallyUnsolveableAxialPixel(x, y, dst);
                resolveAxialPixel(x, y, dst);
            }
        }
        for(int y = 1; y < destinationImage.getHeight(); y += 2){
            for(int x = 0; x < destinationImage.getWidth(); x += 2){
                if(progress != null) progress.step();
                ColorRgba dst = destinationImage.getWriteColor(x, y);
                if(fixBorders) if(x == 0 || x == destinationImage.getWidth()-1) resolveHorizontallyUnsolveableAxialPixel(x, y, dst);
                if(fixBorders) if(y == 0 || y == destinationImage.getHeight()-1) resolveVerticallyUnsolveableAxialPixel(x, y, dst);
                resolveAxialPixel(x, y, dst);
            }
        }
    }
    
    private void resolveVerticallyUnsolveableAxialPixel(int x, int y, ColorRgba dst){
        ColorRgba left = destinationImage.getReadColor(x-1, y);
        ColorRgba right = destinationImage.getReadColor(x+1, y);
        dst.r = (left.r + right.r) / 2;
        dst.g = (left.g + right.g) / 2;
        dst.b = (left.b + right.b) / 2;
        dst.a = (left.a + right.a) / 2;
    }
    
    private void resolveHorizontallyUnsolveableAxialPixel(int x, int y, ColorRgba dst){
        ColorRgba up = destinationImage.getReadColor(x, y-1);
        ColorRgba down = destinationImage.getReadColor(x, y+1);
        dst.r = (up.r + down.r) / 2;
        dst.g = (up.g + down.g) / 2;
        dst.b = (up.b + down.b) / 2;
        dst.a = (up.a + down.a) / 2;
    }
    
    protected abstract void init();
    protected abstract void resolveDiagonalPixel(int x, int y, ColorRgba dst);
    protected abstract void resolveAxialPixel(int x, int y, ColorRgba dst);
}
