package cz.mg.resampler.gui;

import java.awt.Color;


public class Configuration {
    private static final int DARKEN = 64;
    private static final int DIFFERENCE = 16;
    private static Configuration instance = null;
    
    public static final Color DEFAULT_BACKGROUND_COLOR_FIRST = new Color(128+DIFFERENCE, 128+DIFFERENCE, 128+DIFFERENCE);
    public static final Color DEFAULT_BACKGROUND_COLOR_SECOND = new Color(128-DIFFERENCE, 128-DIFFERENCE, 128-DIFFERENCE);
    public static final Color DEFAULT_BACKGROUND_COLOR_OUTER_FIRST = new Color(128+DIFFERENCE-DARKEN, 128+DIFFERENCE-DARKEN, 128+DIFFERENCE-DARKEN);
    public static final Color DEFAULT_BACKGROUND_COLOR_OUTER_SECOND = new Color(128-DIFFERENCE-DARKEN, 128-DIFFERENCE-DARKEN, 128-DIFFERENCE-DARKEN);
    
    private Color backgroundColorFirst = DEFAULT_BACKGROUND_COLOR_FIRST;
    private Color backgroundColorSecond = DEFAULT_BACKGROUND_COLOR_SECOND;
    private Color backgroundColorOuterFirst = DEFAULT_BACKGROUND_COLOR_OUTER_FIRST;
    private Color backgroundColorOuterSecond = DEFAULT_BACKGROUND_COLOR_OUTER_SECOND;
    private int backgroundCheckerWidth = 16;
    private int backgroundCheckerHeight = 16;
    private int maxZoom = 64;
    private int minZoom = -64;
    private boolean showCursorLocation = false;
    
    public static Configuration getInstance(){
        if(instance == null) instance = new Configuration();
        return instance;
    }

    public Color getBackgroundColorFirst() {
        return backgroundColorFirst;
    }

    public void setBackgroundColorFirst(Color backgroundColorFirst) {
        this.backgroundColorFirst = backgroundColorFirst;
    }

    public Color getBackgroundColorSecond() {
        return backgroundColorSecond;
    }

    public void setBackgroundColorSecond(Color backgroundColorSecond) {
        this.backgroundColorSecond = backgroundColorSecond;
    }

    public Color getBackgroundColorOuterFirst() {
        return backgroundColorOuterFirst;
    }

    public void setBackgroundColorOuterFirst(Color backgroundColorOuterFirst) {
        this.backgroundColorOuterFirst = backgroundColorOuterFirst;
    }

    public Color getBackgroundColorOuterSecond() {
        return backgroundColorOuterSecond;
    }

    public void setBackgroundColorOuterSecond(Color backgroundColorOuterSecond) {
        this.backgroundColorOuterSecond = backgroundColorOuterSecond;
    }

    public int getBackgroundCheckerWidth() {
        return backgroundCheckerWidth;
    }

    public void setBackgroundCheckerWidth(int backgroundCheckerWidth) {
        this.backgroundCheckerWidth = backgroundCheckerWidth;
    }

    public int getBackgroundCheckerHeight() {
        return backgroundCheckerHeight;
    }

    public void setBackgroundCheckerHeight(int backgroundCheckerHeight) {
        this.backgroundCheckerHeight = backgroundCheckerHeight;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public boolean isShowCursorLocation() {
        return showCursorLocation;
    }

    public void setShowCursorLocation(boolean showCursorLocation) {
        this.showCursorLocation = showCursorLocation;
    }
}
