package cz.mg.resampler.gui.components.configuration.panels;

import cz.mg.resampler.gui.components.value.IntegerValuePanel;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class IntegerConfigurationPanel extends IntegerValuePanel {
    private final Object object;
    private final String getterName;
    private final String setterName;

    public IntegerConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        this.object = object;
        this.getterName = getterName;
        this.setterName = setterName;
        initComponent(name);
    }
    
    private void initComponent(String name){
        int defaultValue = readObjectParameter();
        writeObjectParameter(Integer.MIN_VALUE);
        int minValue = readObjectParameter();
        writeObjectParameter(Integer.MAX_VALUE);
        int maxValue = readObjectParameter();
        writeObjectParameter(defaultValue);
                
        setTitle(name);
        setDefaultValue(defaultValue);
        setMinValue(minValue);
        setMaxValue(maxValue);
        reset();
        
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                writeObjectParameter(getValue());
            }
        });
    }
    
    private int readObjectParameter(){
        try {
            return (int) object.getClass().getMethod(getterName).invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
    
    private void writeObjectParameter(int value){
        try {
            object.getClass().getMethod(setterName, int.class).invoke(object, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
}
