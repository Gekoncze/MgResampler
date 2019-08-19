package cz.mg.resampler.gui.dialogs;

import java.awt.Color;
import java.awt.Window;
import javax.swing.JColorChooser;


public class ColorChooserDialog {
    public static Color show(Window parent, Color initialColor){
        return JColorChooser.showDialog(parent, "Choose a color", initialColor);
    }
}
