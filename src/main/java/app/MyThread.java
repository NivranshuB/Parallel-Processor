package app;

public class MyThread extends Thread {

    public void run() {

        Config config = Config.getInstance();

//        Scheduler scheduler = Scheduler.getInstance();

        DotFileReader dotFileReader = Main.getDotFileReader();

        BnBScheduler optimalScheduler = BnBScheduler.getInstance(dotFileReader, config);

//        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());

        BnBSchedule optimalSchedule = optimalScheduler.getSchedule();

        String graphName = dotFileReader.getGraphName();

        //Parses the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule, optimalScheduler);

        MainController mainController = MainController.getInstance();

        op.writeFile();

        mainController.createGantt(op);
    }
}
