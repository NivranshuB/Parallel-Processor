package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the nodes in the graph, representing the tasks to be scheduled. Each node can have 0 to many
 * parent Nodes, and 0 to many child Nodes. Each node also has a corresponding weight.
 */

public class Node {

    private List<Node> parent = new ArrayList<Node>();
    private List<Node> child = new ArrayList<Node>();
    private int weight;

    public List<Node> getParent() {
        return parent;
    }

    public void addParent(Node parentNode) {
        parent.add(parentNode);
    }

    public List<Node> getChild() {
        return child;
    }

    public void addChild(Node childNode) {
        child.add(childNode);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
