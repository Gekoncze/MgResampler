package cz.mg.resampler.resamplers.java.convolutionkernel;

import static java.lang.Math.pow;
import static java.lang.Math.abs;


/**
 *  Source of algorithm (no code): https://clouard.users.greyc.fr/Pantheon/experiments/rescaling/index-en.html
 */
public class MitchellConvolutionKernel implements ConvolutionKernel {
    public MitchellConvolutionKernel() {
    }
    
    @Override
    public float compute(float x) {
        float b = 1.0f / 3.0f;
        float c = 1.0f / 3.0f;
        if(abs(x) < 1.0f){
            return (1.0f / 6.0f) * ( (12.0f - 9.0f * b - 6.0f * c) * (float)pow(abs(x), 3.0f) + ((-18.0f + 12.0f * b + 6.0f * c) * (float)pow(abs(x), 2.0f) + (6.0f - 2.0f * b)) );
        } else if(abs(x) < 2.0f){
            return (1.0f / 6.0f) * ( (-b - 6.0f * c) * (float)pow(abs(x), 3.0f) + (6.0f * b + 30.0f * c) * (float)pow(abs(x), 2.0f) + (-12.0f * b - 48.0f * c) * (float)pow(abs(x), 1.0f) + (8.0f * b + 24.0f * c) );
        } else {
            return 0.0f;
        }
    }

    @Override
    public String toString() {
        return "Mitchell";
    }
}
