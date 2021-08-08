package app;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Edge;
import model.Node;

/**
 * Author: Team Untested
 * Utility class used to parse DOT files that represent the tasks to be scheduled.
 */

public class DotFileReader {

    private File filename;
    private String graphName;
    private List<Node> rootNodeList = new ArrayList<Node>();
    private HashMap<String, Node> nodeMap = new HashMap<String, Node>();
    private HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();

    /**
     * Constructs a DotFileReader object with the DOT file provided.
     * @param filename DOT file to be read.
     */
    public DotFileReader(File filename) {
        this.filename = filename;
        parseGraph();
    }

    /**
     * Gets the name of the task graph.
     * @return Name of task graph.
     */
    public String getGraphName() {
        return graphName;
    }

    /**
     * Gets the mapping of all nodes in the task graph.
     * @return Mapping of all nodes in the task graph.
     */
    public HashMap<String, Node> getNodeMap() {
        return nodeMap;
    }

    /**
     * Gets the mapping of all edges in the task graph.
     * @return Mapping of all edges in the task graph.
     */
    public HashMap<String, Edge> getEdgeMap() {
        return edgeMap;
    }

    /**
     * Gets a list of all the root nodes in the task graph.
     * @return List of root nodes in the task graph.
     */
    public List<Node> getRootNodeList() {
        return rootNodeList;
    }

    /**
     * Parses the graph from reading of the input task graph DOT file.
     */
    private void parseGraph() {
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String currentLine = br.readLine();

            saveGraphName(currentLine);

            while ((currentLine = br.readLine()) != null) {
                //Only read lines that contain nodes or edges
               if (currentLine.toLowerCase().contains("weight=")) {
                   //Check if the line describes an edge
                   if (currentLine.toLowerCase().contains("->")) {
                       addEdge(currentLine);
                   } else {
                       addNode(currentLine);
                   }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the task graph name as a String for future use.
     * @param str Name of task graph to save.
     */
    private void saveGraphName(String str) {
        String[] array = str.split(" ");
        graphName = array[1];
    }

    /**
     * Adds a node to the node mapping of the task graph.
     * @param str Node in String that is to be added.
     */
    private void addNode(String str) {
        String[] splitString = str.split("\\s+");
        String nodeName = splitString[1];
        int nodeWeight = parseWeight(str);
        Node newNode = new Node();

        newNode.setName(nodeName);
        newNode.setWeight(nodeWeight);
        nodeMap.put(nodeName, newNode);
    }

    /**
     * Adds an edge to the edge mapping of the task graph.
     * @param str Edge in String that is to be added.
     */
    private void addEdge(String str){
        String[] splitString = str.split("\\s+");
        String parentName = splitString[1];
        Node parentNode = nodeMap.get(parentName);
        String childName = splitString[3];
        Node childNode = nodeMap.get(childName);
        int edgeWeight = parseWeight(str);

        Edge newEdge = new Edge();
        newEdge.setParentNode(parentNode);
        newEdge.setChildNode(childNode);
        newEdge.setWeight(edgeWeight);
        edgeMap.put(parentName+"_"+childName, newEdge);

        parentNode.addChild(childNode);
        childNode.addParent(parentNode);
    }

    /**
     * Parsing the weight that is associated with a node or edge to an integer form.
     * @param str Weight in String that is to be parsed.
     * @return Weight of node or edge in integer form.
     */
    private int parseWeight(String str) {
        Pattern p = Pattern.compile("weight=([0-9]+)");
        Matcher m = p.matcher(str.toLowerCase());
        m.find();
        return Integer.parseInt(m.group().split("=")[1]);
    }

    /**
     * Constructs the root node list for the task graph.
     */
    private void constructRootNodeList() {
        for (Node curr : nodeMap.values()) {
            if (curr.getParent() == null) {
                rootNodeList.add(curr);
            }
        }
    }

}
