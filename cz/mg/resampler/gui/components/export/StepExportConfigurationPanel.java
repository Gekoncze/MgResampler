package cz.mg.resampler.gui.components.export;

import cz.mg.resampler.resamplers.java.utilities.IntegerRegion;


public class StepExportConfigurationPanel extends javax.swing.JPanel implements ExportConfigurationPanel {
    public StepExportConfigurationPanel() {
        initComponents();
    }
    
    @Override
    public IntegerRegion getSourceRegion() {
        return null;
    }

    @Override
    public IntegerRegion getDestinationRegion() {
        return null;
    }
    
    @Override
    public int getDestinationImageWidth() {
        return 0; // TODO
    }

    @Override
    public int getDestinationImageHeight() {
        return 0; // TODO
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
