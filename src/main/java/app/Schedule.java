package app;

import java.util.HashMap;
import java.util.List;
import model.Node;
import model.Edge;

/**
 * Author: Team UNTESTED
 * This class represents an instance of a schedule, both complete and partial.
 * This class is used by the Scheduler class and is responsible for understanding
 * its state and creating all possible schedules that can be created from this
 * schedule by adding one more task. This class depends on the Scheduler class
 * to the get the HashMap representation of the input graph.
 */
public class Schedule {

    public List<Processor> processorList;
    public ScheduleState state;
    public int finishTime;
    public List<Node> unassignedTasks;
    public HashMap<String, Node> nodeMap;
    public HashMap<String, Edge> edgeMap;

    enum ScheduleState {
        PARTIAL,
        COMPLETE,
        OPTIMAL,
        DUPLICATE;
    }

    /**
     * Constructor creates an empty schedule -> This will be used for the first schedule
     */
    public Schedule(HashMap<String, Node> nMap, HashMap<String, Edge> eMap, int numberOfProcessors) {
        state = ScheduleState.PARTIAL;
        finishTime = 0;
        nodeMap = nMap;
        edgeMap = eMap;

        for (Node curr : nodeMap.values()) {
            unassignedTasks.add(curr);
        }

        for (int i = 0; i < numberOfProcessors; i++) {
            processorList.add(new Processor());
        }
    }

    /**
     * Need to decide how subsequent child schedules will be created. These child schedules need to be
     * duplicated from the parent schedule except they will have one extra task allocated to one of their
     * processors. To be implemented...
     */
    public Schedule(List<Processor> pList, ScheduleState st, List<Node> uTasks, HashMap<String, Node> nMap,
                    HashMap<String, Edge> eMap) {

        processorList = pList;
        state = st;
        unassignedTasks = uTasks;
        nodeMap = nMap;
        edgeMap = eMap;
        finishTime = this.estimateFinishTime();
    }

    /**
     * This method is supposed to create all possible partial schedule that can result by adding another task
     * to this schedule. To be implemented...
     * @param nodeMap
     * @param edgeMap
     * @return
     */
    public List<Schedule> create_children(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {

        List<Schedule> childrenSchedule = null;

        for (Node n : unassignedTasks) {
            //check if the all edge dependencies of the task have been fulfilled
            List<Node> taskDependencies = findDependencies(n);

            boolean noDependencies = true;

            for (Node parentTask : taskDependencies) {
                if (!taskFulfilled(parentTask)) {
                    noDependencies = false;
                    break;
                }
            }

            if (noDependencies) {
                for (Processor p : processorList) {
                    childrenSchedule.add(create_child(p, n));
                }
            }
        }

        return sort(childrenSchedule);
    }

    /**
     * Create a new schedule with the task scheduled as early as possible on processor p [need to account for
     * any edge costs if this is on a different processor to its prior task. To be implemented...
     */
    public Schedule create_child(Processor processor, Node node) {

        List<Processor> cProcessorList = null;
        List<Node> cUnassignedTasks = null;
        cUnassignedTasks.addAll(unassignedTasks);

        for (Processor p : processorList) {
            cProcessorList.add(new Processor(p.getTaskOrder(), p.finishTime));
        }

        List<Node> dependentTasks = findDependencies(node);
        int earliestSTime = processor.finishTime;

        for (Node n : dependentTasks) {
            if (!processor.taskPresent(n)) {
                for (Processor p : processorList) {
                    if (p.taskPresent(n)) {
                        int scheduleDelay = p.taskEndTime(n) + n.getWeight();
                        if (scheduleDelay > earliestSTime) {
                            earliestSTime = scheduleDelay;
                        }
                    }
                }
            }
        }

        int processorPos = processorList.indexOf(processor);
        cProcessorList.get(processorPos).assignTask(node, earliestSTime - processor.finishTime);

        cUnassignedTasks.remove(node);
        ScheduleState cState = ScheduleState.PARTIAL;

        if (cUnassignedTasks.size() > 0) {
            cState = ScheduleState.COMPLETE;
        }

        return new Schedule(cProcessorList, cState, cUnassignedTasks, nodeMap, edgeMap);
    }

    /**
     * This is the heuristic function that calculates the underestimate finish time of the current schedule.
     * To be implemented...
     * @return
     */
    public int estimateFinishTime() {
        return -1;
    }

    /**
     * Getter method for finishTime
     * @return
     */
    public int getFinishTime() {
        return finishTime;
    }

    /**
     *
     */
    public List<Node> findDependencies(Node task) {
        List<Node> dependentTasks = null;

        for (Edge e : edgeMap.values()) {
            if (e.getChildNode() == task) {
                dependentTasks.add(e.getParentNode());
            }
        }
        return dependentTasks;
    }

    public boolean taskFulfilled(Node task) {
        boolean taskFulfilled = false;

        for (Processor p : processorList) {
            if (p.taskPresent(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to sort the schedule list. To be implemented...
     * @param sList
     * @return
     */
    private List<Schedule> sort(List<Schedule> sList) {
        return sList;
    }

}
