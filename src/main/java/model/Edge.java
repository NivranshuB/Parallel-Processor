package model;



/**
 * Class to represent the edges in the graph, representing the relationship between tasks (Nodes). Each edge will have
 * 1 parent Node, and 1 child Node. Each Edge also has a corresponding weight.
 */

public class Edge {

    private Node parentNode;
    private Node childNode;
    private int weight;

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


}
