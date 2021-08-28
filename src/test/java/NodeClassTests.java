import app.Processor;
import model.Edge;
import model.Node;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class is used to unit test the methods in the Node class.
 */
public class NodeClassTests {

    private Node testNode;
    private Node secondTestNode;
    private Node parentNode;
    private Node childNode;
    private Node equivalentNode;
    private static final String NODE_NAME = "Test";
    private static final String NEW_NODE_NAME = "Test Node";
    private static final int WEIGHT = 5;
    private static final int NEW_WEIGHT = 18;
    private static final int BOTTOM_WEIGHT = 25;
    private static final int NEW_BOTTOM_WEIGHT = 32;
    private static final int START = 1;
    private static final int NEW_START = 2;
    private static final int PROCESSOR = 3;
    private static final int NEW_PROCESSOR = 4;

    /**
     * Sets up the testNode object for tests.
     */
    @Before
    public void testSetUp() {
        testNode = new Node();
        testNode.setName(NODE_NAME);
        testNode.setWeight(WEIGHT);
        testNode.setStart(START);
        testNode.setProcessor(PROCESSOR);
        testNode.setBottomWeight(BOTTOM_WEIGHT);

        secondTestNode = new Node();
        secondTestNode.setName(NODE_NAME);
        secondTestNode.setWeight(WEIGHT);
        secondTestNode.setStart(START);
        secondTestNode.setProcessor(PROCESSOR);
        secondTestNode.setBottomWeight(BOTTOM_WEIGHT);

        parentNode = new Node();
        parentNode.setName("Parent 1");
        parentNode.addChild(testNode);
        childNode = new Node();
        childNode.setName("Child 1");
        childNode.addParent(testNode);

        testNode.addParent(parentNode);
        testNode.addChild(childNode);

        equivalentNode = new Node();
        equivalentNode.setName("Equivalent 1");
        testNode.addEquivalentNodes(equivalentNode);
    }

    /**
     * This test checks if equivalent nodes for this Node object is correctly retrieved.
     */
    @Test
    public void testGetEquivalentNodes() {
        List<Node> equivalentNodes = testNode.getEquivalentNodes();

        //Check there is only one equivalent node
        if (equivalentNodes.size() == 1) {
            assertEquals(equivalentNode, equivalentNodes.get(0));
        } else {
            fail("List of equivalent nodes is incorrect - should only be one equivalent node.");
        }
    }

    /**
     * This test checks to see if an equivalent node has been added correctly.
     */
    @Test
    public void testAddEquivalentNodes() {
        Node secondEquivalentNode = new Node();
        testNode.addEquivalentNodes(secondEquivalentNode);

        //Check if the second equivalent node is added to this node.
        List<Node> equivalentNodesList = testNode.getEquivalentNodes();

        if (equivalentNodesList.size() == 2) {
            //Checks the first equivalent node in the list
            Node equivalentNodeRetrieved = equivalentNodesList.get(0);

            assertSame(equivalentNode, equivalentNodeRetrieved);

            //Checks the second equivalent node has been added
            Node secondEquivalentNodeRetrieved = equivalentNodesList.get(1);

            assertSame(secondEquivalentNode, secondEquivalentNodeRetrieved);
        } else {
            fail("List of equivalent nodes is incorrect - should only be two equivalent nodes.");
        }
    }

    /**
     * This test checks to see if the bottom weight of the node is correctly retrieved.
     */
    @Test
    public void testGetBottomWeight() {
        assertEquals(BOTTOM_WEIGHT, testNode.getBottomWeight());
    }

    /**
     * This test checks to see if a new bottom weight is correctly set on the Node object.
     */
    @Test
    public void testSetBottomWeight() {
        testNode.setBottomWeight(NEW_BOTTOM_WEIGHT);

        assertEquals(NEW_BOTTOM_WEIGHT, testNode.getBottomWeight());
    }

    /**
     * This test checks to see if a node passed in is equivalent to this node. The node passed in
     * is equivalent, so returns True.
     */
    @Test
    public void testIsEquivalentTrue() {
        assertTrue(testNode.isEquivalent(equivalentNode));
    }

    /**
     * This test checks to see if a node passed in is equivalent to this node. The node passed in
     * is not equivalent, so returns False.
     */
    @Test
    public void testIsEquivalentFalse() {
        assertFalse(testNode.isEquivalent(parentNode));
    }

    /**
     * This test checks that the schedule method correctly schedules this Node object on the correct
     * processor with the correct start time, as determined by the parameters passed in. Also checks
     * that this method returns the correct children that can be scheduled next. In this case, there
     * is one available child.
     */
    @Test
    public void testScheduleWithAvailableChildren() {
        Processor processorArg = new Processor();
        int startTimeArg = 11;

        Set<Node> availableChildren = testNode.schedule(processorArg, startTimeArg);

        assertEquals(processorArg, testNode.getBnBProcessor());
        assertEquals(startTimeArg, testNode.getStart());

        //Check there is one child available to schedule
        if (availableChildren.size() == 1) {
            assertTrue(availableChildren.contains(childNode));
        } else {
            fail("Should only have one available child to schedule");
        }
    }

    /**
     * This test checks that the schedule method correctly schedules this Node object on the correct
     * processor with the correct start time, as determined by the parameters passed in. Also checks
     * that this method returns the correct children that can be scheduled next. In this case, there
     * are no available children which are returned.
     */
    @Test
    public void testScheduleWithNoAvailableChildren() {
        Node parentOfChildNode = new Node();

        childNode.addParent(parentOfChildNode);

        Processor processorArg = new Processor();
        int startTimeArg = 11;

        Set<Node> availableChildren = testNode.schedule(processorArg, startTimeArg);

        assertEquals(processorArg, testNode.getBnBProcessor());
        assertEquals(startTimeArg, testNode.getStart());

        //Check there no children available to schedule
        assertTrue(availableChildren.isEmpty());
    }

    /**
     * This test checks to see if a Node object has been unscheduled correctly.
     */
    @Test
    public void testUnschedule() {
        testNode.unschedule();

        assertEquals(null, testNode.getBnBProcessor());
        assertEquals(0, testNode.getStart());
    }

    /**
     * This test checks to see if a node returns its parents correctly, given one parent in the List
     * of parents.
     */
    @Test
    public void testGetParent() {
        //Check if getting the parent of this node is exactly the same object as the parent added.
        List<Node> parentList = testNode.getParent();

        if (parentList.size() == 1) {
            Node parent = parentList.get(0);

            assertSame(parentNode, parent);
        } else {
            fail("List of parents is incorrect - should only be one parent.");
        }
    }

    /**
     * This test checks to see if a parent node has been added correctly.
     */
    @Test
    public void testAddParent() {
        Node secondParentNode = new Node();
        testNode.addParent(secondParentNode);

        //Check if the second parent node is added to this node.
        List<Node> parentList = testNode.getParent();

        if (parentList.size() == 2) {
            //Checks the first parent of this node
            Node parent = parentList.get(0);

            assertSame(parentNode, parent);

            //Checks the second parent node has been added
            Node secondParent = parentList.get(1);

            assertSame(secondParentNode, secondParent);
        } else {
            fail("List of parents is incorrect - should only be two parents.");
        }
    }

    /**
     * This test checks to see if a child is correctly retrieved, given one child in the List.
     */
    @Test
    public void testGetChild() {
        //Check if the child node retrieved is exactly the same object as the child in the Node object.
        List<Node> childList = testNode.getChild();

        if (childList.size() == 1) {
            Node child = childList.get(0);

            assertSame(childNode, child);
        } else {
            fail("List of children is incorrect - should only be one child.");
        }
    }

    /**
     * This test checks to see if a child is added correctly.
     */
    @Test
    public void testAddChild() {
        Node secondChildNode = new Node();
        testNode.addChild(secondChildNode);

        //Check if the second child node is added to this node.
        List<Node> childList = testNode.getChild();

        if (childList.size() == 2) {
            //Checks the first child of this node
            Node child = childList.get(0);

            assertSame(childNode, child);

            //Checks the second child node has been added
            Node secondChild = childList.get(1);

            assertSame(secondChildNode, secondChild);
        } else {
            fail("List of children is incorrect - should only be two children.");
        }
    }

    /**
     * This test checks to see if the weight of the node is correctly retrieved.
     */
    @Test
    public void testGetWeight() {
        assertEquals(WEIGHT, testNode.getWeight());
    }

    /**
     * This test checks to see if a new weight is correctly set on the Node object.
     */
    @Test
    public void testSetWeight() {
        testNode.setWeight(NEW_WEIGHT);

        assertEquals(NEW_WEIGHT, testNode.getWeight());
    }

    /**
     * This tests checks to see if the start time of a Node object is correctly retrieved.
     */
    @Test
    public void testGetStart() {
        assertEquals(START, testNode.getStart());
    }

    /**
     * This test checks to see if a new start time is correctly set on the Node object.
     */
    @Test
    public void testSetStart() {
        testNode.setStart(NEW_START);

        assertEquals(NEW_START, testNode.getStart());
    }

    /**
     * This tests checks to see if the processor number of a Node object is correctly retrieved.
     */
    @Test
    public void testGetProcessor() {
        assertEquals(PROCESSOR, testNode.getProcessor());
    }

    /**
     * This test checks to see if a new processor number is correctly set on the Node object.
     */
    @Test
    public void testSetProcessor() {
        testNode.setProcessor(NEW_PROCESSOR);

        assertEquals(NEW_PROCESSOR, testNode.getProcessor());
    }

    /**
     * This test checks to see if the name of a Node object is correctly retrieved.
     */
    @Test
    public void testGetName() {
        assertEquals(NODE_NAME, testNode.getName());
    }

    /**
     * This test checks to see if a name in String is correctly set to a Node object.
     */
    @Test
    public void testSetName() {
        testNode.setName(NEW_NODE_NAME);

        assertEquals(NEW_NODE_NAME, testNode.getName());
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is exactly the same
     * object it is being compared to.
     */
    @Test
    public void testEqualsSameObject() {
        assertTrue(testNode.equals(testNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is not a Node object,
     * it is a String.
     */
    @Test
    public void testEqualsNotEdgeObject() {
        assertFalse(testNode.equals("Hi there"));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different parent.
     */
    @Test
    public void testEqualsDifferentParentNode() {
        Node secondParentNode = new Node();
        secondParentNode.setName("Another Parent");

        secondTestNode.addParent(secondParentNode);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * an additional parent.
     */
    @Test
    public void testEqualsAdditionalParentNode() {
        Node secondParentNode = new Node();
        secondParentNode.setName("Another Parent");

        secondTestNode.addParent(parentNode);
        secondTestNode.addParent(secondParentNode);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different child.
     */
    @Test
    public void testEqualsDifferentChildNode() {
        Node secondChildNode = new Node();
        secondChildNode.setName("Another Child");

        secondTestNode.addParent(parentNode);

        secondTestNode.addChild(secondChildNode);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * an additional child.
     */
    @Test
    public void testEqualsAdditionalChildNode() {
        Node secondChildNode = new Node();
        secondChildNode.setName("Another Child");

        secondTestNode.addParent(parentNode);

        secondTestNode.addChild(childNode);
        secondTestNode.addChild(secondChildNode);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different equivalent node.
     */
    @Test
    public void testEqualsDifferentEquivalentNode() {
        Node secondEquivalentNode = new Node();
        secondEquivalentNode.setName("Another Equivalent");

        secondTestNode.addEquivalentNodes(secondEquivalentNode);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * an additional equivalent node.
     */
    @Test
    public void testEqualsAdditionalEquivalentNode() {
        Node secondEquivalentNode = new Node();
        secondEquivalentNode.setName("Another Equivalent");

        secondTestNode.addParent(parentNode);
        secondTestNode.addChild(childNode);

        secondTestNode.addEquivalentNodes(equivalentNode);
        secondTestNode.addEquivalentNodes(secondEquivalentNode);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different name.
     */
    @Test
    public void testEqualsDifferentName() {
        secondTestNode.setName("I have a different name");

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different weight.
     */
    @Test
    public void testEqualsDifferentWeight() {
        secondTestNode.setWeight(50);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different start time.
     */
    @Test
    public void testEqualsDifferentStartTime() {
        secondTestNode.setStart(38);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different processor.
     */
    @Test
    public void testEqualsDifferentProcessor() {
        secondTestNode.setProcessor(8);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * a different bottom weight.
     */
    @Test
    public void testEqualsDifferentBottomWeight() {
        secondTestNode.setBottomWeight(60);

        assertFalse(testNode.equals(secondTestNode));
    }

    /**
     * This test checks to see if the equals method in Node class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is a Node object with
     * the same characteristics, so the object is equal.
     */
    @Test
    public void testEqualsDifferentEqualObject() {
        secondTestNode.addParent(parentNode);
        secondTestNode.addChild(childNode);
        secondTestNode.addEquivalentNodes(equivalentNode);

        assertTrue(testNode.equals(secondTestNode));
    }

}
