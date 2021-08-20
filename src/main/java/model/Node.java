package model;

import app.Processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
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


    public List<Node> getEquivalentNodes() { return equivalentNodes; } //Used by BnBScheduler

    public void addEquivalentNodes(Node node) { equivalentNodes.add(node); } //Used by BnBScheduler

    public int getBottomWeight() { return bottomWeight; } //Used by BnBScheduler

    public void setBottomWeight(int weight) { bottomWeight = weight; } //Used by BnBScheduler

    public void scheduleOneParent() { unscheduledParents--;} //Used by BnBScheduler to specify that a parent has been scheduled

    public void unscheduleOneParent() { unscheduledParents++; } //Used by BnBScheduler to specify that a parent has been unscheduled

    public void scheduleAllParents() { unscheduledParents = 0; } //Used by BnBScheduler to specify that all parents have been scheduled

    public Processor getBnBProcessor() { return processorBnB; } //Used by BnBScheduler to return assigned processor of node.

    /**
     * Checks if the provided node is equivalent to the current node.
     * @param node Node to be compared to.
     * @return true if equivalent, and false is non-equivalent.
     */
    public boolean isEquivalent(Node node) { return equivalentNodes.contains(node); }

    public boolean parentsScheduled() {
        if (unscheduledParents == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals(Node node) {
        if (node.getName().equals(name)) {
            return true;
        }
        return false;
    }


    /**
     * Method that returns the child nodes that become available for scheduling if the current node is scheduled.
     * @return Set containing the child nodes that have become available for scheduling.
     */
    public Set<Node> ifSchedule() {
        Set<Node> availableChildren = new HashSet<Node>();
        for (Node childNode : child) {
            childNode.scheduleOneParent();
            if (childNode.parentsScheduled()) {
                availableChildren.add(childNode);
            }
            childNode.unscheduleOneParent();
        }
        return availableChildren;
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
//            System.out.println("Unschedule parent, " + childNode.getName());
            if (childNode.parentsScheduled()) {
                availableChildren.add(childNode);
//                System.out.println("I can run, " + childNode.getName());
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
     * Reset Node data to be used for the next candidate schedule
     */
    public void resetNode() {
        start = Integer.MAX_VALUE;
        processorBnB = null;
        unscheduledParents = parent.size();
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
     * Gets the processor this node is scheduled on.
     * @return Processor number this node is scheduled on.
     */
    public int getProcessor(){ return processor; }

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
    
    /**
     * Creates a duplicate of this current node.
     */
    public Node duplicateNode() {
        Node dupeNode = new Node();
        dupeNode.setWeight(weight);
        dupeNode.setName(name);

        return dupeNode;
    }
}
