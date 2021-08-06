package app;

import java.util.List;

/**
 * Not sure if we need this class if we are using the HashMap variation to represent the graph
 */
public class Task {

    public Boolean assigned;
    public int start_time;
    public int duration;
    public List<Task> dependency_list;

}
