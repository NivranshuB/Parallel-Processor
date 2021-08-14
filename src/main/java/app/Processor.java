package app;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import model.EmptyNode;
import model.Node;

/**
 * Author: Team UNTESTED
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
     * @return earliest available start time of the current processor
     */
    public int getAvailableStartTime() { return availableStartTime; }

    /**
     * Getter used by BnBScheduler
     * @return bottom weight of the schedule.
     */
    public int getBottomWeight() { return bottomWeight; }

    /**
     * Method to add new Node (task) to this Processor and update the bottom weight after addition of Node.
     * @param node
     * @param startTime
     */
    public void scheduleNode(Node node, int startTime) {
        taskOrderBnB.put(startTime, node);
        availableStartTime = Math.max((startTime + node.getWeight()), availableStartTime);
        bottomWeight = Math.max(node.getBottomWeight(), bottomWeight);
    }

    /**
     * Method to remove a node from the current Processor schedule at the specified time, then recalculate Processor's weight
     * @param startTime start time of the task to be removed
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
     * Gets the task order for the BnB version of this processor
     * @return taskOrderBnB
     */
    public TreeMap<Integer,Node> getTaskOrderBnB() { return taskOrderBnB; }

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
     * Sets the task order for this processor.
     * @param tOrder List of task nodes in the order they would be executed.
     */
    public void setTaskOrder(List<Node> tOrder) {
        taskOrder = tOrder;
    }

    /**
     * Gets the duplicated task order for this processor.
     * @return List of duplicated task nodes in the order they would be executed.
     */
    public List<Node> getDuplicateTaskOrder() {
        List<Node> duplicateTaskOrder = new ArrayList<>();
        for (Node task : taskOrder) {
            duplicateTaskOrder.add(task.duplicateNode());
        }
        return duplicateTaskOrder;
    }

    /**
     * Gets the finish time for this processor.
     * @return Finish time of this processor.
     */
    public int getFinishTime() {
        return finishTime;
    }

    /**
     * Sets the finish time for this processor.
     * @param fTime Finish time for this processor.
     */
    public void setFinishTime(int fTime) {
        finishTime = fTime;
    }

    /**
     * Method that assigns a task to this processor. If there is a gap between the current last task and the next
     * task, then create an empty Node object whose parent is the current last task and child is the new task. The
     * weight of the Node object will be the length of the time gap. This empty Node should be interpreted as a time
     * gap in the schedule.
     * @param node Task node to assign to this processor.
     * @param taskGap If applicable, the time between two tasks where the processor is idle.
     */
    public void assignTask(Node node, int taskGap) {
        if (taskGap != 0) {//Create an empty Node instance to represent the time gap
            Node emptyNode = new EmptyNode();
            //System.out.println("new empty node! of size: " + taskGap);
            if (taskOrder.size() > 0) {
                emptyNode.addParent(taskOrder.get(taskOrder.size() - 1));
            }
            emptyNode.addChild(node);
            emptyNode.setWeight(taskGap);
            emptyNode.setName("empty");
            taskOrder.add(emptyNode);
            finishTime += emptyNode.getWeight();
        }
        taskOrder.add(node);//Add the new task to this processor
        finishTime += node.getWeight();
    }

    /**
     * Checks to see if a task has been scheduled on this processor.
     * @param taskName Task node to check.
     * @return True if the input task is scheduled on this processor, otherwise returns False.
     */
    public boolean taskPresent(String taskName) {
        for (Node n : taskOrder) {
            if (n.getName().equals(taskName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the time this task will finish on this schedule, given a task Node object.
     * @param task Task node to check end time for.
     * @return End time of task node checked.
     */
    public int taskEndTime(Node task) {
        int nodeEndTime = 0;
        for (Node n : taskOrder) {
            if (n.getName().equals(task.getName())) {
                nodeEndTime += task.getWeight();
                break;
            } else {
                nodeEndTime += n.getWeight();
            }
        }

        return nodeEndTime;
    }
}

