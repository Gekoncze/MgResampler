package cz.mg.resampler.resamplers.java.other;


public class ColorHsla {
    public float h;
    public float s;
    public float l;
    public float a;
    
    public ColorHsla() {
    }

    public ColorHsla(ColorHsla color){
        set(color);
    }

    public ColorHsla(float h, float s, float l, float a) {
        set(h, s, l, a);
    }

    public void set(ColorHsla color){
        this.h = color.h;
        this.s = color.s;
        this.l = color.l;
        this.a = color.a;
    }

    public void set(float r, float g, float b, float a){
        this.h = r;
        this.s = g;
        this.l = b;
        this.a = a;
    }

    public void clear(){
        h = 0;
        s = 0;
        l = 0;
        a = 0;
    }
}
