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
        if (output_name == null) {
            filename = input_name + "-output" + file_extension;
        } else {
            filename = output_name + file_extension;
        }

        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("test");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
