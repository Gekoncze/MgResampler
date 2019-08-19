package cz.mg.resampler.gui.components.configuration;

import cz.mg.resampler.gui.utilities.ComponentBuilder;
import cz.mg.resampler.gui.utilities.ValueObservable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ParametersPanel extends JPanel implements ValueObservable {
    private final List<ChangeListener> changeListeners = new LinkedList<>();
    
    public ParametersPanel(Object parentValue, String parentPath) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.build(this, parentValue, parentPath);
    }
    
    public void stateChanged(ChangeEvent e){
        for(ChangeListener l : changeListeners) l.stateChanged(e);
    }
    
    @Override
    public void addChangeListener(ChangeListener changeListener){
        changeListeners.add(changeListener);
    }
    
    @Override
    public void removeChangeListener(ChangeListener changeListener){
        changeListeners.remove(changeListener);
    }
}
