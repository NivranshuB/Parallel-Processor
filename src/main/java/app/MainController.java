package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.fx_viewer.util.DefaultApplication;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    public static Graph g;

    @FXML
    private Label numOfTasks;

    @FXML
    private Label numOfProcessors;

    @FXML
    private Label numOfCores;

    @FXML
    private VBox graph;

    @FXML
    private Label status;



    public void initialize() {
        Config config = Config.getInstance();

        int numOfT = config.getNumOfTasks();
        int numOfP = config.getNumOfProcessors();
        int numOfC = config.getNumOfCores();

        numOfTasks.setText(String.valueOf(numOfT));
        numOfProcessors.setText(String.valueOf(numOfP));
        numOfCores.setText(String.valueOf(numOfC));

        System.setProperty("org.graphstream.ui", "javafx");

//        Graph g = new SingleGraph("test");
        g = new SingleGraph("test");

        FxViewer v = new FxViewer(g, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());

        FileSource fs = new FileSourceDOT();

        fs.addSink(g);

        try {
            fs.readAll(config.getInputFile().getName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }

        for (Node node : g) {
            node.setAttribute("ui.label", node.getId());
//            node.setAttribute("ui.class", "marked");
        }

        String styleSheet = "node {" +
                "fill-color: red;" +
                "}" +
                "node.marked {" +
                "fill-color: green;" +
                "}";

        g.setAttribute("ui.stylesheet", styleSheet);


        Stage primaryStage = Main.getPrimaryStage();

        graph.getChildren().add(panel);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                v.close();
//                v.disableAutoLayout();
            }
        });

        // everything below is temporary

        Scheduler scheduler = Scheduler.getInstance();

//        DotFileReader dotFileReader = Main.getDotFileReader();

        scheduler.addChangeListener(evt -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    status.setText("COMPLETE");
                }
            });

        });

//        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());
//
//        String graphName = dotFileReader.getGraphName();
//
//        //Parses the optimal schedule to the output DOT file
//        OutputParser op = new OutputParser(graphName, config, optimalSchedule);
//
//        op.writeFile();




    }

    public static Graph getVizGraph() {
        return g;
    }

    public void setComplete() {
        status.setText("COMPLETE");
    }
    
}
