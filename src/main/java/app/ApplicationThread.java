package app;

import javafx.application.Platform;
import parallelisation.ParallelScheduler;
import parsers.Config;
import parsers.DotFileReader;
import parsers.OutputParser;
import visualisation.MainController;

import java.util.concurrent.ExecutionException;

/**
 * Author: Team Untested (13)
 * Class that implements the run method for threading.
 */
public class ApplicationThread extends Thread {

    /**
     * Required method for thread implementation, called by start() method.
     */
    public void run() {
        // Set up
        Config config = Config.getInstance();
        DotFileReader dotFileReader = Main.getDotFileReader();
        MainController mainController = MainController.getInstance();
        BnBSchedule optimalSchedule = null;
        BnBScheduler optimalScheduler;

        // Decides on which scheduler implementation to use based on the input graph attributes.
        if (dotFileReader.getEdgeMap().size() == 0 && config.getNumOfProcessors() == 1) {
            SingleProcessorNoEdgesScheduler scheduler = new SingleProcessorNoEdgesScheduler(dotFileReader);
            if (config.getVisualise()) {
                mainController.setScheduler(scheduler);
                mainController.addListener();
            }
            optimalSchedule = scheduler.getSchedule();

        } else if (config.getNumOfCores() > 1 ) {
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
        } else {
            optimalScheduler = new BnBScheduler(dotFileReader, config, 0);
            if (config.getVisualise()) {
                mainController.instantiateOptimalNodes(1);
                mainController.setScheduler(optimalScheduler);
                mainController.addListener();
            }
            optimalSchedule = optimalScheduler.getSchedule();
        }

        //Parses the output and writes the optimum schedule to a new file.
        OutputParser op = new OutputParser(config, optimalSchedule, dotFileReader);
        op.writeFile();

        if (config.getVisualise()) {
            mainController.createGantt(op.getNodeList());
        } else {
            System.out.println("Optimal Schedule found with critical path: " + optimalSchedule.getWeight());
            Platform.exit();
            System.exit(0);
        }
    }
}
