package cz.mg.resampler.resamplers.java.other;


public abstract class Matrix {
    public final int w, h;
    public final float[] data;

    public Matrix(int w, int h) {
        this.w = w;
        this.h = h;
        this.data = new float[w*h];
    }

    public Matrix(int w, int h, float[] data) {
        if(data.length != w*h) throw new IllegalArgumentException("Matrix size mismatch: " + data.length + " w: " + w + " h: " + h);
        this.w = w;
        this.h = h;
        this.data = data;
    }

    public Matrix transpose(){
        Matrix m = this;
        Matrix mt = create(m.h, m.w);
        for(int y = 0; y < m.h; y++){
            for(int x = 0; x < m.w; x++){
                int i = x+y*m.w;
                int ti = y+x*m.h;
                mt.data[ti] = m.data[i];
            }
        }
        return mt;
    }

    public Matrix inverse(){
        Matrix result = create(4, 4);
        if(inverse(this.data, result.data) == true) return result;
        else return null;
    }
    
    protected abstract boolean inverse(float[] m, float[] out);
    protected abstract Matrix create(int w, int h);

    public Matrix multiply(Matrix m2){
        Matrix m1 = this;
        if(m1.w != m2.h) throw new IllegalArgumentException(
                "Wrong matrix size for multiplication: "
                + m1.w + " " + m1.h + " x " + m2.w + " " + m2.h);

        Matrix r = create(m2.w, m1.h);
        for (int y = 0; y < r.h; y++) {
            for (int x = 0; x < r.w; x++) {
                float sum = 0;
                for(int i = 0; i < m1.w; i++){
                    sum += m1.data[i+y*m1.w] * m2.data[x+i*m2.w];
                }
                r.data[x+y*r.w] = sum;
            }
        }
        return r;
    }
}