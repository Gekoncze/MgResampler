//package cz.mg.resampler.resamplers.java.interpolation;
//
//import cz.mg.resampler.resamplers.java.colordifference.ColorDifference;
//import cz.mg.resampler.resamplers.java.colordifference.FastRgbaColorDifference;
//import cz.mg.resampler.resamplers.java.other.SmoothStep;
//import cz.mg.resampler.utilities.ResamplerParameter;
//import cz.mg.resampler.utilities.image.ColorRgba;
//import cz.mg.resampler.utilities.image.Image;
//
//
//public class AdaptiveSmoothInterpolation implements Interpolation {
//    private Interpolation interpolation = new BicubicInterpolation();
//    private ColorDifference colorDifference = new FastRgbaColorDifference();
//    private float in_m = 2.25f;
//    private float in_p = 10.0f;
//    private boolean smooth = true;
//
//    public AdaptiveSmoothInterpolation() {
//    }
//    
//    @ResamplerParameter(order = 0)
//    public Interpolation getInterpolation() {
//        return interpolation;
//    }
//
//    @ResamplerParameter(order = 0)
//    public void setInterpolation(Interpolation interpolation) {
//        this.interpolation = interpolation;
//    }
//
//    @ResamplerParameter(order = 1)
//    public ColorDifference getColorDifference() {
//        return colorDifference;
//    }
//
//    @ResamplerParameter(order = 1)
//    public void setColorDifference(ColorDifference colorDifference) {
//        this.colorDifference = colorDifference;
//    }
//    
//    @ResamplerParameter(order = 2)
//    public float getIn_m() {
//        return in_m;
//    }
//
//    @ResamplerParameter(order = 2)
//    public void setIn_m(float in_m) {
//        if(in_m < 1) in_m = 1;
//        if(in_m > 6) in_m = 6;
//        this.in_m = in_m;
//    }
//    
//    @ResamplerParameter(order = 3)
//    public float getIn_p() {
//        return in_p;
//    }
//
//    @ResamplerParameter(order = 3)
//    public void setIn_p(float in_p) {
//        if(in_p < 0) in_p = 0;
//        if(in_p > 16) in_p = 16;
//        this.in_p = in_p;
//    }
//
//    @ResamplerParameter(order = 4)
//    public boolean isSmooth() {
//        return smooth;
//    }
//
//    @ResamplerParameter(order = 4)
//    public void setSmooth(boolean smooth) {
//        this.smooth = smooth;
//    }
//    
//    @Override
//    public void compute(Image image, float x, float y, ColorRgba result) {
//        int ix1 = (int) x;
//        int iy1 = (int) y;
//        int ix2 = ix1 + 1;
//        int iy2 = iy1 + 1;
//
//        int cx0 = ix1 - 1;
//        int cx1 = ix1;
//        int cx2 = ix2;
//        int cx3 = ix2 + 1;
//        int cy0 = iy1 - 1;
//        int cy1 = iy1;
//        int cy2 = iy2;
//        int cy3 = iy2 + 1;
//
//        ColorRgba[] c = new ColorRgba[4*4];
//        c[0] = image.getReadColor(cx0, cy0); c[1] = image.getReadColor(cx1, cy0); c[2] = image.getReadColor(cx2, cy0); c[3] = image.getReadColor(cx3, cy0);
//        c[4] = image.getReadColor(cx0, cy1); c[5] = image.getReadColor(cx1, cy1); c[6] = image.getReadColor(cx2, cy1); c[7] = image.getReadColor(cx3, cy1);
//        c[8] = image.getReadColor(cx0, cy2); c[9] = image.getReadColor(cx1, cy2); c[10] = image.getReadColor(cx2, cy2); c[11] = image.getReadColor(cx3, cy2);
//        c[12] = image.getReadColor(cx0, cy3); c[13] = image.getReadColor(cx1, cy3); c[14] = image.getReadColor(cx2, cy3); c[15] = image.getReadColor(cx3, cy3);
//
//        ColorRgba cc = new ColorRgba();
//        interpolation.compute(image, x, y, cc);
//
//
//        /*float cd[4*4];
//        float sumd = 0;
//        for(int i = 0; i < 16; i++)
//        {
//            float xx = cx0 + i % 4;
//            float yy = cy0 + i / 4;
//            float dx = xx - x;
//            float dy = yy - y;
//            float dd = sqrt(dx*dx+dy*dy);
//            float d = (1.0 - colorDifference(cb, c[i])); // + 1 / dd; //pow(dd/in_m, in_p);
//            cd[i] = d;
//            sumd += d;
//        }
//
//        FloatColor rc = FloatColor(0, 0, 0, 0);
//        for(int i = 0; i < 16; i++)
//        {
//            rc += c[i] * (cd[i] / sumd);
//        }
//        return rc;*/
//
//
//        int mini1 = 0;
//        float mind1 = 1000000;
//        int mini2 = 0;
//        float mind2 = 1000000;
//
//        for(int i = 0; i < 16; i++)
//        {
//            float xx = cx0 + i % 4;
//            float yy = cy0 + i / 4;
//            float dx = xx - x;
//            float dy = yy - y;
//            float dd = (float)Math.sqrt(dx*dx+dy*dy);
//            float d = colorDifference.compute(cc, c[i]) + (float)Math.pow(dd/in_m, in_p);
//            if(d < mind1)
//            {
//                mind1 = d;
//                mini1 = i;
//            }
//        }
//
//        for(int i = 0; i < 16; i++)
//        {
//            float xx = cx0 + i % 4;
//            float yy = cy0 + i / 4;
//            float dx = xx - x;
//            float dy = yy - y;
//            float dd = (float)Math.sqrt(dx*dx+dy*dy);
//            float d = colorDifference.compute(cc, c[i]) + (float)Math.pow(dd/in_m, in_p);
//            if(d < mind2 && i != mini1)
//            {
//                mind2 = d;
//                mini2 = i;
//            }
//        }
//
//        float lc = laplacianConvolution(x, y, image);
//
//        if(smooth)
//        {
//            float t = 0.5f;
//            t = mind1 / (mind1 + mind2);
//            if(t < 0) t = 0;
//            if(t > 1) t = 1;
//            ColorRgba rc = mix(c[mini1], c[mini2], t);
//            result.set(mix(cc, rc, lc));
//        }
//        else
//        {
//            result.set(mix(cc, c[mini1], lc));
//        }
//    }
//    
//    private float laplacianAt(int x, int y, Image image){
//        ColorRgba l = plus(plus(plus(plus(image.getReadColor(x-1, y), image.getReadColor(x+1, y)), image.getReadColor(x, y-1)), image.getReadColor(x, y+1)), multiply(image.getReadColor(x, y), -4));
//        ColorRgba ld = plus(plus(plus(plus(image.getReadColor(x-1, y-1), image.getReadColor(x+1, y+1)), image.getReadColor(x+1, y-1)), image.getReadColor(x-1, y+1)), multiply(image.getReadColor(x, y), -4));
//
//        l.r = Math.abs(l.r/4);
//        l.g = Math.abs(l.g/4);
//        l.b = Math.abs(l.b/4);
//        l.r = SmoothStep.compute(l.r, in_steepness, in_position);
//        l.g = SmoothStep.compute(l.g, in_steepness, in_position);
//        l.b = SmoothStep.compute(l.b, in_steepness, in_position);
//        l.r = 1 - l.r;
//        l.g = 1 - l.g;
//        l.b = 1 - l.b;
//        float lc = l.r * l.g * l.b;
//
//        ld.r = Math.abs(ld.r/4);
//        ld.g = Math.abs(ld.g/4);
//        ld.b = Math.abs(ld.b/4);
//        ld.r = SmoothStep.compute(ld.r, in_steepness, in_position);
//        ld.g = SmoothStep.compute(ld.g, in_steepness, in_position);
//        ld.b = SmoothStep.compute(ld.b, in_steepness, in_position);
//        ld.r = 1 - ld.r;
//        ld.g = 1 - ld.g;
//        ld.b = 1 - ld.b;
//        float ldc = ld.r * ld.g * ld.b;
//
//        float ll = lc * ldc;
//        ll = 1 - ll;
//
//        return ll;
//    }
//    
//    private static ColorRgba plus(ColorRgba a, ColorRgba b){
//        return new ColorRgba(
//                a.r + b.r,
//                a.g + b.g,
//                a.b + b.b,
//                a.a + b.a
//        );
//    }
//    
//    private static ColorRgba multiply(ColorRgba a, float b){
//        return new ColorRgba(
//                a.r * b,
//                a.g * b,
//                a.b * b,
//                a.a * b
//        );
//    }
//
//    private float laplacianConvolution(float x, float y, Image image){
//        float lc = 0;
//        int ix0 = (int)(Math.floor(x));
//        int iy0 = (int)(Math.floor(y));
//        int a = 3;
//
//        for(int iy = iy0 - a + 1; iy <= iy0 + a; iy++)
//        {
//            for(int ix = ix0 - a + 1; ix <= ix0 + a; ix++)
//            {
//                float l = laplacianAt(ix, iy, image);
//                float kx = kf(x - ix);
//                float ky = kf(y - iy);
//                float k = kx*ky;
//                lc += l * k;
//            }
//        }
//
//        return Math.max(0, Math.min(lc, 1));
//    }
//    
//    private static float kf(float x) {
//        float a = -0.5f;
//        if(Math.abs(x) <= 1.0f){
//            return (a + 2.0f) * (float)Math.pow(Math.abs(x), 3.0f) - (a + 3.0f) * (float)Math.pow(Math.abs(x), 2.0f) + 1.0f;
//        } else if(Math.abs(x) < 2.0f) {
//            return a * (float)Math.pow(Math.abs(x), 3.0f) - 5.0f * a * (float)Math.pow(Math.abs(x), 2.0f) + 8.0f * a * Math.abs(x) - 4.0f * a;
//        } else {
//            return 0.0f;
//        }
//    }
//    
//    private static ColorRgba mix(ColorRgba a, ColorRgba b, float t){
//        return new ColorRgba(
//                mix(a.r, b.r, t),
//                mix(a.g, b.g, t),
//                mix(a.b, b.b, t),
//                mix(a.a, b.a, t)
//        );
//    }
//    
//    private static float mix(float a, float b, float t) {
//        return a*(1-t) + b*t;
//    }
//
//    @Override
//    public String toString() {
//        return "Adaptive smooth";
//    }
//}
