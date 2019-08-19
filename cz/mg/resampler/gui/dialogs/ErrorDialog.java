package cz.mg.resampler.gui.dialogs;

import java.awt.Component;
import javax.swing.JOptionPane;


public class ErrorDialog {
    public static void show(Component parent, String message, Exception e){
        if(e == null){
            JOptionPane.showMessageDialog(parent, message + ".", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, message + ": " + e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
