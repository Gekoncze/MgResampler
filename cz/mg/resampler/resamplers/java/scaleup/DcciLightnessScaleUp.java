package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.Parameter;
import cz.mg.resampler.resamplers.java.greyscale.Greyscale;
import cz.mg.resampler.resamplers.java.greyscale.LightnessGreyscale;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


/**
 *  https://en.wikipedia.org/wiki/Directional_Cubic_Convolution_Interpolation
 *  https://www.mathworks.com/matlabcentral/fileexchange/38570-image-zooming-using-directional-cubic-convolution-interpolation
 */
public class DcciLightnessScaleUp extends GapFillScaleUp {
    private static final int DIAGONAL_WINDOW_SIZE = 4;
    private static final int AXIAL_WINDOW_SIZE = 7;
    
    private Greyscale greyscale = new LightnessGreyscale();
    
    public DcciLightnessScaleUp() {
    }

    public DcciLightnessScaleUp(Greyscale greyscale) {
        this.greyscale = greyscale;
    }

    @Parameter
    public Greyscale getGreyscale() {
        return greyscale;
    }

    @Parameter
    public void setGreyscale(Greyscale greyscale) {
        this.greyscale = greyscale;
    }

    @Override
    public int getWindowHalfSize() {
        return DIAGONAL_WINDOW_SIZE / 2;
    }
    
    @Override
    public String toString() {
        return "DCCI L";
    }
    
    
    ////////////////////////////////////////////////////////////////////////////

    
    private ColorRgba getSrc(int x, int y){
        return sourceImage.getReadColor(x, y);
    }
    
    private ColorRgba getDst(int x, int y){
        if(x < 0 || y < 0 || x > destinationImage.getWidth() || y > destinationImage.getHeight()) return new ColorRgba(getSrc(x/2, y/2));
        return destinationImage.getReadColor(x, y);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void init() {
    }
    
    @Override
    protected void resolveDiagonalPixel(int x, int y, ColorRgba dst) {
        dst.set(resolveDiagonalValue(x, y));
    }
    
    @Override
    protected void resolveAxialPixel(int x, int y, ColorRgba dst) {
        dst.set(resolveAxialValue(x, y));
    }
    
    private static ColorRgba mix(ColorRgba a, float wa, ColorRgba b, float wb){
        return new ColorRgba(
                (a.r*wa + b.r*wb),
                (a.g*wa + b.g*wb),
                (a.b*wa + b.b*wb),
                (a.a*wa + b.a*wb)
        );
    }
    
    private static ColorRgba mix(ColorRgba a, float wa, ColorRgba b, float wb, ColorRgba c, float wc, ColorRgba d, float wd){
        return new ColorRgba(
                (a.r*wa + b.r*wb + c.r*wc + d.r*wd),
                (a.g*wa + b.g*wb + c.g*wc + d.g*wd),
                (a.b*wa + b.b*wb + c.b*wc + d.b*wd),
                (a.a*wa + b.a*wb + c.a*wc + d.a*wd)
        );
    }
    
    private float l(ColorRgba c){
        return greyscale.compute(c);
    }
    
    private ColorRgba resolveDiagonalValue(int x, int y){
        ColorRgba[][] w = getDiagonalWindow(x, y);
        float d1 = 0;
        for(int ix = 1; ix <= 3; ix++){
            for(int iy = 0; iy<= 2; iy++){
                d1 += Math.abs(l(w[ix][iy]) - l(w[ix-1][iy+1]));
            }
        }
        float d2 = 0;
        for(int ix = 0; ix <= 2; ix++){
            for(int iy = 0; iy <= 2; iy++){
                d2 += Math.abs(l(w[ix][iy]) - l(w[ix+1][iy+1]));
            }
        }
        if((1.0f + d1) / (1.0f + d2) > 1.15f) return mix(w[0][0], -1.0f/16.0f, w[1][1], +9.0f/16.0f, w[2][2], +9.0f/16.0f, w[3][3], -1.0f/16.0f); // up-right direction
        if((1.0f + d2) / (1.0f + d1) > 1.15f) return mix(w[3][0], -1.0f/16.0f, w[2][1], +9.0f/16.0f, w[1][2], +9.0f/16.0f, w[0][3], -1.0f/16.0f); // down-right direction
        float w1 = 1.0f / (1.0f + (float)Math.pow(d1, 5.0f));
        float w2 = 1.0f / (1.0f + (float)Math.pow(d2, 5.0f));
        float weight1 = w1 / (w1 + w2);
        float weight2 = w2 / (w1 + w2);
        ColorRgba downRightPixel = mix(w[0][0], -1.0f/16.0f, w[1][1], +9.0f/16.0f, w[2][2], +9.0f/16.0f, w[3][3], -1.0f/16.0f);
        ColorRgba upRightPixel   = mix(w[3][0], -1.0f/16.0f, w[2][1], +9.0f/16.0f, w[1][2], +9.0f/16.0f, w[0][3], -1.0f/16.0f);
        return mix(downRightPixel, weight1, upRightPixel, weight2);
    }
    
    private ColorRgba resolveAxialValue(int x, int y){
        ColorRgba[][] w = getAxialWindow(x, y);
        int ix = 3;
        int iy = 3;
        float d1 = Math.abs(l(w[ix+1][iy-2]) - l(w[ix-1][iy-2])) +
                   Math.abs(l(w[ix+2][iy-1]) - l(w[ix+0][iy-1])) + Math.abs(l(w[ix+0][iy-1]) - l(w[ix-2][iy-1])) +
                   Math.abs(l(w[ix+3][iy+0]) - l(w[ix+1][iy+0])) + Math.abs(l(w[ix+1][iy+0]) - l(w[ix-1][iy+0])) + Math.abs(l(w[ix-1][iy+0]) - l(w[ix-3][iy+0])) +
                   Math.abs(l(w[ix+2][iy+1]) - l(w[ix+0][iy+1])) + Math.abs(l(w[ix+0][iy+1]) - l(w[ix-2][iy+1])) +
                   Math.abs(l(w[ix+1][iy+2]) - l(w[ix-1][iy+2]));
        float d2 = Math.abs(l(w[ix-2][iy+1]) - l(w[ix-2][iy-1])) +
                   Math.abs(l(w[ix-1][iy+2]) - l(w[ix-1][iy+0])) + Math.abs(l(w[ix-1][iy+0]) - l(w[ix-1][iy-2])) +
                   Math.abs(l(w[ix+0][iy+3]) - l(w[ix+0][iy+1])) + Math.abs(l(w[ix+0][iy+1]) - l(w[ix+0][iy-1])) + Math.abs(l(w[ix+0][iy-1]) - l(w[ix+0][iy-3])) +
                   Math.abs(l(w[ix+1][iy+2]) - l(w[ix+1][iy+0])) + Math.abs(l(w[ix+1][iy+0]) - l(w[ix+1][iy-2])) +
                   Math.abs(l(w[ix+2][iy+1]) - l(w[ix+2][iy-1]));
        if((1.0f+d1) / (1.0f+d2) > 1.15f) return mix(w[ix+0][iy-3], -1.0f/16.0f, w[ix+0][iy-1], +9.0f/16.0f, w[ix+0][iy+1], +9.0f/16.0f, w[ix+0][iy+3], -1.0f/16.0f); // horizontal direction
        if((1.0f+d2) / (1.0f+d1) > 1.15f) return mix(w[ix-3][iy+0], -1.0f/16.0f, w[ix-1][iy+0], +9.0f/16.0f, w[ix+1][iy+0], +9.0f/16.0f, w[ix+3][iy+0], -1.0f/16.0f); // vertical direction
        float w1 = 1.0f / (1.0f + (float)Math.pow(d1, 5.0f));
        float w2 = 1.0f / (1.0f + (float)Math.pow(d2, 5.0f));
        float weight1 = w1 / (w1+w2);
        float weight2 = w2 / (w1+w2);
        ColorRgba horizontalPixel = mix(w[ix-3][iy+0], -1.0f/16.0f, w[ix-1][iy+0], +9.0f/16.0f, w[ix+1][iy+0], +9.0f/16.0f, w[ix+3][iy+0], -1.0f/16.0f);
        ColorRgba verticalPixel   = mix(w[ix+0][iy-3], -1.0f/16.0f, w[ix+0][iy-1], +9.0f/16.0f, w[ix+0][iy+1], +9.0f/16.0f, w[ix+0][iy+3], -1.0f/16.0f);
        return mix(verticalPixel, weight1, horizontalPixel, weight2);
    }
    
    private ColorRgba[][] getDiagonalWindow(int x, int y){
        ColorRgba[][] window = new ColorRgba[DIAGONAL_WINDOW_SIZE][DIAGONAL_WINDOW_SIZE];
        window[0][0] = getDst(x-3, y-3); window[1][0] = getDst(x-1, y-3); window[2][0] = getDst(x+1, y-3); window[3][0] = getDst(x+3, y-3);
        window[0][1] = getDst(x-3, y-1); window[1][1] = getDst(x-1, y-1); window[2][1] = getDst(x+1, y-1); window[3][1] = getDst(x+3, y-1);
        window[0][2] = getDst(x-3, y+1); window[1][2] = getDst(x-1, y+1); window[2][2] = getDst(x+1, y+1); window[3][2] = getDst(x+3, y+1);
        window[0][3] = getDst(x-3, y+3); window[1][3] = getDst(x-1, y+3); window[2][3] = getDst(x+1, y+3); window[3][3] = getDst(x+3, y+3);
        return window;
    }
    
    private ColorRgba[][] getAxialWindow(int x, int y){
        ColorRgba[][] window = new ColorRgba[AXIAL_WINDOW_SIZE][AXIAL_WINDOW_SIZE];
        /*ndow[0][0] = getDst(x-3, y-3*//*ndow[1][0] = getDst(x-2, y-3*//*ndow[2][0] = getDst(x-1, y-3*/window[3][0] = getDst(x+0, y-3);/*ndow[4][0] = getDst(x+1, y-3*//*ndow[5][0] = getDst(x+2, y-3*//*ndow[6][0] = getDst(x+3, y-3*/
        /*ndow[0][1] = getDst(x-3, y-2*//*ndow[1][1] = getDst(x-2, y-2*/window[2][1] = getDst(x-1, y-2);/*ndow[3][1] = getDst(x+0, y-2*/window[4][1] = getDst(x+1, y-2);/*ndow[5][1] = getDst(x+2, y-2*//*ndow[6][1] = getDst(x+3, y-2*/
        /*ndow[0][2] = getDst(x-3, y-1*/window[1][2] = getDst(x-2, y-1);/*ndow[2][2] = getDst(x-1, y-1*/window[3][2] = getDst(x+0, y-1);/*ndow[4][2] = getDst(x+1, y-1*/window[5][2] = getDst(x+2, y-1);/*ndow[6][2] = getDst(x+3, y-1*/
        window[0][3] = getDst(x-3, y+0);/*ndow[1][3] = getDst(x-2, y+0*/window[2][3] = getDst(x-1, y+0);/*ndow[3][3] = getDst(x+0, y+0*/window[4][3] = getDst(x+1, y+0);/*ndow[5][3] = getDst(x+2, y+0*/window[6][3] = getDst(x+3, y+0);
        /*ndow[0][4] = getDst(x-3, y+1*/window[1][4] = getDst(x-2, y+1);/*ndow[2][4] = getDst(x-1, y+1*/window[3][4] = getDst(x+0, y+1);/*ndow[4][4] = getDst(x+1, y+1*/window[5][4] = getDst(x+2, y+1);/*ndow[6][4] = getDst(x+3, y+1*/
        /*ndow[0][5] = getDst(x-3, y+2*//*ndow[1][5] = getDst(x-2, y+2*/window[2][5] = getDst(x-1, y+2);/*ndow[3][5] = getDst(x+0, y+2*/window[4][5] = getDst(x+1, y+2);/*ndow[5][5] = getDst(x+2, y+2*//*ndow[6][5] = getDst(x+3, y+2*/
        /*ndow[0][6] = getDst(x-3, y+3*//*ndow[1][6] = getDst(x-2, y+3*//*ndow[2][6] = getDst(x-1, y+3*/window[3][6] = getDst(x+0, y+3);/*ndow[4][6] = getDst(x+1, y+3*//*ndow[5][6] = getDst(x+2, y+3*//*ndow[6][6] = getDst(x+3, y+3*/
        return window;
    }
}
