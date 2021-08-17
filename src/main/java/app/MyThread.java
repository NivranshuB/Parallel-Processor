package app;

public class MyThread extends Thread {

    public void run() {

        Config config = Config.getInstance();

        Scheduler scheduler = Scheduler.getInstance();

        DotFileReader dotFileReader = Main.getDotFileReader();

        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());

        String graphName = dotFileReader.getGraphName();

        //Parses the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule);

        op.writeFile();
    }
}
