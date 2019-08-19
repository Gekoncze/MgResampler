package cz.mg.resampler.resamplers.java.interpolation;

import cz.mg.resampler.resamplers.java.colordifference.ColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.FastRgbColorDifference;
import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import cz.mg.resampler.resamplers.java.Image;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import cz.mg.resampler.Parameter;


public class SmoothNearestInterpolation implements Interpolation {
    private Interpolation interpolation = new BicubicInterpolation();
    private ColorDifference colorDifference = new FastRgbColorDifference();
    private float m = 2.25f;
    private float p = 10.0f;
    
    public SmoothNearestInterpolation() {
    }

    public SmoothNearestInterpolation(Interpolation interpolation, ColorDifference colorDifference, float m, float p) {
        this.interpolation = interpolation;
        this.colorDifference = colorDifference;
        this.m = m;
        this.p = p;
    }

    @Parameter(order = 0)
    public Interpolation getInterpolation() {
        return interpolation;
    }

    @Parameter(order = 0)
    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    @Parameter(order = 1)
    public ColorDifference getColorDifference() {
        return colorDifference;
    }

    @Parameter(order = 1)
    public void setColorDifference(ColorDifference colorDifference) {
        this.colorDifference = colorDifference;
    }

    @Parameter(order = 2)
    public float getM() {
        return m;
    }

    @Parameter(order = 2)
    public void setM(float m) {
        if(m < 1) m = 1;
        if(m > 6) m = 6;
        this.m = m;
    }
    
    @Parameter(order = 3)
    public float getP() {
        return p;
    }

    @Parameter(order = 3)
    public void setP(float p) {
        if(p < 0) p = 0;
        if(p > 16) p = 16;
        this.p = p;
    }
    
    @Override
    public void compute(Image image, float x, float y, ColorRgba result) {
        int ix1 = (int) x;
        int iy1 = (int) y;
        int ix2 = ix1 + 1;
        int iy2 = iy1 + 1;

        int cx0 = ix1 - 1;
        int cx1 = ix1;
        int cx2 = ix2;
        int cx3 = ix2 + 1;
        int cy0 = iy1 - 1;
        int cy1 = iy1;
        int cy2 = iy2;
        int cy3 = iy2 + 1;

        ColorRgba[] c = new ColorRgba[4*4];
        c[0] = image.getReadColor(cx0, cy0); c[1] = image.getReadColor(cx1, cy0); c[2] = image.getReadColor(cx2, cy0); c[3] = image.getReadColor(cx3, cy0);
        c[4] = image.getReadColor(cx0, cy1); c[5] = image.getReadColor(cx1, cy1); c[6] = image.getReadColor(cx2, cy1); c[7] = image.getReadColor(cx3, cy1);
        c[8] = image.getReadColor(cx0, cy2); c[9] = image.getReadColor(cx1, cy2); c[10] = image.getReadColor(cx2, cy2); c[11] = image.getReadColor(cx3, cy2);
        c[12] = image.getReadColor(cx0, cy3); c[13] = image.getReadColor(cx1, cy3); c[14] = image.getReadColor(cx2, cy3); c[15] = image.getReadColor(cx3, cy3);

        ColorRgba cc = new ColorRgba();
        interpolation.compute(image, x, y, cc);

        int mini1 = 0;
        float mind1 = 1000000;

        for(int i = 0; i < 16; i++)
        {
            float xx = cx0 + i % 4;
            float yy = cy0 + i / 4;
            float dx = xx - x;
            float dy = yy - y;
            float dd = (float) sqrt(dx*dx+dy*dy);
            float d = colorDifference.compute(cc, c[i]) + (float)pow(dd/m, p);
            if(d < mind1)
            {
                mind1 = d;
                mini1 = i;
            }
        }
        result.set(c[mini1]);
    }

    @Override
    public String toString() {
        return "Smooth nearest";
    }
}
