package cz.mg.resampler.gui.utilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;


/**
 *  This class helps with some drawing challenges which are not part of the awt Graphics.
 */
public class DrawingUtilities {
    /**
     *  Draws two colored checker in given rectangular region.
     *  The checker is aligned to the top left corner of the screen.
     *  @param g: the graphics object to be used for drawing
     *  @param c: the component used to align the checker
     *  @param x: the horizontal position of the rectangular region of the checker
     *  @param y: the vertical position of the rectangular region of the checker
     *  @param w: the horizontal size of the rectangular region of the checker
     *  @param h: the vertical size of the rectangular region of the checker
     *  @param cw: the width of the checker cells
     *  @param ch: the height of the checker cells
     *  @param c1: the first color of the checker
     *  @param c2: the second color of the checker
     */
    public static void drawChecker(Graphics g, Component c, int x, int y, int w, int h, int cw, int ch, Color c1, Color c2){
        Shape oldClip = g.getClip();
        g.clipRect(x, y, w, h);
        int rw = c.getWidth();
        int rh = c.getHeight();
        int row = 0;
        int sx = -c.getLocationOnScreen().x;
        int sy = -c.getLocationOnScreen().y;
        for(int cy = sy; cy < rh; cy += ch){
            Color cc = row % 2 == 0 ? c1 : c2;
            for(int cx = sx; cx < rw; cx += cw){
                g.setColor(cc);
                g.fillRect(cx, cy, cw, ch);
                cc = (cc == c1 ? c2 : c1);
            }
            row++;
        }
        g.setClip(oldClip);
    }
    
    /**
     *  Draws text that is aligned to the center of given rectangular area.
     *  @param g: the graphics object to be used for drawing
     *  @param text: the text to be drawn
     *  @param x: the horizontal position of the rectangular area
     *  @param y: the vertical position of the rectangular area
     *  @param w: the horizontal size of the rectangular area
     *  @param h: the vertical size of the rectangular area
     */
    public static void drawTextCentered(Graphics g, String text, int x, int y, int w, int h){
        FontMetrics m = g.getFontMetrics();
        int fh = m.getHeight();
        int fw = m.stringWidth(text);
        int dx = w/2 - fw/2;
        int dy = h/2 - fh/2;
        g.drawString(text, dx, dy + m.getAscent());
    }
    
    /**
     *  Creates contrast color for given color.
     *  The resulting color may not be optimally contrasting.
     *  Alpha channel is ignored.
     *  @param color: the color for which to create the contrast color
     *  @return the contrast color
     */
    public static Color highlight(Color color){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        
        if(r < 128) r += 128;
        else r -= 128;
        
        if(g < 128) g += 128;
        else g -= 128;
        
        if(b < 128) b += 128;
        else b -= 128;
        
        return new Color(r, g, b); // not using alpha
    }
}
