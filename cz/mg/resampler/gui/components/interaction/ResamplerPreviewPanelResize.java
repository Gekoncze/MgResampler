package cz.mg.resampler.gui.components.interaction;

import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ResamplerPreviewPanelResize {
    public static void apply(final ResamplerPreviewPanel panel){
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.refresh();
            }
        });
    }
}
