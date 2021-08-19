import app.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SystemTests {

    /**
     * This sets up for a system level testing input.
     */
    @Before
    public void testSetUp() {
        BnBScheduler.reset();
    }

    /**
     *
     */
    public BnBSchedule getOptimalSchedule(String[] inputArg) {
        ArgumentParser parser = new ArgumentParser();
        Config config;

        try {
            config = parser.parse(inputArg);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to parse arguments "+ e.getMessage());
            return null;
        }

        assertNotNull(config);
        System.out.println(config.getInputFile().getName());
        System.out.println("Number of processors: " + config.getNumOfProcessors());
        System.out.println("Number of tasks: " + config.toString());

        DotFileReader dotFileReader = new DotFileReader(config.getInputFile());
        BnBScheduler optimalScheduler = BnBScheduler.getInstance(dotFileReader, config);

        BnBSchedule optimalSchedule = optimalScheduler.getSchedule();
        optimalSchedule.printSchedule();

        return optimalSchedule;
    }

    /**
     * Tests for 4 node input graph, one processor
     */
    @Test
    public void NodesFourProcessorOneTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "1"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(24, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 4 node input graph, two processor
     */
    @Test
    public void NodesFourProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(19, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 4 node input graph, four processor
     */
    @Test
    public void NodesFourProcessorFourTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "4"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(19, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 7 node input graph, single processor
     */
    @Test
    public void NodesSevenProcessorOneTest() {

    }

    /**
     * Tests for 7 node input graph, two processor
     */
    @Test
    public void NodesSevenProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_7_OutTree.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(28, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 7 node input graph, four processor
     */
    @Test
    public void NodesSevenProcessorFourTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_7_OutTree.dot", "4"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(22, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 8 node input graph, single processor
     */
    @Test
    public void NodesEightProcessorOneTest() {

    }

    /**
     * Tests for 8 node input graph, two processor
     */
    @Test
    public void NodesEightProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_8_Random.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(581, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 8 node input graph, four processor
     */
    @Test
    public void NodesEightProcessorFourTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_8_Random.dot", "4"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(581, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 9 node input graph, single processor
     */
    @Test
    public void NodesNineProcessorOneTest() {

    }

    /**
     * Tests for 9 node input graph, two processor
     */
    @Test
    public void NodesNineProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_9_SeriesParallel.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(55, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 9 node input graph, four processor
     */
    @Test
    public void NodesNineProcessorFourTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_9_SeriesParallel.dot", "4"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(55, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node input graph, single processor
     */
    @Test
    public void NodesTenProcessorOneTest() {

    }

    /**
     * Tests for 10 node input graph, two processor
     */
    @Test
    public void NodesTenProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Random.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(50, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node input graph, four processor
     */
    @Test
    public void NodesTenProcessorFourTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Random.dot", "4"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(50, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 11 node input graph, single processor
     */
    @Test
    public void NodesElevenProcessorOneTest() {

    }

    /**
     * Tests for 11 node input graph, two processor
     */
    @Test
    public void NodesElevenProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_11_OutTree.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(350, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 11 node input graph, four processor
     */
    @Test
    public void NodesElevenProcessorFourTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_11_OutTree.dot", "4"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(227, optimal.calculateCriticalPath());
    }

    //=========================New test cases==================================//

    /**
     * Tests for 10 node on forked join input graph, two processor
     */
    @Test
    public void NodesTenForkJoinProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Fork_Join.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(499, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node on forked join input graph, two processor
     */
    @Test
    public void NodesTenForkProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Fork.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(300, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node on forked join input graph, two processor
     */
    @Test
    public void NodesTwentyOneIndependentProcessorTwoTest() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_21_Independent.dot", "2"};

        BnBSchedule optimal = getOptimalSchedule(inputArg);

        assertEquals(66, optimal.calculateCriticalPath());
    }

}
