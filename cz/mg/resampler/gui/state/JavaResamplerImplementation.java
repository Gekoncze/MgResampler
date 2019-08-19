package cz.mg.resampler.gui.state;

import cz.mg.resampler.resamplers.java.CompositeResampler;
import cz.mg.resampler.resamplers.java.scaledown.ScaleDown;
import cz.mg.resampler.resamplers.java.scaleup.ScaleUp;
import cz.mg.resampler.resamplers.java.stretch.Stretch;
import cz.mg.resampler.Parameter;


public class JavaResamplerImplementation implements ResamplerImplementation {
    private final CompositeResampler resampler = new CompositeResampler();

    public JavaResamplerImplementation() {
    }
    
    @Parameter(order = 0)
    public Stretch getStretch() {
        return resampler.getStretch();
    }

    @Parameter(order = 0)
    public void setStretch(Stretch stretch) {
        resampler.setStretch(stretch);
    }

    @Parameter(order = 1)
    public ScaleUp getScaleUp() {
        return resampler.getScaleUp();
    }

    @Parameter(order = 1)
    public void setScaleUp(ScaleUp scaleUp) {
        resampler.setScaleUp(scaleUp);
    }
    
    @Parameter(order = 2)
    public ScaleDown getScaleDown() {
        return resampler.getScaleDown();
    }

    @Parameter(order = 2)
    public void setScaleDown(ScaleDown scaleDown) {
        resampler.setScaleDown(scaleDown);
    }
    
    @Parameter(order = 3)
    public boolean isFixBorder() {
        return resampler.isFixBorder();
    }

    @Parameter(order = 3)
    public void setFixBorder(boolean fixBorder) {
        resampler.setFixBorder(fixBorder);
    }

    @Override
    public cz.mg.resampler.StretchResampler getResampler() {
        return resampler;
    }

    @Override
    public String toString() {
        return "Java implementation";
    }
}
