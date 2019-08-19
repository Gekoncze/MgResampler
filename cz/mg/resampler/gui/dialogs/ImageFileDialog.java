package cz.mg.resampler.gui.dialogs;

import cz.mg.resampler.gui.utilities.ImageUtilities;
import java.awt.Component;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImageFileDialog {
    private static JFileChooser fileChooser = null;
    
    private static void createInstance(){
        if(fileChooser == null){
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
        }
    }
    
    public static BufferedImage open(Window parent){
        createInstance();
        
        fileChooser.setSelectedFile(null);
        fileChooser.showOpenDialog(parent);
        
        if(fileChooser.getSelectedFile() != null){
            try {
                return ImageUtilities.load(new FileInputStream(fileChooser.getSelectedFile()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Could not open image '" + fileChooser.getSelectedFile().toPath().toString() + "': " + e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
    
    public static String savePath(Component parent){
        createInstance();
        
        fileChooser.setSelectedFile(null);
        fileChooser.showSaveDialog(parent);
        
        if(fileChooser.getSelectedFile() != null){
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
    
    public static void save(Window parent, String path, BufferedImage image, ImageUtilities.ImageFileFormat format){
        try {
            ImageUtilities.save(new FileOutputStream(new File(path)), image, format);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Could not save image '" + path + "': " + e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
