import app.BnBSchedule;
import app.DotFileReader;
import app.Processor;
import app.SingleProcessorNoEdgesScheduler;
import model.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This class is used to test the SingleProcessorNoEdgesScheduler class.
 */
public class SingleProcessorNoEdgesSchedulerClassTests {

    private SingleProcessorNoEdgesScheduler testScheduler;
    private DotFileReader exampleDotFileReader;
    private File DEFAULT_INPUT_FILE = new File("src\\test\\test_files\\Nodes_21_Independent.dot");
    private Map<String, Node> DEFAULT_NODE_MAP = new HashMap<>();
    private BnBSchedule DEFAULT_OUTPUT;
    private int DEFAULT_OUTPUT_CRITICAL_PATH;

    /**
     * This is used to set up objects for the tests.
     */
    @Before
    public void testSetUp() {
        exampleDotFileReader = new DotFileReader(DEFAULT_INPUT_FILE);

        testScheduler = new SingleProcessorNoEdgesScheduler(exampleDotFileReader);

        Node nodeOne = new Node();
        nodeOne.setName("1");
        nodeOne.setWeight(6);

        Node nodeTwo = new Node();
        nodeTwo.setName("2");
        nodeTwo.setWeight(6);

        Node nodeThree = new Node();
        nodeThree.setName("3");
        nodeThree.setWeight(3);

        Node nodeFour = new Node();
        nodeFour.setName("4");
        nodeFour.setWeight(9);

        Node nodeFive = new Node();
        nodeFive.setName("5");
        nodeFive.setWeight(4);

        Node nodeSix = new Node();
        nodeSix.setName("6");
        nodeSix.setWeight(10);

        Node nodeSeven = new Node();
        nodeSeven.setName("7");
        nodeSeven.setWeight(3);

        Node nodeEight = new Node();
        nodeEight.setName("8");
        nodeEight.setWeight(9);

        Node nodeNine = new Node();
        nodeNine.setName("9");
        nodeNine.setWeight(7);

        Node nodeTen = new Node();
        nodeTen.setName("10");
        nodeTen.setWeight(9);

        Node nodeEleven = new Node();
        nodeEleven.setName("11");
        nodeEleven.setWeight(7);

        Node nodeTwelve = new Node();
        nodeTwelve.setName("12");
        nodeTwelve.setWeight(10);

        Node nodeThirteen = new Node();
        nodeThirteen.setName("13");
        nodeThirteen.setWeight(4);

        Node nodeFourteen = new Node();
        nodeFourteen.setName("14");
        nodeFourteen.setWeight(3);

        Node nodeFifteen = new Node();
        nodeFifteen.setName("15");
        nodeFifteen.setWeight(2);

        Node nodeSixteen = new Node();
        nodeSixteen.setName("16");
        nodeSixteen.setWeight(8);

        Node nodeSeventeen = new Node();
        nodeSeventeen.setName("17");
        nodeSeventeen.setWeight(3);

        Node nodeEighteen = new Node();
        nodeEighteen.setName("18");
        nodeEighteen.setWeight(8);

        Node nodeNineteen = new Node();
        nodeNineteen.setName("19");
        nodeNineteen.setWeight(9);

        Node nodeTwenty = new Node();
        nodeTwenty.setName("20");
        nodeTwenty.setWeight(8);

        Node nodeTwentyone = new Node();
        nodeTwentyone.setName("21");
        nodeTwentyone.setWeight(4);

        DEFAULT_NODE_MAP.put("1", nodeOne);
        DEFAULT_NODE_MAP.put("2", nodeTwo);
        DEFAULT_NODE_MAP.put("3", nodeThree);
        DEFAULT_NODE_MAP.put("4", nodeFour);
        DEFAULT_NODE_MAP.put("5", nodeFive);
        DEFAULT_NODE_MAP.put("6", nodeSix);
        DEFAULT_NODE_MAP.put("7", nodeSeven);
        DEFAULT_NODE_MAP.put("8", nodeEight);
        DEFAULT_NODE_MAP.put("9", nodeNine);
        DEFAULT_NODE_MAP.put("10", nodeTen);
        DEFAULT_NODE_MAP.put("11", nodeEleven);
        DEFAULT_NODE_MAP.put("12", nodeTwelve);
        DEFAULT_NODE_MAP.put("13", nodeThirteen);
        DEFAULT_NODE_MAP.put("14", nodeFourteen);
        DEFAULT_NODE_MAP.put("15", nodeFifteen);
        DEFAULT_NODE_MAP.put("16", nodeSixteen);
        DEFAULT_NODE_MAP.put("17", nodeSeventeen);
        DEFAULT_NODE_MAP.put("18", nodeEighteen);
        DEFAULT_NODE_MAP.put("19", nodeNineteen);
        DEFAULT_NODE_MAP.put("20", nodeTwenty);
        DEFAULT_NODE_MAP.put("21", nodeTwentyone);

        List<Processor> processorList = new ArrayList<Processor>();

        Processor p = new Processor();

        p.scheduleNode(nodeOne, 0);
        p.scheduleNode(nodeTwo, 6);
        p.scheduleNode(nodeThree, 12);
        p.scheduleNode(nodeFour, 15);
        p.scheduleNode(nodeFive, 24);
        p.scheduleNode(nodeSix, 28);
        p.scheduleNode(nodeSeven, 38);
        p.scheduleNode(nodeEight, 41);
        p.scheduleNode(nodeNine, 50);
        p.scheduleNode(nodeTen, 57);
        p.scheduleNode(nodeEleven, 66);
        p.scheduleNode(nodeTwelve, 73);
        p.scheduleNode(nodeThirteen, 83);
        p.scheduleNode(nodeFourteen, 87);
        p.scheduleNode(nodeFifteen, 90);
        p.scheduleNode(nodeSixteen, 92);
        p.scheduleNode(nodeSeventeen, 100);
        p.scheduleNode(nodeEighteen, 103);
        p.scheduleNode(nodeNineteen, 111);
        p.scheduleNode(nodeTwenty, 120);
        p.scheduleNode(nodeTwentyone, 128);

        processorList.add(p);

        DEFAULT_OUTPUT = new BnBSchedule(processorList);

        DEFAULT_OUTPUT_CRITICAL_PATH = DEFAULT_OUTPUT.calculateCriticalPath();
    }

    /**
     * This test checks to see if the schedules received from using the getSchedule method is correct.
     */
    @Test
    public void testGetSchedule() {
        BnBSchedule scheduleTestOutput = testScheduler.getSchedule();

        scheduleTestOutput.printSchedule();

        int scheduleTestOutputCriticalPath = scheduleTestOutput.calculateCriticalPath();

        assertEquals(DEFAULT_OUTPUT_CRITICAL_PATH, scheduleTestOutputCriticalPath);
    }

}
