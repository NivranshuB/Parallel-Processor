import app.DotFileReader;
import model.Edge;
import model.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class is used to unit test the methods in the DotFileReader class.
 */
public class DotFileReaderClassTests {

    private DotFileReader testDotFileReader;

    private File DEFAULT_INPUT_FILE = new File("src\\test\\test_files\\Nodes_7_OutTree.dot");
    private String DEFAULT_GRAPH_NAME = "\"OutTree-Balanced-MaxBf-3_Nodes_7_CCR_2.0_WeightType_Random\"";
    private HashMap<String, Node> DEFAULT_NODE_MAP = new HashMap<String, Node>();
    private HashMap<String, Edge> DEFAULT_EDGE_MAP = new HashMap<String, Edge>();
    private List<Node> DEFAULT_ROOT_NODE_LIST = new ArrayList<Node>();

    /**
     * This sets up the objects required for testing.
     */
    @Before
    public void testSetUp() {
        testDotFileReader = new DotFileReader(DEFAULT_INPUT_FILE);

        //Creating the nodes of the graph
        Node nodeZero = new Node();
        nodeZero.setName("0");
        nodeZero.setWeight(5);

        Node nodeOne = new Node();
        nodeOne.setName("1");
        nodeOne.setWeight(6);

        Node nodeTwo = new Node();
        nodeTwo.setName("2");
        nodeTwo.setWeight(5);

        Node nodeThree = new Node();
        nodeThree.setName("3");
        nodeThree.setWeight(6);

        Node nodeFour = new Node();
        nodeFour.setName("4");
        nodeFour.setWeight(4);

        Node nodeFive = new Node();
        nodeFive.setName("5");
        nodeFive.setWeight(7);

        Node nodeSix = new Node();
        nodeSix.setName("6");
        nodeSix.setWeight(7);

        DEFAULT_NODE_MAP.put("0", nodeZero);
        DEFAULT_NODE_MAP.put("1", nodeOne);
        DEFAULT_NODE_MAP.put("2", nodeTwo);
        DEFAULT_NODE_MAP.put("3", nodeThree);
        DEFAULT_NODE_MAP.put("4", nodeFour);
        DEFAULT_NODE_MAP.put("5", nodeFive);
        DEFAULT_NODE_MAP.put("6", nodeSix);

        //Creating the edges of the graph
        Edge edgeZero = new Edge();
        edgeZero.setParentNode(nodeZero);
        edgeZero.setChildNode(nodeOne);
        edgeZero.setWeight(15);

        Edge edgeOne = new Edge();
        edgeOne.setParentNode(nodeZero);
        edgeOne.setChildNode(nodeTwo);
        edgeOne.setWeight(11);

        Edge edgeTwo = new Edge();
        edgeTwo.setParentNode(nodeZero);
        edgeTwo.setChildNode(nodeThree);
        edgeTwo.setWeight(11);

        Edge edgeThree = new Edge();
        edgeThree.setParentNode(nodeOne);
        edgeThree.setChildNode(nodeFour);
        edgeThree.setWeight(19);

        Edge edgeFour = new Edge();
        edgeFour.setParentNode(nodeOne);
        edgeFour.setChildNode(nodeFive);
        edgeFour.setWeight(4);

        Edge edgeFive = new Edge();
        edgeFive.setParentNode(nodeOne);
        edgeFive.setChildNode(nodeSix);
        edgeFive.setWeight(21);

        DEFAULT_EDGE_MAP.put(nodeZero.getName()+"_"+nodeOne.getName(), edgeZero);
        nodeZero.addChild(nodeOne);
        nodeOne.addParent(nodeZero);

        DEFAULT_EDGE_MAP.put(nodeZero.getName()+"_"+nodeTwo.getName(), edgeOne);
        nodeZero.addChild(nodeTwo);
        nodeTwo.addParent(nodeZero);

        DEFAULT_EDGE_MAP.put(nodeZero.getName()+"_"+nodeThree.getName(), edgeTwo);
        nodeZero.addChild(nodeThree);
        nodeThree.addParent(nodeZero);

        DEFAULT_EDGE_MAP.put(nodeOne.getName()+"_"+nodeFour.getName(), edgeThree);
        nodeOne.addChild(nodeFour);
        nodeFour.addParent(nodeOne);

        DEFAULT_EDGE_MAP.put(nodeOne.getName()+"_"+nodeFive.getName(), edgeFour);
        nodeOne.addChild(nodeFive);
        nodeFive.addParent(nodeOne);

        DEFAULT_EDGE_MAP.put(nodeOne.getName()+"_"+nodeSix.getName(), edgeFive);
        nodeOne.addChild(nodeSix);
        nodeSix.addParent(nodeOne);

        //Creating the root node list
        DEFAULT_ROOT_NODE_LIST.add(nodeZero);

    }

    /**
     * Check that the graph name is read and retrieved correctly.
     */
    @Test
    public void testGetGraphName() {
        String graphNameRetrieved = testDotFileReader.getGraphName();

        assertEquals(DEFAULT_GRAPH_NAME, graphNameRetrieved);
    }

    /**
     * Check that the node map is read and retrieved correctly.
     */
    @Test
    public void testGetNodeMap() {
        HashMap<String, Node> nodeMapRetrieved = testDotFileReader.getNodeMap();

        assertEquals(DEFAULT_NODE_MAP.keySet(), nodeMapRetrieved.keySet());

        //Need to check that every value associated with a key is correct
        int hashMapSize = nodeMapRetrieved.size();

        String[] retrievedKeySet = nodeMapRetrieved.keySet().toArray(new String[0]);
        String[] defaultKeySet = DEFAULT_NODE_MAP.keySet().toArray(new String[0]);

        for (int i = 0; i < hashMapSize; i++) {
            String retrievedKey = retrievedKeySet[i];
            String defaultKey = defaultKeySet[i];

            assertEquals(defaultKey, retrievedKey);
            assertEquals(DEFAULT_NODE_MAP.get(defaultKey), nodeMapRetrieved.get(retrievedKey));
        }
    }

    /**
     * Check that the edge map is read and retrieved correctly.
     */
    @Test
    public void testGetEdgeMap() {
        HashMap<String, Edge> edgeMapRetrieved = testDotFileReader.getEdgeMap();

        assertEquals(DEFAULT_EDGE_MAP.keySet(), edgeMapRetrieved.keySet());

        //Need to check that every value associated with a key is correct
        int hashMapSize = edgeMapRetrieved.size();

        String[] retrievedKeySet = edgeMapRetrieved.keySet().toArray(new String[0]);
        String[] defaultKeySet = DEFAULT_EDGE_MAP.keySet().toArray(new String[0]);

        for (int i = 0; i < hashMapSize; i++) {
            String retrievedKey = retrievedKeySet[i];
            String defaultKey = defaultKeySet[i];

            assertEquals(defaultKey, retrievedKey);
            assertEquals(DEFAULT_EDGE_MAP.get(defaultKey), edgeMapRetrieved.get(retrievedKey));
        }
    }

    /**
     * Check that the root node list is retrieved correctly.
     */
    @Test
    public void testGetRootNodeList() {
        List<Node> retrievedRootNodeList = testDotFileReader.getRootNodeList();

        //Check content of root node list
        if (retrievedRootNodeList.size() == 1) {
            for (int i = 0; i < retrievedRootNodeList.size(); i++) {
                Node defaultRootNode = DEFAULT_ROOT_NODE_LIST.get(0);
                Node retrievedRootNode = retrievedRootNodeList.get(0);

                assertEquals(defaultRootNode, retrievedRootNode);
            }
        } else {
            fail("Should only have one node in root node list");
        }
    }
}
