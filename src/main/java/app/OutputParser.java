package app;

import model.Edge;
import model.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Author: Team Untested
 * Utility class used to output graph representing task schedules in DOT format
 */

public class OutputParser {

    private Config config;
    private Schedule schedule;
    private String graphName;
    private BnBSchedule bnbschedule;
    private BnBScheduler bnBScheduler;

    public static int max = 0;

    private Map<String, Node> nodeMap;

    /**
     * Constructs the OutputParser object used to create an output graph DOT file.
     *
     * @param graphName Name of the output graph.
     * @param config    Config object with output file args.
     * @param schedule  Schedule to output.
     */

    public OutputParser(String graphName, Config config, BnBSchedule schedule, BnBScheduler scheduler) {
        this.graphName = graphName.replaceAll("\"", ""); // removes quotation marks from graph name
        this.graphName = this.graphName + "-output";
        this.config = config;
        this.bnbschedule = schedule;
        this.bnBScheduler = scheduler;
    }

    /**
     * Writes the output graph file in the required DOT format.
     */
    public void writeFile() {

        try {
            FileWriter writer = new FileWriter(config.getOutputFile());
            writer.write("digraph \"" + graphName + "\" {\n");

            nodeMap = bnbschedule.getNodeMap();

            List<String> stringSchedule = bnbschedule.getStringStorage();

            int processorCount = 0;
            int criticalPath = 0;
            int start = 0;

            for (String string : stringSchedule) {
                String[] stringArray;

                stringArray = string.split(",");
                for (String splitString : stringArray) {
                    String[] attributes = splitString.split("-");

                    if (attributes.length >= 2) {
                        Iterator<Map.Entry<String, Node>> iterator = nodeMap.entrySet().iterator();

                        while (iterator.hasNext()) {
                            Map.Entry<String, Node> entry = iterator.next();
                            Node node = entry.getValue();
                            if (node.getName().equals(attributes[1])) {
                                start = Integer.parseInt(attributes[0].replaceAll("\\s+",""));
                                int potentialCriticalPath = start + node.getWeight();
                                if (potentialCriticalPath > criticalPath) {
                                    criticalPath = potentialCriticalPath;
                                }
                                node.setProcessor(processorCount);
                                node.setStart(start);
//                                System.out.println("node: " + attributes[1] + " " + " " + attributes[0] + " " + processorCount);
                                writer.write("\t" + attributes[1] + "\t [Weight=" + node.getWeight() + ",Start=" + start + ",Processor=" + processorCount + "];\n");
                            }
                        }
                    }
                }
                processorCount++;
            }

            System.out.println("Critical path = " + criticalPath);

//            List<Processor> processorList = bnBScheduler.getListOfProcessors();


            max = criticalPath;

//            HashMap<String, Edge> edgeMap = schedule.edgeMap;

            HashMap<String, Edge> edgeMap = bnBScheduler.getEdgeMap();


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

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }
}
