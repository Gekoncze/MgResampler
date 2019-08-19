package cz.mg.resampler.gui.components.value;

import cz.mg.resampler.gui.utilities.ReflectionUtilities;
import cz.mg.resampler.gui.utilities.ValueObservable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class EnumValuePanel<T extends Enum> extends javax.swing.JPanel implements ValueObservable {
    private final T[] values;
    private T value;
    protected final List<ChangeListener> changeListeners = new LinkedList<>();
    private JComboBox<T> jComboBox;

    public EnumValuePanel(Class enumClass) throws ReflectiveOperationException {
        this.values = (T[]) ReflectionUtilities.getEnumValues(enumClass);
        initComponents();
        postInitComponents();
    }
    
    private void postInitComponents(){
        jComboBox = new JComboBox<>(new ObjectComboBoxModel());
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;
        c.insets = new java.awt.Insets(2, 4, 2, 4);
        add(jComboBox, c);
        jComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for(ChangeListener l : changeListeners) l.stateChanged(new ChangeEvent(EnumValuePanel.this));
            }
        });
    }
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T value){
        if(value == null){
            this.value = null;
            return;
        }
        
        for(T currentValue : values){
            if(currentValue != null){
                if(value == currentValue) this.value = currentValue;
            }
        }
    }
    
    public String getTitle(){
        return ((TitledBorder)getBorder()).getTitle();
    }
    
    public void setTitle(String title){
        ((TitledBorder)getBorder()).setTitle(title);
    }
    
    public JPanel getParametersPanel(){
        return jPanelParameters;
    }
    
    @Override
    public void addChangeListener(ChangeListener changeListener){
        changeListeners.add(changeListener);
    }
    
    @Override
    public void removeChangeListener(ChangeListener changeListener){
        changeListeners.remove(changeListener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelParameters = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("N/A"));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWeights = new double[] {1.0};
        layout.rowWeights = new double[] {0.0};
        setLayout(layout);

        jPanelParameters.setLayout(new java.awt.GridLayout(1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelParameters, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(evt.getButton() == 1 && evt.getClickCount() == 2){
            jPanelParameters.setVisible(!jPanelParameters.isVisible());
        }
    }//GEN-LAST:event_formMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelParameters;
    // End of variables declaration//GEN-END:variables

    private class ObjectComboBoxModel implements ComboBoxModel<T> {
        private final LinkedList<ListDataListener> listeners = new LinkedList<>();
        
        @Override
        public void setSelectedItem(Object anItem) {
            value = (T) anItem;
            for(ListDataListener l : listeners) l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
        }

        @Override
        public Object getSelectedItem() {
            return value;
        }

        @Override
        public int getSize() {
            return values.length;
        }

        @Override
        public T getElementAt(int index) {
            return values[index];
        }
        
        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }
    }
}
