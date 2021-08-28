import app.Config;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * This class is used to test the Config class.
 */
public class ConfigClassTests {

    private Config testConfig;

    private int NUMBER_OF_TASKS = 2;
    private int NUMBER_OF_CORES = 1;
    private int NUMBER_OF_PROCESSORS = 1;
    private boolean VISUALISATION_STATUS = TRUE;
    private File INPUT_FILE = new File("src\\test\\test_files\\Node_7_OutTree.dot");
    private File OUTPUT_FILE = new File("src\\test\\test_files\\Node_7_OutTree_Output.dot");

    private int NEW_NUMBER_OF_TASKS = 5;
    private int NEW_NUMBER_OF_CORES = 2;
    private int NEW_NUMBER_OF_PROCESSORS = 3;
    private boolean NEW_VISUALISATION_STATUS = FALSE;
    private File NEW_INPUT_FILE = new File("src\\test\\test_files\\Node_8_Random.dot");
    private File NEW_OUTPUT_FILE = new File("src\\test\\test_files\\Node_7_Random_Output.dot");

    /**
     * This sets up the Config object for testing.
     */
    @Before
    public void testSetUp() {
        testConfig = Config.getInstance();
        testConfig.setNumOfTasks(NUMBER_OF_TASKS);
        testConfig.setNumOfCores(NUMBER_OF_CORES);
        testConfig.setNumOfProcessors(NUMBER_OF_PROCESSORS);
        testConfig.setVisualise(VISUALISATION_STATUS);
        testConfig.setInputFile(INPUT_FILE);
        testConfig.setOutputFile(OUTPUT_FILE);
    }

    /**
     * This test ensures that only a Singleton instance of Config is created.
     */
    @Test
    public void testGetInstance() {
        Config testSecondConfig = Config.getInstance();

        assertSame(testConfig, testSecondConfig);
    }

    /**
     * This test checks to ensure the correct input file is retrieved.
     */
    @Test
    public void testGetInputFile() {
        File inputFileRetrieved = testConfig.getInputFile();

        assertEquals(INPUT_FILE, inputFileRetrieved);
    }

    /**
     * This test checks to ensure that an input file can be correctly set on the Config object.
     */
    @Test
    public void testSetInputFile() {
        testConfig.setInputFile(NEW_INPUT_FILE);

        assertEquals(NEW_INPUT_FILE, testConfig.getInputFile());
    }

    /**
     * This test checks to ensure that the correct number of tasks is retrieved.
     */
    @Test
    public void testGetNumOfTasks() {
        int numberOfTasks = testConfig.getNumOfTasks();

        assertEquals(NUMBER_OF_TASKS, numberOfTasks);
    }

    /**
     * This test checks to ensure that a new number of tasks can be correctly set ont the Config object.
     */
    @Test
    public void testSetNumOfTasks() {
        testConfig.setNumOfTasks(NEW_NUMBER_OF_TASKS);

        assertEquals(NEW_NUMBER_OF_TASKS, testConfig.getNumOfTasks());
    }

    /**
     * This test checks to ensure that the correct number of cores is retrieved.
     */
    @Test
    public void testGetNumOfCores() {
        int numberOfCores = testConfig.getNumOfCores();

        assertEquals(NUMBER_OF_CORES, numberOfCores);
    }

    /**
     * This test checks to ensure that the number of cores used can be correctly set on the Config object.
     */
    @Test
    public void testSetNumOfCores() {
        testConfig.setNumOfCores(NEW_NUMBER_OF_CORES);

        assertEquals(NEW_NUMBER_OF_CORES, testConfig.getNumOfCores());
    }

    /**
     * This test checks to ensure that the correct number of processors is retrieved.
     */
    @Test
    public void testGetNumOfProcessors() {
        int numberOfProcessors = testConfig.getNumOfProcessors();

        assertEquals(NUMBER_OF_PROCESSORS, numberOfProcessors);
    }

    /**
     * This test checks to ensure that the number of processors used can be correctly set on the
     * Config object.
     */
    @Test
    public void testSetNumOfProcessors() {
        testConfig.setNumOfProcessors(NEW_NUMBER_OF_PROCESSORS);

        assertEquals(NEW_NUMBER_OF_PROCESSORS, testConfig.getNumOfProcessors());
    }

    /**
     * This test checks to ensure that the visualisation status is correctly retrieved.
     */
    @Test
    public void testGetVisualise() {
        boolean visualisation = testConfig.getVisualise();

        assertEquals(VISUALISATION_STATUS, visualisation);
    }

    /**
     * This test checks to ensure that the visualisation status can be correctly set on
     * the Config object.
     */
    @Test
    public void testSetVisualise() {
        testConfig.setVisualise(NEW_VISUALISATION_STATUS);

        assertEquals(NEW_VISUALISATION_STATUS, testConfig.getVisualise());
    }

    /**
     * This test checks to ensure the correct output file is retrieved.
     */
    @Test
    public void testGetOutputFile() {
        File outputFile = testConfig.getOutputFile();

        assertEquals(OUTPUT_FILE, outputFile);
    }

    /**
     * This test checks to ensure that an output file can be correctly set on the Config object.
     */
    @Test
    public void testSetOutputFile() {
        testConfig.setOutputFile(NEW_OUTPUT_FILE);

        assertEquals(NEW_OUTPUT_FILE, testConfig.getOutputFile());
    }

}
