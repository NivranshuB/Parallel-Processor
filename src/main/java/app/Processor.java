package app;

import java.util.List;
import model.Node;

/**
 * Author: Team UNTESTED
 * This class represents the ordered lists of nodes for a particular processor of
 * a particular schedule. It also stores the time that this processor is at after all
 * its tasks have been finished.
 */
public class Processor {
    
    public List<Node> task_order;
    public int finish_time;

    public void assign_task(Task task) {
        
    }

    /**
     * Gets the finish time for this processor.
     * @return int Finish time of this processor.
     */
    public int getFinishTime() {
        return finish_time;
    }
}

