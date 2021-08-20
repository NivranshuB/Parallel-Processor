package app;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController {

    public static Graph g;

    private static MainController mainController = null;

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
    private VBox chart;

    @FXML
    private Label status;

    @FXML
    private Label time;

    @FXML
    private Label bestTime;

    private Config config;

    public void initialize() {

        mainController = this;

        config = Config.getInstance();

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

        System.out.println("or is initialise first?");

//        NumberAxis xAxis = new NumberAxis();
//        CategoryAxis yAxis = new CategoryAxis();
//
//        StackedBarChart<Number, String> sbc = new StackedBarChart<>(xAxis, yAxis);
//
//        XYChart.Series<Number, String> series1 = new XYChart.Series<>();
//        XYChart.Series<Number, String> series2 = new XYChart.Series<>();
//
//        sbc.setTitle("Task allocation gantt chart"); // probably delete this
//        xAxis.setLabel("Time");
//        yAxis.setLabel("Processors");
//
//        int processorCount = config.getNumOfProcessors();
////        String nameArray[] = new String[processorCount];
//        List<String> nameArray = new ArrayList<>();
//
//        for (int i = 0; i < processorCount; i++) {
//            nameArray.add(String.valueOf(i));
//        }
//
//        yAxis.setCategories(FXCollections.observableList(nameArray));
//
//        series1.setName("test");
//
//        sbc.getData().add(series2);
//        sbc.getData().add(series1);
//
//        series1.getData().add(new XYChart.Data<>(3, "1", "testwords"));
//        series2.getData().add(new XYChart.Data<>(2, "1"));
//        XYChart.Data item = series1.getData().get(0);
//
//        Tooltip.install(item.getNode(), new Tooltip("blah"));
//        series2.getData().get(0).getNode().setVisible(false);
//
//        chart.getChildren().add(sbc);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                v.close();
//                v.disableAutoLayout();
            }
        });

        final Timeline[] timeline = new Timeline[1];

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timeline[0] = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    int timeCurrent = 0;

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        time.setText(String.valueOf(timeCurrent) + " seconds");
                        timeCurrent++;
                    }
                }));
                timeline[0].setCycleCount(Timeline.INDEFINITE);
                timeline[0].play();
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

                        timeline[0].stop();
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

    public static MainController getInstance() {
        return mainController;
    }

    public void createGantt(OutputParser op) {

        Map<String, model.Node> nodeMap = op.getNodeMap();

        int processorCount = config.getNumOfProcessors();
//        String nameArray[] = new String[processorCount];
        List<String> nameArray = new ArrayList<>();

        for (int i = 0; i < processorCount; i++) {
            nameArray.add(String.valueOf(i));
        }

        NumberAxis xAxis = new NumberAxis(); // tasks time
        CategoryAxis yAxis = new CategoryAxis(); // processors

        StackedBarChart<Number, String> sbc = new StackedBarChart<>(xAxis, yAxis);

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
        ObservableList<XYChart.Data<Number, String>> invisibleList = FXCollections.observableArrayList();
        XYChart.Series<Number, String> invisibleTask = new XYChart.Series<>();

        for (int i = 0; i < processorCount; i++) {
            int currentTime = 0;
            Iterator<Map.Entry<String, model.Node>> iterator = nodeMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, model.Node> entry = iterator.next();
                model.Node node = entry.getValue();

                if (node.getProcessor() == i) {
                    ObservableList<XYChart.Data<Number, String>> oList = FXCollections.observableArrayList();
                    if (node.getStart() == 0) {
                        oList.add(new XYChart.Data<Number, String>(node.getWeight(), String.valueOf(node.getProcessor())));
                        sbc.getData().add(new XYChart.Series<Number, String>(oList));
                        currentTime = node.getWeight();
                    } else if (node.getStart() == currentTime) {
                        oList.add(new XYChart.Data<Number, String>(node.getStart() + node.getWeight(), String.valueOf(node.getProcessor())));
                        sbc.getData().add(new XYChart.Series<Number, String>(oList));
                        currentTime = node.getStart() + node.getWeight();
                    } else {
                        oList.add(new XYChart.Data<Number, String>(currentTime + node.getStart(), String.valueOf(node.getProcessor())));
//                        invisibleList.add(new XYChart.Data<Number, String>(currentTime + node.getStart(), String.valueOf(node.getProcessor())));
                        XYChart.Series<Number, String> emptyTask = new XYChart.Series<Number, String>(oList);
//                        XYChart.Series<Number, String> emptyTask = new XYChart.Series<Number, String>(invisibleList);
//                        emptyTask.getNode().setVisible(false);
                        sbc.getData().add(emptyTask);
                        ObservableList<XYChart.Data<Number, String>> otherList = FXCollections.observableArrayList();
                        otherList.add(new XYChart.Data<Number, String>(node.getStart() + node.getWeight(), String.valueOf(node.getProcessor())));
                        sbc.getData().add(new XYChart.Series<Number, String>(otherList));
                        currentTime = node.getStart() + node.getWeight();
//                        currentTime = node.getStart();
                    }
                    System.out.println("this is current time: " + currentTime);
                }
            }
        }

//        XYChart.Series<Number, String> series1 = new XYChart.Series<>();
//        XYChart.Series<Number, String> series2 = new XYChart.Series<>();


//        GanttChart<Number, String> ganttChart = new GanttChart<>(xAxis, yAxis);
//
//        String processor = "0";
//        XYChart.Series<Number, String> series1 = new XYChart.Series();
//        series1.getData().add(new XYChart.Data<>(3, processor, 2));

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                ganttChart.getData().add(series1);
//            }
//        });


//        ganttChart.setTitle("Task allocation gantt chart"); // probably delete this
        sbc.setTitle("Task allocation gantt chart"); // probably delete this
        xAxis.setLabel("Time");
        yAxis.setLabel("Processors");
//        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(OutputParser.max);
        System.out.println(OutputParser.max);
//        xAxis.setUpperBound(1000);
//        xAxis.setTickUnit(7.22);
//        xAxis.setAnimated(false);
        xAxis.setTickUnit(1);
        System.out.println(xAxis.tickUnitProperty());

        yAxis.setCategories(FXCollections.observableList(nameArray));

//        series1.setName("test");
//
//        sbc.getData().add(series2);
//        sbc.getData().add(series1);
//        sbc.getData().add(new XYChart.Series<Number, String>());
//        sbc.getData().add(new XYChart.Series<Number, String>());

//        series1.getData().add(new XYChart.Data<>(3, "1", "testwords"));
//        series2.getData().add(new XYChart.Data<>(2, "1"));
//        XYChart.Data item = series1.getData().get(0);

//        Tooltip.install(item.getNode(), new Tooltip("blah"));
//        series2.getData().get(0).getNode().setVisible(false);
//        sbc.getData().add(invisibleList);

//        for (XYChart.Data<Number, String> task : invisibleList) {
//            System.out.println("should be invisilbe" + task.getNode());
////            task.getNode().setVisible(false);
//        }

//        chart.setStyle(".status-blue {" +
//                "-fx-background-color: blue;");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                chart.getChildren().add(ganttChart);
                chart.getChildren().add(sbc);
            }
        });

    }

}
