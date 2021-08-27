import model.Edge;
import model.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * This class is used to unit test the methods in the Edge class.
 */
public class EdgeClassTests {

    private Edge testEdge;
    private static Node parentNode;
    private static Node childNode;
    private static final int EDGE_WEIGHT = 6;
    private static final int NEW_EDGE_WEIGHT = 10;

    /**
     * This sets up an Edge object for testing.
     */
    @Before
    public void testSetUp() {
        parentNode = new Node();
        childNode = new Node();

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
}
