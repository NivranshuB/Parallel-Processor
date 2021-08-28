import app.ArgumentParser;
import app.Config;
import org.junit.Test;

import java.io.File;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;

/**
 * This class is used to test the ArgumentParser class.
 */
public class ArgumentParserClassTests {

    //NEED TO CHECK ALL TESTS IN THIS CLASS

    private ArgumentParser testArgumentParser;
    private Config testConfigOutput;

    private File DEFAULT_INPUT_FILE = new File("src\\test\\test_files\\Nodes_7_OutTree.dot");
    private File DEFAULT_OUTPUT_FILE = new File("src\\test\\test_files\\Nodes_7_OutTree-output.dot");
    private int NUM_OF_PROCESSORS = 1;
    private int DEFAULT_NUM_OF_CORES = 1;
    private boolean DEFAULT_VISUALISATION_STATUS = FALSE;

    private File NEW_OUTPUT_FILE = new File("src\\test\\test_files\\OUTPUT_TEST.dot");
    private int NEW_NUM_OF_CORES = 2;
    private boolean NEW_VISUALISATION_STATUS = TRUE;

    private File NON_EXISTENT_INPUT_FILE = new File("src\\test\\test_files\\Non-Existent_File.dot");

    private String[] ONE_P_NO_OPTIONS_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS)};
    private String[] ONE_P_ALL_OPTIONS_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS),
            "-p", Integer.toString(NEW_NUM_OF_CORES), "-v", "-o", NEW_OUTPUT_FILE.toString()};
    private String[] NULL_P_NO_OPTIONS_ARGS = {DEFAULT_INPUT_FILE.toString()};
    private String[] ONE_P_WRONG_INPUT_NO_OPTIONS_ARGS = {NON_EXISTENT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS)};
    private String[] ZERO_P_NO_OPTIONS_ARGS = {DEFAULT_INPUT_FILE.toString(), "0"};
    private String[] NEGATIVE_P_NO_OPTIONS_ARGS = {DEFAULT_INPUT_FILE.toString(), "-1"};
    private String[] DECIMAL_P_NO_OPTIONS_ARGS = {DEFAULT_INPUT_FILE.toString(), "1.5"};
    private String[] ONE_P_NO_CORES_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS), "-p"};
    private String[] ONE_P_TWO_CORE_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS), "-p", "2", "3"};
    private String[] ONE_P_INVALID_OPTION_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS), "-t"};
    private String[] ONE_P_ZERO_CORES_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS), "-p", "0"};
    private String[] ONE_P_NEGATIVE_CORES_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS), "-p", "-1"};
    private String[] ONE_P_DECIMAL_CORES_ARGS = {DEFAULT_INPUT_FILE.toString(), Integer.toString(NUM_OF_PROCESSORS), "-p", "1.5"};

    /**
     * This test checks to see if a String array of arguments is parsed successfully. These arguments
     * involve the input graph file and uses 1 processor for scheduling. All optional arguments are set
     * to default.
     */
    @Test
    public void testParseSuccessful_OneProcessor_NoOptions() {
        testArgumentParser = new ArgumentParser();

        testConfigOutput = testArgumentParser.parse(ONE_P_NO_OPTIONS_ARGS);

        //Check values in testConfigOutput object are correct
        //Check input file is correct
        assertEquals(DEFAULT_INPUT_FILE, testConfigOutput.getInputFile());

        //Check output file is correct
        assertEquals(DEFAULT_OUTPUT_FILE, testConfigOutput.getOutputFile());

        //Check number of processors used is correct
        assertEquals(NUM_OF_PROCESSORS, testConfigOutput.getNumOfProcessors());

        //Check number of cores is correct
        assertEquals(DEFAULT_NUM_OF_CORES, testConfigOutput.getNumOfCores());

        //Check visualisation status is correct
        assertEquals(DEFAULT_VISUALISATION_STATUS, testConfigOutput.getVisualise());
    }

    /**
     * This test checks to see if a String array of arguments is parsed successfully. These arguments
     * involve the input graph file and uses 1 processor for scheduling. All optional arguments are
     * provided. The "Number of cores" will be set to 2, visualise option is wanted, and the output file
     * name is provided (OUTPUT_TEST).
     */
    @Test
    public void testParseSuccessful_OneProcessor_AllOptions() {
        testArgumentParser = new ArgumentParser();

        testConfigOutput = testArgumentParser.parse(ONE_P_ALL_OPTIONS_ARGS);

        //Check values in testOutputConfig is correct
        //Check input file is correct
        assertEquals(DEFAULT_INPUT_FILE, testConfigOutput.getInputFile());

        //Check output file is correct
        assertEquals(NEW_OUTPUT_FILE, testConfigOutput.getOutputFile());

        //Check number of processors used is correct
        assertEquals(NUM_OF_PROCESSORS, testConfigOutput.getNumOfProcessors());

        //Check number of cores is correct
        assertEquals(NEW_NUM_OF_CORES, testConfigOutput.getNumOfCores());

        //Check visualisation status is correct
        assertEquals(NEW_VISUALISATION_STATUS, testConfigOutput.getVisualise());
    }

    /**
     * This test checks to see if a String array of arguments parsed throws an IllegalArgumentException.
     * These arguments involve the input graph file. All optional arguments are set to default. Note that
     * the number of processors to be used is not provided.
     */
    @Test
    public void testParseIllegalArgumentException_NullProcessors_NoOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException thrown
            testConfigOutput = testArgumentParser.parse(NULL_P_NO_OPTIONS_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("INPUT.dot and P are required.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws an IllegalArgumentException.
     * These arguments involve the input graph file which doesn't exist and 1 processor. All optional
     * arguments are set to default.
     */
    @Test
    public void testParseIllegalArgumentException_NonExistentFile_NoOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException thrown
            testConfigOutput = testArgumentParser.parse(ONE_P_WRONG_INPUT_NO_OPTIONS_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("File " + NON_EXISTENT_INPUT_FILE.getAbsolutePath() + " not found.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws an IllegalArgumentException.
     * These arguments involve the input graph file and 0 processors. All optional
     * arguments are set to default. Note that the number of processor to use is invalid as cannot be
     * zero.
     */
    @Test
    public void testParseIllegalArgumentException_ZeroProcessors_NoOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException thrown
            testConfigOutput = testArgumentParser.parse(ZERO_P_NO_OPTIONS_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("P must be a positive integer.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws an IllegalArgumentException.
     * These arguments involve the input graph file and -1 processors. All optional
     * arguments are set to default. Note that the number of processor to use is invalid as cannot be
     * negative.
     */
    @Test
    public void testParseIllegalArgumentException_NegativeProcessors_NoOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException is thrown
            testConfigOutput = testArgumentParser.parse(NEGATIVE_P_NO_OPTIONS_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("P must be a positive integer.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws a NumberFormatException.
     * These arguments involve the input graph file and 1.5 processors. All optional
     * arguments are set to default. Note that the number of processor to use is invalid as cannot be
     * a decimal number.
     */
    @Test
    public void testParseIllegalArgumentException_OnePointFiveProcessors_NoOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException is thrown
            testConfigOutput = testArgumentParser.parse(DECIMAL_P_NO_OPTIONS_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("P must be an integer.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws a ParseException.
     * These arguments involve the input graph file and 1 processor. The "Number of cores" option
     * will be set to take a number of cores for which its argument is not specified. All other options
     * are set to default.
     */
    @Test
    public void testParseMissingArgumentException_OneProcessor_MissingNoOfCoresArgOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct MissingArgumentException thrown
            testConfigOutput = testArgumentParser.parse(ONE_P_NO_CORES_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("Can't parse the optional arguments.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws an IllegalArgumentException.
     * These arguments involve the input graph file and 1 processor. The "Number of cores" option is set
     * to have zero cores. All other options are set to default. Note that you cannot have zero cores for
     * code execution.
     */
    @Test
    public void testParseIllegalArgumentException_OneProcessor_ZeroCoresOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException thrown
            testConfigOutput = testArgumentParser.parse(ONE_P_ZERO_CORES_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("N must be a positive integer.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws an IllegalArgumentException.
     * These arguments involve the input graph file and 1 processor. The "Number of cores" option is set
     * to have -1 cores. All other options are set to default. Note that you cannot have negative cores for
     * code execution.
     */
    @Test
    public void testParseIllegalArgumentException_OneProcessor_NegativeCoresOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct IllegalArgumentException thrown
            testConfigOutput = testArgumentParser.parse(ONE_P_NEGATIVE_CORES_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("N must be a positive integer.", e.getMessage());
        }
    }

    /**
     * This test checks to see if a String array of arguments parsed throws a NumberFormatException.
     * These arguments involve the input graph file and 1 processor. The "Number of cores" option is set
     * to have 1.5 cores. All other options are set to default. Note that you cannot have a decimal
     * number of cores (must be a positive integer) for code execution.
     */
    @Test
    public void testParseIllegalArgumentException_OneProcessors_OnePointFiveCoresOptions() {
        testArgumentParser = new ArgumentParser();

        try {
            //Check correct NumberFormatException thrown
            testConfigOutput = testArgumentParser.parse(ONE_P_DECIMAL_CORES_ARGS);

        } catch (IllegalArgumentException e) {
            assertEquals("N must be an integer.", e.getMessage());
        }
    }

}
