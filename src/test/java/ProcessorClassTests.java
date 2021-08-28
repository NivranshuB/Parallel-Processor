import app.Processor;
import model.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class is used to test the Processor class.
 */
public class ProcessorClassTests {

    private Processor testProcessor;
    private Processor testScheduledProcessor;
    private Node scheduledNode;
    private Node secondScheduledNode;

    /**
     * This is used to set up objects for the tests.
     */
    @Before
    public void testSetUp() {
        List<Node> taskOrderList = new ArrayList<Node>();

        Node taskNode = new Node();
        taskNode.setName("Task Node");
        taskNode.setWeight(5);

        taskOrderList.add(taskNode);

        testProcessor = new Processor(taskOrderList, 5);

        scheduledNode = new Node();
        scheduledNode.setName("Node to Schedule");
        scheduledNode.setWeight(5);

        secondScheduledNode = new Node();
        secondScheduledNode.setName("Second Node to Schedule");
        secondScheduledNode.setWeight(6);

        testScheduledProcessor = new Processor();

    }

    /**
     * Check that a node is scheduled correctly and the Processor object records this correctly.
     */
    @Test
    public void testScheduleNode() {
        List<Node> taskOrderList = new ArrayList<Node>();

        Node taskNode = new Node();
        taskNode.setName("Node to Schedule");
        taskNode.setWeight(5);

        taskOrderList.add(taskNode);

        testScheduledProcessor.scheduleNode(scheduledNode, 2);

        assertEquals(7, testScheduledProcessor.getAvailableStartTime());

        //Check task order list
        //Check size
        if (taskOrderList.size() != testScheduledProcessor.getTaskOrder().size()) {
            fail("Task order list is not the correct size");
        }

        for (int i = 0; i < taskOrderList.size(); i++) {
            Node taskOrderListNode = taskOrderList.get(i);
            Node retrievedTaskOrderListNode = testScheduledProcessor.getTaskOrder().get(i);

            assertTrue(taskOrderListNode.equals(retrievedTaskOrderListNode));
        }

        //Check BnB output
        String bNBStringOutput = "2-Node to Schedule, ";

        assertEquals(bNBStringOutput, testScheduledProcessor.toString());
    }

    /**
     * Check that a node is unscheduled correctly and the processor object records this correctly.
     * In this case, there is still one task on the schedule after removal of one task and this starts
     * at startTime 9.
     */
    @Test
    public void testUnscheduleNodeStartTimeNonZero() {
        testScheduledProcessor.scheduleNode(scheduledNode, 2);
        testScheduledProcessor.scheduleNode(secondScheduledNode, 6);

        testScheduledProcessor.unscheduleNodeAtTime(2);

        List<Node> taskOrderList = new ArrayList<Node>();

        Node taskNode = new Node();
        taskNode.setName("Second Node to Schedule");
        taskNode.setWeight(6);

        taskOrderList.add(taskNode);

        //Check task order list
        //Check size
        /**
        if (taskOrderList.size() != testScheduledProcessor.getTaskOrder().size()) {
            fail("Task order list is not the correct size");
        }

        for (int i = 0; i < taskOrderList.size(); i++) {
            Node taskOrderListNode = taskOrderList.get(i);
            Node retrievedTaskOrderListNode = testScheduledProcessor.getTaskOrder().get(i);

            assertTrue(taskOrderListNode.equals(retrievedTaskOrderListNode));
        }
         */

        //Check available start time
        assertEquals(6, testScheduledProcessor.getAvailableStartTime());

        //Check BnB output
        String bNBStringOutput = "6-Second Node to Schedule, ";

        assertEquals(bNBStringOutput, testScheduledProcessor.toString());
    }

    /**
     * Check that a node is unscheduled correctly and the processor object records this correctly.
     * In this case, there are no tasks on the processor schedule and startTime is 0
     */
    @Test
    public void testUnscheduleNodeStartTimeZero() {
        testScheduledProcessor.scheduleNode(scheduledNode, 2);

        testScheduledProcessor.unscheduleNodeAtTime(2);

        List<Node> taskOrderList = new ArrayList<Node>();

        //Check task order list
        //Check size
        /**
         if (taskOrderList.size() != testScheduledProcessor.getTaskOrder().size()) {
         fail("Task order list is not the correct size");
         }

         for (int i = 0; i < taskOrderList.size(); i++) {
         Node taskOrderListNode = taskOrderList.get(i);
         Node retrievedTaskOrderListNode = testScheduledProcessor.getTaskOrder().get(i);

         assertTrue(taskOrderListNode.equals(retrievedTaskOrderListNode));
         }
         */

        //Check available start time
        assertEquals(0, testScheduledProcessor.getAvailableStartTime());

        //Check BnB output
        String bNBStringOutput = "";

        assertEquals(bNBStringOutput, testScheduledProcessor.toString());
    }

    /**
     * Check that an object being passed is is compared correctly with this Processor object using
     * the equals method. In this case, the object being passed in is exactly the same object as the
     * Processor object being compared to.
     */
    @Test
    public void testEqualsSameObject() {
        assertTrue(testProcessor.equals(testProcessor));
    }

    /**
     * Check that an object being passed is is compared correctly with this Processor object using
     * the equals method. In this case, the object being passed in is not a Processor object, it is a
     * String.
     */
    @Test
    public void testEqualsNotProcessorObject() {
        assertFalse(testProcessor.equals("Hi There"));
    }

    /**
     * Check that an object being passed is is compared correctly with this Processor object using
     * the equals method. In this case, the object being passed in is a Processor object with an
     * additional task in the taskOrder list.
     */
    @Test
    public void testEqualsAdditionalTask() {
        List<Node> taskOrderList = new ArrayList<Node>();

        Node taskNode = new Node();
        taskNode.setName("Task Node");
        taskNode.setWeight(5);

        Node taskNodeTwo = new Node();
        taskNodeTwo.setName("Task Node 2");
        taskNodeTwo.setWeight(6);

        taskOrderList.add(taskNode);
        taskOrderList.add(taskNodeTwo);

        Processor secondProcessor = new Processor(taskOrderList, 11);

        assertFalse(testProcessor.equals(secondProcessor));
    }

    /**
     * Check that an object being passed is is compared correctly with this Processor object using
     * the equals method. In this case, the object being passed in is a Processor object with a
     * different task in the taskOrderList (but same number of tasks).
     */
    @Test
    public void testEqualsDifferentTask() {
        List<Node> taskOrderList = new ArrayList<Node>();

        Node taskNode = new Node();
        taskNode.setName("Different Task Node");
        taskNode.setWeight(6);

        taskOrderList.add(taskNode);

        Processor secondProcessor = new Processor(taskOrderList, 6);

        assertFalse(testProcessor.equals(secondProcessor));
    }

    /**
     * Check that an object being passed is is compared correctly with this Processor object using
     * the equals method. In this case, the object being passed in is a Processor object with the
     * same characteristics as the Processor object it is being compared with, this means the two
     * objects are equal.
     */
    @Test
    public void testEqualsSameCharacteristicsEqualObjects() {
        List<Node> taskOrderList = new ArrayList<Node>();

        Node taskNode = new Node();
        taskNode.setName("Task Node");
        taskNode.setWeight(5);

        taskOrderList.add(taskNode);

        Processor secondProcessor = new Processor(taskOrderList, 5);

        assertTrue(testProcessor.equals(secondProcessor));
    }
}
