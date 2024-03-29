package cz.mg.resampler.resamplers.java.other;


public class HighQualityMatrix extends Matrix {
    public HighQualityMatrix(int w, int h) {
        super(w, h);
    }

    public HighQualityMatrix(int w, int h, float[] data) {
        super(w, h, data);
    }

    /**
     *  source: ftp://ftp.freedesktop.org/pub/mesa/mesa-18.1.7.tar.gz/src/mesa/math/m_matrix.c
     */    
    @Override
    protected boolean inverse(float[] m, float[] out) {
       double[][] wtmp = new double[4][8];
       double m0, m1, m2, m3, s;
       double[] r0, r1, r2, r3;

       r0 = wtmp[0];
       r1 = wtmp[1];
       r2 = wtmp[2];
       r3 = wtmp[3];

       r0[0] = MAT(m,0,0);
       r0[1] = MAT(m,0,1);
       r0[2] = MAT(m,0,2);
       r0[3] = MAT(m,0,3);
       r0[4] = 1.0f;
       r0[5] = r0[6] = r0[7] = 0.0f;

       r1[0] = MAT(m,1,0);
       r1[1] = MAT(m,1,1);
       r1[2] = MAT(m,1,2);
       r1[3] = MAT(m,1,3);
       r1[5] = 1.0f;
       r1[4] = r1[6] = r1[7] = 0.0f;

       r2[0] = MAT(m,2,0);
       r2[1] = MAT(m,2,1);
       r2[2] = MAT(m,2,2);
       r2[3] = MAT(m,2,3);
       r2[6] = 1.0f;
       r2[4] = r2[5] = r2[7] = 0.0f;

       r3[0] = MAT(m,3,0);
       r3[1] = MAT(m,3,1);
       r3[2] = MAT(m,3,2);
       r3[3] = MAT(m,3,3);
       r3[7] = 1.0f;
       r3[4] = r3[5] = r3[6] = 0.0f;

       /* choose pivot - or die */
       if (Math.abs(r3[0])>Math.abs(r2[0])) { double[] tmp = r2; r2 = r3; r3 = tmp; };//SWAP_ROWS(r3, r2);
       if (Math.abs(r2[0])>Math.abs(r1[0])) { double[] tmp = r1; r1 = r2; r2 = tmp; };//SWAP_ROWS(r2, r1);
       if (Math.abs(r1[0])>Math.abs(r0[0])) { double[] tmp = r0; r0 = r1; r1 = tmp; };//SWAP_ROWS(r1, r0);
       if (r0[0] == 0.0) return false;

       /* eliminate first variable     */
       m1 = r1[0]/r0[0];
       m2 = r2[0]/r0[0];
       m3 = r3[0]/r0[0];
       s = r0[1];
       r1[1] -= m1 * s;
       r2[1] -= m2 * s;
       r3[1] -= m3 * s;
       s = r0[2];
       r1[2] -= m1 * s;
       r2[2] -= m2 * s;
       r3[2] -= m3 * s;
       s = r0[3];
       r1[3] -= m1 * s;
       r2[3] -= m2 * s;
       r3[3] -= m3 * s;
       s = r0[4];
       if (s != 0.0F) { r1[4] -= m1 * s; r2[4] -= m2 * s; r3[4] -= m3 * s; }
       s = r0[5];
       if (s != 0.0F) { r1[5] -= m1 * s; r2[5] -= m2 * s; r3[5] -= m3 * s; }
       s = r0[6];
       if (s != 0.0F) { r1[6] -= m1 * s; r2[6] -= m2 * s; r3[6] -= m3 * s; }
       s = r0[7];
       if (s != 0.0F) { r1[7] -= m1 * s; r2[7] -= m2 * s; r3[7] -= m3 * s; }

       /* choose pivot - or die */
       if (Math.abs(r3[1])>Math.abs(r2[1])){ double[] tmp = r2; r2 = r3; r3 = tmp; };//SWAP_ROWS(r3, r2);
       if (Math.abs(r2[1])>Math.abs(r1[1])){ double[] tmp = r1; r1 = r2; r2 = tmp; };//SWAP_ROWS(r2, r1);
       if (r1[1] == 0.0)  return false;

       /* eliminate second variable */
       m2 = r2[1]/r1[1];
       m3 = r3[1]/r1[1];
       r2[2] -= m2 * r1[2];
       r3[2] -= m3 * r1[2];
       r2[3] -= m2 * r1[3];
       r3[3] -= m3 * r1[3];
       s = r1[4]; if (0.0F != s) { r2[4] -= m2 * s; r3[4] -= m3 * s; }
       s = r1[5]; if (0.0F != s) { r2[5] -= m2 * s; r3[5] -= m3 * s; }
       s = r1[6]; if (0.0F != s) { r2[6] -= m2 * s; r3[6] -= m3 * s; }
       s = r1[7]; if (0.0F != s) { r2[7] -= m2 * s; r3[7] -= m3 * s; }

       /* choose pivot - or die */
       if (Math.abs(r3[2])>Math.abs(r2[2])){ double[] tmp = r2; r2 = r3; r3 = tmp; }//SWAP_ROWS(r3, r2);
       if (r2[2] == 0.0)  return false;

       /* eliminate third variable */
       m3 = r3[2]/r2[2];
       r3[3] -= m3 * r2[3];
       r3[4] -= m3 * r2[4];
       r3[5] -= m3 * r2[5];
       r3[6] -= m3 * r2[6];
       r3[7] -= m3 * r2[7];

       /* last check */
       if (0.0F == r3[3]) return false;

       s = 1.0F/r3[3];             /* now back substitute row 3 */
       r3[4] *= s; r3[5] *= s; r3[6] *= s; r3[7] *= s;

       m2 = r2[3];                 /* now back substitute row 2 */
       s  = 1.0F/r2[2];
       r2[4] = s * (r2[4] - r3[4] * m2);
       r2[5] = s * (r2[5] - r3[5] * m2);
       r2[6] = s * (r2[6] - r3[6] * m2);
       r2[7] = s * (r2[7] - r3[7] * m2);
       m1 = r1[3];
       r1[4] -= r3[4] * m1;
       r1[5] -= r3[5] * m1;
       r1[6] -= r3[6] * m1;
       r1[7] -= r3[7] * m1;
       m0 = r0[3];
       r0[4] -= r3[4] * m0;
       r0[5] -= r3[5] * m0;
       r0[6] -= r3[6] * m0;
       r0[7] -= r3[7] * m0;

       m1 = r1[2];                 /* now back substitute row 1 */
       s  = 1.0F/r1[1];
       r1[4] = s * (r1[4] - r2[4] * m1);
       r1[5] = s * (r1[5] - r2[5] * m1);
       r1[6] = s * (r1[6] - r2[6] * m1);
       r1[7] = s * (r1[7] - r2[7] * m1);
       m0 = r0[2];
       r0[4] -= r2[4] * m0;
       r0[5] -= r2[5] * m0;
       r0[6] -= r2[6] * m0;
       r0[7] -= r2[7] * m0;

       m0 = r0[1];                 /* now back substitute row 0 */
       s  = 1.0F/r0[0];
       r0[4] = s * (r0[4] - r1[4] * m0);
       r0[5] = s * (r0[5] - r1[5] * m0);
       r0[6] = s * (r0[6] - r1[6] * m0);
       r0[7] = s * (r0[7] - r1[7] * m0);

       MAT_OUT(out,0,0, r0[4]);
       MAT_OUT(out,0,1, r0[5]);
       MAT_OUT(out,0,2, r0[6]);
       MAT_OUT(out,0,3, r0[7]);
       MAT_OUT(out,1,0, r1[4]);
       MAT_OUT(out,1,1, r1[5]);
       MAT_OUT(out,1,2, r1[6]);
       MAT_OUT(out,1,3, r1[7]);
       MAT_OUT(out,2,0, r2[4]);
       MAT_OUT(out,2,1, r2[5]);
       MAT_OUT(out,2,2, r2[6]);
       MAT_OUT(out,2,3, r2[7]);
       MAT_OUT(out,3,0, r3[4]);
       MAT_OUT(out,3,1, r3[5]);
       MAT_OUT(out,3,2, r3[6]);
       MAT_OUT(out,3,3, r3[7]);

       return true;
    }
    
    private static float MAT(float[] m, int row, int column){
        return m[column*4 + row];
    }

    private static void MAT_OUT(float[] m, int row, int column, double value){
        m[column*4 + row] = (float) value;
    }

    @Override
    protected Matrix create(int w, int h) {
        return new HighQualityMatrix(w, h);
    }
}
