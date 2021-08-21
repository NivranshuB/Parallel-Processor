package app;

import javafx.application.Platform;

import java.util.concurrent.ExecutionException;

public class MyThread extends Thread {

    public void run() {



        Config config = Config.getInstance();

//        Scheduler scheduler = Scheduler.getInstance();

        DotFileReader dotFileReader = Main.getDotFileReader();

//        BnBScheduler optimalScheduler = BnBScheduler.getInstance(dotFileReader, config);

//        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());

//        BnBSchedule optimalSchedule = optimalScheduler.getSchedule();

//        String graphName = dotFileReader.getGraphName();

        //Parses the optimal schedule to the output DOT file
//        OutputParser op = new OutputParser(graphName, config, optimalSchedule, optimalScheduler);

        //        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());
//		System.out.println("Here is optimal: \n" + optimalSchedule);
        MainController mainController = MainController.getInstance();

        BnBSchedule optimalSchedule = null;
        BnBScheduler optimalScheduler = null;

        if (config.getNumOfCores() > 1 && dotFileReader.getRootNodeList().size() > 1) {
            System.out.println("Using parallelisation");
            System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
            ParallelScheduler parallel = new ParallelScheduler(config, dotFileReader);
            mainController.setScheduler(parallel);
            mainController.addListener();

            try {
                optimalSchedule = parallel.checkBestSchedule();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (dotFileReader.getEdgeMap().size() == 0 && config.getNumOfProcessors() == 1) {
            SingleProcessorNoEdgesScheduler scheduler = new SingleProcessorNoEdgesScheduler(dotFileReader);
            optimalSchedule = scheduler.getSchedule();
            mainController.setScheduler(scheduler);
            mainController.addListener();
        } else {
            System.out.println("Using serial");
            System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
            optimalScheduler = new BnBScheduler(dotFileReader, config);
            mainController.setScheduler(optimalScheduler);
            mainController.addListener();
            optimalSchedule = optimalScheduler.getSchedule();
        }



        System.out.println(optimalSchedule);
        System.out.println("We reached here");
        optimalSchedule.printSchedule();

//        //optimalSchedule = scheduler.getOptimalSchedule(nodeMap, edgeMap, numberOfProcessors);
        String graphName = dotFileReader.getGraphName();
//        //Corban's code to parse the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule, optimalScheduler);

        op.writeFile();

//        mainController.createGantt(op);
        mainController.createGantt(op.getNodeList());

        if (!config.getVisualise()) {
            Platform.exit();
            System.exit(0);
        }

    }
}
