package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class LinearConvolutionKernel implements ConvolutionKernel {
    public LinearConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        if(abs(x) < 1.0f){
            return 1.0f - abs(x);
        } else {
            return 0.0f;
        }
    }

    @Override
    public String toString() {
        return "Linear";
    }
}
