package cz.mg.resampler.gui.components.export;

import cz.mg.resampler.resamplers.java.utilities.IntegerRegion;


public interface ExportConfigurationPanel {
    public IntegerRegion getSourceRegion();
    public IntegerRegion getDestinationRegion();
    public int getDestinationImageWidth();
    public int getDestinationImageHeight();
}
