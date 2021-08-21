package app;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import model.Node;
import model.Edge;

/**
 * Author: Team UNTESTED
 * Singleton class that is responsible for the creation of all the possible schedule
 * combinations for completing all tasks. This class stores the representation of
 * the input task graph as two HashMaps, one representing the different tasks and
 * their weights while the other representing the different task dependencies of the
 * task graph and their weights.
 */
public class Scheduler {

    protected List<PropertyChangeListener> listeners = new ArrayList<>();

    public void addChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }
}
