package cz.mg.resampler.gui.components.interaction;

import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import cz.mg.resampler.gui.utilities.View;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class ResamplerPreviewPanelMovement implements MouseListener, MouseMotionListener {
    private final ResamplerPreviewPanel panel;
    private int dragMouseX = 0;
    private int dragMouseY = 0;
    private float dragViewX = 0;
    private float dragViewY = 0;

    public ResamplerPreviewPanelMovement(ResamplerPreviewPanel panel) {
        this.panel = panel;
    }
    
    public static void apply(ResamplerPreviewPanel panel){
        ResamplerPreviewPanelMovement interaction = new ResamplerPreviewPanelMovement(panel);
        panel.addMouseListener(interaction);
        panel.addMouseMotionListener(interaction);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragViewX = panel.getMyParent().getView().getRx();
        dragViewY = panel.getMyParent().getView().getRy();
        dragMouseX = e.getX();
        dragMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        View view = panel.getMyParent().getView();
        view.setRx(dragViewX);
        view.setRy(dragViewY);
        float sx0 = view.destinationToSourceX_I2F(dragMouseX);
        float sy0 = view.destinationToSourceY_I2F(dragMouseY);
        float sx1 = view.destinationToSourceX_I2F(e.getX());
        float sy1 = view.destinationToSourceY_I2F(e.getY());
        float sdx = sx1 - sx0;
        float sdy = sy1 - sy0;
        view.moveBySource(sdx, sdy);

        panel.getMyParent().refresh();
        panel.getMyParent().updateSliderX();
        panel.getMyParent().updateSliderY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
