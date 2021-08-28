package app;

import model.Node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author: Team Untested (13)
 * Class that implements the parallel scheduler, allowing the application to utilise numerous threads in order to
 * speed up the search for the optimum schedule.
 */
public class ParallelScheduler extends Scheduler {

    private List<Node> rootNodes;
    private int coreCount;
    private DotFileReader dotFileReader;
    List<Future<BnBSchedule>> futureList = new ArrayList<>();
    ExecutorService executor;

    /**
     * Constructor for ParallelScheduler object. 
     * @param config reference to Config object.
     * @param dotFileReader reference to DotFileReader object.
     */
    public ParallelScheduler(Config config, DotFileReader dotFileReader) {
        MainController mainController = MainController.getInstance();
        this.dotFileReader = dotFileReader;
        coreCount = config.getNumOfCores();
        if (config.getVisualise()) {
            MainController.getInstance().instantiateOptimalNodes(coreCount);
        }
        rootNodes = dotFileReader.getRootNodeList();
        int numOfRoot = rootNodes.size();
        executor = Executors.newFixedThreadPool(numberOfThreads(numOfRoot));

        if (numOfRoot >= coreCount) {
            moreRootNodesThanCores(mainController, config, executor, numOfRoot);
        } else {
            lessRootNodesThanCores(mainController, config, executor);
        }
    }

    /**
     * Method that allocates tasks to the ExecutorService when there are less root nodes than cores.
     * @param mainController Refers to the controller for visualisation
     * @param config Config object to obtain information such as current file
     * @param executor ExecutorService object to execute parallel tasks
     */
    private void lessRootNodesThanCores(MainController mainController, Config config, ExecutorService executor) {

        List<List<String>> scheduleList = createParallelisationFreeNodeList();
        int coreCounter = 0;

        for (List<String> startNodes : scheduleList) {
            DotFileReader currentFileReader = new DotFileReader(config.getInputFile());
            BnBScheduler currScheduler = new BnBScheduler(currentFileReader, config, startNodes, coreCounter);

            futureList.add(executor.submit(currScheduler));
            if (config.getVisualise()) {
                mainController.setScheduler(currScheduler);
                mainController.addListener();
            }
            coreCounter++;
            if (coreCounter == coreCount) {
                break;
            }
        }
    }

    /**
     * Method that allocates tasks to the ExecutorService when there are more root nodes than cores
     * @param mainController Refers to the controller for visualisation
     * @param config Config object to obtain information such as current file
     * @param executor ExecutorService object to execute parallel tasks
     * @param numOfRoot Number of root nodes
     */
    private void moreRootNodesThanCores(MainController mainController, Config config, ExecutorService executor, int numOfRoot) {
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
     * @return Number of threads that should be created.
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

    /**
     * Method that creates a list of lists of Strings representing the nodes to be explored by the different threads.
     * @return List of List of Strings of nodes to be fed to scheduler objects.
     */
    private List<List<String>> createParallelisationFreeNodeList() {

        List<List<Node>> tree = new ArrayList<List<Node>>();
        int scheduleCount = 0;
        int looper = 0;

        if (rootNodes.size() == 1) {

            List<Node> currentList = new ArrayList<Node>();
            currentList.add(rootNodes.get(0));
            if (getChildSchedules(currentList, false, false) != null) {
                tree.addAll(getChildSchedules(currentList, false, false));
                scheduleCount = tree.size();
            }

            while (scheduleCount < coreCount && looper < 5) {
                List<List<Node>> newTree = new ArrayList<List<Node>>();
                for (List<Node> curr : tree) {
                    if (getChildSchedules(curr, false, false) != null) {
                        newTree.addAll(getChildSchedules(curr, false, false));
                        scheduleCount = tree.size() + newTree.size();
                    }
                    if (scheduleCount >= coreCount) { break;}
                }
                tree.addAll(newTree);
                looper++;
            }

        } else {

            for (Node n : rootNodes) {
            List<Node> currentList = new ArrayList<Node>();
            currentList.add(n);
            tree.add(currentList);
            }

            for (Node n : rootNodes) {
                if (scheduleCount < coreCount) {
                    List<Node> currentList = new ArrayList<Node>();

                    if (getChildSchedules(currentList, true, true) != null) {
                        tree.addAll(getChildSchedules(currentList, true, true));
                        scheduleCount = tree.size();
                    }

                } else {
                    List<Node> currentList = new ArrayList<Node>();
                    if (!currentList.isEmpty()) {
                        tree.add(currentList);
                    }
                }
            }
        }

        return convertNodeToString(tree);

    }

    /**
     * Helper function to convert list of list of Nodes into list of list of String.
     * @param input List of List of Nodes to be converted
     * @return Returns List of List of Strings representing the List of List of Nodes by name
     */
    private List<List<String>> convertNodeToString(List<List<Node>> input) {
        List<List<String>> outputString = new ArrayList<>();

        for (List<Node> nodeList : input){
            List<String> strList = new ArrayList<String>();
            for (Node n : nodeList) {
                strList.add(n.getName());
            }
            outputString.add(strList);
        }
        outputString.sort(new Comparator<List>() {
            @Override
            public int compare(List o1, List o2) {
                return o1.size() - o2.size();
            }
        });
        return outputString;
    }

    /**
     * Outputs the schedules obtained after either adding child nodes to the specified schedule, or adding other root
     * nodes (if in top level)
     * @param input List of nodes representing current partial schedule
     * @param topLevel Specifies if current input is referring to the top level
     * @param includeChildren Specifies if the user would like to include children in the schedule.
     * @return
     */
    private List<List<Node>> getChildSchedules(List<Node> input, boolean topLevel, boolean includeChildren) {
        List<List<Node>> output = new ArrayList<List<Node>>();

        if (topLevel) {
            for (Node n : rootNodes) {
                List<Node> temp = new ArrayList<Node>();
                temp.add(rootNodes.get(0));
                temp.add(n);
                output.add(temp);
            }

            if (includeChildren) {
                for (Node n : rootNodes.get(0).getChild()) {
                    if (n.getParent().size() == 1) {
                        List<Node> temp = new ArrayList<Node>();
                        temp.add(rootNodes.get(0));
                        temp.add(n);
                        output.add(temp);
                    }
                }
            }
        } else {
            Node currentNode = input.get(input.size()-1);
            for (Node n : currentNode.getChild()) {
                if (n.getParent().size() == 1) {
                    List<Node> temp = new ArrayList<Node>();
                    temp.addAll(input);
                    temp.add(n);
                    output.add(temp);
                } else {
                    boolean flag = true;
                    List<String> childList = new ArrayList<String>();
                    List<String> parentList = new ArrayList<String>();
                    n.getParent().forEach(temp -> childList.add(temp.getName()));
                    input.forEach(temp -> parentList.add(temp.getName()));
                    for (String str : childList) {
                        if (!parentList.contains(str)){
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        List<Node> temp = new ArrayList<Node>();
                        temp.addAll(input);
                        temp.add(n);
                        output.add(temp);
                    }
                }
            }
        }
        return (output.size() > 1) ? output : null;
    }
}
