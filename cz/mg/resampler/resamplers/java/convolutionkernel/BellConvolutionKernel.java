package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.pow;
import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class BellConvolutionKernel implements ConvolutionKernel {
    public BellConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        if(abs(x) < 0.5f){
            return 0.75f - (float)pow(abs(x), 2.0f);
        } else if(abs(x) < 1.5f) {
            return 0.5f * (float)pow(abs(x)-1.5f, 2.0f);
        } else {
            return 0.0f;
        }
    }

    @Override
    public String toString() {
        return "Bell";
    }
}
