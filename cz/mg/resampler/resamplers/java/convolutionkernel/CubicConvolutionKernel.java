package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.pow;
import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class CubicConvolutionKernel implements ConvolutionKernel {
    public CubicConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        float a = -0.5f;
        if(abs(x) <= 1.0f){
            return (a + 2.0f) * (float)pow(abs(x), 3.0f) - (a + 3.0f) * (float)pow(abs(x), 2.0f) + 1.0f;
        } else if(abs(x) < 2.0f) {
            return a * (float)pow(abs(x), 3.0f) - 5.0f * a * (float)pow(abs(x), 2.0f) + 8.0f * a * abs(x) - 4.0f * a;
        } else {
            return 0.0f;
        }
    }

    @Override
    public String toString() {
        return "Cubic";
    }
}
