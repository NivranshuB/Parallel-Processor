package app;

import model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelSchedule {

    private List<Node> rootNodes;
    private int coreCount;
    List<Future<BnBSchedule>> futureList = new ArrayList<>();
    ExecutorService executor;

    public ParallelSchedule(Config config, DotFileReader dotFileReader) {

        coreCount = config.getNumOfCores();
        rootNodes = dotFileReader.getRootNodeList();
        int numOfRoot = rootNodes.size();
        executor = Executors.newFixedThreadPool(numberOfThreads(numOfRoot));

        System.out.println("Create");
        System.out.println("Number of roots: " + rootNodes.size());

        for (int i = 0; i < numOfRoot; i++) {

            if ((i == numOfRoot - 1) || (i == coreCount - 1))  {
                List<String> temp = new ArrayList<String>();
                rootNodes.forEach(s -> temp.add(s.getName()));
                DotFileReader currentFileReader = new DotFileReader(config.getInputFile());
                System.out.println("Size of rootNodes is: " + rootNodes.size());
                System.out.println("Root node list: " + temp.toString());
                System.out.println("Number of threads: " + (i+1));
                BnBScheduler currScheduler = new BnBScheduler(currentFileReader, config, temp);
                futureList.add(executor.submit(currScheduler));

                break;
            } else {
                List<String> temp = new ArrayList<String>();
                temp.add(rootNodes.get(0).getName());
                rootNodes.remove(0);
                DotFileReader currentFileReader = new DotFileReader(config.getInputFile());
                System.out.println("Does this run? Size of rootNodes is " + rootNodes.size());
                System.out.println("Root node list: " + temp.toString());
                BnBScheduler currScheduler = new BnBScheduler(currentFileReader, config, temp);
                futureList.add(executor.submit(currScheduler));

            }

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
            System.out.println("Schedule:");
            System.out.println(current.getStringStorage());
            if (current.getWeight() < max) {
                max = current.getWeight();
                System.out.println("Current max weight: " + max);
                lowestTime = current;
            }
        }
        executor.shutdown();
        return lowestTime;
    }




}
