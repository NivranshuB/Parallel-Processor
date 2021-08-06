package app;

import java.util.ArrayList;
import java.util.Collections;
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
public class Schedule implements Comparable<Schedule> {

    public List<Processor> processorList = new ArrayList<>();//Each processor consists of the ordered list of Tasks scheduled on it
    public ScheduleState state;
    public int finishTime;//If the schedule is not complete then this is an estimate
    public List<Node> unassignedTasks = new ArrayList<>();
    public HashMap<String, Node> nodeMap;//All the tasks of the graph
    public HashMap<String, Edge> edgeMap;//All the task dependencies of the graph


    /**
     * Returns 1 if finish time of the current Schedule is greater than the Schedule being compared to, -1 if greater
     * than the Schedule being compared to, and 0 if the finish times are equal.
     * @param comparee
     * @return
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
     */
    public Schedule(HashMap<String, Node> nMap, HashMap<String, Edge> eMap, int numberOfProcessors) {
        state = ScheduleState.PARTIAL;
        finishTime = 0;//Arbitrary placeholder value
        nodeMap = nMap;
        edgeMap = eMap;

        for (Node curr : nodeMap.values()) {//Initially all tasks are unassigned
            unassignedTasks.add(curr);
        }

        for (int i = 0; i < numberOfProcessors; i++) {//Create add new empty processors to procrocessorList
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
     * @return List of all child schedules (these have exactly one more task scheduled on them compared to their parent)
     */
    public List<Schedule> create_children(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {

        List<Schedule> childrenSchedule = new ArrayList<>();

        for (Node n : unassignedTasks) {

            //find all the task dependencies for a particular unassigned task Node
//            taskDependencies;
            List<Node>  taskDependencies = findDependencies(n);

            //For each task dependency check if the parent task has been fulfilled or not
            boolean dependenciesFulfilled = true;
            if (taskDependencies != null){
                for (Node parentTask : taskDependencies) {
                    if (unassignedTasks.contains(parentTask)) {//check if parent task has been fulfilled
                        dependenciesFulfilled = false;
                        break;
                    }
                }
            }


            //If all task dependencies have been fulfilled then we can schedule this task
            if (dependenciesFulfilled) {
                for (Processor p : processorList) {//Create new schedules by scheduling this task on all processors
                    childrenSchedule.add(create_child(p, n));//one at a time
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

        List<Processor> cProcessorList = new ArrayList<>();//The processor list for the new child schedule and the list of
        List<Node> cUnassignedTasks = new ArrayList<>();//unassigned tasks are originally the same as the parent schedule
        cUnassignedTasks.addAll(unassignedTasks);

        for (Processor p : processorList) {
            cProcessorList.add(new Processor(p.getTaskOrder(), p.finishTime));
        }

        //Find all the dependent tasks for the task we are about to schedule
        List<Node> dependentTasks = findDependencies(node);
        int earliestSTime = processor.finishTime;//This variable represents the earliest start time for this task in
                                                 //this processor

        //For loop that check if any of the dependent tasks were scheduled in a different processor to the current one
        if (dependentTasks != null){
            for (Node n : dependentTasks) {
                if (!processor.taskPresent(n)) {//if a dependent task was scheduled on a different processor...
                    for (Processor p : processorList) {
                        if (p.taskPresent(n)) {//Check what is the earliest time that we can schedule the current task by
                            int scheduleDelay = p.taskEndTime(n) + n.getWeight();//taking into account the communication cost
                            if (scheduleDelay > earliestSTime) {
                                earliestSTime = scheduleDelay;//Update the earliest start time of the task if required
                            }
                        }
                    }
                }
            }
        }


        int processorPos = processorList.indexOf(processor);//Update the right processor of the cProcessorList
        cProcessorList.get(processorPos).assignTask(node, earliestSTime - processor.finishTime);

        cUnassignedTasks.remove(node);//Remove the task we just scheduled from the child's unassigned tasks list
        ScheduleState cState = ScheduleState.PARTIAL;

        if (cUnassignedTasks.size() > 0) {//If the child does not have any unassigned tasks it will be labelled complete
            cState = ScheduleState.COMPLETE;
        }

        //return a new Schedule instancte (the child schedule)
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
     *This method given a task t finds all the tasks that t depends on before it can be executed using edgeMap.
     * @param task
     * @return List of all the parent tasks
     */
    public List<Node> findDependencies(Node task) {
        List<Node> dependentTasks = new ArrayList<>();

        for (Edge e : edgeMap.values()) {
            if (e.getChildNode() == task) {
                dependentTasks.add(e.getParentNode());
            }
        }
        return dependentTasks;
    }

    /**
     * Given a particular task, find if it has been fulfilled
     * @param task
     * @return
     */
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
        Collections.sort(sList);
        return sList;
    }



}
