package cz.mg.resampler.gui.utilities;


/**
 *  This class helps with conversion of objects into numeric types.
 *  Sometimes in swing there are components that return numeric values as objects.
 *  The purpose of this class is to make the conversions back to numerics easier.
 */
public class NumberUtilities {
    /**
     *  Converts given object to integer, if possible.
     *  @param o: the object to be converted
     *  @return the integer value
     *  @throws RuntimeException if the object cannot be converted to int
     */
    public static int toInteger(Object o){
        if(o instanceof Integer) return (int)(Integer)o;
        if(o instanceof Float) return (int)(float)(Float)o;
        if(o instanceof Double) return (int)(double)(Double)o;
        throw new RuntimeException();
    }
    
    /**
     *  Converts given object to float, if possible.
     *  @param o: the object to be converted
     *  @return the float value
     *  @throws RuntimeException if the object cannot be converted to float
     */
    public static float toFloat(Object o){
        if(o instanceof Integer) return (float)(int)(Integer)o;
        if(o instanceof Float) return (float)(Float)o;
        if(o instanceof Double) return (float)(double)(Double)o;
        throw new RuntimeException();
    }
    
    /**
     *  Converts given object to double, if possible.
     *  @param o: the object to be converted
     *  @return the double value
     *  @throws RuntimeException if the object cannot be converted to double
     */
    public static double toDouble(Object o){
        if(o instanceof Integer) return (double)(int)(Integer)o;
        if(o instanceof Float) return (double)(Float)o;
        if(o instanceof Double) return (double)(Double)o;
        throw new RuntimeException();
    }
}
