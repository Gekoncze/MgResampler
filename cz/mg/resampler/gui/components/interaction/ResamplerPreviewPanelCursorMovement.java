package cz.mg.resampler.gui.components.interaction;

import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import cz.mg.resampler.gui.Configuration;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class ResamplerPreviewPanelCursorMovement {
    public static void apply(final ResamplerPreviewPanel panel){
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                update(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                update(e);
            }
            
            public void update(MouseEvent e){
                panel.getMyParent().getStatusBar().update(e.getX(), e.getY());
                panel.getMyParent().setCursorX(e.getX());
                panel.getMyParent().setCursorY(e.getY());
                if(Configuration.getInstance().isShowCursorLocation()) panel.getMyParent().repaint();
            }
        });
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                panel.getMyParent().getStatusBar().hideMouseLabels();
                panel.getMyParent().setCursorX(-1);
                panel.getMyParent().setCursorY(-1);
                if(Configuration.getInstance().isShowCursorLocation()) panel.getMyParent().repaint();
            }
        });
    }
}
