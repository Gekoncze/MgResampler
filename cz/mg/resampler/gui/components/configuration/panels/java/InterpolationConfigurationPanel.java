package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.gui.state.ResamplerImplementation;
import cz.mg.resampler.resamplers.java.interpolation.NearestLinearInterpolation;
//import cz.mg.resampler.resamplers.java.interpolation.AdaptiveSmoothInterpolation;
import cz.mg.resampler.resamplers.java.interpolation.BicubicInterpolation;
import cz.mg.resampler.resamplers.java.interpolation.BilinearInterpolation;
import cz.mg.resampler.resamplers.java.interpolation.Interpolation;
import cz.mg.resampler.resamplers.java.interpolation.NearestInterpolation;
import cz.mg.resampler.resamplers.java.interpolation.SmoothNearestInterpolation;


public class InterpolationConfigurationPanel extends ObjectConfigurationPanel<ResamplerImplementation> {
    private static final Class BASE_CLASS = Interpolation.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        NearestInterpolation.class,
        BilinearInterpolation.class,
        BicubicInterpolation.class,
        SmoothNearestInterpolation.class,
        NearestLinearInterpolation.class,
//        AdaptiveSmoothInterpolation.class
    };
    
    public InterpolationConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
