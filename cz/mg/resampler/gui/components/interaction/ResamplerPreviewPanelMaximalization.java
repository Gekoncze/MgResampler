package cz.mg.resampler.gui.components.interaction;

import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ResamplerPreviewPanelMaximalization {
    public static void apply(final ResamplerPreviewPanel panel){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount() == 2) {
                    panel.getMyParent().switchPanelMaximization(panel);
                }
            }
        });
    }
}
