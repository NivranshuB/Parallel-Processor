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
      
		Scheduler scheduler = Scheduler.getInstance();

		//Joe's code that uses the DOT filepath from the args to create a new
		//DotFileReader object/graph representation

		DotFileReader dotFileReader = new DotFileReader(config.getInputFile());

        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());

		String graphName = dotFileReader.getGraphName();

        //Corban's code to parse the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule);

        op.writeFile();
    }

}
