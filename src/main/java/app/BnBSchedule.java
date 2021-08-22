package app;

import model.Node;

import java.util.*;

/**
 * Class that stores the schedule of tasks and their starting times, as well as data associated with the current
 * schedule.
 */
public class BnBSchedule {

    private int max;
    private Set<Node> availableNodes = new HashSet<Node>();
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
     * Get the string representation of the schedule.
     * @return String representation of the schedule in the format "startingTime_nodeName".
     */
    public List<String> getStringStorage() {
        return stringStorage;
    }

    /**
     *
     * @return
     */
    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

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

}
