package cz.mg.resampler.gui.components.configuration.panels;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.gui.state.JavaResamplerImplementation;
import cz.mg.resampler.gui.state.ResamplerImplementation;


public class ResamplerImplementationConfigurationPanel extends ObjectConfigurationPanel<ResamplerImplementation> {
    private static final Class BASE_CLASS = ResamplerImplementation.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        JavaResamplerImplementation.class,
    };
    
    public ResamplerImplementationConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
