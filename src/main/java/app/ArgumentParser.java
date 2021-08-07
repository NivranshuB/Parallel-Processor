package app;

import java.io.File;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;

public class ArgumentParser {

	/**
	 * Parses the command-line inputs provided by the user and alert the user to any problems.
	 * @param args Arguments to parse from the user.
	 * @return Config object containing the configuration options which the user selected.
	 * @throws IllegalArgumentException Alert the user with regards to invalid arguments.
	 */
	public Config parse(String[] args) throws IllegalArgumentException {

		Config config = Config.getInstance();

		CommandLineParser parser = new DefaultParser();
		CommandLine cli;

		Options options = new Options();

		// set up optional args
		options.addOption("p", true, "use N cores for execution in parallel (default is sequential)");
		options.addOption("v", "visualise the search");
		options.addOption("o", true, "output file is named OUTPUT (default is INPUT−output.dot)");

		// check for required arguments
		if (args.length < 2) {
			throw new IllegalArgumentException("INPUT.dot and P are required.");
		}

		// get the input file
		String inputFilePath = args[0];
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists()) {
			throw new IllegalArgumentException("File " + inputFile.getAbsolutePath() + " not found.");
		}
		config.setInputFile(inputFile);

		// get the number of processors
		try {
			config.setNumOfProcessors(Integer.parseInt(args[1]));
			if (Integer.parseInt(args[1]) <= 0) {
				throw new IllegalArgumentException("P must be a positive integer.");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("P must be an integer.");
		}

		// set default optional args
		config.setNumOfCores(1);
		config.setVisualise(false);
		String outputFilePath = inputFilePath.replace(".dot", "") + "-output.dot";
		File outputFile = new File(outputFilePath);
		config.setOutputFile(outputFile);

		// get the optional arguments
		String[] optionalArgs = new String[args.length - 2];

		for (int i = 0; i < optionalArgs.length; i++) {
			optionalArgs[i] = args[i + 2];
		}

		// parse the optional arguments
		try {
			cli = parser.parse(options, optionalArgs);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can't parse the optional arguments.");
		}

		// ensure the arguments are valid
		if (cli.getArgList().size() != 0) {
			throw new IllegalArgumentException("Invalid arguments.");
		}

		// get the number of cores
		if (cli.hasOption("p")) {
			try {
				config.setNumOfCores(Integer.parseInt(cli.getOptionValue("p")));
				if (Integer.parseInt(cli.getOptionValue("p")) <= 0) {
					throw new IllegalArgumentException("N must be a positive integer.");
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("N must be an integer.");
			}
		}

		// set visualise option
		config.setVisualise(cli.hasOption("v"));

		// set the output file
		if (cli.hasOption("o")) {
			outputFilePath = cli.getOptionValue("o");
			outputFile = new File(outputFilePath);
			config.setOutputFile(outputFile);
		}

		return config;
	}
}