package cz.mg.resampler.gui.utilities;

import cz.mg.resampler.gui.dialogs.ErrorLogDialog;
import java.util.HashMap;


public class ComponentBuilderCache {
    private static ComponentBuilderCache instance = null;
    
    private final HashMap<String,Object> valueCache = new HashMap<>();

    public static ComponentBuilderCache getInstance() {
        if(instance == null) instance = new ComponentBuilderCache();
        return instance;
    }
    
    private ComponentBuilderCache() {
    }
    
    public Object getValue(String path, Class valueClass){
        if(valueClass == null) return null;
        Object value = valueCache.get(path);
        if(value == null){
            value = createValue(valueClass);
            valueCache.put(path, value);
        }
        return value;
    }
    
    private Object createValue(Class valueClass){
        try {
            return ReflectionUtilities.createInstance(valueClass);
        } catch (ReflectiveOperationException e) {
            ErrorLogDialog.log(ErrorUtilities.generateLog(e));
            return null;
        }
    }

    public HashMap<String, Object> getValueCache() {
        return valueCache;
    }
}
