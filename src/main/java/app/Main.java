package app;



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
      
		Scheduler scheduler = Scheduler.getInstance();

		//Joe's code that uses the DOT filepath from the args to create a new
		//DotFileReader object/graph representation

		DotFileReader dotFileReader = new DotFileReader(config.getInputFile());

		//debugging
		System.out.println("graph name = " + dotFileReader.getGraphName());

//        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());
//		System.out.println("Here is optimal: \n" + optimalSchedule);

		BnBScheduler optimalScheduler = BnBScheduler.getInstance(dotFileReader, config);

		BnBSchedule optimalSchedule = optimalScheduler.getSchedule();
		System.out.println(optimalSchedule);
		System.out.println("We reached here");
		optimalSchedule.printSchedule();

//        //optimalSchedule = scheduler.getOptimalSchedule(nodeMap, edgeMap, numberOfProcessors);
		String graphName = dotFileReader.getGraphName();
//        //Corban's code to parse the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule, optimalScheduler);

        op.writeFile();
    }

}
