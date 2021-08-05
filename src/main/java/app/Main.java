package app;

public class Main {

    public static void main(String[] args) {

        ArgumentParser parser = new ArgumentParser();
		Config config;
		
		try {
			config = parser.parse(args);
		} catch (IllegalArgumentException e) {
			System.out.println("Unable to parse arguments"+ e.getMessage());
			return;
		}

        // debugging
        System.out.println("input file = " + config.getInputFile());
		System.out.println("number of processors = " + config.getNumOfProcessors());
		System.out.println("number of cores = " + config.getNumOfCores());
		System.out.println("visualise = " + config.getVisualise());
		System.out.println("output file = " + config.getOutputFile());


    }

}
