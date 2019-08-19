package cz.mg.resampler.resamplers.java.other;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;
import static java.lang.Math.max;
import static java.lang.Math.min;


/**
 *  Source of equations (no code): https://en.wikipedia.org/wiki/HSL_and_HSV
 */
public class RgbaToHsla {
    public static ColorHsla rgbaToHsla(ColorRgba c){
        return rgbaToHsla(c.r, c.g, c.b, c.a);
    }
    
    public static ColorHsla rgbaToHsla(float r, float g, float b, float a){
        float vmin = min(min(r, g), b);
        float vmax = max(max(r, g), b);
        float h = hue(r, g, b, vmin, vmax);
        float l = lightness(vmin, vmax);
        float s = saturation(vmin, vmax, l);
        return new ColorHsla(h, s, l, a);
    }
    
    private static float hue(float r, float g, float b, float min, float max){
        if(max == min){
            return 0.0f;
        } else if(max == r && g >= b){
            return ((float)(g-b)/(max-min))/6.0f + 0.0f;
        } else if(max == r && g < b){
            return ((float)(g-b)/(max-min))/6.0f + 1.0f;
        } else if(max == g){
            return ((float)(b-r)/(max-min))/6.0f + 1.0f/3.0f;
        } else {
            return ((float)(r-g)/(max-min))/6.0f + 2.0f/3.0f;
        }
    }

    private static float saturation(float min, float max, float l){
        if(max == min || (2.0f-(max+min)) == 0.0f){
           return 0.0f;
        } else if(l <= 0.5f){
            return (float)(max-min)/(max+min); 
        } else {
            return (float)(max-min)/(2.0f-(max+min));
        }
    }

    private static float lightness(float min, float max){
        return (max + min) / 2.0f;
    }
}
