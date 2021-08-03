package app;

import java.io.FileWriter;
import java.io.IOException;

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
        String name = null;
        if (output_name == null) {
            name = input_name + "-output";
        } else {
            name = output_name;
        }
        filename = name + file_extension;

        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("digraph \"" + name + "\" {\n");

            // to be used when class structure is sorted
//            for (Node n : graph) {
//                writer.write("\t" + n + " \t\t[Weight=" + n.getWeight()
//                                    + ",Start=" + n.getStart()
//                                    + ",Processor=" + n.getProcessor() + "];\n");
//            }
//            for (Edge e : graph) {
//                writer.write("\t" + e.first() + "->" + e.second() + " \t[Weight=" + e.getWeight() + "];\n");
//            }

            //test output - will delete later
            writer.write("\ta \t\t[Weight=2,Start=0,Processor=1];\n");
            writer.write("\ta -> b \t[Weight=1];\n");

            writer.write("}");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
