import model.Edge;
import model.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class is used to unit test the methods in the Edge class.
 */
public class EdgeClassTests {

    private Edge testEdge;
    private static Node parentNode;
    private static Node childNode;
    private static final int EDGE_WEIGHT = 6;

    private static Node newParentNode;
    private static Node newChildNode;
    private static final int NEW_EDGE_WEIGHT = 10;

    /**
     * This sets up an Edge object for testing.
     */
    @Before
    public void testSetUp() {
        parentNode = new Node();
        childNode = new Node();

        parentNode.setName("Parent 1");
        childNode.setName("Child 1");

        newParentNode = new Node();
        newChildNode = new Node();

        newParentNode.setName("Parent 2");
        newChildNode.setName("Child 2");

        testEdge = new Edge();
        testEdge.setParentNode(parentNode);
        testEdge.setChildNode(childNode);
        testEdge.setWeight(EDGE_WEIGHT);


    }

    /**
     * This test checks to see if the correct parent node of the edge is returned.
     */
    @Test
    public void testGetParentNode() {
        assertSame(parentNode, testEdge.getParentNode());
    }

    /**
     * This test checks to see if a parent node can be correctly set on the Edge object.
     */
    @Test
    public void testSetParentNode() {
        Node newParentNode = new Node();

        testEdge.setParentNode(newParentNode);

        assertSame(newParentNode, testEdge.getParentNode());
    }

    /**
     * This test checks to see if the correct child node of the edge is returned.
     */
    @Test
    public void testGetChildNode() {
        assertSame(childNode, testEdge.getChildNode());
    }

    /**
     * This test checks to see if a child node can be correctly set on the Edge object.
     */
    @Test
    public void testSetChildNode() {
        Node newChildNode = new Node();

        testEdge.setChildNode(newChildNode);

        assertSame(newChildNode, testEdge.getChildNode());
    }

    /**
     * This test checks to see if the correct weight for the edge is returned.
     */
    @Test
    public void testGetWeight() {
        assertEquals(EDGE_WEIGHT, testEdge.getWeight());
    }

    /**
     * This test checks to see if a new weight can be correctly set on the edge.
     */
    @Test
    public void testSetWeight() {
        testEdge.setWeight(NEW_EDGE_WEIGHT);

        assertEquals(NEW_EDGE_WEIGHT, testEdge.getWeight());
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is exactly the same
     * object it is being compared to.
     */
    @Test
    public void testEqualsSameObject() {
        assertTrue(testEdge.equals(testEdge));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is not an Edge object.
     */
    @Test
    public void testEqualsNotEdgeObject() {
        assertFalse(testEdge.equals(new Node()));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is an Edge with a
     * different parent.
     */
    @Test
    public void testEqualsDifferentParentEdge() {
        Edge secondTestEdge = new Edge();

        secondTestEdge.setParentNode(newParentNode);
        secondTestEdge.setChildNode(childNode);
        secondTestEdge.setWeight(EDGE_WEIGHT);

        assertFalse(testEdge.equals(secondTestEdge));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is an Edge with a
     * different child.
     */
    @Test
    public void testEqualsDifferentChildEdge() {
        Edge secondTestEdge = new Edge();

        secondTestEdge.setParentNode(parentNode);
        secondTestEdge.setChildNode(newChildNode);
        secondTestEdge.setWeight(EDGE_WEIGHT);

        assertFalse(testEdge.equals(secondTestEdge));
    }

    /**
     * This test checks to see if the equals method in Edge class returns the correct boolean when
     * an object to be compared is passed in. In this case, the object passed in is an Edge with a
     * different weight.
     */
    @Test
    public void testEqualsDifferentWeightEdge() {
        Edge secondTestEdge = new Edge();

        secondTestEdge.setParentNode(parentNode);
        secondTestEdge.setChildNode(childNode);
        secondTestEdge.setWeight(NEW_EDGE_WEIGHT);

        assertFalse(testEdge.equals(secondTestEdge));
    }
}
