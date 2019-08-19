package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.gui.state.ResamplerImplementation;
import cz.mg.resampler.resamplers.java.scaledown.ScaleDown;
import cz.mg.resampler.resamplers.java.scaledown.StairStepScaleDown;


public class ScaleDownConfigurationPanel extends ObjectConfigurationPanel<ResamplerImplementation> {
    private static final Class BASE_CLASS = ScaleDown.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        null,
        StairStepScaleDown.class,
    };
    
    public ScaleDownConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
