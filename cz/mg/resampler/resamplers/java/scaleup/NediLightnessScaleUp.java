package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.resamplers.java.other.HighQualityMatrix;
import cz.mg.resampler.resamplers.java.other.LowQualityMatrix;
import cz.mg.resampler.resamplers.java.other.Matrix;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;
import cz.mg.resampler.resamplers.java.greyscale.Greyscale;
import cz.mg.resampler.resamplers.java.greyscale.LightnessGreyscale;


/**
 *  source: http://chiranjivi.tripod.com/EDITut.html
 */
public class NediLightnessScaleUp extends GapFillScaleUp {
    private static final int WINDOW_SIZE = 5;
    private static final int NEIGHBOUR_COUNT = 4;
    private static final float MIN_COMPUTATION_CORRECTION_1 = 0;
    private static final float MAX_COMPUTATION_CORRECTION_1 = 8;
    private static final float MIN_COMPUTATION_CORRECTION_2 = 0;
    private static final float MAX_COMPUTATION_CORRECTION_2 = 8;
    
    private float computationCorrection1 = 0;
    private float computationCorrection2 = 0;
    private float c0min, c0max;
    private float c1min, c1max;
    private float c2;
    private boolean highQualityMatrix = true;
    private Greyscale greyscale = new LightnessGreyscale();

    public NediLightnessScaleUp() {
    }

    public NediLightnessScaleUp(float computationCorrection1, float computationCorrection2, boolean highQualityMatrixInverse, Greyscale greyscale) {
        this.computationCorrection1 = computationCorrection1;
        this.computationCorrection2 = computationCorrection2;
        this.highQualityMatrix = highQualityMatrixInverse;
        this.greyscale = greyscale;
    }
    
    @Parameter(order = 3)
    public Greyscale getGreyscale() {
        return greyscale;
    }

    @Parameter(order = 3)
    public void setGreyscale(Greyscale greyscale) {
        this.greyscale = greyscale;
    }

    @Parameter(order = 0)
    public float getComputationCorrection1() {
        return computationCorrection1;
    }

    @Parameter(order = 0)
    public void setComputationCorrection1(float computationCorrection1) {
        if(computationCorrection1 < MIN_COMPUTATION_CORRECTION_1) computationCorrection1 = MIN_COMPUTATION_CORRECTION_1;
        if(computationCorrection1 > MAX_COMPUTATION_CORRECTION_1) computationCorrection1 = MAX_COMPUTATION_CORRECTION_1;
        this.computationCorrection1 = computationCorrection1;
    }
    
    @Parameter(order = 1)
    public float getComputationCorrection2() {
        return computationCorrection2;
    }

    @Parameter(order = 1)
    public void setComputationCorrection2(float computationCorrection1) {
        if(computationCorrection1 < MIN_COMPUTATION_CORRECTION_2) computationCorrection1 = MIN_COMPUTATION_CORRECTION_2;
        if(computationCorrection1 > MAX_COMPUTATION_CORRECTION_2) computationCorrection1 = MAX_COMPUTATION_CORRECTION_2;
        this.computationCorrection2 = computationCorrection1;
    }

    @Parameter(order = 2)
    public boolean isHighQualityMatrix() {
        return highQualityMatrix;
    }

    @Parameter(order = 2)
    public void setHighQualityMatrix(boolean highQualityMatrix) {
        this.highQualityMatrix = highQualityMatrix;
    }
    
    private void computeCorrections(){
        float c = (float) Math.pow(0.1, computationCorrection1);
        c0min = 0.0f - c;
        c0max = 0.0f + c;
        c1min = 1.0f - c;
        c1max = 1.0f + c;
        c2 = (float) Math.pow(0.1, MAX_COMPUTATION_CORRECTION_2 - computationCorrection2 + MIN_COMPUTATION_CORRECTION_1);
    }

    @Override
    public int getWindowHalfSize() {
        if(!isFixBorders()) return 0;
        return (WINDOW_SIZE - 1) / 2;
    }

    @Override
    public String toString() {
        return "NEDI L";
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    
    
    private float getSrcValue(int x, int y){
        return greyscale.compute(sourceImage.getReadColor(x, y));
    }
    
    private ColorRgba getDst(int x, int y){
        return destinationImage.getReadColor(x, y);
    }
    
    private Matrix createMatrix(int w, int h){
        if(highQualityMatrix){
            return new HighQualityMatrix(w, h);
        } else {
            return new LowQualityMatrix(w, h);
        }
    }
    
    private Matrix createMatrix(int w, int h, float[] data){
        if(highQualityMatrix){
            return new HighQualityMatrix(w, h, data);
        } else {
            return new LowQualityMatrix(w, h, data);
        }
    }

    
    ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void init() {
        computeCorrections();
    }
    
    @Override
    protected void resolveDiagonalPixel(int x, int y, ColorRgba dst) {
        float[] w = computeDiagonalWeights(x, y);
        dst.set(mix(
                getDst(x-1, y-1), w[0], getDst(x+1, y-1), w[1],
                getDst(x-1, y+1), w[2], getDst(x+1, y+1), w[3]
        ));
    }
    
    @Override
    protected void resolveAxialPixel(int x, int y, ColorRgba dst){
        float[] w = computeAxialWeights(x, y);
        dst.set(mix(
                                       getDst(x+0, y-1), w[0],
                getDst(x-1, y+0), w[1],                       getDst(x+1, y+0), w[2],
                                       getDst(x+0, y+1), w[3]
        ));
    }
    
    private static ColorRgba mix(ColorRgba a, float wa, ColorRgba b, float wb, ColorRgba c, float wc, ColorRgba d, float wd){
        return new ColorRgba(
                (a.r*wa + b.r*wb + c.r*wc + d.r*wd) / (wa+wb+wc+wd),
                (a.g*wa + b.g*wb + c.g*wc + d.g*wd) / (wa+wb+wc+wd),
                (a.b*wa + b.b*wb + c.b*wc + d.b*wd) / (wa+wb+wc+wd),
                (a.a*wa + b.a*wb + c.a*wc + d.a*wd) / (wa+wb+wc+wd)
        );
    }
    
    private float[] computeDiagonalWeights(int lx, int ly){
        float[] window = getValueWindow(lx, ly);
        float[][] neighbourMatrix = getNeighboursMatrixDiagonal(lx, ly);
        return resolveWeights(window, neighbourMatrix);
    }
    
    private float[] computeAxialWeights(int lx, int ly){
        float[] window = getValueWindow(lx, ly);
        float[][] neighbourMatrix = getNeighboursMatrixAxial(lx, ly);
        return resolveWeights(window, neighbourMatrix);
    }
    
    private float[] getValueWindow(int lx, int ly){
        int x = lx / 2;
        int y = ly / 2;
        int dx = WINDOW_SIZE / 2;
        int dy = WINDOW_SIZE / 2;
        float[] window = new float[WINDOW_SIZE * WINDOW_SIZE];
        for(int wy = 0; wy < WINDOW_SIZE; wy++){
            for(int wx = 0; wx < WINDOW_SIZE; wx++){
                int ix = x + wx - dx;
                int iy = y + wy - dy;
                int iw = wx+wy*WINDOW_SIZE;
                window[iw] = getSrcValue(ix, iy);
            }
        }
        return window;
    }
    
    private float[][] getNeighboursMatrixDiagonal(int lx, int ly){
        int x = lx / 2;
        int y = ly / 2;
        int dx = WINDOW_SIZE / 2;
        int dy = WINDOW_SIZE / 2;
        float[][] neighbourMatrix = new float[WINDOW_SIZE * WINDOW_SIZE][];
        for(int wx = 0; wx < WINDOW_SIZE; wx++){
            for(int wy = 0; wy < WINDOW_SIZE; wy++){
                int ix = x + wx - dx;
                int iy = y + wy - dy;
                int iw = wx+wy*WINDOW_SIZE;
                neighbourMatrix[iw] = getNeighboursDiagonal(ix, iy);
            }
        }
        return neighbourMatrix;
    }
    
    private float[][] getNeighboursMatrixAxial(int lx, int ly){
        int x = lx / 2;
        int y = ly / 2;
        int dx = WINDOW_SIZE / 2;
        int dy = WINDOW_SIZE / 2;
        float[][] neighbourMatrix = new float[WINDOW_SIZE * WINDOW_SIZE][];
        for(int wx = 0; wx < WINDOW_SIZE; wx++){
            for(int wy = 0; wy < WINDOW_SIZE; wy++){
                int ix = x + wx - dx;
                int iy = y + wy - dy;
                int iw = wx+wy*WINDOW_SIZE;
                neighbourMatrix[iw] = getNeighboursAxial(ix, iy);
            }
        }
        return neighbourMatrix;
    }
    
    private float[] getNeighboursDiagonal(int x, int y){
        float[] neighbours = new float[4];
        neighbours[0] = getSrcValue(x-1, y-1);
        neighbours[1] = getSrcValue(x+1, y-1);
        neighbours[2] = getSrcValue(x-1, y+1);
        neighbours[3] = getSrcValue(x+1, y+1);
        return neighbours;
    }
    
    private float[] getNeighboursAxial(int x, int y){
        float[] neighbours = new float[4];
        neighbours[0] = getSrcValue(x, y-1);
        neighbours[1] = getSrcValue(x-1, y);
        neighbours[2] = getSrcValue(x+1, y);
        neighbours[3] = getSrcValue(x, y+1);
        return neighbours;
    }
    
    private float[] resolveWeights(float[] window, float[][] neighbourMatrix){
        // R = (1 / M*M) * CT * C
        // r = (1 / M*M) * CT * y
        // a = (1 / R) * r = (1 / (CT * c)) * (CT * y)
        
        Matrix y = createMatrix(1, WINDOW_SIZE*WINDOW_SIZE, window);
        Matrix c = createMatrix(NEIGHBOUR_COUNT, WINDOW_SIZE*WINDOW_SIZE);
        for(int iy = 0; iy < c.h; iy++) for(int x = 0; x < c.w; x++) c.data[x + iy*c.w] = neighbourMatrix[iy][x];
        Matrix ct = c.transpose();
        
        Matrix t1Original = ct.multiply(c);
        Matrix t1 = t1Original.inverse();
        if(invalidInverseMatrix(t1Original, t1)) return new float[]{0.25f, 0.25f, 0.25f, 0.25f};
        
        Matrix t2 = ct.multiply(y);
        Matrix a = t1.multiply(t2);
        fixWeights(a.data);
        return a.data;
    }
    
    private boolean invalidInverseMatrix(Matrix originalMatrix, Matrix inverseMatrix){
        if(inverseMatrix == null) return true;
        Matrix ones = originalMatrix.multiply(inverseMatrix);
        for(int ix = 0; ix < 4; ix++){
            for(int iy = 0; iy < 4; iy++){
                float value = ones.data[ix*4+iy];
                if(ix == iy){
                    if(value < c1min || value > c1max) return true;
                } else {
                    if(value < c0min || value > c0max) return true;
                }
            }
        }
        return false;
    }
    
    private void fixWeights(float[] weights){
        float sum = 0;
        for(int i = 0; i < weights.length; i++) sum += weights[i];
        if(sum < c2) for(int i = 0; i < weights.length; i++) weights[i] = 0.25f;
    }
}
