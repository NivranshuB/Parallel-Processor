package app;

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
    
    public List<Node> taskOrder;
    public int finishTime;

    public void assignTask(Node node, int taskGap) {
        if (taskGap == 0) {
            taskOrder.add(node);
        } else {
            Node emptyNode = new EmptyNode();
            emptyNode.addParent(taskOrder.get(taskOrder.size() - 1));
            emptyNode.addChild(node);
            emptyNode.setWeight(taskGap);

            taskOrder.add(node);
        }
    }

    public boolean taskPresent(Node task) {
        for (Node n : taskOrder) {
            if (n == task) {
                return true;
            }
        }

        return false;
    }

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

