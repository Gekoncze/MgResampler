package cz.mg.resampler.gui.components.configuration;

import cz.mg.resampler.gui.components.value.ObjectValuePanel;
import cz.mg.resampler.gui.dialogs.ErrorLogDialog;
import cz.mg.resampler.gui.utilities.ErrorUtilities;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ObjectConfigurationPanel<T> extends ObjectValuePanel<T> {
    private final Class baseClass;
    private final Class[] derivedClasses;
    private final Object object;
    private final String getterName;
    private final String setterName;
    private final ParametersPanel[] parameterPanels;

    public ObjectConfigurationPanel(Object object, String path, String name, String getterName, String setterName, Class baseClass, Class[] derivedClasses) {
        super(path, derivedClasses);
        
        this.baseClass = baseClass;
        this.derivedClasses = derivedClasses;
        this.object = object;
        this.getterName = getterName;
        this.setterName = setterName;
        this.parameterPanels = new ParametersPanel[derivedClasses.length];
        
        setTitle(name);
        setValue(readObjectParameter());
        
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                writeObjectParameter(getValue());
                if(e.getSource() instanceof ObjectValuePanel) rebuild();
            }
        });
        
        rebuild();
    }
    
    private void rebuild(){
        getParametersPanel().removeAll();
        if(getValue() != null) getParametersPanel().add(getValuePanel(getValue()));
        getParametersPanel().revalidate();
    }
    
    private ParametersPanel getValuePanel(T value){
        for(int i = 0; i < derivedClasses.length; i++){
            if(value.getClass().equals(derivedClasses[i])){
                ParametersPanel panel = new ParametersPanel(value, getPath());
                panel.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        for(ChangeListener l : changeListeners) l.stateChanged(new ChangeEvent(e.getSource()));
                    }
                });
                if(parameterPanels[i] == null) parameterPanels[i] = panel;
                return parameterPanels[i];
            }
        }
        throw new RuntimeException();
    }
    
    private T readObjectParameter(){
        try {
            return (T) object.getClass().getMethod(getterName).invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            ErrorLogDialog.log(ErrorUtilities.generateLog(e));
            return null;
        }
    }
    
    private void writeObjectParameter(T value){
        try {
            object.getClass().getMethod(setterName, baseClass).invoke(object, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            ErrorLogDialog.log(ErrorUtilities.generateLog(e));
        }
    }
}
