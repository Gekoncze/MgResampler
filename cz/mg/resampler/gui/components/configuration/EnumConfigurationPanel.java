package cz.mg.resampler.gui.components.configuration;

import cz.mg.resampler.gui.components.value.EnumValuePanel;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class EnumConfigurationPanel<T extends Enum> extends EnumValuePanel<T> {
    private final Object object;
    private final String getterName;
    private final String setterName;
    private final Class enumClass;

    public EnumConfigurationPanel(Object object, String path, String name, String getterName, String setterName, Class enumClass) throws ReflectiveOperationException {
        super(enumClass);
        this.object = object;
        this.getterName = getterName;
        this.setterName = setterName;
        this.enumClass = enumClass;
        
        setTitle(name);
        setValue(readObjectParameter());
        
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                writeObjectParameter(getValue());
            }
        });
    }
    
    private T readObjectParameter(){
        try {
            return (T) object.getClass().getMethod(getterName).invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
    
    private void writeObjectParameter(T value){
        try {
            object.getClass().getMethod(setterName, enumClass).invoke(object, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }
    }
}
