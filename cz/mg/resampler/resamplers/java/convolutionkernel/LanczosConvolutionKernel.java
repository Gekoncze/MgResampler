package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.sin;
import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class LanczosConvolutionKernel implements ConvolutionKernel {
    private static final float PI = 3.1415f;
    private float a = 3;

    public LanczosConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        if(abs(x) >= a) return 0.0f;
        else return sinc(x) * sinc(x/a);
    }
    
    float sinc(float x)
    {
        if(x == 0.0f) return 1.0f;
        else return (float) (sin(PI * x) / (PI * x));
    }

    @Override
    public String toString() {
        return "Lanczos";
    }
}
