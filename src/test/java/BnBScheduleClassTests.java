import app.BnBSchedule;
import app.Processor;
import model.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class is used to test the BnBScheduleClass
 */
public class BnBScheduleClassTests {

    private BnBSchedule testBnBSchedule;
    private BnBSchedule secondTestBnBSchedule;

    private Processor p1 = new Processor();
    private Processor p2 = new Processor();

    private List<Processor> p1List = new ArrayList<Processor>();
    private List<Processor> p2List = new ArrayList<Processor>();

    /**
     * This is used to set up objects for the test.
     */
    @Before
    public void testSetUp() {
        Processor p1 = new Processor();
        Processor p2 = new Processor();

        Node p1Node = new Node();
        p1Node.setName("Test Node");
        p1Node.setWeight(5);

        Node p2Node = new Node();
        p2Node.setName("Test Node");
        p2Node.setWeight(5);

        p1.scheduleNode(p1Node, 6);
        p2.scheduleNode(p2Node, 6);
    }

    /**
     * This test checks to see if the max value is returned correctly.
     */
    @Test
    public void testGetWeight() {
        testBnBSchedule = new BnBSchedule();

        assertEquals(Integer.MAX_VALUE, testBnBSchedule.getWeight());
    }

    /**
     * This tests checks that the Node list is correctly retrieved for this schedule. In this case,
     * the Node list is empty.
     */
    @Test
    public void testGetNodeListEmpty() {
        testBnBSchedule = new BnBSchedule();

        List<Node> expectedNodeList = new ArrayList<Node>();

        assertEquals(expectedNodeList, testBnBSchedule.getNodeList());
    }

    /**
     * This test checks to see if the equals method in BnBSchedule class returns the correct boolean
     * when an object to be compared is passed in. In this case, the object passed in is exactly the
     * same object it is being compared to.
     */
    @Test
    public void testEqualsSameObject() {
        testBnBSchedule = new BnBSchedule(p1List);

        assertTrue(testBnBSchedule.equals(testBnBSchedule));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is not a
     * BnBSchedule object, it is a String.
     */
    @Test
    public void testEqualsNotEdgeObject() {
        testBnBSchedule = new BnBSchedule(p1List);

        assertFalse(testBnBSchedule.equals("Hi There"));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a
     * BnBSchedule object with a different max value.
     */
    @Test
    public void testEqualsDifferentMax() {
        testBnBSchedule = new BnBSchedule(p1List);
        secondTestBnBSchedule = new BnBSchedule();

        assertFalse(testBnBSchedule.equals(secondTestBnBSchedule));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a
     * BnBSchedule object with the same characteristics, therefore the objects are equal.
     */
    @Test
    public void testEqualsSameCharacteristics() {
        p1List.add(p1);
        p2List.add(p2);

        testBnBSchedule = new BnBSchedule(p1List);
        secondTestBnBSchedule = new BnBSchedule(p2List);

        assertTrue(testBnBSchedule.equals(secondTestBnBSchedule));
    }

}
