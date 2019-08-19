package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.pow;
import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class HermiteConvolutionKernel implements ConvolutionKernel {
    public HermiteConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        if(abs(x) <= 1.0f){
            return 2.0f * (float)pow(abs(x), 3.0f) - 3.0f * (float)pow(abs(x), 2.0f) + 1.0f;
        } else {
            return 0.0f;
        }
    }

    @Override
    public String toString() {
        return "Hermite";
    }
}
