package app;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Team UNTESTED
 * Parent class that all scheduler classes extend.
 */
public class Scheduler {

    protected List<PropertyChangeListener> listeners = new ArrayList<>();

    /**
     * Adds a change listener.
     * @param listener
     */
    public void addChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }
}
