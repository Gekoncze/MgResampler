package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.gui.state.ResamplerImplementation;
import cz.mg.resampler.resamplers.java.stretch.BoxAverageStretch;
import cz.mg.resampler.resamplers.java.stretch.BoxStretch;
import cz.mg.resampler.resamplers.java.stretch.ConvolutionStretch;
import cz.mg.resampler.resamplers.java.stretch.InterpolationStretch;
import cz.mg.resampler.resamplers.java.stretch.Stretch;


public class StretchConfigurationPanel extends ObjectConfigurationPanel<ResamplerImplementation> {
    private static final Class BASE_CLASS = Stretch.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        BoxStretch.class,
        BoxAverageStretch.class,
        InterpolationStretch.class,
        ConvolutionStretch.class
    };
    
    public StretchConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
