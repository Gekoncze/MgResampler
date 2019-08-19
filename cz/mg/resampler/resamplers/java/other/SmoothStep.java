package cz.mg.resampler.resamplers.java.other;


/**
 *  Class that provides static methods for computing smooth step function.
 */
public class SmoothStep {
    /**
     *  Computes smooth step function for given parameters.
     *  @param x: the input value for the function; range <0;1>
     *  @param steepness: parameter specifiing the steepness of the smooth step; range (0;1>
     *  @param position: parameter specifiing the center of the smooth step; range (0;1)
     *  @return: value in the range <0;1>
     */
    public static float compute(float x, float steepness, float position){
        boolean back = position < 0.5;
        float power = log(0.5f) / log(back ? 1.0f - position : position);
        if(back){
            if(x < position){
                return pow(invPow(x, power)*2.0f, 1.0f/steepness) / 2.0f;
            } else {
                return 1.0f - pow((-invPow(x, power)+1.0f)*2.0f, 1.0f/steepness) / 2.0f;
            }
        } else {
            if(x < position){
                return pow(pow(x, power)*2.0f, 1.0f/steepness) / 2.0f;
            } else {
                return 1.0f - pow((-pow(x, power)+1.0f)*2.0f, 1.0f/steepness) / 2.0f;
            }
        }
    }

    /**
     *  Computes smooth step function for given parameters. (simplified)
     *  @param x: the input value for the function; range <0;1>
     *  @param steepness: parameter specifiing the steepness of the smooth step; range (0;1>
     *  @return: value in the range <0;1>
     */
    public static float compute(float x, float steepness){
        if(x < 0.5f){
            return pow(x*2.0f, 1.0f/steepness) / 2.0f;
        } else {
            return 1.0f - pow((-x+1.0f)*2.0f, 1.0f/steepness) / 2.0f;
        }
    }
    
    private static float invPow(float value, float power){
        return 1.0f - pow(1.0f - value, power);
    }
    
    private static float pow(float value, float power){
        return (float)Math.pow(value, power);
    }
    
    private static float log(float value){
        return (float)Math.log(value);
    }
}
