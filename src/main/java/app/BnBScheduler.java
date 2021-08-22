package app;
import model.Edge;
import model.Node;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Author: Team UNTESTED
 * Singleton class that is a variation of the Scheduler class with a different pruning algorithm. This class is
 * responsible for performing an exhaustive search (with pruning to limit the search size) of all possible
 * schedules for the provided tasks on the specified number of processors. This class stores the representation of
 * the input task graph as two HashMaps, one representing the different tasks and their weights while the other
 * representing the different task dependencies of the task graph and their weights.
 */
public class BnBScheduler extends Scheduler implements Callable<BnBSchedule> {

    private HashMap<String, Node> nodeMap;
    private HashMap<String, Edge> edgeMap;
    private List<Processor> listOfProcessors = new ArrayList<Processor>();
    private BnBSchedule optimalSchedule;
    private Set<Node> availableToSchedule = new HashSet<Node>();
    private List<Node> startingParallelNodes = new ArrayList<Node>();
    private Boolean startFlag = true;
    private final int coreNumber;

    private MainController mainController = MainController.getInstance();

    public BnBScheduler(DotFileReader dotFileReader, Config config, int coreNm) {

        coreNumber = coreNm;
        nodeMap = dotFileReader.getNodeMap();
        edgeMap = dotFileReader.getEdgeMap();
        int processorCount = config.getNumOfProcessors();
        int count = 0;
        // Create processors
        while (count < processorCount) {
            Processor newProcessor = new Processor();
            listOfProcessors.add(newProcessor);
            count++;
        }

        System.out.println(dotFileReader.getRootNodeList().size());
        // Store all root nodes into the availableToSchedule set.
        availableToSchedule.addAll(dotFileReader.getRootNodeList());

        // Calculate bottom weight of all nodes
        for (Node n : availableToSchedule) {
            calculateBottomWeight(n);
        }

        // Check for equivalency of nodes
        for (Node n : nodeMap.values()) {
            for (Node parent : n.getParent()) {
                for (Node other : parent.getChild()) {
                    if (!n.getName().equals(other.getName()) && !n.isEquivalent(other) && nodeInterchangeability(n, other)) {
                        n.addEquivalentNodes(other);
                        other.addEquivalentNodes(n);
                    }
                }
            }
        }

        optimalSchedule = new BnBSchedule();
    }

    public BnBScheduler(DotFileReader dotFileReader, Config config, List<String> startingNodes, int coreNm) {

        coreNumber = coreNm;
        nodeMap = dotFileReader.getNodeMap();
        edgeMap = dotFileReader.getEdgeMap();

        int processorCount = config.getNumOfProcessors();
        int count = 0;
        // Create processors
        while (count < processorCount) {
            Processor newProcessor = new Processor();
            listOfProcessors.add(newProcessor);
            count++;
        }

        // Store all root nodes into the availableToSchedule set.
        availableToSchedule.addAll(dotFileReader.getRootNodeList());
        System.out.println("Available to schedule size: " + availableToSchedule.size());

        for (String n : startingNodes) {
            startingParallelNodes.add(nodeMap.get(n));
        }

        // Calculate bottom weight of all nodes
        for (Node n : availableToSchedule) {
            calculateBottomWeight(n);
        }

        // Check for equivalency of nodes
        for (Node n : nodeMap.values()) {
            for (Node parent : n.getParent()) {
                for (Node other : parent.getChild()) {
                    if (!n.getName().equals(other.getName()) && !n.isEquivalent(other) && nodeInterchangeability(n, other)) {
                        n.addEquivalentNodes(other);
                        other.addEquivalentNodes(n);
                    }
                }
            }
        }

        optimalSchedule = new BnBSchedule();
    }

    /**
     * Method that returns the Schedule representing the optimum schedule.
     * @return BnBSchedule object representing the optimal schedule.
     */
    public BnBSchedule getSchedule() {
        System.out.println(availableToSchedule.size());
        optimalScheduleSearch(availableToSchedule);

        //temp
        for (PropertyChangeListener l : listeners) {
            l.propertyChange(new PropertyChangeEvent(this, "optimal schedule", "old", optimalSchedule.getWeight()));
        }

        return optimalSchedule;
    }

    /**
     * Method that returns the Schedule representing the optimum schedule.
     * @return BnBSchedule object representing the optimal schedule.
     */
    public BnBSchedule getSchedule(Set<Node> availableList) {
        System.out.println(availableList.size());
        optimalScheduleSearch(availableList);
        return optimalSchedule;
    }

    /**
     * Remove the specified Node from the processor it has been allocated to.
     * @param node Node to be unscheduled.
     */
    public void unscheduleNode(Node node) {
        if (node.getBnBProcessor() != null) {
            node.getBnBProcessor().unscheduleNodeAtTime(node.getStart());
        }
        node.unschedule();
    }

    /**
     * Method that calculates the earliest start time for the current node considering whether or not the node is to be
     * scheduled on the same processor as its parent or not.
     * @param n Node to be scheduled
     * @param p Processor that node is intending to be scheduled on
     * @return earliest start time due to parents
     */
    public int startTimeAfterParent(Node n, Processor p) {
        int offsetedTime = 0;
        for (Node parent : n.getParent()) {
            if (parent.getBnBProcessor() == p) {
                offsetedTime = Math.max(offsetedTime, parent.getStart() + parent.getWeight());
            } else {
                // Add transfer weight between processors if not on the same processor
                String edge = parent.getName() + "_" + n.getName();
                offsetedTime = Math.max(offsetedTime, parent.getStart() + parent.getWeight() + edgeMap.get(edge).getWeight());
            }
        }
        return offsetedTime;
    }

    /**
     * Recursive function that calculates the bottom weight of the specified node where the bottom weight is the sum of its
     * own weight and the maximum of the bottom weights of its own child nodes.
     * @param node Node to be calculated
     * @return Bottom weight of the calculated node
     */
    public int calculateBottomWeight(Node node) {
        if (node.getChild().size() > 0) {
            int maxChildBottomWeight = 0;
            for (Node child : node.getChild()) {
                maxChildBottomWeight = Math.max(maxChildBottomWeight, calculateBottomWeight(child));
            }
            node.setBottomWeight(node.getWeight() + maxChildBottomWeight);
        } else {
            node.setBottomWeight(node.getWeight());
        }
        return node.getBottomWeight();
    }

    /**
     * Check if two nodes are exactly identical, and therefore their positions can be interchangeable.
     * @param node First node to be compared
     * @param check Second node to be compared
     */
    public boolean nodeInterchangeability(Node node, Node check) {
        //Check if weights are the same
        if (node.getWeight() != check.getWeight()) {return false;}

        //Check if the number of parents and child nodes of the two nodes are the same
        if (node.getParent().size() != check.getParent().size() || node.getChild().size() != check.getChild().size()) {
            return false;
        }

        //Check if all parents in node appear in check, and that they have the same transmission costs.
        for (Node parent : node.getParent()){
            String nodeToParent = parent.getName() + "_" + node.getName();
            String checkToParent = parent.getName() + "_" + check.getName();
            if (!(check.getParent().contains(parent) && edgeMap.get(nodeToParent).getWeight() == edgeMap.get(checkToParent).getWeight())) {
                return false;
            }
        }

        //Check if all children in node appear in check, and that they have the same transmission costs.
        for (Node child : node.getChild()){
            String nodeToChild = node.getName() + "_" + child.getName();
            String checkToChild = check.getName() + "_" + child.getName();
            if (check.getChild().contains(child) && edgeMap.get(nodeToChild).getWeight() == edgeMap.get(checkToChild).getWeight()) {

            } else {
                return false;
            }
        }
        return true; //Conclude that they are equivalent and interchangeable
    }
    
    /**
     * Checks if a node is interchangeable with another in the set of nodes
     * @param node Node to be checked
     * @param nodeSet Set of nodes to be compared to
     * @return true if interchangeable, false otherwise
     */
    public boolean checkInterchangeableNode(Node node, Set<Node> nodeSet) {
        for (Node n : nodeSet) {
            if (n.getEquivalentNodes().contains(node)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a processor is interchangeable with another in the set of processors
     * @param processor Processor to be checked
     * @param processorSet Set of processors to be compared to
     * @param node Node that is intended to be added
     * @param startTime Start time of Node to be checked
     * @return true if interchangeable, false otherwise
     */
    public boolean checkInterchangeableProcessor(Processor processor, Set<Processor> processorSet, Node node, int startTime) {
        int comparedStartTime = 0;
        //hypothetically test if the start time is the same if the node is scheduled on another processor
        for (Processor p : processorSet) {
            comparedStartTime = Math.max(p.getAvailableStartTime(), startTimeAfterParent(node, p));
            if (comparedStartTime == startTime && processor.toString().equals(p.toString())) { return true;}
        }
        return false;
    }

    private void optimalScheduleSearch(Set<Node> freeNodes) {
        BnBSchedule optimalCandidate = new BnBSchedule();
//        freeNodes.forEach(s -> System.out.println(s.getName()));
        // Only run if there are available nodes to schedule
        if (freeNodes.size() > 0) {

            // Only schedule one of the equivalent nodes if there are equivalent nodes (Pruning stage 1)
            Set<Node> uniqueNodes = new HashSet<Node>();
            for (Node n : freeNodes) {
                if (!checkInterchangeableNode(n, uniqueNodes)) {
                    uniqueNodes.add(n);
                    //Check if processors are equivalent
                    Set<Processor> uniqueProcessors = new HashSet<Processor>();
                    for (Processor p : listOfProcessors) {
                        // Calculate earliest start time of Node on current Processor
                        int start = Math.max(p.getAvailableStartTime(), startTimeAfterParent(n, p));
                        if (!checkInterchangeableProcessor(p, uniqueProcessors, n, start)) {
                            uniqueProcessors.add(p);

                            // Check if the weight of the schedule is greater than current optimum schedule's weight. If
                            // yes, stop checking. Otherwise, continue.
                            if ((n.getBottomWeight() + start) <= optimalSchedule.getWeight()) {
                                Set<Node> newFreeNodes = n.schedule(p, start);
                                p.scheduleNode(n, start);
                                newFreeNodes.addAll(freeNodes);
                                newFreeNodes.remove(n);
                                optimalScheduleSearch(newFreeNodes);
                                unscheduleNode(n);
                            }
                        }
                    }
                }
            }
        } else { //If all free nodes scheduled
            int max = -1; //Initialise max

            for (Processor process : listOfProcessors) {
                max = Math.max(max, process.getAvailableStartTime());
            }
            if (max < optimalSchedule.getWeight()) {
                optimalSchedule = new BnBSchedule(listOfProcessors);

                if (Config.getInstance().getVisualise()) {
                    mainController.createGantt(optimalSchedule.getNodeList());
                    for (PropertyChangeListener l : listeners) {
                        l.propertyChange(new PropertyChangeEvent(this, "update progress", "old", optimalSchedule.getWeight()));
                    }
                }
//                optimalSchedule.printSchedule();
                MainController.getInstance().addOptimalToSearchGraph(optimalSchedule.calculateCriticalPath(), coreNumber);
            }
        }
    }

    private void parallelScheduleSearch(Set<Node> freeNodes, Node startNode) {
        BnBSchedule optimalCandidate = new BnBSchedule();
//        freeNodes.forEach(s -> System.out.println(s.getName()));
        // Only run if there are available nodes to schedule
        if (freeNodes.size() > 0 && startFlag == true) {

            // Only schedule one of the equivalent nodes if there are equivalent nodes (Pruning stage 1)
            Set<Node> uniqueNodes = new HashSet<Node>();
            for (Node n : freeNodes) {
                if (startFlag) {
                    n = startNode;
                }
                if (!checkInterchangeableNode(n, uniqueNodes)) {
                    uniqueNodes.add(n);

                    //Check if processors are equivalent
                    Set<Processor> uniqueProcessors = new HashSet<Processor>();
                    for (Processor p : listOfProcessors) {
                        // Calculate earliest start time of Node on current Processor
                        int start = Math.max(p.getAvailableStartTime(), startTimeAfterParent(n, p));
                        if (!checkInterchangeableProcessor(p, uniqueProcessors, n, start)) {
                            uniqueProcessors.add(p);

                            // Check if the weight of the schedule is greater than current optimum schedule's weight. If
                            // yes, stop checking. Otherwise, continue.
                            if ((n.getBottomWeight() + start) <= optimalSchedule.getWeight()) {
                                Set<Node> newFreeNodes = n.schedule(p, start);
                                p.scheduleNode(n, start);
                                newFreeNodes.addAll(freeNodes);
                                newFreeNodes.remove(n);
                                optimalScheduleSearch(newFreeNodes);
                                unscheduleNode(n);
                            }

                        }

                    }

                }
            }


        } else { //If all free nodes scheduled
            int max = -1; //Initialise max
            startFlag = false;

            for (Processor process : listOfProcessors) {
                max = Math.max(max, process.getAvailableStartTime());
            }
            if (max < optimalSchedule.getWeight()) {
                optimalSchedule = new BnBSchedule(listOfProcessors);
//                optimalSchedule.printSchedule();
                for (PropertyChangeListener l : listeners) { // doesn't do anything
                    l.propertyChange(new PropertyChangeEvent(this, "update progress", "old", optimalSchedule.getWeight()));
                }
                MainController.getInstance().addOptimalToSearchGraph(optimalSchedule.calculateCriticalPath(), coreNumber);

            }
        }
    }

    public BnBSchedule parallelSchedule() {
        BnBSchedule output = new BnBSchedule();

        for (Node n : startingParallelNodes){
            parallelScheduleSearch(availableToSchedule, n);
            if (optimalSchedule.getWeight() < output.getWeight()) {
                output = optimalSchedule;
            };
        }

        return output;
    }

    @Override
    public BnBSchedule call() throws Exception {
        return parallelSchedule();
    }
}
