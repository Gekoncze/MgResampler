package cz.mg.resampler.resamplers.java.other;


public class ColorYuva {
    public float y;
    public float u;
    public float v;
    public float a;
    
    public ColorYuva() {
    }

    public ColorYuva(ColorYuva color){
        set(color);
    }

    public ColorYuva(float y, float u, float v, float a) {
        set(y, u, v, a);
    }

    public void set(ColorYuva color){
        this.y = color.y;
        this.u = color.u;
        this.v = color.v;
        this.a = color.a;
    }

    public void set(float y, float u, float v, float a){
        this.y = y;
        this.u = u;
        this.v = v;
        this.a = a;
    }

    public void clear(){
        y = 0;
        u = 0;
        v = 0;
        a = 0;
    }
}
