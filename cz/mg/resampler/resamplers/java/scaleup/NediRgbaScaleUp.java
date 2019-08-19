package cz.mg.resampler.resamplers.java.scaleup;

import cz.mg.resampler.resamplers.java.other.HighQualityMatrix;
import cz.mg.resampler.resamplers.java.other.LowQualityMatrix;
import cz.mg.resampler.resamplers.java.other.Matrix;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.Parameter;


/**
 *  source: http://chiranjivi.tripod.com/EDITut.html
 */
public class NediRgbaScaleUp extends GapFillScaleUp {
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

    public NediRgbaScaleUp() {
    }

    public NediRgbaScaleUp(float computationCorrection1, float computationCorrection2, boolean highQualityMatrix) {
        this.computationCorrection1 = computationCorrection1;
        this.computationCorrection2 = computationCorrection2;
        this.highQualityMatrix = highQualityMatrix;
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
        return "NEDI RGBA";
    }
    
    
    ////////////////////////////////////////////////////////////////////////////

    
    private float getSrcColor(int x, int y, int offset){
        ColorRgba c = sourceImage.getReadColor(x, y);
        switch(offset){
            case 0: return c.r;
            case 1: return c.g;
            case 2: return c.b;
            case 3: return c.a;
        }
        throw new RuntimeException();
    }
    
    private float getDstColor(int x, int y, int offset){
        ColorRgba c = destinationImage.getReadColor(x, y);
        switch(offset){
            case 0: return c.r;
            case 1: return c.g;
            case 2: return c.b;
            case 3: return c.a;
        }
        throw new RuntimeException();
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
    protected void resolveDiagonalPixel(int lx, int ly, ColorRgba dst) {
        dst.r = crop(resolveDiagonalValue(lx, ly, 0));
        dst.g = crop(resolveDiagonalValue(lx, ly, 1));
        dst.b = crop(resolveDiagonalValue(lx, ly, 2));
        dst.a = crop(resolveDiagonalValue(lx, ly, 3));
    }
    
    @Override
    protected void resolveAxialPixel(int lx, int ly, ColorRgba dst) {
        dst.r = crop(resolveAxialValue(lx, ly, 0));
        dst.g = crop(resolveAxialValue(lx, ly, 1));
        dst.b = crop(resolveAxialValue(lx, ly, 2));
        dst.a = crop(resolveAxialValue(lx, ly, 3));
    }
    
    private float resolveDiagonalValue(int lx, int ly, int componentOffset){
        float[] window = getSourceWindow(lx, ly, componentOffset);
        float[][] neighbourMatrix = getSourceNeighbourMatrixDiagonal(lx, ly, componentOffset);
        float[] neighbours = new float[]{
            getDstColor(lx-1, ly-1, componentOffset),
            getDstColor(lx+1, ly-1, componentOffset),
            getDstColor(lx-1, ly+1, componentOffset),
            getDstColor(lx+1, ly+1, componentOffset),
        };
        float[] neighbourWeights = resolveWeights(window, neighbourMatrix);
        return
                neighbours[0]*neighbourWeights[0] +
                neighbours[1]*neighbourWeights[1] +
                neighbours[2]*neighbourWeights[2] +
                neighbours[3]*neighbourWeights[3];
    }
    
    private float resolveAxialValue(int lx, int ly, int componentOffset){
        float[] window = getSourceWindow(lx, ly, componentOffset);
        float[][] neighbourMatrix = getSourceNeighbourMatrixAxial(lx, ly, componentOffset);
        float[] neighbours = new float[]{
            getDstColor(lx, ly-1, componentOffset),
            getDstColor(lx-1, ly, componentOffset),
            getDstColor(lx+1, ly, componentOffset),
            getDstColor(lx, ly+1, componentOffset),
        };
        float[] neighbourWeights = resolveWeights(window, neighbourMatrix);
        float result = 
                neighbours[0]*neighbourWeights[0] +
                neighbours[1]*neighbourWeights[1] +
                neighbours[2]*neighbourWeights[2] +
                neighbours[3]*neighbourWeights[3];
        return result;
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
        else for(int i = 0; i < weights.length; i++) weights[i] /= sum;
    }
    
    private float[] getSourceWindow(int lx, int ly, int componentOffset){
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
                window[iw] = getSrcColor(ix, iy, componentOffset);
            }
        }
        return window;
    }
    
    private float[][] getSourceNeighbourMatrixDiagonal(int lx, int ly, int componentOffset){
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
                neighbourMatrix[iw] = getSourceNeighboursDiagonal(ix, iy, componentOffset);
            }
        }
        return neighbourMatrix;
    }
    
    private float[][] getSourceNeighbourMatrixAxial(int lx, int ly, int componentOffset){
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
                neighbourMatrix[iw] = getNeighboursAxial(ix, iy, componentOffset);
            }
        }
        return neighbourMatrix;
    }
    
    private float[] getSourceNeighboursDiagonal(int x, int y, int componentOffset){
        float[] neighbours = new float[4];
        neighbours[0] = getSrcColor(x-1, y-1, componentOffset);
        neighbours[1] = getSrcColor(x+1, y-1, componentOffset);
        neighbours[2] = getSrcColor(x-1, y+1, componentOffset);
        neighbours[3] = getSrcColor(x+1, y+1, componentOffset);
        return neighbours;
    }
    
    private float[] getNeighboursAxial(int x, int y, int componentOffset){
        float[] neighbours = new float[4];
        neighbours[0] = getSrcColor(x, y-1, componentOffset);
        neighbours[1] = getSrcColor(x-1, y, componentOffset);
        neighbours[2] = getSrcColor(x+1, y, componentOffset);
        neighbours[3] = getSrcColor(x, y+1, componentOffset);
        return neighbours;
    }
    
    private static float crop(float d){
        float f = (float) d;
        if(f < 0) return 0;
        if(f > 1) return 1;
        return f;
    }
}
