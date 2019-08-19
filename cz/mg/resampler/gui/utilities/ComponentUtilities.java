package cz.mg.resampler.gui.utilities;

import java.awt.Component;
import java.awt.Container;


/**
 *  The purpose of this class is to help with some component related problems.
 */
public class ComponentUtilities {
    /**
     *  This method visits all nodes of the component tree structure and executes
     *  given listener's methods for each of them.
     *  The nodes are visited two times - once when the three node is entered
     *  (parent to child) and once when the node is exited (child to parent).
     *  For both cases there are separate methods to be implemented in the
     *  component visit listener.
     *  @param component: the root component of the component tree
     *  @param listener: the listener whose method is executed for each node
     */
    public static void forAll(Component component, ComponentVisitListener listener) {
        listener.componentEntered(component);
        if(component instanceof Container) {
            for(Component child : ((Container) component).getComponents()) {
                forAll(child, listener);
            }
        }
        listener.componentExited(component);
    }
    
    /**
     *  The listener interface used for the forAll method.
     */
    public static interface ComponentVisitListener {
        public void componentEntered(Component component);
        public void componentExited(Component component);
    }
    
    /**
     *  Adapter used to simplify listener creation.
     */
    public static abstract class ComponentVisitAdapter implements ComponentVisitListener {
        @Override
        public void componentEntered(Component component) {
        }

        @Override
        public void componentExited(Component component) {
        }
    }
}
