package cz.mg.resampler.gui.utilities;

import cz.mg.resampler.resamplers.java.utilities.ColorRgba;


public class ColorUtilities {
    public static int getARGB(ColorRgba c){
        return createColor(floatToInt(c.r), floatToInt(c.g), floatToInt(c.b), floatToInt(c.a));
    }
    
    public static int floatToInt(float f){
        int i = Math.round(f * 255.0f);
        if(i < 0) i = 0;
        if(i > 255) i = 255;
        return i;
    }
    
    public static float intToFloat(int i){
        if(i < 0) i = 0;
        if(i > 255) i = 255;
        return i / 255.0f;
    }
    
    public static int getRed(int color){
        return (color >> 16) & 0xFF;
    }
    
    public static int getGreen(int color){
        return (color >> 8) & 0xFF;
    }
    
    public static int getBlue(int color){
        return (color >> 0) & 0xFF;
    }
    
    public static int getAlpha(int color){
        return (color >> 24) & 0xFF;
    }
    
    public static int createColor(int r, int g, int b, int a){
        return (a << 24) | (r << 16) | (g << 8) | (b << 0);
    }
    
    public static void set(ColorRgba c, int color){
        c.r = intToFloat(getRed(color));
        c.g = intToFloat(getGreen(color));
        c.b = intToFloat(getBlue(color));
        c.a = intToFloat(getAlpha(color));
    }
}
