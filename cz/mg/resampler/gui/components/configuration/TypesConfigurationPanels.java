package cz.mg.resampler.gui.components.configuration;

import cz.mg.resampler.gui.components.configuration.panels.*;
import cz.mg.resampler.gui.state.ResamplerImplementation;


public class TypesConfigurationPanels {
    public static final Class[] TYPES = new Class[]{
        float.class,
        int.class,
        boolean.class,
        
        ResamplerImplementation.class,
        
        cz.mg.resampler.resamplers.java.stretch.Stretch.class,
        cz.mg.resampler.resamplers.java.interpolation.Interpolation.class,
        cz.mg.resampler.resamplers.java.convolutionkernel.ConvolutionKernel.class,
        cz.mg.resampler.resamplers.java.colordifference.SolidColorDifference.class,
        cz.mg.resampler.resamplers.java.colordifference.SimpleColorDifference.class,
        cz.mg.resampler.resamplers.java.colordifference.ColorDifference.class,
        cz.mg.resampler.resamplers.java.scaleup.ScaleUp.class,
        cz.mg.resampler.resamplers.java.scaledown.ScaleDown.class,
        cz.mg.resampler.resamplers.java.greyscale.Greyscale.class,
    };
    
    public static final Class[] PANELS = new Class[]{
        FloatConfigurationPanel.class,
        IntegerConfigurationPanel.class,
        BooleanConfigurationPanel.class,
        
        ResamplerImplementationConfigurationPanel.class,
        
        cz.mg.resampler.gui.components.configuration.panels.java.StretchConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.InterpolationConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.ConvolutionKernelConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.SolidColorDifferenceConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.SimpleColorDifferenceConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.ColorDifferenceConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.ScaleUpConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.ScaleDownConfigurationPanel.class,
        cz.mg.resampler.gui.components.configuration.panels.java.GreyscaleConfigurationPanel.class,
    };
}
