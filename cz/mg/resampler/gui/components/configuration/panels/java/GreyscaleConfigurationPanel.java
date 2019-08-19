package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.resamplers.java.StretchResampler;
import cz.mg.resampler.resamplers.java.greyscale.Greyscale;
import cz.mg.resampler.resamplers.java.greyscale.LightnessGreyscale;
import cz.mg.resampler.resamplers.java.greyscale.WeightAlphaGreyscale;
import cz.mg.resampler.resamplers.java.greyscale.WeightGreyscale;


public class GreyscaleConfigurationPanel extends ObjectConfigurationPanel<StretchResampler> {
    private static final Class BASE_CLASS = Greyscale.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        LightnessGreyscale.class,
        WeightGreyscale.class,
        WeightAlphaGreyscale.class
    };
    
    public GreyscaleConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
