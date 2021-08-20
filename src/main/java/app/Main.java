package app;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Team UNTESTED
 * Main method where the implementation of the program begins.
 */
public class Main {



    public static void main(String[] args) {
        //SV's code to parse command line input arguments to extract the number of
        //processors, DOT file and other args

        ArgumentParser parser = new ArgumentParser();
		Config config;
		
		try {
			config = parser.parse(args);
		} catch (IllegalArgumentException e) {
			System.out.println("Unable to parse arguments "+ e.getMessage());
			return;
		}

        // debugging
        System.out.println("input file = " + config.getInputFile());
		System.out.println("number of processors = " + config.getNumOfProcessors());
		System.out.println("number of cores = " + config.getNumOfCores());
		System.out.println("visualise = " + config.getVisualise());
		System.out.println("output file = " + config.getOutputFile());
      
//		Scheduler scheduler = Scheduler.getInstance();

		//Joe's code that uses the DOT filepath from the args to create a new
		//DotFileReader object/graph representation

		DotFileReader dotFileReader = new DotFileReader(config.getInputFile());

		//debugging
		System.out.println("graph name = " + dotFileReader.getGraphName());

//        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());
//		System.out.println("Here is optimal: \n" + optimalSchedule);
		BnBSchedule optimalSchedule = null;

		if (config.getNumOfCores() > 1 && dotFileReader.getRootNodeList().size() > 1) {
			System.out.println("Using parallelisation");
			System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
			ParallelSchedule parallel = new ParallelSchedule(config, dotFileReader);

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
		} else {
			System.out.println("Using serial");
			System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
			BnBScheduler optimalScheduler = new BnBScheduler(dotFileReader, config);
			optimalSchedule = optimalScheduler.getSchedule();
		}


		System.out.println(optimalSchedule);
		System.out.println("We reached here");
		optimalSchedule.printSchedule();

//        //optimalSchedule = scheduler.getOptimalSchedule(nodeMap, edgeMap, numberOfProcessors);
		String graphName = dotFileReader.getGraphName();
//        //Corban's code to parse the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule, dotFileReader);

        op.writeFile();
    }

}
