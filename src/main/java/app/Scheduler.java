package app;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    //Static variable single_instance of type Singleton
    private static Scheduler single_instance = null;
  
    public List<Schedule> openSchedules;
    public int optimalTime;
    public Schedule optimalSchedule;

    List<PropertyChangeListener> listeners = new ArrayList<>();
  
    /**
     * Private constructor restricted to the class itself ensures the
     * class is not instantiated more than once.
     */
    private Scheduler() {
        openSchedules = new ArrayList<>();
        optimalTime = MAX_VALUE;
        optimalSchedule = null;
    }

    /**
     * Static method to create/return a Singleton instance of Scheduler class.
     * @return Singleton Scheduler instance.
     */
    public static Scheduler getInstance() {
    
        if (single_instance == null)
            single_instance = new Scheduler();
  
        return single_instance;
    }

    /**
     * This method is called from the main class and is responsible for finding the optimal schedule.
     * @param nodeMap Mapping of nodes in the task graph.
     * @param edgeMap Mapping of edges in the task graph.
     * @param numberOfProcessors Number of processors available for scheduling.
     * @return Optimal schedule for given task graph.
     */
    public Schedule getOptimalSchedule(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap, int numberOfProcessors) {


        Schedule emptySchedule = new Schedule(nodeMap, edgeMap, numberOfProcessors);

        //Initially just the empty schedule in the list
        openSchedules.add(emptySchedule);

        int iterationCounter = 0;

        while (openSchedules.size() > 0) {
            //Pop off the first schedule which has the lowest finish time estimate
            List<Schedule> newSchedules = openSchedules.remove(0).create_children(nodeMap, edgeMap);
            //Get the list of all children schedules created by adding one task to this schedule

            Iterator<Schedule> iterator = newSchedules.listIterator();

            while (iterator.hasNext()) {
                Schedule s = iterator.next();

                //If schedule is complete and has a better finish time than the current optimal schedule
                if (s.state == Schedule.ScheduleState.COMPLETE && s.getFinishTime() < optimalTime) {
                    optimalSchedule = s;
                    optimalTime = s.getFinishTime();
                    iterator.remove();
                } else if (s.getFinishTime() > optimalTime) {//If schedule has a worse time than the current optimal time
                    iterator.remove();                  //Remove that schedule
                }
            }

            if (openSchedules.size() < 1) {
                openSchedules = newSchedules;
            } else if (newSchedules.size() > 1) {
                openSchedules = merge(openSchedules, newSchedules);
            }

            iterationCounter++;
        }

        //temp
        for (PropertyChangeListener l : listeners) {
            l.propertyChange(new PropertyChangeEvent(this, "schedule", "old", "new"));
//            System.out.println("does this bit work?");
        }

        return optimalSchedule;
    }

    public void addChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Method that merges two schedule lists, that are sorted, by their finish time.
     * @param x First sorted schedule.
     * @param y Second sorted schedule.
     * @return Merged list of the two sorted schedules.
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
        if (x.size() > 0){
            if (countX == x.size()) {
                mergedList.addAll(y.subList(countY, y.size()-1));
            } else {
                mergedList.addAll(x.subList(countX, x.size()-1));
            }
        }

        return mergedList;
    }

}
