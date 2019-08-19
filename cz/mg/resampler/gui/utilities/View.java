package cz.mg.resampler.gui.utilities;

import cz.mg.resampler.Image;
import cz.mg.resampler.gui.Configuration;
import cz.mg.resampler.resamplers.java.utilities.ComputationUtilities;
import cz.mg.resampler.resamplers.java.utilities.IntegerRegion;


public class View {
    private static final int SOURCE_EXPAND_SIZE = 2;
    
    private float rx = 0.5f;
    private float ry = 0.5f;
    private int zoom = 0;
    
    private int sw = 1;
    private int sh = 1;
    private int dw = 2;
    private int dh = 2;

    public View() {
    }
    
    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        if(rx < 0) rx = 0;
        if(rx > 1) rx = 1;
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        if(ry < 0) ry = 0;
        if(ry > 1) ry = 1;
        this.ry = ry;
    }

    public int getSourceWidth() {
        return sw;
    }

    public void setSourceWidth(int sw) {
        if(sw < 1) sw = 1;
        this.sw = sw;
    }

    public int getSourceHeight() {
        return sh;
    }

    public void setSourceHeight(int sh) {
        if(sh < 1) sh = 1;
        this.sh = sh;
    }

    public int getDestinationWidth() {
        return dw;
    }

    public void setDestinationWidth(int dw) {
        if(dw < 2) dw = 2;
        this.dw = dw;
    }

    public int getDestinationHeight() {
        return dh;
    }

    public void setDestinationHeight(int dh) {
        if(dh < 2) dh = 2;
        this.dh = dh;
    }
    
    public void zoomIn(){
        zoom++;
        if(zoom > getMaxZoom()) zoom = getMaxZoom();
    }
    
    public void zoomOut(){
        zoom--;
        if(zoom < getMinZoom()) zoom = getMinZoom();
    }
    
    public void zoomReset(){
        zoom = 0;
    }
    
    public void zoomFit() {
        rx = 0.5f;
        ry = 0.5f;
        
        int dx = sourceToDestinationX_F2I(0.0f);
        int dy = sourceToDestinationY_F2I(0.0f);
        
        while(dx > 0 || dy > 0){
            zoomIn();
            if(zoom == getMaxZoom()) break;
            dx = sourceToDestinationX_F2I(0.0f);
            dy = sourceToDestinationY_F2I(0.0f);
        }
        
        dx = sourceToDestinationX_F2I(0.0f);
        dy = sourceToDestinationY_F2I(0.0f);
        
        while(dx < 0 || dy < 0){
            zoomOut();
            if(zoom == getMinZoom()) break;
            dx = sourceToDestinationX_F2I(0.0f);
            dy = sourceToDestinationY_F2I(0.0f);
        }
    }
    
    public void zoomIn(int mx, int my){
        float sx0 = destinationToSourceX_I2F(mx);
        float sy0 = destinationToSourceY_I2F(my);
        
        zoom++;
        if(zoom > getMaxZoom()) zoom = getMaxZoom();
        
        float sx1 = destinationToSourceX_I2F(mx);
        float sy1 = destinationToSourceY_I2F(my);
        
        moveBySource(sx1 - sx0, sy1 - sy0);
    }
    
    public void zoomOut(int mx, int my){
        float sx0 = destinationToSourceX_I2F(mx);
        float sy0 = destinationToSourceY_I2F(my);
        
        zoom--;
        if(zoom < getMinZoom()) zoom = getMinZoom();
        
        float sx1 = destinationToSourceX_I2F(mx);
        float sy1 = destinationToSourceY_I2F(my);
        
        moveBySource(sx1 - sx0, sy1 - sy0);
    }
    
    public void zoomReset(int mx, int my){
        float sx0 = destinationToSourceX_I2F(mx);
        float sy0 = destinationToSourceY_I2F(my);
        
        zoom = 0;
        
        float sx1 = destinationToSourceX_I2F(mx);
        float sy1 = destinationToSourceY_I2F(my);
        
        moveBySource(sx1 - sx0, sy1 - sy0);
    }
        
    public float getZoom(){
        if(zoom > 0){
            return zoom + 1;
        } else if(zoom < 0){
            return (1.0f / (-zoom + 1));
        } else {
            return 1.0f;
        }
    }
    
    private int getMinZoom(){
        return Configuration.getInstance().getMinZoom() + 1;
    }
    
    private int getMaxZoom(){
        return Configuration.getInstance().getMaxZoom() - 1;
    }
    
    public void moveBySource(float sdx, float sdy){
        float rdx = sdx / sw;
        float rdy = sdy / sh;
        setRx(rx - rdx);
        setRy(ry - rdy);
    }
    
    public Image[] getImages(Image source, Image destination){
        return getImagesOptimized(source, destination);
    }
    
    private Image[] getImagesOptimized(Image source, Image destination){
        IntegerRegion intersection = getDstIntersectionRegion();
        if(intersection == null) return null;
        IntegerRegion srcRegion = expandSrcRegion(dstRegionToSrcRegion(intersection));
        IntegerRegion dstRegion = srcRegionToDstRegion(srcRegion);
        return new Image[]{
            getSrcImageFull(source).subimage(srcRegion.getX(), srcRegion.getY(), srcRegion.getWidth(), srcRegion.getHeight()),
            getDstImageFull(destination).subimage(dstRegion.getX(), dstRegion.getY(), dstRegion.getWidth(), dstRegion.getHeight())
        };
    }
    
    private Image getSrcImageFull(Image source){
        return source;
    }
    
    private Image getDstImageFull(Image destination){
        return destination.subimage(
                poziceVelkehoObrazkuVPaneluX(),
                poziceVelkehoObrazkuVPaneluY(),
                velikostVelkehoObrazkuWidth(),
                velikostVelkehoObrazkuHeight()
        );
    }
    
    private int velikostVelkehoObrazkuWidth(){
        return Math.round(sw*getZoom());
    }
    
    private int velikostVelkehoObrazkuHeight(){
        return Math.round(sh*getZoom());
    }
    
    private int poziceVelkehoObrazkuVPaneluX(){
        float wnx = rx - 0.5f; // pozice kamery převedená z <0;1> do <-1;1>
        int vycentrovaniPaneluX = dw / 2;
        int vycentrovaniObrazkuX = velikostVelkehoObrazkuWidth() / 2;
        int posunKameryX = Math.round(velikostVelkehoObrazkuWidth() * wnx);
        return vycentrovaniPaneluX - vycentrovaniObrazkuX - posunKameryX;
    }
    
    private int poziceVelkehoObrazkuVPaneluY(){
        float wny = ry - 0.5f; // pozice kamery převedená z <0;1> do <-1;1>
        int vycentrovaniPaneluY = dh / 2;
        int vycentrovaniObrazkuY = velikostVelkehoObrazkuHeight() / 2;
        int posunKameryY = Math.round(velikostVelkehoObrazkuHeight() * wny);
        return vycentrovaniPaneluY - vycentrovaniObrazkuY - posunKameryY;
    }
    
    public float destinationToSourceX_I2F(int dx){
        int poziceVeVetsimObrazkuX = dx - poziceVelkehoObrazkuVPaneluX();
        float nx = ComputationUtilities.boxIntegerToNormalized(poziceVeVetsimObrazkuX, velikostVelkehoObrazkuWidth());
        float sx = ComputationUtilities.normalizedToBoxFloat(nx, sw);
        return sx;
    }
    
    public float destinationToSourceY_I2F(int dy){
        int poziceVeVetsimObrazkuY = dy - poziceVelkehoObrazkuVPaneluY();
        float ny = ComputationUtilities.boxIntegerToNormalized(poziceVeVetsimObrazkuY, velikostVelkehoObrazkuHeight());
        float sy = ComputationUtilities.normalizedToBoxFloat(ny, sh);
        return sy;
    }
    
    public int sourceToDestinationX_F2I(float sx){
        float nx = ComputationUtilities.boxFloatToNormalized(sx, sw);
        int poziceVeVetsimObrazkuX = ComputationUtilities.normalizedToBoxInteger(nx, velikostVelkehoObrazkuWidth());
        int dx = poziceVeVetsimObrazkuX + poziceVelkehoObrazkuVPaneluX();
        return dx;
    }
    
    public int sourceToDestinationY_F2I(float sy){
        float ny = ComputationUtilities.boxFloatToNormalized(sy, sh);
        int poziceVeVetsimObrazkuY = ComputationUtilities.normalizedToBoxInteger(ny, velikostVelkehoObrazkuHeight());
        int dy = poziceVeVetsimObrazkuY + poziceVelkehoObrazkuVPaneluY();
        return dy;
    }
    
    public int destinationToSourceX_I2I(int dx){
        int poziceVeVetsimObrazkuX = dx - poziceVelkehoObrazkuVPaneluX();
        float nx = ComputationUtilities.boxIntegerToNormalized(poziceVeVetsimObrazkuX, velikostVelkehoObrazkuWidth());
        int sx = ComputationUtilities.normalizedToBoxInteger(nx, sw);
        return sx;
    }
    
    public int destinationToSourceY_I2I(int dy){
        int poziceVeVetsimObrazkuY = dy - poziceVelkehoObrazkuVPaneluY();
        float ny = ComputationUtilities.boxIntegerToNormalized(poziceVeVetsimObrazkuY, velikostVelkehoObrazkuHeight());
        int sy = ComputationUtilities.normalizedToBoxInteger(ny, sh);
        return sy;
    }
    
    public int sourceToDestinationX_I2I(int sx){
        float nx = ComputationUtilities.boxIntegerToNormalized(sx, sw);
        int poziceVeVetsimObrazkuX = ComputationUtilities.normalizedToBoxInteger(nx, velikostVelkehoObrazkuWidth());
        int dx = poziceVeVetsimObrazkuX + poziceVelkehoObrazkuVPaneluX();
        return dx;
    }
    
    public int sourceToDestinationY_I2I(int sy){
        float ny = ComputationUtilities.boxIntegerToNormalized(sy, sh);
        int poziceVeVetsimObrazkuY = ComputationUtilities.normalizedToBoxInteger(ny, velikostVelkehoObrazkuHeight());
        int dy = poziceVeVetsimObrazkuY + poziceVelkehoObrazkuVPaneluY();
        return dy;
    }
    
    private IntegerRegion getNewImageRegion(){
        return new IntegerRegion(0, 0, velikostVelkehoObrazkuWidth(), velikostVelkehoObrazkuHeight());
    }
    
    private IntegerRegion getViewRegion(){
        return new IntegerRegion(-poziceVelkehoObrazkuVPaneluX(), -poziceVelkehoObrazkuVPaneluY(), dw, dh);
    }
    
    private IntegerRegion getDstIntersectionRegion(){
        return IntegerRegion.intersection(getNewImageRegion(), getViewRegion());
    }
    
    private IntegerRegion dstRegionToSrcRegion(IntegerRegion dstRegion){
        return new IntegerRegion(
                Math.round(dstRegion.getX() / getZoom()),
                Math.round(dstRegion.getY() / getZoom()),
                Math.round(dstRegion.getWidth() / getZoom()),
                Math.round(dstRegion.getHeight() / getZoom())
        );
    }
    
    private IntegerRegion expandSrcRegion(IntegerRegion srcRegion){
        return new IntegerRegion(
                srcRegion.getX() - SOURCE_EXPAND_SIZE,
                srcRegion.getY() - SOURCE_EXPAND_SIZE,
                srcRegion.getWidth() + 2*SOURCE_EXPAND_SIZE,
                srcRegion.getHeight() + 2*SOURCE_EXPAND_SIZE
        );
    }
    
    private IntegerRegion srcRegionToDstRegion(IntegerRegion srcRegion){
        return new IntegerRegion(
                Math.round(srcRegion.getX() * getZoom()),
                Math.round(srcRegion.getY() * getZoom()),
                Math.round(srcRegion.getWidth() * getZoom()),
                Math.round(srcRegion.getHeight() * getZoom())
        );
    }
}
