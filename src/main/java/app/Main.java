package app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Author: Team UNTESTED
 * Main method where the implementation of the program begins.
 */
public class Main extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        //Parses command line input arguments to extract the number of processors, DOT file and other args

        ArgumentParser parser = new ArgumentParser();
        Config config;

        try {
            config = parser.parse(args);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to parse arguments " + e.getMessage());
            return;
        }

        Scheduler scheduler = Scheduler.getInstance();

        //Uses the DOT filepath from the args to create a new DotFileReader object/graph representation
        DotFileReader dotFileReader = new DotFileReader(config.getInputFile());

        launch(args);

        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());

        String graphName = dotFileReader.getGraphName();

        //Parses the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(graphName, config, optimalSchedule);

        op.writeFile();
    }

    @Override
    public void start(Stage arg0) throws Exception {
        primaryStage = arg0;
        URL url = Paths.get("./src/main/java/app/Main.fxml").toUri().toURL();
        Parent root = FXMLLoader.load(url);
//		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        arg0.setTitle("Task Scheduler");
        arg0.setScene(new Scene(root));
        arg0.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
