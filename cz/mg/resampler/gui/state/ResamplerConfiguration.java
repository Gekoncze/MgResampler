package cz.mg.resampler.gui.state;

import cz.mg.resampler.Parameter;


public class ResamplerConfiguration {
    private ResamplerImplementation resamplerImplementation = new JavaResamplerImplementation();

    public ResamplerConfiguration() {
    }

    @Parameter(order = 0)
    public ResamplerImplementation getResamplerImplementation() {
        return resamplerImplementation;
    }

    @Parameter(order = 0)
    public void setResamplerImplementation(ResamplerImplementation resamplerImplementation) {
        this.resamplerImplementation = resamplerImplementation;
    }
}
