package app;

import model.Node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author: Team Untested
 * Class that implements the parallel scheduler, allowing the application to utilise numerous threads in order to
 * speed up the search for the optimum schedule.
 */
public class ParallelScheduler extends Scheduler {

    private List<Node> rootNodes;
    private int coreCount;
    List<Future<BnBSchedule>> futureList = new ArrayList<>();
    ExecutorService executor;

    /**
     * Constructor for ParallelScheduler object.
     * @param config reference to Config object.
     * @param dotFileReader reference to DotFileReader object.
     */
    public ParallelScheduler(Config config, DotFileReader dotFileReader) {

        MainController mainController = MainController.getInstance();
        coreCount = config.getNumOfCores();
        if (MainController.getInstance() != null) {
            MainController.getInstance().instantiateOptimalNodes(coreCount);
        }
        rootNodes = dotFileReader.getRootNodeList();
        int numOfRoot = rootNodes.size();
        executor = Executors.newFixedThreadPool(numberOfThreads(numOfRoot));



        int coreCounter = 0;

        for (int i = 0; i < numOfRoot; i++) {

            if ((i == numOfRoot - 1) || (i == coreCount - 1))  {
                List<String> temp = new ArrayList<String>();
                rootNodes.forEach(s -> temp.add(s.getName()));
                DotFileReader currentFileReader = new DotFileReader(config.getInputFile());
                BnBScheduler currScheduler = new BnBScheduler(currentFileReader, config, temp, coreCounter);
                futureList.add(executor.submit(currScheduler));
                if (config.getVisualise()) {
                    mainController.setScheduler(currScheduler);
                    mainController.addListener();
                }

                break;
            } else {
                List<String> temp = new ArrayList<String>();
                temp.add(rootNodes.get(0).getName());
                rootNodes.remove(0);
                DotFileReader currentFileReader = new DotFileReader(config.getInputFile());
                BnBScheduler currScheduler = new BnBScheduler(currentFileReader, config, temp, coreCounter);
                futureList.add(executor.submit(currScheduler));
                if (config.getVisualise()) {
                    mainController.setScheduler(currScheduler);
                    mainController.addListener();
                }
            }

            if (coreCounter > coreCount) {
                coreCount = 0;
            }

            coreCounter++;
        }
    }

    /**
     * Method that returns the number of threads that should be created
     * @param numOfRoot Number of root nodes
     * @return
     */
    private int numberOfThreads(int numOfRoot) {
        return Math.min(coreCount, numOfRoot);
    }

    /**
     * Method that finds the thread with the schedule with the lowest total weight.
     * @return BnBSchedule with the lowest total weight
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public BnBSchedule checkBestSchedule() throws InterruptedException, ExecutionException {
        int max = Integer.MAX_VALUE;
        BnBSchedule lowestTime = null;

        for (Future<BnBSchedule> currSchedule : futureList) {
            BnBSchedule current = currSchedule.get();
            if (current.getWeight() < max) {
                max = current.getWeight();
                lowestTime = current;
            }
        }
        executor.shutdown();
        for (PropertyChangeListener l : listeners) {
            l.propertyChange(new PropertyChangeEvent(this, "optimal schedule", "old", lowestTime.getWeight()));
        }
        return lowestTime;
    }




}
