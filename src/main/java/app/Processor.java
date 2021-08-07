package app;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Default processor constructor that creates the initial empty Processor instance.
     */
    public Processor() {
        taskOrder = new ArrayList<>();
        finishTime = 0;
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
            System.out.println("new empty node! of size: " + taskGap);
            if (taskOrder.size() > 0) {
                emptyNode.addParent(taskOrder.get(taskOrder.size() - 1));
            }
            emptyNode.addChild(node);
            emptyNode.setWeight(taskGap);
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
            if (n.getName() == taskName) {
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
            if (n == task) {
                nodeEndTime += task.getWeight();
                break;
            } else {
                nodeEndTime += n.getWeight();
            }
        }

        return nodeEndTime;
    }
}

