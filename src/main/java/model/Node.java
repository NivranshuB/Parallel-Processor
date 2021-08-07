package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the Nodes in the graph, representing the tasks to be scheduled. Each node can have 0 to many
 * parent Nodes, and 0 to many child Nodes. Each node also has a corresponding weight.
 */
public class Node {

    private List<Node> parent = new ArrayList<Node>();
    private List<Node> child = new ArrayList<Node>();
    private int weight;
    private int start;
    private int processor;

    /**
     * Gets the parent nodes of this node.
     * @return Parent nodes of this node.
     */
    public List<Node> getParent() {
        return parent;
    }

    /**
     * Adds a parent node of this node.
     * @param parentNode Parent node of this node.
     */
    public void addParent(Node parentNode) {
        parent.add(parentNode);
    }

    /**
     * Gets the child nodes of this node.
     * @return Child nodes of this node.
     */
    public List<Node> getChild() {
        return child;
    }

    /**
     * Adds a child node of this node.
     * @param childNode Child node of this node.
     */
    public void addChild(Node childNode) {
        child.add(childNode);
    }

    /**
     * Gets the weight of this node.
     * @return Weight of this node.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of this node.
     * @param weight Weight of this node.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the start time of when this node is computed.
     * @return Start time when node is computed.
     */
    public int getStart(){ return start; }

    /**
     * Gets the processor this node is scheduled on.
     * @return Processor number this node is scheduled on.
     */
    public int getProcessor(){ return processor; }
}
