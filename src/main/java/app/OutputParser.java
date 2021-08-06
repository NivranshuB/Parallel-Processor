package app;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Author: Team Untested
 *
 */

public class OutputParser {

    private final String file_extension = ".dot";

    String output_name = null;
    String input_name = "input";

    public OutputParser() {}

    public OutputParser(String output_name){
        this.output_name = output_name;
    }

    public void writeFile() { //Schedule schedule
        String filename = null;
        if (output_name == null) {
            filename = input_name + "-output" + file_extension;
        } else {
            filename = output_name + file_extension;
        }

        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("digraph \"outputExample\" {\n");
//            for (Node n : graph) {
//                writer.write("\t" + n + " \t\t[Weight=" + n.getWeight()
//                                    + ",Start=" + n.getStart()
//                                    + ",Processor=" + n.getProcessor() + "];\n");
//            }
//            for (Edge e : graph) {
//                writer.write("\t" + e.first() + "->" + e.second() + " \t[Weight=" + e.getWeight() + "];\n");
//            }
            writer.write("\ta \t\t[Weight=2,Start=0,Processor=1];\n");
            writer.write("\ta -> b \t[Weight=1];\n");

            writer.write("}");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
