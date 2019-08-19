package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


/**
 *  https://en.wikipedia.org/wiki/Directional_Cubic_Convolution_Interpolation
 *  https://www.mathworks.com/matlabcentral/fileexchange/38570-image-zooming-using-directional-cubic-convolution-interpolation
 */
public class DcciRgbaScaleUp extends GapFillScaleUp {
    private static final int DIAGONAL_WINDOW_SIZE = 4;
    private static final int AXIAL_WINDOW_SIZE = 7;

    public DcciRgbaScaleUp() {
    }

    @Override
    public int getWindowHalfSize() {
        return DIAGONAL_WINDOW_SIZE / 2;
    }
    
    @Override
    public String toString() {
        return "DCCI RGBA";
    }
    
    
    ////////////////////////////////////////////////////////////////////////////

    
    private float getSrc(int x, int y, int offset){
        ColorRgba c = sourceImage.getReadColor(x, y);
        switch(offset){
            case 0: return c.r;
            case 1: return c.g;
            case 2: return c.b;
            case 3: return c.a;
        }
        throw new RuntimeException();
    }
    
    private float getDst(int x, int y, int offset){
        if(x < 0 || y < 0 || x > destinationImage.getWidth() || y > destinationImage.getHeight()) return getSrc(x/2, y/2, offset);
        ColorRgba c = destinationImage.getReadColor(x, y);
        switch(offset){
            case 0: return c.r;
            case 1: return c.g;
            case 2: return c.b;
            case 3: return c.a;
        }
        throw new RuntimeException();
    }
    
    ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void init() {
    }
    
    @Override
    protected void resolveDiagonalPixel(int x, int y, ColorRgba dst) {
        dst.r = crop(resolveDiagonalValue(x, y, 0));
        dst.g = crop(resolveDiagonalValue(x, y, 1));
        dst.b = crop(resolveDiagonalValue(x, y, 2));
        dst.a = crop(resolveDiagonalValue(x, y, 3));
    }
    
    @Override
    protected void resolveAxialPixel(int x, int y, ColorRgba dst) {
        dst.r = crop(resolveAxialValue(x, y, 0));
        dst.g = crop(resolveAxialValue(x, y, 1));
        dst.b = crop(resolveAxialValue(x, y, 2));
        dst.a = crop(resolveAxialValue(x, y, 3));
    }
    
    private float resolveDiagonalValue(int x, int y, int componentOffset){
        float[][] w = getDiagonalWindow(x, y, componentOffset);
        float d1 = 0;
        for(int ix = 1; ix <= 3; ix++){
            for(int iy = 0; iy<= 2; iy++){
                d1 += Math.abs(w[ix][iy] - w[ix-1][iy+1]);
            }
        }
        float d2 = 0;
        for(int ix = 0; ix <= 2; ix++){
            for(int iy = 0; iy <= 2; iy++){
                d2 += Math.abs(w[ix][iy] - w[ix+1][iy+1]);
            }
        }
        if((1.0f + d1) / (1.0f + d2) > 1.15f) return (-1 * w[0][0] + 9 * w[1][1] + 9 * w[2][2] - 1 * w[3][3]) / 16; // up-right direction
        if((1.0f + d2) / (1.0f + d1) > 1.15f) return (-1 * w[3][0] + 9 * w[2][1] + 9 * w[1][2] - 1 * w[0][3]) / 16; // down-right direction
        float w1 = 1.0f / (1.0f + (float)Math.pow(d1, 5.0f));
        float w2 = 1.0f / (1.0f + (float)Math.pow(d2, 5.0f));
        float weight1 = w1 / (w1 + w2);
        float weight2 = w2 / (w1 + w2);
        float downRightPixel = (-1.0f * w[0][0] + 9.0f * w[1][1] + 9.0f * w[2][2] - 1.0f * w[3][3]) / 16.0f;
        float upRightPixel   = (-1.0f * w[3][0] + 9.0f * w[2][1] + 9.0f * w[1][2] - 1.0f * w[0][3]) / 16.0f;
        return downRightPixel * weight1 + upRightPixel * weight2;
    }
    
    private float resolveAxialValue(int x, int y, int componentOffset){
        float[][] w = getAxialWindow(x, y, componentOffset);
        int ix = 3;
        int iy = 3;
        float d1 = Math.abs(w[ix+1][iy-2] - w[ix-1][iy-2]) +
                   Math.abs(w[ix+2][iy-1] - w[ix+0][iy-1]) + Math.abs(w[ix+0][iy-1] - w[ix-2][iy-1]) +
                   Math.abs(w[ix+3][iy+0] - w[ix+1][iy+0]) + Math.abs(w[ix+1][iy+0] - w[ix-1][iy+0]) + Math.abs(w[ix-1][iy+0] - w[ix-3][iy+0]) +
                   Math.abs(w[ix+2][iy+1] - w[ix+0][iy+1]) + Math.abs(w[ix+0][iy+1] - w[ix-2][iy+1]) +
                   Math.abs(w[ix+1][iy+2] - w[ix-1][iy+2]);
        float d2 = Math.abs(w[ix-2][iy+1] - w[ix-2][iy-1]) +
                   Math.abs(w[ix-1][iy+2] - w[ix-1][iy+0]) + Math.abs(w[ix-1][iy+0] - w[ix-1][iy-2]) +
                   Math.abs(w[ix+0][iy+3] - w[ix+0][iy+1]) + Math.abs(w[ix+0][iy+1] - w[ix+0][iy-1]) + Math.abs(w[ix+0][iy-1] - w[ix+0][iy-3]) +
                   Math.abs(w[ix+1][iy+2] - w[ix+1][iy+0]) + Math.abs(w[ix+1][iy+0] - w[ix+1][iy-2]) +
                   Math.abs(w[ix+2][iy+1] - w[ix+2][iy-1]);
        if((1.0f+d1) / (1.0f+d2) > 1.15f) return (-1.0f * w[ix+0][iy-3] + 9.0f * w[ix+0][iy-1] + 9.0f * w[ix+0][iy+1] - 1.0f * w[ix+0][iy+3]) / 16.0f; // horizontal direction
        if((1.0f+d2) / (1.0f+d1) > 1.15f) return (-1.0f * w[ix-3][iy+0] + 9.0f * w[ix-1][iy+0] + 9.0f * w[ix+1][iy+0] - 1.0f * w[ix+3][iy+0]) / 16.0f; // vertical direction
        float w1 = 1.0f / (1.0f + (float)Math.pow(d1, 5.0f));
        float w2 = 1.0f / (1.0f + (float)Math.pow(d2, 5.0f));
        float weight1 = w1 / (w1+w2);
        float weight2 = w2 / (w1+w2);
        float HorizontalPixel = (-1.0f * w[ix-3][iy+0]+9.0f * w[ix-1][iy+0]+9.0f * w[ix+1][iy+0]-1.0f * w[ix+3][iy+0]) / 16.0f;
        float VerticalPixel   = (-1.0f * w[ix+0][iy-3]+9.0f * w[ix+0][iy-1]+9.0f * w[ix+0][iy+1]-1.0f * w[ix+0][iy+3]) / 16.0f;
        return VerticalPixel * weight1 + HorizontalPixel * weight2;
    }
    
    private float[][] getDiagonalWindow(int x, int y, int componentOffset){
        float[][] window = new float[DIAGONAL_WINDOW_SIZE][DIAGONAL_WINDOW_SIZE];
        window[0][0] = getDst(x-3, y-3, componentOffset); window[1][0] = getDst(x-1, y-3, componentOffset); window[2][0] = getDst(x+1, y-3, componentOffset); window[3][0] = getDst(x+3, y-3, componentOffset);
        window[0][1] = getDst(x-3, y-1, componentOffset); window[1][1] = getDst(x-1, y-1, componentOffset); window[2][1] = getDst(x+1, y-1, componentOffset); window[3][1] = getDst(x+3, y-1, componentOffset);
        window[0][2] = getDst(x-3, y+1, componentOffset); window[1][2] = getDst(x-1, y+1, componentOffset); window[2][2] = getDst(x+1, y+1, componentOffset); window[3][2] = getDst(x+3, y+1, componentOffset);
        window[0][3] = getDst(x-3, y+3, componentOffset); window[1][3] = getDst(x-1, y+3, componentOffset); window[2][3] = getDst(x+1, y+3, componentOffset); window[3][3] = getDst(x+3, y+3, componentOffset);
        return window;
    }
    
    private float[][] getAxialWindow(int x, int y, int componentOffset){
        float[][] window = new float[AXIAL_WINDOW_SIZE][AXIAL_WINDOW_SIZE];
        /*ndow[0][0] = getDst(x-3, y-3, componentOffset*//*ndow[1][0] = getDst(x-2, y-3, componentOffset*//*ndow[2][0] = getDst(x-1, y-3, componentOffset*/window[3][0] = getDst(x+0, y-3, componentOffset);/*ndow[4][0] = getDst(x+1, y-3, componentOffset*//*ndow[5][0] = getDst(x+2, y-3, componentOffset*//*ndow[6][0] = getDst(x+3, y-3, componentOffset*/
        /*ndow[0][1] = getDst(x-3, y-2, componentOffset*//*ndow[1][1] = getDst(x-2, y-2, componentOffset*/window[2][1] = getDst(x-1, y-2, componentOffset);/*ndow[3][1] = getDst(x+0, y-2, componentOffset*/window[4][1] = getDst(x+1, y-2, componentOffset);/*ndow[5][1] = getDst(x+2, y-2, componentOffset*//*ndow[6][1] = getDst(x+3, y-2, componentOffset*/
        /*ndow[0][2] = getDst(x-3, y-1, componentOffset*/window[1][2] = getDst(x-2, y-1, componentOffset);/*ndow[2][2] = getDst(x-1, y-1, componentOffset*/window[3][2] = getDst(x+0, y-1, componentOffset);/*ndow[4][2] = getDst(x+1, y-1, componentOffset*/window[5][2] = getDst(x+2, y-1, componentOffset);/*ndow[6][2] = getDst(x+3, y-1, componentOffset*/
        window[0][3] = getDst(x-3, y+0, componentOffset);/*ndow[1][3] = getDst(x-2, y+0, componentOffset*/window[2][3] = getDst(x-1, y+0, componentOffset);/*ndow[3][3] = getDst(x+0, y+0, componentOffset*/window[4][3] = getDst(x+1, y+0, componentOffset);/*ndow[5][3] = getDst(x+2, y+0, componentOffset*/window[6][3] = getDst(x+3, y+0, componentOffset);
        /*ndow[0][4] = getDst(x-3, y+1, componentOffset*/window[1][4] = getDst(x-2, y+1, componentOffset);/*ndow[2][4] = getDst(x-1, y+1, componentOffset*/window[3][4] = getDst(x+0, y+1, componentOffset);/*ndow[4][4] = getDst(x+1, y+1, componentOffset*/window[5][4] = getDst(x+2, y+1, componentOffset);/*ndow[6][4] = getDst(x+3, y+1, componentOffset*/
        /*ndow[0][5] = getDst(x-3, y+2, componentOffset*//*ndow[1][5] = getDst(x-2, y+2, componentOffset*/window[2][5] = getDst(x-1, y+2, componentOffset);/*ndow[3][5] = getDst(x+0, y+2, componentOffset*/window[4][5] = getDst(x+1, y+2, componentOffset);/*ndow[5][5] = getDst(x+2, y+2, componentOffset*//*ndow[6][5] = getDst(x+3, y+2, componentOffset*/
        /*ndow[0][6] = getDst(x-3, y+3, componentOffset*//*ndow[1][6] = getDst(x-2, y+3, componentOffset*//*ndow[2][6] = getDst(x-1, y+3, componentOffset*/window[3][6] = getDst(x+0, y+3, componentOffset);/*ndow[4][6] = getDst(x+1, y+3, componentOffset*//*ndow[5][6] = getDst(x+2, y+3, componentOffset*//*ndow[6][6] = getDst(x+3, y+3, componentOffset*/
        return window;
    }
    
    private static float crop(float d){
        float f = (float) d;
        if(f < 0) return 0;
        if(f > 1) return 1;
        return f;
    }
}
