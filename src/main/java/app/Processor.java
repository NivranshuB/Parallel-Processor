package app;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import model.Node;

/**
 * Author: Team Untested (13)
 * This class represents the ordered lists of nodes for a particular processor of
 * a particular schedule. It also stores the time that this processor is at after all
 * its tasks have been finished.
 */
public class Processor {
    
    public List<Node> taskOrder;//Ordered list of tasks scheduled on this processor
    public int finishTime;//The current finish time of this schedule
    private int availableStartTime; //Earliest time the processor is available. Used by BnBScheduler
    private int bottomWeight; //Used by BnBScheduler
    //Used by BnBScheduler to store schedules in ascending order, and to be able to use in-built methods to retrieve last
    //nodes and etc. No need to insert emptyNodes in between.
    private TreeMap<Integer, Node> taskOrderBnB = new TreeMap<Integer, Node>();

    /**
     * Default processor constructor that creates the initial empty Processor instance.
     */
    public Processor() {
        taskOrder = new ArrayList<>();
        finishTime = 0;
        availableStartTime = 0;
        bottomWeight = 0;
    }

    /**
     * Processor constructor used when creating the processors of the child schedule.
     * @param taskList List of task nodes to schedule on this processor.
     * @param fTime Current finish time of tasks scheduled on this processor.
     */
    public Processor(List<Node> taskList, int fTime) {
        taskOrder = taskList;
        finishTime = fTime;
    }

    /**
     * Getter used by BnBScheduler
     * @return Earliest available start time of the current processor
     */
    public int getAvailableStartTime() { return availableStartTime; }

    /**
     * Method to add new Node (task) to this Processor and update the bottom weight after addition of Node.
     * @param node Node to be scheduled to this processor.
     * @param startTime Start time for computation of the Node.
     */
    public void scheduleNode(Node node, int startTime) {
        taskOrderBnB.put(startTime, node);
        availableStartTime = Math.max((startTime + node.getWeight()), availableStartTime);
        bottomWeight = Math.max(node.getBottomWeight(), bottomWeight);
        taskOrder.add(node);
    }

    /**
     * Method to remove a node from the current Processor schedule at the specified time, then recalculate Processor's weight
     * @param startTime Start time of the task to be removed
     */
    public void unscheduleNodeAtTime(int startTime) {
        taskOrderBnB.remove(startTime);
        if (taskOrderBnB.size() > 0) {
            Node newLastNode = taskOrderBnB.lastEntry().getValue();
            availableStartTime = newLastNode.getStart() + newLastNode.getWeight();
        } else {
            availableStartTime = 0;
        }
    }

    /**
     * Returns the string representation of the TaskOrderBnB, showing the time periods when tasks are scheduled on it,
     * omitting free time. The format is "startTime-taskName, startTime-taskName..."
     * @return String representing time periods with tasks scheduled.
     */
    public String toString() {
        String str = "";
        for (int startTime : taskOrderBnB.keySet()) {
            str = str + startTime + "-" + taskOrderBnB.get(startTime).getName() + ", ";
        }
        return str;
    }

    /**
     * Gets the task order for this processor.
     * @return List of task nodes in the order they would be executed.
     */
    public List<Node> getTaskOrder() {
        return taskOrder;
    }

    /**
     * Defines an equals method for the Processor class that overrides the default equals method.
     * @param o Object to compare this Processor object to.
     * @return Returns True, if objects are equal, False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        //Check if the object passed in is this object
        if (o == this) {
            return true;
        }

        //Check if the object passed in is a Processor object
        if (!(o instanceof Processor)) {
            return false;
        }

        //Cast object passed in to Processor object
        Processor objectArg = (Processor) o;

        //Check values in the object passed in
        // Check taskOrder
        for (int i = 0; i < taskOrder.size(); i++) {
            Node taskNode = taskOrder.get(i);

            if (!(taskNode.equals(taskOrder.get(i)))) {
                return false;
            }
        }

        if (!(this.finishTime == objectArg.finishTime)) {
            return false;
        }

        if (!(this.availableStartTime == objectArg.availableStartTime)) {
            return false;
        }

        if (!(this.bottomWeight == objectArg.bottomWeight)) {
            return false;
        }

        if (!(taskOrderBnB.equals(objectArg.taskOrderBnB))) {
            return false;
        }

        //Object is the same as this Processor
        return true;
    }
}

