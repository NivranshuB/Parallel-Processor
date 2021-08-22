package app;

import model.Edge;
import model.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Author: Team UNTESTED
 * Utility class used to output graph representing task schedules in DOT format
 */

public class OutputParser {

    private Config config;
    private String graphName;
    private BnBSchedule bnbschedule;
    private DotFileReader dotFileReader;

    public static int max = 0;

    private List<Node> nodeList;

    /**
     * Constructs the OutputParser object used to create an output graph DOT file.
     * @param config    Config object with output file args.
     * @param schedule  Schedule to output.
     * @param dotFileReader DotFileReader object with graph information.
     */
    public OutputParser(Config config, BnBSchedule schedule, DotFileReader dotFileReader) {
        // removes quotation marks from graph name
        this.graphName = dotFileReader.getGraphName().replaceAll("\"", "");
        this.graphName = this.graphName + "-output";
        this.config = config;
        this.bnbschedule = schedule;
        this.dotFileReader = dotFileReader;
    }

    /**
     * Writes the output graph file in the required DOT format.
     */
    public void writeFile() {

        try {
            FileWriter writer = new FileWriter(config.getOutputFile());
            writer.write("digraph \"" + graphName + "\" {\n");

            nodeList = bnbschedule.getNodeList();

            for (Node n : nodeList) {
                String nodeName = n.getName();
                int nodeWeight = n.getWeight();
                int nodeStart = n.getStart();
                int nodeProcessor = n.getProcessor();
                writer.write("\t" + nodeName + "\t [Weight=" + nodeWeight + ",Start=" + nodeStart + ",Processor=" + nodeProcessor + "];\n");
            }

            max = bnbschedule.getWeight();

            HashMap<String, Edge> edgeMap = dotFileReader.getEdgeMap();

            Iterator<Map.Entry<String, Edge>> edgeIterate = edgeMap.entrySet().iterator();

            while (edgeIterate.hasNext()) {
                Map.Entry<String, Edge> entry = edgeIterate.next();
                String[] keys = entry.getKey().split("_");
                String parent = keys[0];
                String child = keys[1];
                Edge edge = entry.getValue();
                writer.write("\t" + parent + " -> " + child + "\t [Weight=" + edge.getWeight() + "];\n");
            }

            writer.write("}");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the node list in ths OutputParser object.
     * @return List of scheduled nodes.
     */
    public List<Node> getNodeList() {
        return nodeList;
    }
}
