package app;

import java.util.List;

/**
 * Author: Team UNTESTED
 * Singleton class that is responsible for the creation of all the possible schedule
 * combinations for completing all tasks. This class stores the representation of
 * the input task graph as two HashMaps, one representing the different tasks and
 * their weights while the other representing the different task dependencies of the
 * task graph and their weights.
 */

public class Scheduler {
    // static variable single_instance of type Singleton
    private static Scheduler single_instance = null;
  
    public List<Schedule> open_schedule_list;
    public int optimal_time;
    public Schedule optimal_schedule;
  
    // private constructor restricted to this class itself
    private Scheduler() {
    
     
    }
  
    // static method to create instance of Singleton class
    public static Scheduler getInstance() {
    
        if (single_instance == null)
            single_instance = new Scheduler();
  
        return single_instance;
    }

    public Schedule getOptimalSchedule() {
        return null;
    }
}
