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

    public int getNumberOfTasks() {
        return taskOrderBnB.values().size();
    }

}

