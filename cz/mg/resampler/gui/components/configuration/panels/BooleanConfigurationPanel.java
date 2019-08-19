package cz.mg.resampler.gui.components.configuration.panels;

import cz.mg.resampler.gui.components.value.BooleanValuePanel;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class BooleanConfigurationPanel extends BooleanValuePanel {
    private final Object object;
    private final String getterName;
    private final String setterName;

    public BooleanConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        this.object = object;
        this.getterName = getterName;
        this.setterName = setterName;
        initComponent(name);
    }
    
    private void initComponent(String name){
        setTitle(name);
        setDefaultValue(readObjectParameter());
        reset();
        
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                writeObjectParameter(getValue());
            }
        });
    }
    
    private boolean readObjectParameter(){
        try {
            return (boolean) object.getClass().getMethod(getterName).invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
    
    private void writeObjectParameter(boolean value){
        try {
            object.getClass().getMethod(setterName, boolean.class).invoke(object, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
}
