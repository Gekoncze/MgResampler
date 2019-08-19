package cz.mg.resampler.gui.utilities;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import cz.mg.resampler.Parameter;


public class DebugUtilities {
    private static void printSeparator(){
        System.out.println("");
        System.out.println("");
        System.out.println("---------- ---------- ---------- ---------- ----------");
        System.out.println("");
        System.out.println("");
    }
    
    public static void printStateTree(Object o){
        printSeparator();
        printStateTree(o, 0);
    }
    
    private static void printStateTree(Object o, int indentation){
        printIndentation(indentation);
        System.out.println("" + o);
        for(Method method : getObjectParameterMethods(o)){
            Object child = readObjectParameter(o, method);
            printStateTree(child, indentation + 1);
        }
    }
    
    private static List<Method> getObjectParameterMethods(Object o) {
        List<Method> methods = new LinkedList<>();
        for(Class c : getObjectClassAndSuperclasses(o)){
            for(Method method : c.getDeclaredMethods()) {
                if(method.isAnnotationPresent(Parameter.class)) {
                    if(method.getName().startsWith("get")) methods.add(method);
                    if(method.getName().startsWith("is")) methods.add(method);
                }
            }
        }
        return methods;
    }
    
    private static List<Class> getObjectClassAndSuperclasses(Object o) {
        List<Class> classes = new LinkedList<>();
        Class currentClass = o.getClass();
        while(currentClass != null){
            classes.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return classes;
    }
    
    private static Object readObjectParameter(Object o, Method method){
        try {
            return method.invoke(o);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("" + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    
    private static void printIndentation(int indentation){
        for(int i = 0; i < indentation; i++) System.out.print("    ");
    }
    
    public static void printPanelTree(JPanel panel){
        printSeparator();
        printPanelTree(panel, 0);
    }

    private static void printPanelTree(JPanel panel, int i) {
        printIndentation(i);
        System.out.println("" + panel.getClass().getSimpleName());
        for(Component component : panel.getComponents()){
            if(component instanceof JPanel) printPanelTree((JPanel) component, i + 1);
        }
    }
    
    public static void printValueCache(){
        printSeparator();
        HashMap<String, Object> cache = ComponentBuilderCache.getInstance().getValueCache();
        List<String> keys = new ArrayList<>(cache.keySet());
        Collections.sort(keys);
        for(String key : keys){
            System.out.println("key: " + key + "; value: " + cache.get(key));
        }
    }
}
