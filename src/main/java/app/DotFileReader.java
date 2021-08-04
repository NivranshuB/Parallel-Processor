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
 * Utility class used to parse DOT files that represent the tasks to be scheduled.
 */

public class DotFileReader {

    private String filePath;
    private String graphName;
    private List<Node> rootNodeList = new ArrayList<Node>();
    private HashMap<String, Node> nodeMap = new HashMap<String, Node>();
    private HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();

    public DotFileReader(String filePath) {
        this.filePath = filePath;
        parseGraph();
    }

    public String getGraphName() {
        return graphName;
    }

    public String getFilePath() { return filePath; }

    public HashMap<String, Node> getNodeMap() {
        return nodeMap;
    }

    public HashMap<String, Edge> getEdgeMap() {
        return edgeMap;
    }

    public List<Node> getRootNodeList() {
        return rootNodeList;
    }

    private void parseGraph() {
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String currentLine = br.readLine();

            saveGraphName(currentLine);

            while ((currentLine = br.readLine()) != null) {
                //only read lines that contain nodes or edges
               if (currentLine.toLowerCase().contains("weight=")) {
                   //check if the line describes an edge
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

    private void saveGraphName(String str) {
        String[] array = str.split(" ");
        graphName = array[1];
    }

    private void addNode(String str) {
        String[] splitString = str.split("\\s+");
        String nodeName = splitString[1];
        int nodeWeight = parseWeight(str);
        Node newNode = new Node();

        newNode.setWeight(nodeWeight);
        nodeMap.put(nodeName, newNode);
    }

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

    private int parseWeight(String str) {
        Pattern p = Pattern.compile("weight=([0-9]+)");
        Matcher m = p.matcher(str.toLowerCase());
        m.find();
        return Integer.parseInt(m.group().split("=")[1]);
    }

    private void constructRootNodeList() {
        for (Node curr : nodeMap.values()) {
            if (curr.getParent() == null) {
                rootNodeList.add(curr);
            }
        }
    }

}
