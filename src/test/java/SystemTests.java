import app.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * This is the test class that is responsible for testing the entire system from when the arguments are parsed
 * to the length of the optimal schedule returned. This class relies on the input dot files present in
 * src/test/test_files.
 */
public class SystemTests {

    /**
     * This sets up for a system level testing input. It makes sure that the singleton BnBScheduler instance
     * has its fields reset before any input test graph.
     */
    @Before
    public void testSetUp() {
//        BnBScheduler.reset();
    }

    /**
     * Method that given the String[] of command line input arguments returns the optimal BnBSchedule object.
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

        DotFileReader dotFileReader = new DotFileReader(config.getInputFile());
        System.out.println("Number of root nodes: " + dotFileReader.getRootNodeList().size());
        System.out.println("Number of edges: " + dotFileReader.getEdgeMap().size());
        System.out.println("Number of processors: " + config.getNumOfProcessors());

        BnBSchedule optimalSchedule = new BnBSchedule();
        if (config.getNumOfCores() > 1 && dotFileReader.getRootNodeList().size() > 1 && dotFileReader.getEdgeMap().size() != 0) {
            System.out.println("Using parallelisation");

            ParallelScheduler parallel = new ParallelScheduler(config, dotFileReader);

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
            BnBScheduler optimalScheduler = new BnBScheduler(dotFileReader, config, 0);
            optimalSchedule = optimalScheduler.getSchedule();
        }


//        BnBScheduler optimalScheduler = BnBScheduler.getInstance(dotFileReader, config);


        optimalSchedule.printSchedule();

        return optimalSchedule;
    }

    /**
     * Tests for 4 node input graph, on 1 processor with the default single core.
     */
    @Test
    public void Nodes4Processor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(24, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 4 node input graph, on 1 processors with the default single core.
     */
    @Test
    public void Nodes4Processor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(19, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 4 node input graph, on 4 processors with the default single core.
     */
    @Test
    public void Nodes4Processor4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(19, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 4 node input graph, on 4 processors with 3 cores.
     */
    @Test
    public void Nodes4Processor4Cores3Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_4.dot", "4", "-p", "3"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(19, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 7 node input graph, on 1 processor with the default single core.
     */
    @Test
    public void Nodes7Processor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_7_OutTree.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(40, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 7 node input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes7Processor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_7_OutTree.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(28, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 7 node input graph, on 2 processors with 2 cores.
     */
    @Test
    public void Nodes7Processor2Cores2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_7_OutTree.dot", "2", "-p", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(28, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 7 node input graph, on 4 processors with the default single core.
     */
    @Test
    public void Nodes7Processor4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_7_OutTree.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(22, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 8 node input graph, on 1 processor with the default single core.
     */
    @Test
    public void Nodes8Processor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_8_Random.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(969, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 8 node input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes8Processor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_8_Random.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(581, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 8 node input graph, on 4 processors with the default single core.
     */
    @Test
    public void Nodes8Processor4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_8_Random.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(581, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 8 node input graph, on 4 processors with 2 cores.
     */
    @Test
    public void Nodes8Processor4Cores2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_8_Random.dot", "4", "-p", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(581, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 9 node input graph, on 1 processor with the default single core.
     */
    @Test
    public void Nodes9Processor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_9_SeriesParallel.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(55, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 9 node input graph, on 1 processor with 3 cores.
     */
    @Test
    public void Nodes9Processor1Cores3Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_9_SeriesParallel.dot", "1", "-p", "3"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(55, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 9 node input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes9Processor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_9_SeriesParallel.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(55, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 9 node input graph, on 4 processors with the default single core.
     */
    @Test
    public void Nodes9Processor4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_9_SeriesParallel.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(55, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node input graph, on 1 processor with the default single core.
     */
    @Test
    public void Nodes10Processor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Random.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(63, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes10Processor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Random.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(50, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node input graph, on 2 processors with 3 cores.
     */
    @Test
    public void Nodes10Processor2Cores3Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Random.dot", "2", "-p", "3"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(50, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node input graph, on 4 processors with the default single core.
     */
    @Test
    public void Nodes10Processor4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Random.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(50, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node on forked join input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes10ForkJoinProcessor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Fork_Join.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(499, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 10 node on forked split input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes10ForkProcessor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_10_Fork.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(300, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 11 node input graph, on 1 processor with the default single core.
     */
    @Test
    public void Nodes11Processor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_11_OutTree.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(640, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 11 node input graph, on 2 processors with the default single core.
     */
    @Test
    public void Nodes11Processor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_11_OutTree.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(350, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 11 node input graph, on 4 processors with the default single core.
     */
    @Test
    public void Nodes11Processor4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_11_OutTree.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(227, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 11 node input graph, on 4 processors with 4 cores.
     */
    @Test
    public void Nodes11Processor4Cores4Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_11_OutTree.dot", "4", "-p", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(227, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 15 node input graph with a large amount of edges (light on memory), on 4 processors
     * with the default single core.
     */
    @Test
    public void Nodes15Edges80Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_15_NumerousEdges.dot", "4"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(516, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 15 node input graph with a very small amount of edges (memory intensive), on 3 processors
     * with the default single core.
     */
    @Test
    public void Nodes15Edges10Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_15_SparseEdges.dot", "3"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(197, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 21 node input graph with no edge dependencies, on 1 processors with the default single core.
     * This is one of the worst case input graphs for DFS variation algorithms.
     */
    @Test
    public void Nodes21IndependentProcessor1Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_21_Independent.dot", "1"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(132, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 21 node input graph with no edge dependencies, on 1 processors with 6 cores.
     * This is one of the worst case input graphs for DFS variation algorithms.
     */
    @Test
    public void Nodes21IndependentProcessor1Cores6Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_21_Independent.dot", "1", "-p", "6"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(132, optimal.calculateCriticalPath());
    }

    /**
     * Tests for 21 node input graph with no edge dependencies, on 2 processors with the default single core.
     * This is one of the worst case input graphs for DFS variation algorithms.
     */
    @Test
    public void Nodes21IndependentProcessor2Test() {
        String[] inputArg = {"src\\test\\test_files\\Nodes_21_Independent.dot", "2"};
        BnBSchedule optimal = getOptimalSchedule(inputArg);
        assertEquals(66, optimal.calculateCriticalPath());
    }
}
