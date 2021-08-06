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
     * Default processor constructor that creates the initial empty Processor instance
     */
    public Processor() {
        taskOrder = new ArrayList<>();
        finishTime = 0;
    }

    /**
     * Processor constructor used when creating the processors of the child schedule
     * @param taskList
     * @param fTime
     */
    public Processor(List<Node> taskList, int fTime) {
        taskOrder = taskList;
        finishTime = fTime;
    }

    public List<Node> getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(List<Node> tOrder) {
        taskOrder = tOrder;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int fTime) {
        finishTime = fTime;
    }

    /**
     * Method that assigns a task to this processor. If there is a gap between the current last task and the next
     * task, then create an empty Node object whose parent is the current last task and child is the new task. The
     * weight of the Node object will be the length of the time gap. This empty Node should be interpreted as a time
     * gap in the schedule.
     * @param node
     * @param taskGap
     */
    public void assignTask(Node node, int taskGap) {
        if (taskGap != 0) {//Create an empty Node instance to represent the time gap
            Node emptyNode = new EmptyNode();
            emptyNode.addParent(taskOrder.get(taskOrder.size() - 1));
            emptyNode.addChild(node);
            emptyNode.setWeight(taskGap);

        }
        taskOrder.add(node);//Add the new task to this processor
    }

    /**
     * Method that returns true if the input task is scheduled on this processor otherwise returns false
     * @param task
     * @return
     */
    public boolean taskPresent(Node task) {
        for (Node n : taskOrder) {
            if (n == task) {
                return true;
            }
        }

        return false;
    }

    /**
     * Given a task Node object this method returns the time that this task will finish on this schedule
     * @param task
     * @return
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

