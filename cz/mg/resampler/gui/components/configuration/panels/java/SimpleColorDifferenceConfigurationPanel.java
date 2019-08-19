package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.resamplers.java.colordifference.FastRgbColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.SphereHslColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.FastHslColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.ConeHslColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.CubeRgbColorDifference;
import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.resamplers.java.StretchResampler;
import cz.mg.resampler.resamplers.java.colordifference.AlphaColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.ColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.WeightHslColorDifference;
import cz.mg.resampler.resamplers.java.colordifference.WeightRgbColorDifference;


public class SimpleColorDifferenceConfigurationPanel extends ObjectConfigurationPanel<StretchResampler> {
    private static final Class BASE_CLASS = ColorDifference.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        FastRgbColorDifference.class,
        CubeRgbColorDifference.class,
        WeightRgbColorDifference.class,
        FastHslColorDifference.class,
        ConeHslColorDifference.class,
        SphereHslColorDifference.class,
        WeightHslColorDifference.class,
        AlphaColorDifference.class
    };
    
    public SimpleColorDifferenceConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
