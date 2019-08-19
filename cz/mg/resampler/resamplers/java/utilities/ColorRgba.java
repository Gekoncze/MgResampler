package cz.mg.resampler.resamplers.java.utilities;


/**
 *  Represents a color in RGBA color space.
 *  The color component values are in the range <0;1>.
 *  The type of the color components is float.
 */
public class ColorRgba {
    public float r;
    public float g;
    public float b;
    public float a;

    /**
     *  Constructs color object with all color components set to 0.
     */
    public ColorRgba() {
    }
    
    /**
     *  Constructs color object with all color components set to the same values as are in the given color object.
     *  @param color: the color object to copy values from
     */
    public ColorRgba(ColorRgba color){
        set(color);
    }

    /**
     *  Constructs color object with color components set to given values.
     *  @param r: the red component value
     *  @param g: the green component value
     *  @param b: the blue component value
     *  @param a: the alpha component value
     */
    public ColorRgba(float r, float g, float b, float a) {
        set(r, g, b, a);
    }
    
    /**
     *  Sets all color components to the same values as are in the given color object.
     *  @param color: the color object to copy values from
     */
    public void set(ColorRgba color){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }
    
    /**
     *  Sets all color components to given values.
     *  @param r: the new red component value
     *  @param g: the new green component value
     *  @param b: the new blue component value
     *  @param a: the new alpha component value
     */
    public void set(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    /**
     *  Sets all color components to 0.
     */
    public void clear(){
        r = 0;
        g = 0;
        b = 0;
        a = 0;
    }
}
