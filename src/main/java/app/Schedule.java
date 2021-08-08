package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import model.EmptyNode;
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
public class Schedule implements Comparable<Schedule> {

    public List<Processor> processorList = new ArrayList<>();//Each processor consists of the ordered list of Tasks scheduled on it
    public ScheduleState state;
    public int finishTime;//If the schedule is not complete then this is an estimate
    public List<Node> unassignedTasks = new ArrayList<>();
    public HashMap<String, Node> nodeMap;//All the tasks of the graph
    public HashMap<String, Edge> edgeMap;//All the task dependencies of the graph


    /**
     * Compares the finish time of this schedule and the schedule provided.
     * @param comparee Other schedule to compare this schedule to.
     * @return Returns 1 if finish time of the current Schedule is greater than the Schedule being compared to,
     * -1 if greater than the Schedule being compared to, and 0 if the finish times are equal.
     */
    @Override
    public int compareTo(Schedule comparee) {
        if (this.finishTime < comparee.finishTime) {
            return 1;
        } else if (this.finishTime > comparee.finishTime) {
            return -1;
        } else {
            return 0;
        }
    }


    enum ScheduleState {
        PARTIAL,
        COMPLETE,
        OPTIMAL,
        DUPLICATE;
    }

    /**
     * Constructor creates an empty schedule -> This will be used for the first schedule
     * @param nMap Mapping of nodes in the task graph.
     * @param eMap Mapping of edges in the task graph.
     * @param numberOfProcessors Number of processors to schedule tasks on.
     */
    public Schedule(HashMap<String, Node> nMap, HashMap<String, Edge> eMap, int numberOfProcessors) {
        state = ScheduleState.PARTIAL;
        nodeMap = nMap;
        edgeMap = eMap;

        for (Node curr : nodeMap.values()) {//Initially all tasks are unassigned
            unassignedTasks.add(curr);
        }

        for (int i = 0; i < numberOfProcessors; i++) {//Create add new empty processors to processorList
            processorList.add(new Processor());
        }

        finishTime = this.estimateFinishTime();
    }

    /**
     * Need to decide how subsequent child schedules will be created. These child schedules need to be
     * duplicated from the parent schedule except they will have one extra task allocated to one of their
     * processors.
     * @param pList List of processors with scheduled tasks.
     * @param st Schedule status.
     * @param uTasks List of unassigned tasks for the task graph.
     * @param nMap Mapping of nodes in the task graph.
     * @param eMap Mapping of edges in the task graph.
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
     * Creates all possible partial schedules that can result by adding another task to this schedule.
     * @param nodeMap Mapping of nodes in the task graph.
     * @param edgeMap Mapping of edges in the task graph.
     * @return List of all child schedules (these have exactly one more task scheduled on them compared to their parent)
     */
    public List<Schedule> create_children(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {

        List<Schedule> childrenSchedule = new ArrayList<>();

        for (Node n : unassignedTasks) {

            //Find all the task dependencies for a particular unassigned task Node
            List<Node>  taskDependencies = findDependencies(n);

            //For each task dependency check if the parent task has been fulfilled or not
            boolean dependenciesFulfilled = true;
            if (taskDependencies != null){
                for (Node parentTask : taskDependencies) {
                    for (Node ut : unassignedTasks) {
                        if (ut.getName().equals(parentTask.getName())) {//Check if parent task has been fulfilled
                            dependenciesFulfilled = false;
                            break;
                        }
                    }
                }
            }


            //If all task dependencies have been fulfilled then we can schedule this task
            if (dependenciesFulfilled) {
                for (Processor p : processorList) {//Create new schedules by scheduling this task on all processors
                    childrenSchedule.add(create_child(p, n));//One at a time
                }
            }
        }

        return sort(childrenSchedule);
    }

    /**
     * Creates a new schedule with the task scheduled as early as possible on processor p [need to account for
     * any edge costs if this is on a different processor to its prior task.
     * @param processor Processor to schedule task on.
     * @param node Task node to schedule.
     * @return Schedule with task node scheduled.
     */
    public Schedule create_child(Processor processor, Node node) {

        List<Processor> cProcessorList = new ArrayList<>();//The processor list for the new child schedule
        List<Node> cUnassignedTasks = new ArrayList<>();//List of unassigned tasks for the new child schedule

        //Unassigned tasks list is the same as the parent schedule, minus the current task
        for (Node task : unassignedTasks) {
            if (task.getName() != node.getName()) {
                cUnassignedTasks.add(task.duplicateNode());
            }
        }

        for (Processor p : processorList) {
            cProcessorList.add(new Processor(p.getDuplicateTaskOrder(), p.finishTime));
        }

        //Find all the dependent tasks for the task we are about to schedule
        List<Node> dependentTasks = findDependencies(node);
        int earliestSTime = processor.finishTime;//Variable represents earliest start time for this task in this processor
        int commCost = 0;

        //For loop that checks if any of the dependent tasks were scheduled in a different processor to the current one
        if (dependentTasks != null){
            for (Node n : dependentTasks) {
                if (!processor.taskPresent(n.getName())) {//If a dependent task was scheduled on a different processor...
                    for (Processor p : processorList) {
                        if (p.taskPresent(n.getName())) {//Check what is the earliest time that we can schedule the current task by

                            for (Edge e : edgeMap.values()) {
                                if (e.getParentNode().getName().equals(n.getName()) && e.getChildNode().getName().equals(node.getName())) {
                                    if (e.getWeight() + p.getFinishTime() > commCost) {

                                        commCost = e.getWeight() + p.getFinishTime();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Node childDuplicateExtraTask = node.duplicateNode();

        int processorPos = processorList.indexOf(processor);//Update the right processor of the cProcessorList

        cProcessorList.get(processorPos).assignTask(childDuplicateExtraTask, commCost);

        ScheduleState cState = ScheduleState.PARTIAL;

        if (cUnassignedTasks.size() < 1) {//If the child does not have any unassigned tasks it will be labelled COMPLETE
            cState = ScheduleState.COMPLETE;
        }

        //Return a new Schedule instance (the child schedule)
        Schedule cSchedule = new Schedule(cProcessorList, cState, cUnassignedTasks, nodeMap, edgeMap);
        return cSchedule;
    }

    /**
     * This is the heuristic function that calculates the underestimate finish time of the current schedule.
     * @return Underestimate of the schedule's finish time.
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
     * @return Estimated finish times for each processor.
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
     * @return Maximum of estimated finishing times.
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
     * Gets the finish time of this schedule.
     * @return Finish time of this schedule.
     */
    public int getFinishTime() {
        return finishTime;
    }

    /**
     * This method, given a task t, finds all the tasks that t depends on before it can be executed using edgeMap.
     * @param task Task node to find dependencies on.
     * @return List of all the parent tasks.
     */
    public List<Node> findDependencies(Node task) {
        List<Node> dependentTasks = new ArrayList<>();

        for (Edge e : edgeMap.values()) {
            if (e.getChildNode().getName().equals(task.getName())) {
                dependentTasks.add(e.getParentNode());
            }
        }
        return dependentTasks;
    }

    /**
     * Given a particular task, find if it has been fulfilled.
     * @param task Task node to check if it has been fulfilled.
     * @return True if task has been fulfilled, False otherwise.
     */
    public boolean taskFulfilled(Node task) {
        boolean taskFulfilled = false;

        for (Processor p : processorList) {
            if (p.taskPresent(task.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sorts the schedule list.
     * @param sList List of schedules to sort.
     * @return Sorted list of schedules.
     */
    private List<Schedule> sort(List<Schedule> sList) {
        Collections.sort(sList);
        return sList;
    }

    /**
     * Outputs this schedule as a string.
     * @return This schedule in string format.
     */
    public String toString() {
        String scheduleString = "";
        for (Processor p : processorList) {
            scheduleString = scheduleString + "Processor " + processorList.indexOf(p);
            scheduleString = (scheduleString + "----------------------------\n");
            List<Node> taskOrder = p.getTaskOrder();
            int currTime = 0;
            for (Node task : taskOrder) {

                if (!(task instanceof EmptyNode)) {
                    scheduleString = (scheduleString + "Task " + task.getName() + ":" + " Weight=" + task.getWeight() + " Start time=" + currTime + '\n');
                } else {
                    System.out.println("Task gap of " + task.getWeight());
                }
                currTime += task.getWeight();
            }

        }
        return scheduleString;
    }

}
