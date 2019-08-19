package cz.mg.resampler.resamplers.java.other;

import static java.lang.Math.sqrt;


public class Vector3f {
    public float x;
    public float y;
    public float z;

    public Vector3f() {
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public static float distance(Vector3f a, Vector3f b){
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float dz = a.z - b.z;
        return (float) sqrt(dx*dx + dy*dy + dz*dz);
    }
}
