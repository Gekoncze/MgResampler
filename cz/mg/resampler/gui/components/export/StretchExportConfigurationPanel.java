package cz.mg.resampler.gui.components.export;

import cz.mg.resampler.resamplers.java.utilities.IntegerRegion;


public class StretchExportConfigurationPanel extends javax.swing.JPanel implements ExportConfigurationPanel {
    public StretchExportConfigurationPanel() {
        initComponents();
        postInitComponents();
    }
    
    private void postInitComponents(){
        sourceImageSizeLabels.setTitle("Source image");
        sourceRegionChooser.setTitle("Source region");
        destinationImageSizeChooser.setTitle("Destination image");
        destinationRegionChooser.setTitle("Destination region");
    }
    
    public void setSourceImageSize(int imageWidth, int imageHeight){
        sourceImageSizeLabels.setImageWidth(imageWidth);
        sourceImageSizeLabels.setImageHeight(imageHeight);
        sourceRegionChooser.setRegion(new IntegerRegion(0, 0, imageWidth, imageHeight));
    }
    
    public void setDestinationImageSize(int imageWidth, int imageHeight){
        destinationImageSizeChooser.setImageWidth(imageWidth);
        destinationImageSizeChooser.setImageHeight(imageHeight);
        destinationRegionChooser.setRegion(new IntegerRegion(0, 0, imageWidth, imageHeight));
    }
    
    @Override
    public IntegerRegion getSourceRegion(){
        return sourceRegionChooser.getRegion();
    }
    
    @Override
    public IntegerRegion getDestinationRegion(){
        return destinationRegionChooser.getRegion();
    }

    @Override
    public int getDestinationImageWidth() {
        return destinationImageSizeChooser.getImageWidth();
    }

    @Override
    public int getDestinationImageHeight() {
        return destinationImageSizeChooser.getImageHeight();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelContent = new javax.swing.JPanel();
        sourceImageSizeLabels = new cz.mg.resampler.gui.components.export.ImageSizeLabelsPanel();
        sourceRegionChooser = new cz.mg.resampler.gui.components.export.IntegerRegionChooserPanel();
        destinationImageSizeChooser = new cz.mg.resampler.gui.components.export.ImageSizeChooserPanel();
        destinationRegionChooser = new cz.mg.resampler.gui.components.export.IntegerRegionChooserPanel();
        jPanelDummy = new javax.swing.JPanel();

        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWeights = new double[] {1.0};
        layout.rowWeights = new double[] {1.0};
        setLayout(layout);

        java.awt.GridBagLayout jPanelContentLayout = new java.awt.GridBagLayout();
        jPanelContentLayout.columnWeights = new double[] {1.0};
        jPanelContentLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
        jPanelContent.setLayout(jPanelContentLayout);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanelContent.add(sourceImageSizeLabels, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanelContent.add(sourceRegionChooser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanelContent.add(destinationImageSizeChooser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanelContent.add(destinationRegionChooser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelContent.add(jPanelDummy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanelContent, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private cz.mg.resampler.gui.components.export.ImageSizeChooserPanel destinationImageSizeChooser;
    private cz.mg.resampler.gui.components.export.IntegerRegionChooserPanel destinationRegionChooser;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelDummy;
    private cz.mg.resampler.gui.components.export.ImageSizeLabelsPanel sourceImageSizeLabels;
    private cz.mg.resampler.gui.components.export.IntegerRegionChooserPanel sourceRegionChooser;
    // End of variables declaration//GEN-END:variables
}
