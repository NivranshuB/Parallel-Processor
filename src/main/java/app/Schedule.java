package app;

import java.util.List;

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
