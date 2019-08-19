package cz.mg.resampler.resamplers.java.utilities;


/**
 *  Class containing helper methods for computations.
 * 
 *  integer = discrete coordinates
 *  float = continuous coordinates
 *  normalized = normalized continuous coordinates
 * 
 *  grid = grid pixel abstraction
 *  box = box pixel abstraction
 * 
 * 
 *  Example for interval of size 5 (could be an image with horizontal resolution of 5 pixels)
 * 
 *                     -----------------------------------------
 *                     |       |       |       |       |       |
 *                     |   0   |   1   |   2   |   3   |   4   |
 *                     |       |       |       |       |       |
 *                     -----------------------------------------
 *   box integer (d.c) 0       1       2       3       4       5  index <0;4> -1 & 5 for rounding errors
 *                     |\      |\      |\      |\      |\      |
 *                     |||\    |||\    |||\    |||\    |||\    |
 *                     |||||\  |||||\  |||||\  |||||\  |||||\  |
 *                     |||||||\|||||||\|||||||\|||||||\|||||||\|
 *     box float (c.c) 0  ...  1  ...  2  ...  3  ...  4  ...  5  <0;5>
 *                     |       |       |       |       |       |
 *                     |       |       |       |       |       |
 *    normalized (c.c) 0  ...  ? ? ... ? . ? . ?  ...  ?  ...  1  <0;1>
 *                     |         |         |         |         |
 *                     |         |         |         |         |
 *    grid float (c.c) 0   ...   1   ...   2   ...   3   ...   4  <0;4>
 *                     |\|||||||/|\|||||||/|\|||||||/|\|||||||/|
 *                     |||\|||/|||||\|||/|||||\|||/|||||\|||/|||
 *                     |||||X|||||||||X|||||||||X|||||||||X|||||
 *                     |||/   \|||||/   \|||||/   \|||||/   \|||
 *                     |/       \|/       \|/       \|/       \|
 *  grid integer (d.c) 0         1         2         3         4  begin index <0;3> end index <1;4> -1 & 4 for rounding errors
 *                    ---       ---       ---       ---       ---
 *                    |0|       |1|       |2|       |3|       |4|
 *                    ---       ---       ---       ---       ---
 */
public class ComputationUtilities {
    /**
     *  Transforms integer value to normalized value.
     *  @param i: the integer value
     *  @param size: the size of the region
     *  @return nv: the normalized value
     */
    public static float boxIntegerToNormalized(int i, int size){
        if((size-1) == 0) return 0.0f;
        float n = (float)i / (float)(size-1);
        return n;
    }
    
    /**
     *  Transforms normalized value to integer value.
     *  Important note: may return +- 1 on interval borders due to rounding.
     *  @param n: the normalized value
     *  @param size: the size of the region
     *  @return iv: the integer value
     */
    public static int normalizedToBoxInteger(float n, int size){
        int i = (int)(n * size); // has rounding errors on interval borders, but faster
        return i;
    }
    
    public static int normalizedToBoxIntegerSafe(float n, int size){
        int i = (int) Math.round(n*(size-1)+0.5); // no rounding errors, but slower
        return i;
    }
    
    /**
     *  Transforms float value to normalized value.
     *  @param f: the float value
     *  @param size: the size of the region
     *  @return n: the normalized value
     */
    public static float boxFloatToNormalized(float f, int size){
        float n = (float)f / (float)size;
        return n;
    }
    
    public static float gridFloatToNormalized(float f, int size){
        float n = (float)f / (float)size;
        return n;
    }
    
    /**
     *  Transforms normalized value to float value.
     *  @param n: the normalized value
     *  @param size: the size of the region
     *  @return f: the float value
     */
    public static float normalizedToBoxFloat(float n, int size){
        float f = n * size;
        return f;
    }
    
    public static float normalizedToGridFloat(float n, int size){
        float f = n * (size - 1);
        return f;
    }
    
    /**
     *  Transforms integer value to float value.
     *  @param i: the integer value
     *  @param size: the size of the region
     *  @return f: the float value
     */
    public static float boxIntegerToBoxFloat(int i, int size){
        float f = ComputationUtilities.normalizedToBoxFloat(boxIntegerToNormalized(i, size), size);
        return f;
    }
    
    /**
     *  Transforms float value to integer value.
     *  @param f: the float value
     *  @param size: the size of the region
     *  @return i: the integer value
     */
    public static int boxFloatToBoxInteger(float f, int size){
        int i = normalizedToBoxInteger(ComputationUtilities.boxFloatToNormalized(f, size), size);
        return i;
    }
}
