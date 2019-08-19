package cz.mg.resampler.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public abstract class BufferedPanel extends JPanel {
    private BufferedImage buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateBuffer();
        Graphics gg = buffer.getGraphics();
        draw(gg);
        g.drawImage(buffer, 0, 0, this);
    }
    
    private void updateBuffer(){
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if(panelWidth < 1) panelWidth = 1;
        if(panelHeight < 1) panelHeight = 1;
        if(panelWidth != buffer.getWidth() || panelHeight != buffer.getHeight()){
            buffer = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
        }
    }
    
    protected Color getBufferColor(int x, int y){
        if(x < 0 || y < 0) return Color.BLACK;
        if(x >= buffer.getWidth() || y >= buffer.getHeight()) return Color.BLACK;
        return new Color(buffer.getRGB(x, y));
    }
    
    protected abstract void draw(Graphics g);
}
