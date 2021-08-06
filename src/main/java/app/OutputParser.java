package app;

import model.Edge;
import model.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: Team Untested
 * Utility class used to output graph representing task schedules in DOT format
 */

public class OutputParser {

    private final String file_extension = ".dot";

    private String output_name = null;
    private String input_name;
    private DotFileReader dfr;
    private Boolean newName = false;

    public OutputParser() {}

    public OutputParser(String output_name){
        this.output_name = output_name;
    } // delete?

    public OutputParser(DotFileReader dfr) { this.dfr = dfr; }

    public void writeFile() {

        input_name = dfr.getFilePath().split("\\.")[0];

        String graphName = dfr.getGraphName();
        graphName = graphName.replaceAll("\"",""); // removes quotaion marks from graph name
        graphName = graphName + "-output";

        String name = null;
        if (!newName) { // if a new name from the CL args is present or not
            name = input_name + "-output"; // default output name
        } else {
            name = input_name;
        }
        String filename = name + file_extension;

        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("digraph \"" + graphName + "\" {\n");

            HashMap<String, Node> nodeMap = dfr.getNodeMap();

            Iterator<Map.Entry<String, Node>> iterator = nodeMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Node> entry = iterator.next();
                String key = entry.getKey();
                Node node = entry.getValue();
                writer.write("\t" + key + "\t [Weight=" + node.getWeight() + ",Start=" + node.getStart() + ",Processor=" + node.getProcessor() + "];\n");
            }

            HashMap<String, Edge> edgeMap = dfr.getEdgeMap();

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
}
