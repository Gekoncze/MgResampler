package cz.mg.resampler.gui.utilities;

import cz.mg.resampler.Parameter;
import cz.mg.resampler.gui.components.configuration.ParametersPanel;
import cz.mg.resampler.gui.components.configuration.TypesConfigurationPanels;
import cz.mg.resampler.gui.dialogs.ErrorLogDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ComponentBuilder {
    private int y = 0;
    private ParametersPanel target;
    
    public ComponentBuilder() {
    }
    
    public void build(ParametersPanel target, Object parentValue, String parentPath){
        if(target == null) throw new NullPointerException();
        if(parentPath == null) throw new NullPointerException();
        if(parentValue == null) return;
        
        buildBegin(target);
        List<MethodParameter> parameters = getParameters(parentValue);
        Collections.sort(parameters, new Comparator<MethodParameter>() {
            @Override
            public int compare(MethodParameter p1, MethodParameter p2) {
                return Integer.compare(p1.order, p2.order);
            }
        });
        for(MethodParameter parameter : parameters){
            Class parameterType = parameter.getter.getReturnType();
            String getterName = parameter.getter.getName();
            String rawParameterName = getterName.startsWith("get") ? getterName.replaceFirst("get", "") : getterName.replaceFirst("is", "");
            String setterName = "set" + rawParameterName;
            String parameterName = makeParameterNameHumanReadable(rawParameterName);
            for(int i = 0; i < TypesConfigurationPanels.TYPES.length; i++){
                Class currentParameterType = TypesConfigurationPanels.TYPES[i];
                Class currentPanelType = TypesConfigurationPanels.PANELS[i];
                if(parameterType == currentParameterType){
                    try {
                        String path = parentPath + ".(" + parentValue.getClass().getName() + ")." + rawParameterName;
                        JPanel configurationPanel = (JPanel) ReflectionUtilities.createInstance(currentPanelType, parentValue, path, parameterName, getterName, setterName);
                        addConfigurationPanel(configurationPanel);
                    } catch (Exception e) {
                        ErrorLogDialog.log(ErrorUtilities.generateLog(e));
                    }
                }
            }
        }
        buildEnd(target);
    }
    
    private void buildBegin(ParametersPanel target){
        this.target = target;
        target.setLayout(new GridBagLayout());
        y = 0;
    }
    
    private void buildEnd(JPanel target){
        GridBagLayout layout = (GridBagLayout) target.getLayout();
        layout.columnWeights = new double[] {1.0};
        target.revalidate();
        this.target = null;
    }
    
    private GridBagConstraints createContraints(){
        GridBagConstraints constraints = new java.awt.GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = y++;
        constraints.anchor = java.awt.GridBagConstraints.LINE_START;
        constraints.insets = new java.awt.Insets(2, 4, 2, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        return constraints;
    }
    
    private List<MethodParameter> getParameters(Object parentValue) {
        List<MethodParameter> parameterMethods = new LinkedList<>();
        for(Method method : parentValue.getClass().getMethods()) {
            if(method.isAnnotationPresent(Parameter.class)) {
                if(method.getName().startsWith("get") || method.getName().startsWith("is")){
                    int order = method.getAnnotation(Parameter.class).order();
                    parameterMethods.add(new MethodParameter(method, order));
                }
            }
        }
        return parameterMethods;
    }
    
    private void addConfigurationPanel(JPanel component){
        target.add(component, createContraints());
        ((ValueObservable)component).addChangeListener(new ExtendedChangeListener(target) {
            @Override
            public void stateChanged(ChangeEvent e) {
                parametersPanel.stateChanged(e);
            }
        });
    }
    
    private String makeParameterNameHumanReadable(String oldName){
        StringBuilder newName = new StringBuilder(oldName);
        for(int i = 0; i < newName.length(); i++){
            char ch = newName.charAt(i);
            if(i != 0 && Character.isUpperCase(ch)){
                newName.insert(i, " ");
                i++;
            }
        }
        for(int i = 0; i < newName.length(); i++){
            char ch = newName.charAt(i);
            if(i != 0 && Character.isUpperCase(ch)){
                newName.setCharAt(i, Character.toLowerCase(ch));
            }
        }
        return newName.toString();
    }
    
    private static abstract class ExtendedChangeListener implements ChangeListener {
        protected final ParametersPanel parametersPanel;

        public ExtendedChangeListener(ParametersPanel parametersPanel) {
            this.parametersPanel = parametersPanel;
        }
    };
    
    private static class MethodParameter {
        private final Method getter;
        private final int order;

        public MethodParameter(Method getter, int order) {
            this.getter = getter;
            this.order = order;
        }
    }
}
