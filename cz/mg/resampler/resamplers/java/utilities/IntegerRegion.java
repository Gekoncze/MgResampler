package cz.mg.resampler.resamplers.java.utilities;


public class IntegerRegion {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public IntegerRegion(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public static IntegerRegion intersection(IntegerRegion a, IntegerRegion b){
        int leftA = a.x;
        int rightA = a.x + a.width - 1;
        
        int leftB = b.x;
        int rightB = b.x + b.width - 1;
        
        int upA = a.y;
        int downA = a.y + a.height - 1;
        
        int upB = b.y;
        int downB = b.y + b.height - 1;
        
        int left = Math.max(leftA, leftB);
        int up = Math.max(upA, upB);
        int right = Math.min(rightA, rightB);
        int down = Math.min(downA, downB);
        
        int x = left;
        int y = up;
        int w = right - left + 1;
        int h = down - up + 1;
        
        if(w <= 0 || h <= 0) return null;
        else return new IntegerRegion(x, y, w, h);
    }
    
    public boolean isInside(int x, int y){
        return x >= this.x && y >= this.y && x < (this.x + this.width) && y < (this.y + this.height);
    }
}
