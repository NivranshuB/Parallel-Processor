package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Node;
import model.Edge;

/**
 * Author: Team UNTESTED
 * Singleton class that is responsible for the creation of all the possible schedule
 * combinations for completing all tasks. This class stores the representation of
 * the input task graph as two HashMaps, one representing the different tasks and
 * their weights while the other representing the different task dependencies of the
 * task graph and their weights.
 */

public class Scheduler {

    private static final int MAX_VALUE = 2147483647;

    // static variable single_instance of type Singleton
    private static Scheduler single_instance = null;
  
    public List<Schedule> openSchedules;
    public int optimalTime;
    public Schedule optimalSchedule;
  
    // private constructor restricted to this class itself
    private Scheduler() {
        openSchedules = null;
        optimalTime = MAX_VALUE;
        optimalSchedule = null;
    }
  
    // static method to create instance of Singleton class
    public static Scheduler getInstance() {
    
        if (single_instance == null)
            single_instance = new Scheduler();
  
        return single_instance;
    }

    /**
     *This method is called from the main class and is responsible for finding the optimal schedule.
     */
    public Schedule getOptimalSchedule(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap, int numberOfProcessors) {


        Schedule emptySchedule = new Schedule(nodeMap, edgeMap, numberOfProcessors);

        //initially just the empty schedule in the list
        openSchedules.add(emptySchedule);

        while (openSchedules.size() > 0) {

            //pop off the first schedule which has the lowest finish time estimate
            List<Schedule> newSchedules = openSchedules.remove(0).create_children(nodeMap, edgeMap);
            //get the list of all children schedules created by adding one task to this schedule

            for (Schedule s : newSchedules) {
                //if schedule is complete and has a better finish time than the current optimal schedule
                if (s.state == Schedule.ScheduleState.COMPLETE && s.getFinishTime() < optimalTime) {
                    optimalSchedule = s;
                    optimalTime = s.getFinishTime();
                    newSchedules.remove(s);
                } else if (s.getFinishTime() > optimalTime) {//if schedule has a worse time than the current optimal time
                    newSchedules.remove(s);                  //dump that schedule
                }
            }

            openSchedules = merge(openSchedules, newSchedules);
        }

        return optimalSchedule;
    }

    /**
     * Method that merges two schedule lists, that are sorted, by their finish time. To be implemented...
     */
    private List<Schedule> merge(List<Schedule> x, List<Schedule> y) {
        int countX = 0;
        int countY = 0;
        int maxX = x.size();
        int maxY = y.size();
        List<Schedule> mergedList = new ArrayList<Schedule>();

        while ((countX < maxX) && (countY < maxY)) {
            if ((x.get(countX).finishTime <= y.get(countY).finishTime)) {
                mergedList.add(x.get(countX));
                countX++;
            } else {
                mergedList.add(y.get(countY));
                countY++;
            }
        }

        if (countX == (x.size() - 1)) {
            mergedList.addAll(x.subList(countX, x.size()-1));
        } else {
            mergedList.addAll(y.subList(countY, y.size()-1));
        }
        return mergedList;
    }

}
