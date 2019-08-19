package cz.mg.resampler.gui.components.interaction;

import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class ResamplerPreviewPanelZoom {
    public static void apply(final ResamplerPreviewPanel panel){
        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() < 0) panel.zoomIn(e.getX(), e.getY());
                else if(e.getWheelRotation() > 0) panel.zoomOut(e.getX(), e.getY());
            }
        });
    }
}
