package app;

import model.Edge;
import model.Node;

import java.util.*;

/**
 * Author: Team Untested (13)
 * Class that stores the schedule of tasks and their starting times, as well as data associated with the current
 * schedule.
 */
public class BnBSchedule {

    private int max;
    private Map<String, Node> nodeMap = new HashMap<String, Node>();
    private List<String> stringStorage = new ArrayList<String>();
    private int criticalPath = 0;

    /**
     * Default constructor for BnBSchedule that sets its max length to max value.
     */
    public BnBSchedule() {
        max = Integer.MAX_VALUE;
    }

    /**
     * Constructs BnBSchedule with the current list of processors and the tasks scheduled on them.
     * @param processors List of processors with tasks scheduled on them
     */
    public BnBSchedule(List<Processor> processors) {
        max = 0;
        for (Processor p : processors) {
            for (Node n : p.getTaskOrder()) {
                nodeMap.put(n.getName(), n);
            }
            stringStorage.add(p.toString());
            max = Math.max(max, p.getAvailableStartTime());
        }
    }

    /**
     * Method that calculates the critical path (total time taken by the entire schedule)
     * @return Total time taken by the entire schedule
     */
    public int calculateCriticalPath() {
        criticalPath = 0;
        int start = 0;

        for (String string : stringStorage) {
            String[] stringArray;

            stringArray = string.split(",");
            for (String splitString : stringArray) {
                String[] attributes = splitString.split("-");

                if (attributes.length >= 2) {
                    Iterator<Map.Entry<String, Node>> iterator = nodeMap.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, Node> entry = iterator.next();
                        Node node = entry.getValue();
                        if (node.getName().equals(attributes[1])) {
                            start = Integer.parseInt(attributes[0].replaceAll("\\s+", ""));
                            int potentialCriticalPath = start + node.getWeight();
                            if (potentialCriticalPath > criticalPath) {
                                criticalPath = potentialCriticalPath;
                            }
                        }
                    }
                }
            }

        }

        return criticalPath;
    }

    /**
     * Getter to return max weight of current schedule.
     * @return returns maximum weight of current schedule
     */
    public int getWeight() {
        return max;
    }

    /**
     * Prints the schedule to the console. Used during development and debugging.
     */
    public void printSchedule() {
        stringStorage.forEach(System.out::println);
    }

    /**
     * Retrieves the node list for this schedule.
     * @return List of nodes scheduled.
     */
    public List<Node> getNodeList() {
        int processorCount = 0;
        int criticalPath = 0;
        int start = 0;

        List<Node> nodeList = new ArrayList<>();

        for (String string : stringStorage) {
            String[] stringArray;

            stringArray = string.split(",");
            for (String splitString : stringArray) {
                String[] attributes = splitString.split("-");

                if (attributes.length >= 2) {
                    Iterator<Map.Entry<String, Node>> iterator = nodeMap.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, Node> entry = iterator.next();
                        Node node = entry.getValue();
                        if (node.getName().equals(attributes[1])) {
                            start = Integer.parseInt(attributes[0].replaceAll("\\s+", ""));
                            int potentialCriticalPath = start + node.getWeight();
                            if (potentialCriticalPath > criticalPath) {
                                criticalPath = potentialCriticalPath;
                            }
                            node.setProcessor(processorCount);
                            node.setStart(start);

                            nodeList.add(node);

                        }
                    }
                }
            }
            processorCount++;
        }

        max = criticalPath;

        return nodeList;
    }

    /**
     * This method checks to see if an object passed in is equal to this BnBSchedule. This overrides
     * the default equals method in the Object class.
     * @param o Object that is being passed in to compare this object to.
     * @return Returns True, if the two objects are equal, False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        //Check if the object passed in is this object
        if (o == this) {
            return true;
        }

        //Check if the object passed in is a BnBSchedule object
        if (!(o instanceof BnBSchedule)) {
            return false;
        }

        //Cast object passed in to BnBSchedule object
        BnBSchedule objectArg = (BnBSchedule) o;

        //Check values in the object passed in
        if (!(this.max == objectArg.max)) {
            return false;
        }

        if (!(this.nodeMap.equals(objectArg.nodeMap))) {
            return false;
        }

        //Check size of stringStorage
        if (this.stringStorage.size() != objectArg.stringStorage.size()) {
            return false;
        }

        //Check the values in stringStorage
        for (int i = 0; i < stringStorage.size(); i++) {
            String stringElement = this.stringStorage.get(i);
            String objectStringElement = objectArg.stringStorage.get(i);

            if (!(stringElement.equals(objectStringElement))) {
                return false;
            }
        }

        if (!(this.criticalPath == objectArg.criticalPath)) {
            return false;
        }

        //Object is the same as this BnBSchedule object
        return true;
    }

}
