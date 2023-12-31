package model;

import app.Processor;

/**
 * Author: Team Untested (13)
 * Class to represent the edges in the graph, representing the relationship between tasks (Nodes). Each edge will have
 * 1 parent Node, and 1 child Node. Each Edge also has a corresponding weight.
 */
public class Edge {

    private Node parentNode;
    private Node childNode;
    private int weight;

    /**
     * Gets the parent node for this edge.
     * @return Parent node of edge.
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * Sets the parent node for this edge.
     * @param parentNode Parent node of edge.
     */
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Gets the child node for this edge.
     * @return Child node of edge.
     */
    public Node getChildNode() {
        return childNode;
    }

    /**
     * Sets the child node for this edge.
     * @param childNode Child node of edge.
     */
    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    /**
     * Gets the weight of this edge.
     * @return Weight of edge.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of this edge.
     * @param weight Weight of edge.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Defines an equals method for the Edge class that overrides the default equals method.
     * @param o Object to compare this Edge object to.
     * @return Returns True, if objects are equal, False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        //Check if the object passed in is this object
        if (o == this) {
            return true;
        }

        //Check if the object passed in is an Edge object
        if (!(o instanceof Edge)) {
            return false;
        }

        //Cast object passed in to Edge object
        Edge objectArg = (Edge) o;

        //Check values in the object passed in
        if (!(this.parentNode.equals(objectArg.parentNode))) {
            return false;
        }

        if (!(this.childNode.equals(objectArg.childNode))) {
            return false;
        }

        if (!(this.weight == objectArg.weight)) {
            return false;
        }

        //Object is the same as this Edge
        return true;
    }
}
