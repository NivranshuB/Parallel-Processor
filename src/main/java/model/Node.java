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
    private String name;
    private int weight;
    private int start;
    private int processor;

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

    public int getStart(){ return start; }

    public int getProcessor(){ return processor; }

    public String getName() {
        return name;
    }

    public void setName(String nm) {
        name = nm;
    }

    public Node duplicateNode() {
        Node dupeNode = new Node();
        dupeNode.setWeight(weight);
        dupeNode.setName(name);

        return dupeNode;
    }
}
