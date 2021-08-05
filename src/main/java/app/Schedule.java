package app;

import java.util.List;

/**
 * Author: Team UNTESTED
 * This class represents an instance of a schedule, both complete and partial.
 * This class is used by the Scheduler class and is responsible for understanding
 * its state and creating all possible schedules that can be created from this
 * schedule by adding one more task. This class depends on the Scheduler class
 * to the get the HashMap representation of the input graph.
 */
public class Schedule {

    public List<Processor> processor_list;
    public ScheduleState state;
    public int schedule_finish_time;
    public List<Task> unassigned_tasks;

    enum ScheduleState {
        PARTIAL,
        COMPLETE,
        OPTIMAL,
        DUPLICATE;
    }

    public List<Schedule> create_children() {
        return null;
    }

    public Schedule create_child(Processor processor, Task task) {
        return null;
    }

    public int calculateCost() {
        return -1;
    }
}
