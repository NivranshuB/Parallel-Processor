package model;

import app.Processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Team UNTESTED
 * Class to represent the Nodes in the graph, representing the tasks to be scheduled. Each node can have 0 to many
 * parent Nodes, and 0 to many child Nodes. Each node also has a corresponding weight.
 */
public class Node {

    private List<Node> parent = new ArrayList<Node>();
    private List<Node> child = new ArrayList<Node>();
    private List<Node> equivalentNodes = new ArrayList<Node>(); //Used by BnBScheduler
    private String name;
    private int unscheduledParents;
    private int weight;
    private int start;
    private int processor;
    private Processor processorBnB;
    private int bottomWeight; //used by BnBScheduler

    /**
     * Retrieves the equivalent nodes for this Node object.
     * @return List of equivalent nodes.
     */
    public List<Node> getEquivalentNodes() { return equivalentNodes; } //Used by BnBScheduler

    /**
     * Adds a node which is deemed to be equivalent to this Node object. Equivalent is when it has the same
     * characteristics that it can be swapped interchangeably in the schedule.
     * @param node Node deemed to be equivalent.
     */
    public void addEquivalentNodes(Node node) { equivalentNodes.add(node); } //Used by BnBScheduler

    /**
     * Retrieves the bottom weight stored in this Node.
     * @return Bottom weight of this Node.
     */
    public int getBottomWeight() { return bottomWeight; } //Used by BnBScheduler

    /**
     * Sets the bottom weight for this Node.
     * @param weight Bottom weight for this Node.
     */
    public void setBottomWeight(int weight) { bottomWeight = weight; } //Used by BnBScheduler

    /**
     * Method used to decrement the count of unscheduled parents for this Node.
     */
    public void scheduleOneParent() { unscheduledParents--;} //Used by BnBScheduler to specify that a parent has been scheduled

    /**
     * Method used to increment the count of unscheduled parents for this Node.
     */
    public void unscheduleOneParent() { unscheduledParents++; } //Used by BnBScheduler to specify that a parent has been unscheduled

    /**
     * Retrieves the processor this Node object is assigned to.
     * @return Processor Node object is assigned to.
     */
    public Processor getBnBProcessor() { return processorBnB; } //Used by BnBScheduler to return assigned processor of node.

    /**
     * Checks if the provided node is equivalent to the current node.
     * @param node Node to be compared to.
     * @return true if equivalent, and false is non-equivalent.
     */
    public boolean isEquivalent(Node node) { return equivalentNodes.contains(node); }

    /**
     * Checks if there all parents for this Node object have been scheduled.
     * @return True if all parents for this Node object have been scheduled, False otherwise.
     */
    public boolean parentsScheduled() {
        if (unscheduledParents == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that schedules the current node on the specified processor at the specified start time
     * @param p Processor that the node is to be scheduled on
     * @param start Start time of the node task on the processor
     * @return A Set of Nodes containing all the child nodes that are now available to be scheduled next
     */
    public Set<Node> schedule(Processor p, int start) {
        processorBnB = p;
        this.start = start;
        Set<Node> availableChildren = new HashSet<Node>();
        for (Node childNode : child) {
            childNode.scheduleOneParent();
            if (childNode.parentsScheduled()) {
                availableChildren.add(childNode);
            }
        }
        return availableChildren;
    }

    /**
     * Used in BnB Scheduler to remove a node from being placed on the schedule
     */
    public void unschedule() {
        processorBnB = null;
        start = 0;
        for (Node c : child) {
            c.unscheduleOneParent();
        }
    }

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
        unscheduledParents++;
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
     * Sets the start time of when this node is computed.
     * @param start Start time for computation of this Node.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gets the processor this node is scheduled on.
     * @return Processor number this node is scheduled on.
     */
    public int getProcessor(){ return processor; }

    /**
     * Sets the processor this node is scheduled on.
     * @param processor Processor used to schedule this Node.
     */
    public void setProcessor(int processor) {
        this.processor = processor;
    }

    /**
     * Gets the name of this node as a string.
     * @return Name of this node as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this node as a string.
     * @param nm Name of this node as a string.
     */
    public void setName(String nm) {
        name = nm;
    }

}
