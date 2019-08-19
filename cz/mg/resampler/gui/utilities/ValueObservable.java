package cz.mg.resampler.gui.utilities;

import javax.swing.event.ChangeListener;


/**
 *  Interface for custom components that hold some object as value which can be changed.
 *  Those changes can be tracked by listeners. Listeners can be added or removed.
 *  Listeners shall be instances of javax.swing.event.ChangeListener.
 */
public interface ValueObservable {
    public void addChangeListener(ChangeListener changeListener);
    public void removeChangeListener(ChangeListener changeListener);
}
