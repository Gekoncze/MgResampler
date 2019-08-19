package cz.mg.resampler.gui.utilities;

import java.lang.reflect.Constructor;


public class ReflectionUtilities {
    public static Object createInstance(Class c, Object... arguments) throws ReflectiveOperationException {
        for(Constructor constructor : c.getConstructors()){
            if(constructor.getParameterCount() == arguments.length){
                Class[] parameterTypes = constructor.getParameterTypes();
                boolean compatible = true;
                for(int i = 0; i < arguments.length; i++){
                    Class parameterType = parameterTypes[i];
                    Object argument = arguments[i];
                    if(!parameterType.isInstance(argument)){
                        compatible = false;
                        System.out.println("" + parameterType.getSimpleName() + " incompatible with " + argument.getClass().getSimpleName());
                    }
                }
                if(compatible) return constructor.newInstance(arguments);
            }
        }
        System.out.print("\nNo match for");
        for(int i = 0; i < arguments.length; i++) System.out.print(" " + arguments[i].getClass().getSimpleName());
        System.out.println("; number of constructors par: " + c.getConstructors()[0].getParameterTypes().length);
        throw new NoSuchMethodException();
    }
    
    public static Enum[] getEnumValues(Class enumClass) throws ReflectiveOperationException {
        Object[] values = (Object[])enumClass.getMethod("values").invoke(null);
        Enum[] enums = new Enum[values.length];
        for(int i = 0; i < values.length; i++) enums[i] = (Enum) values[i];
        return enums;
    }
}
