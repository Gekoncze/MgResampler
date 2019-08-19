package cz.mg.resampler.gui.components.configuration.panels;

import cz.mg.resampler.gui.components.value.FloatValuePanel;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class FloatConfigurationPanel extends FloatValuePanel {
    private final Object object;
    private final String getterName;
    private final String setterName;

    public FloatConfigurationPanel(Object object, String path, String name, String getterName, String setterName) {
        this.object = object;
        this.getterName = getterName;
        this.setterName = setterName;
        initComponent(name);
    }

    private void initComponent(String name){
        float defaultValue = readObjectParameter();
        writeObjectParameter(Float.MIN_VALUE);
        float minValue = readObjectParameter();
        writeObjectParameter(Float.MAX_VALUE);
        float maxValue = readObjectParameter();
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
    
    private float readObjectParameter(){
        try {
            return (float) object.getClass().getMethod(getterName).invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
    
    private void writeObjectParameter(float value){
        try {
            object.getClass().getMethod(setterName, float.class).invoke(object, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
}
