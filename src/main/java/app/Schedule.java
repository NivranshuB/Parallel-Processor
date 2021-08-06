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

    enum ScheduleState {
        PARTIAL,
        COMPLETE,
        OPTIMAL,
        DUPLICATE;
    }

    /**
     * Constructor creates an empty schedule -> This will be used for the first schedule
     */
    public Schedule(HashMap<String, Node> nodeMap, int numberOfProcessors) {
        state = ScheduleState.PARTIAL;
        finishTime = 0;

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
    public Schedule() {

        //need to underestimate the finish time of the new child schedule
        finishTime = estimateFinishTime();
    }

    /**
     * This method is supposed to create all possible partial schedule that can result by adding another task
     * to this schedule. To be implemented...
     * @param nodeMap
     * @param edgeMap
     * @return
     */
    public List<Schedule> create_children(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {

        List<Schedule> childrenSchedule;

        for (Node n : unassignedTasks) {
            //check if the all edge dependencies of the task have been fulfilled

            //if they do not have any edge dependencies that need to be fulfilled
            for (Processor p : processorList) {


            }
        }

        return null;
    }

    /**
     * Create a new schedule with the task scheduled as early as possible on processor p [need to account for
     * any edge costs if this is on a different processor to its prior task. To be implemented...
     */
    public Schedule create_child(Processor processor, Task task) {
        return null;
    }

    /**
     * This is the heuristic function that calculates the underestimate finish time of the current schedule.
     * @return int Underestimate of the schedule's finish time.
     */
    public int estimateFinishTime() {
        //Calculate the computation time for all unassigned nodes placed on one processor
        int unassignedTasksComputationTime = 0;

        for (Node n: unassignedTasks) {
            //Get the weight of the unassigned task nodes and add this to the computation time
            unassignedTasksComputationTime += n.getWeight();
        }

        //Calculates the estimated finish time for each processor
        int[] processorFinishTimes = this.calculateProcessorEstimatedFinishTime(unassignedTasksComputationTime);

        //Calculates the maximum of all finish times, including the current schedule finish time
        int finishTime = this.getMaxEstimateFinishTimes(processorFinishTimes);

        return finishTime;
    }

    /**
     * Takes the processor list and adds the computation time for all the unassigned task nodes to each processor's
     * finish time as an estimated finish time for that processor.
     * @param unassignedTasksComputationTime Computation time for all unassigned task nodes.
     * @return int[] Estimated finish times for each processor.
     */
    private int[] calculateProcessorEstimatedFinishTime(int unassignedTasksComputationTime) {
        int[] processorFinishTimes = new int[processorList.size()];
        int processorCount = 0;

        //For each processor, add the computation time for all the unassigned task nodes to the processor's current finish time
        for (Processor p: processorList) {
            int estimatedFinishTime = p.getFinishTime() + unassignedTasksComputationTime;

            processorFinishTimes[processorCount] = estimatedFinishTime;
            processorCount++;
        }

        return processorFinishTimes;
    }

    /**
     * Calculates the maximum of all estimated finishing times provided, taking into account the current finish time
     * for this schedule.
     * @param processorFinishTimes Finish times for each processor.
     * @return int Maximum of estimated finishing times.
     */
    private int getMaxEstimateFinishTimes(int[] processorFinishTimes) {
        //Set the max finish time to be the current finish time for the schedule
        int maxFinishTime = finishTime;

        for (int i = 0; i < processorFinishTimes.length; i++) {
            if (processorFinishTimes[i] > maxFinishTime) {
                maxFinishTime = processorFinishTimes[i];
            }
        }

        return maxFinishTime;
    }

    /**
     * Getter method for finishTime
     * @return
     */
    public int getFinishTime() {
        return finishTime;
    }
}
