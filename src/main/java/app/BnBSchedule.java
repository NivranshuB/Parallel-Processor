package app;

import model.Node;
import java.util.*;

public class BnBSchedule {

    private int max;
    private Set<Node> availableNodes = new HashSet<Node>();
    private Map<String, Node> nodeMap = new HashMap<String, Node>();
    private List<String> stringStorage = new ArrayList<String>();

    public BnBSchedule() {
        max = Integer.MAX_VALUE;
    }

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

    public BnBSchedule(List<Processor> processors, Set<Node> availableNodes) {
        max = 0;

        this.availableNodes.addAll(availableNodes);

        // Update the max weight of current schedule.
        for (Processor curr : processors) {
            max = Math.max(max, curr.getAvailableStartTime());
            for (Node node : curr.getTaskOrderBnB().values()) {
                nodeMap.put(node.getName(), node);
            }
        }
    }

    /**
     * Getter to return max weight of current schedule
     * @return returns maximum weight of current schedule
     */
    public int getWeight() {return max;}

    public void printSchedule() {stringStorage.forEach(System.out::println); }

    public List<String> getStringStorage() {return stringStorage; }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

}
