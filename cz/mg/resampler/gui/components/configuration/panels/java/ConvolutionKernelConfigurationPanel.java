package cz.mg.resampler.gui.components.configuration.panels.java;

import cz.mg.resampler.gui.components.configuration.ObjectConfigurationPanel;
import cz.mg.resampler.gui.state.ResamplerImplementation;
import cz.mg.resampler.resamplers.java.convolutionkernel.BellConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.ConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.CubicConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.HermiteConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.LanczosConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.LinearConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.MitchellConvolutionKernel;
import cz.mg.resampler.resamplers.java.convolutionkernel.NearestConvolutionKernel;


public class ConvolutionKernelConfigurationPanel extends ObjectConfigurationPanel<ResamplerImplementation> {
    private static final Class BASE_CLASS = ConvolutionKernel.class;
    private static final Class[] DERIVED_CLASSES = new Class[]{
        NearestConvolutionKernel.class,
        LinearConvolutionKernel.class,
        CubicConvolutionKernel.class,
        BellConvolutionKernel.class,
        HermiteConvolutionKernel.class,
        MitchellConvolutionKernel.class,
        LanczosConvolutionKernel.class
    };
    
    public ConvolutionKernelConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        super(object, path, name, getterName, setterName, BASE_CLASS, DERIVED_CLASSES);
    }
}
