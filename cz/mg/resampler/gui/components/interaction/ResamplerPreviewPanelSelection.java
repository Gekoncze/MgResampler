package cz.mg.resampler.gui.components.interaction;

import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ResamplerPreviewPanelSelection {
    public static void apply(final ResamplerPreviewPanel panel){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.getMyParent().setSelectedPreviewPanel(panel);
            }
        });
    }
}
