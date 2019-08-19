package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class NearestConvolutionKernel implements ConvolutionKernel {
    public NearestConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        if(abs(x) < 0.5f){
            return 1.0f;
        } else if(x == 0.5f){
            return 1.0f;
        } else {
            return 0.0f;
        }
    }

    @Override
    public String toString() {
        return "Nearest";
    }
}
