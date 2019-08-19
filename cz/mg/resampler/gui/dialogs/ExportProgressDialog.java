package cz.mg.resampler.gui.dialogs;

import cz.mg.resampler.Resampler;
import cz.mg.resampler.gui.utilities.ErrorUtilities;
import cz.mg.resampler.gui.utilities.ImageUtilities;
import cz.mg.resampler.gui.utilities.ImageState;
import cz.mg.resampler.Image;
import cz.mg.resampler.Worker;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.Timer;


public class ExportProgressDialog extends javax.swing.JDialog {
    private static final int UPDATE_DELAY = 100;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private Resampler resampler;
    
    private final Timer timer = new Timer(UPDATE_DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Worker.State state = worker.getState();
            switch(state){
                case RUNNING: setProgress(worker.getProgress()); break;
                case ERROR:
                    ErrorDialog.show(ExportProgressDialog.this, "There was an error while resampling the image", worker.getException());
                    ErrorLogDialog.log(ErrorUtilities.generateLog(worker.getException()));
                    break;
                case DONE: save(); break;
            }
        }
        
        private void save(){
            try {
                image.updateFromResamplerImage(resampler);
                worker.clear();
                ImageUtilities.save(stream, ((ImageState)image).getSwingImage(), format);
                
                setProgress(1.0f);
                jButtonCancel.setText("Close");
            } catch (IOException e) {
                ErrorDialog.show(ExportProgressDialog.this, "Could not save image", e);
                ErrorLogDialog.log(ErrorUtilities.generateLog(e));
            }
        }
    });
    
    private final Worker worker = new Worker();
    private final ImageState image;
    private final OutputStream stream;
    private final ImageUtilities.ImageFileFormat format;
    
    public static final void show(Frame parent, Resampler resampler, ImageState sourceImage, ImageState destinationImage, Image sourceResamplerImage, Image destinationResamplerImage, String path, ImageUtilities.ImageFileFormat format){
        try (OutputStream stream = new FileOutputStream(path)) {
            ExportProgressDialog dialog = new ExportProgressDialog(parent, destinationImage, stream, format);
            dialog.resampler = resampler;
            dialog.worker.start(resampler, sourceResamplerImage, destinationResamplerImage);
            dialog.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Could not open file: " + e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static final void show(Dialog parent, Resampler resampler, ImageState sourceImage, ImageState destinationImage, Image sourceResamplerImage, Image destinationResamplerImage, String path, ImageUtilities.ImageFileFormat format){
        try (OutputStream stream = new FileOutputStream(path)) {
            ExportProgressDialog dialog = new ExportProgressDialog(parent, destinationImage, stream, format);
            dialog.resampler = resampler;
            dialog.worker.start(resampler, sourceResamplerImage, destinationResamplerImage);
            dialog.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Could not open file: " + e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private ExportProgressDialog(Frame parent, ImageState image, OutputStream stream, ImageUtilities.ImageFileFormat format) {
        super(parent, true);
        this.image = image;
        this.stream = stream;
        this.format = format;
        initComponents();
        postInitComponent();
    }
    
    private ExportProgressDialog(Dialog parent, ImageState image, OutputStream stream, ImageUtilities.ImageFileFormat format) {
        super(parent, true);
        this.image = image;
        this.stream = stream;
        this.format = format;
        initComponents();
        postInitComponent();
    }
    
    private void postInitComponent(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                shutdown();
            }
        });
        timer.start();
        setLocationRelativeTo(null);
    }
    
    private void cancel(){
        worker.cancel();
        dispose();
    }
    
    private void shutdown(){
        worker.cancel();
        timer.stop();
    }
    
    private void setProgress(float progress){
        jLabelProgress.setText(workerProgressToString(progress));
        jProgressBar.setValue((int) (progress * 100.0f));
    }
    
    private String workerProgressToString(float progress){
        progress = progress * 100.0f;
        return DECIMAL_FORMAT.format(progress) + " %";
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

        jProgressBar = new javax.swing.JProgressBar();
        jButtonCancel = new javax.swing.JButton();
        jPanelLabels = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jLabelProgress = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resampling");
        setMinimumSize(new java.awt.Dimension(256, 128));
        setPreferredSize(new java.awt.Dimension(256, 128));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWeights = new double[] {1.0};
        layout.rowWeights = new double[] {0.0, 0.0, 0.0};
        getContentPane().setLayout(layout);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
        getContentPane().add(jProgressBar, gridBagConstraints);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 8, 4);
        getContentPane().add(jButtonCancel, gridBagConstraints);

        jPanelLabels.setLayout(new java.awt.GridBagLayout());

        jLabelTitle.setText("Resampling:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 2, 2);
        jPanelLabels.add(jLabelTitle, gridBagConstraints);

        jLabelProgress.setText("N/A");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 2, 8);
        jPanelLabels.add(jLabelProgress, gridBagConstraints);

        getContentPane().add(jPanelLabels, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        cancel();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabelProgress;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanelLabels;
    private javax.swing.JProgressBar jProgressBar;
    // End of variables declaration//GEN-END:variables
}
