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

    private Config config;
    private Schedule schedule;
    private String graphName;

    /**
     * Constructs the OutputParser object used to create an output graph DOT file.
     * @param graphName Name of the output graph.
     * @param config Config object with output file args.
     * @param schedule Schedule to output.
     */
    public OutputParser(String graphName, Config config, Schedule schedule) {
        this.graphName = graphName.replaceAll("\"",""); // removes quotaion marks from graph name
        this.graphName = this.graphName + "-output";
        this.config = config;
        this.schedule = schedule;
    }

    /**
     * Writes the output graph file in the required DOT format.
     */
    public void writeFile() {

        try {
            FileWriter writer = new FileWriter(config.getOutputFile());
            writer.write("digraph \"" + graphName + "\" {\n");

            HashMap<String, Node> nodeMap = schedule.nodeMap;

            Iterator<Map.Entry<String, Node>> iterator = nodeMap.entrySet().iterator();

            int criticalPath = 0;

            while (iterator.hasNext()) {
                Map.Entry<String, Node> entry = iterator.next();
                String key = entry.getKey();
                Node node = entry.getValue();
                int start = 0;
                int processor = 0;
                for (Processor p : schedule.processorList) {
                    int potentialCriticalPath = p.finishTime;
                    if (potentialCriticalPath > criticalPath) {
                        criticalPath = potentialCriticalPath;
                    }
                    int timeCount = 0;
                    for (Node n : p.getTaskOrder()) {
                        System.out.println(n + " in processor: " + p + " weight: " + n.getWeight());
                        System.out.println("timeCount: " + timeCount);
                        if (node.getName() == n.getName()) {
                            System.out.println("this is start inside all the loops: " + start);
                            start = timeCount;
                            processor = schedule.processorList.indexOf(p);
                        } else {
                            timeCount += n.getWeight();
                        }
                    }
                }
                writer.write("\t" + key + "\t [Weight=" + node.getWeight() + ",Start=" + start + ",Processor=" + processor + "];\n");
            }

            System.out.println("Critical path: " + criticalPath);

            HashMap<String, Edge> edgeMap = schedule.edgeMap;

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
