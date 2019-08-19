package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.gui.state.ResamplerImplementation;
import cz.mg.resampler.resamplers.java.scaleup.*;


public class ScaleUpConfigurationPanel extends ObjectConfigurationPanel<ResamplerImplementation> {
    private static final Class BASE_CLASS = ScaleUp.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        null,
        StairStepScaleUp.class,
        NediLightnessScaleUp.class,
        NediRgbaScaleUp.class,
        DcciLightnessScaleUp.class,
        DcciRgbaScaleUp.class
    };
    
    public ScaleUpConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
