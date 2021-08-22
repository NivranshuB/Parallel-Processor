package app;

import javafx.application.Platform;

import java.util.concurrent.ExecutionException;

/**
 * Author: Team UNTESTED
 */
public class MyThread extends Thread {

    public void run() {
        Config config = Config.getInstance();

        DotFileReader dotFileReader = Main.getDotFileReader();

        MainController mainController = MainController.getInstance();

        BnBSchedule optimalSchedule = null;
        BnBScheduler optimalScheduler;

        if (config.getNumOfCores() > 1 && dotFileReader.getRootNodeList().size() > 1) {
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
            optimalScheduler = new BnBScheduler(dotFileReader, config, 0);
            if (config.getVisualise()) {
                mainController.instantiateOptimalNodes(1);
                mainController.setScheduler(optimalScheduler);
                mainController.addListener();
            }
            optimalSchedule = optimalScheduler.getSchedule();
        }


        OutputParser op = new OutputParser(config, optimalSchedule, dotFileReader);

        op.writeFile();

        if (config.getVisualise()) {
            mainController.createGantt(op.getNodeList());
        }

        if (!config.getVisualise()) {
            Platform.exit();
            System.exit(0);
        }

    }
}
