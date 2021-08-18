package app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController {

    public static Graph g;

    @FXML
    private OutputParser outputParser;

    @FXML
    private Main main;

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

    @FXML
    private Label time;

    @FXML
    private Label bestTime;


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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
//                        System.out.println("called every 1 second");
                        final String timeNow = timeFormat.format(new Date());
//                        time.setText(timeNow);
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });



        // everything below is temporary

        DotFileReader dotFileReader = Main.getDotFileReader();

//        Scheduler scheduler = Scheduler.getInstance();
        BnBScheduler scheduler = BnBScheduler.getInstance(dotFileReader, config);

        scheduler.addChangeListener(evt -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (evt.getPropertyName().equals("update node")) {
                        model.Node searchedNode = (model.Node) evt.getNewValue();
                        for (org.graphstream.graph.Node vizNode : MainController.getVizGraph()) {
                            if (searchedNode.getName().equals(vizNode.getId())) {
                                vizNode.setAttribute("ui.class", "marked");
                                try {
//                                  Thread.sleep(1);
                                } catch (Exception e) {
                                }
                            }
                        }

                    } else {
                        status.setText("COMPLETE");

                        bestTime.setText(String.valueOf(evt.getNewValue()));
                    }
                }
            });

        });

        //temp viz that delays but updates node to green from red
//        for (org.graphstream.graph.Node vizNode : MainController.getVizGraph()) {
//            if (n.getName().equals(vizNode.getId())) {
//                vizNode.setAttribute("ui.class", "marked");
//                try {
////                    Thread.sleep(1);
//                } catch (Exception e){
//                }
//            }
//        }

//        DotFileReader dotFileReader = Main.getDotFileReader();
//
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
