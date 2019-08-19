package cz.mg.resampler.resamplers.java.greyscale;

import cz.mg.resampler.Parameter;


public class WeightGreyscale implements Greyscale {
    private float rw = 0.299f;
    private float gw = 0.587f;
    private float bw = 0.114f;

    public WeightGreyscale() {
    }
    
    public WeightGreyscale(float rw, float gw, float bw) {
        this.rw = rw;
        this.gw = gw;
        this.bw = bw;
    }
    
    @Parameter(order = 0)
    public float getRed() {
        return rw;
    }

    @Parameter(order = 0)
    public void setRed(float rw) {
        if(rw < 0) rw = 0;
        if(rw > 1) rw = 1;
        this.rw = rw;
    }

    @Parameter(order = 1)
    public float getGreen() {
        return gw;
    }

    @Parameter(order = 1)
    public void setGreen(float gw) {
        if(gw < 0) gw = 0;
        if(gw > 1) gw = 1;
        this.gw = gw;
    }

    @Parameter(order = 2)
    public float getBlue() {
        return bw;
    }

    @Parameter(order = 2)
    public void setBlue(float bw) {
        if(bw < 0) bw = 0;
        if(bw > 1) bw = 1;
        this.bw = bw;
    }
    
    @Override
    public float compute(float r, float g, float b, float a) {
        float total = rw + gw + bw;
        if(total < 0.000001f) return 0;
        return (r*rw + g*gw + b*bw) / total;
    }
    
    @Override
    public String toString() {
        return "Weight";
    }
}
