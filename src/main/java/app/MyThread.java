package app;

import javafx.application.Platform;

import java.util.concurrent.ExecutionException;

public class MyThread extends Thread {

    public void run() {



        Config config = Config.getInstance();


        DotFileReader dotFileReader = Main.getDotFileReader();

        MainController mainController = MainController.getInstance();

        BnBSchedule optimalSchedule = null;
        BnBScheduler optimalScheduler = null;
//        Scheduler optimalScheduler = null;

        if (config.getNumOfCores() > 1 && dotFileReader.getRootNodeList().size() > 1) {
            System.out.println("Using parallelisation");
            System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
            ParallelScheduler parallel = new ParallelScheduler(config, dotFileReader);
            if (config.getVisualise()) {
                mainController.setScheduler(parallel);
                mainController.addListener();
            }

            try {
                optimalSchedule = parallel.checkBestSchedule();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (dotFileReader.getEdgeMap().size() == 0 && config.getNumOfProcessors() == 1) {
            SingleProcessorNoEdgesScheduler scheduler = new SingleProcessorNoEdgesScheduler(dotFileReader);
            if (config.getVisualise()) {
                mainController.setScheduler(scheduler);
                mainController.addListener();
            }
            optimalSchedule = scheduler.getSchedule();

        } else {
            System.out.println("Using serial");
            System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
            mainController.instantiateOptimalNodes(1);
            optimalScheduler = new BnBScheduler(dotFileReader, config, 0);
            if (config.getVisualise()) {
                mainController.setScheduler(optimalScheduler);
                mainController.addListener();
            }
            optimalSchedule = optimalScheduler.getSchedule();

        }



        System.out.println(optimalSchedule);
        System.out.println("We reached here");
        optimalSchedule.printSchedule();

//        //optimalSchedule = scheduler.getOptimalSchedule(nodeMap, edgeMap, numberOfProcessors);
        String graphName = dotFileReader.getGraphName();
//        //Corban's code to parse the optimal schedule to the output DOT file
//        OutputParser op = new OutputParser(graphName, config, optimalSchedule, optimalScheduler);
        OutputParser op = new OutputParser(graphName, config, optimalSchedule, dotFileReader);

        op.writeFile();

//        mainController.createGantt(op);
        if (config.getVisualise()) {
            mainController.createGantt(op.getNodeList());
        }


        if (!config.getVisualise()) {
            Platform.exit();
            System.exit(0);
        }

    }
}
