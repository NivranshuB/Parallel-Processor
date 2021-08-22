package app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    private static final int UPDATE_INTERVAL = 1000;

    public static Graph g;

    private static MainController mainController = null;

    private Config config;

    private Scheduler scheduler;

    private final Timeline[] timeline = new Timeline[1];

    @FXML
    private Label numOfTasks;
    
    @FXML
    private Label currentBest;

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

    @FXML
    private VBox memory;

    private XYChart.Series<Number, Number> memorySeries = new XYChart.Series<>();

    private double memoryStartTime;

    @FXML
    private Thread Monitor;

    @FXML
    private VBox cpu;
    private LineChart<Number, Number> cpuChart;
    private List<XYChart.Series<Number, Number>> cpuSeries = new ArrayList<>();
    private double cpuStartTime;

    private int processorCount = 0;

    private NumberAxis xAxis;

    private CategoryAxis yAxis;

    private List<String> nameArray;

    private StackedBarChart<Number, String> sbc;

    public void initialize() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() { // Duration.seconds(1)
                timeline[0] = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                    int timeCurrent = 0;

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        int milliseconds = timeCurrent % 1000;
                        int seconds = timeCurrent / 1000;
                        time.setText(seconds + "." + String.format("%03d", milliseconds));
                        timeCurrent++;
                    }
                }));
                timeline[0].setCycleCount(Timeline.INDEFINITE);
                timeline[0].play();
            }
        });

        mainController = this;

        initialiseMemory();
        initialiseCPU();
        config = Config.getInstance();
        
        int numOfT = config.getNumOfTasks();
        int numOfP = config.getNumOfProcessors();
        int numOfC = config.getNumOfCores();

        numOfTasks.setText(String.valueOf(numOfT));
        numOfProcessors.setText(String.valueOf(numOfP));
        numOfCores.setText(String.valueOf(numOfC));
        currentBest.setStyle("-fx-font-size: 17;");
        System.setProperty("org.graphstream.ui", "javafx");

        Graph g = new SingleGraph("test");

        FxViewer v = new FxViewer(g, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());

        FileSource fs = new FileSourceDOT();

        fs.addSink(g);

        try {
            fs.readAll(config.getInputFile().getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }

        for (Node node : g) {
            node.setAttribute("ui.label", node.getId());
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

        processorCount = config.getNumOfProcessors();
        nameArray = new ArrayList<>();

        for (int i = 0; i < processorCount; i++) {
            nameArray.add(String.valueOf(i));
        }

        xAxis = new NumberAxis(); // tasks time
        yAxis = new CategoryAxis(); // processors

        sbc = new StackedBarChart<>(xAxis, yAxis);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chart.getChildren().add(sbc);
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                v.close();
            }
        });

        Monitor = new Thread(new MemoryInfo(this, UPDATE_INTERVAL));
        Monitor.start();
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
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

    public void createGantt(List<model.Node> nodeList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sbc.getData().clear();

                ObservableList<XYChart.Data<Number, String>> invisibleList = FXCollections.observableArrayList();

                for (int i = 0; i < processorCount; i++) {
                    int currentTime = 0;
                    for (model.Node node : nodeList) {
                        if (node.getProcessor() == i) {
                            ObservableList<XYChart.Data<Number, String>> oList = FXCollections.observableArrayList();
                            XYChart.Data<Number, String> taskData;
                            if (node.getStart() == 0) { // if task starts at time = 0
                                taskData = new XYChart.Data<>(node.getWeight(), String.valueOf(node.getProcessor()));
                                oList.add(taskData);
                                sbc.getData().add(new XYChart.Series<>(oList));
                                currentTime = node.getWeight();
                            } else {
                                ObservableList<XYChart.Data<Number, String>> otherList = FXCollections.observableArrayList();
                                if (currentTime == 0 && node.getStart() != 0) { // if task is first task on processor when time != 0
                                    XYChart.Data<Number, String> data = new XYChart.Data<>(node.getStart(), String.valueOf(node.getProcessor()));
                                    oList.add(data);
                                    invisibleList.add(data);
                                    XYChart.Series<Number, String> emptyTask = new XYChart.Series<>(oList);
                                    sbc.getData().add(emptyTask);
                                } else if (node.getStart() != currentTime) { // if task is not first task on processor and time != 0
                                    XYChart.Data<Number, String> data = new XYChart.Data<>(node.getStart() - currentTime, String.valueOf(node.getProcessor()));
                                    oList.add(data);
                                    invisibleList.add(data);
                                    XYChart.Series<Number, String> emptyTask = new XYChart.Series<>(oList);
                                    sbc.getData().add(emptyTask);
                                }
                                // used for above two or if task starts immediately after the previous
                                taskData = new XYChart.Data<>(node.getWeight(), String.valueOf(node.getProcessor()));
                                otherList.add(taskData);
                                sbc.getData().add(new XYChart.Series<>(otherList));
                                currentTime = node.getStart() + node.getWeight();
                            }
                            Tooltip tooltip = new Tooltip("Task: " + node.getName() + "\n" + "Weight: " + node.getWeight() + "\n" + "Start time: " + node.getStart());
                            tooltip.setShowDelay(Duration.seconds(0));
                            Tooltip.install(taskData.getNode(), tooltip);
                        }
                    }
                }

                xAxis.setLabel("Time");
                yAxis.setLabel("Processors");
                xAxis.setAnimated(false);
//                xAxis.setLowerBound(0);
//                xAxis.setUpperBound(OutputParser.max);
                xAxis.setTickUnit(1);
                yAxis.setCategories(FXCollections.observableList(nameArray));
                sbc.setLegendVisible(false);

                for (XYChart.Data<Number, String> task : invisibleList) {
                    task.getNode().setVisible(false);
                }
            }
        });
    }

    public void addListener() {
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

                    }
                    if (evt.getPropertyName().equals("update progress")) {
                        bestTime.setText(String.valueOf(evt.getNewValue()));
                    } else {
                        status.setText("COMPLETE");

                        bestTime.setText(String.valueOf(evt.getNewValue()));

                        timeline[0].stop();
                    }
                }
            });

        });
    }

    public void updateMemory(double ramUsage) {
        // update memory graph
        if (this.memoryStartTime < 1) {
            memoryStartTime = System.currentTimeMillis();
        }
        double timeElapsed = (System.currentTimeMillis() - this.memoryStartTime) / 1000;

        // update ram usage
        Platform.runLater(() -> {
            this.memorySeries.getData().add(new XYChart.Data<>(timeElapsed, ramUsage / (1024 * 1024)));
        });
    }

    private void initialiseMemory() {
        // initialise memory view
        NumberAxis memoryXAxis = new NumberAxis();
        memoryXAxis.setLabel("Time (s)");
        NumberAxis memoryYAxis = new NumberAxis();
        memoryYAxis.setLabel("Memory Usage (Mb)");
        LineChart<Number, Number> memoryChart = new LineChart<>(memoryXAxis, memoryYAxis);

        memoryChart.getData().add(memorySeries);
        memoryChart.setLegendVisible(false);
        this.memory.getChildren().add(memoryChart);
        memoryChart.prefWidthProperty().bind(this.memory.widthProperty());
        memoryChart.prefHeightProperty().bind(this.memory.heightProperty());
    }

    public void updateCPU(List<Double> perCoreUsage) {

        Platform.runLater(() -> {

            for (int i = 0; i < perCoreUsage.size(); i++) {

                if (this.cpuStartTime < 1) {
                    this.cpuStartTime = System.currentTimeMillis();
                }
                double timeElapsed = (System.currentTimeMillis() - this.cpuStartTime) / 1000;

                if (this.cpuSeries.size() <= i) {
                    XYChart.Series<Number, Number> trend = new XYChart.Series<>();
                    this.cpuChart.getData().add(trend);

                    this.cpuSeries.add(trend);
                }

                this.cpuSeries.get(i).getData().add(new XYChart.Data<>(timeElapsed, perCoreUsage.get(i) * 100));
            }
        });
    }

    private void initialiseCPU() {
        NumberAxis cpuXAxis = new NumberAxis();
        cpuXAxis.setLabel("Time (s)");
        NumberAxis cpuYAxis = new NumberAxis();
        cpuYAxis.setLabel("CPU Usage %");
        cpuYAxis.setForceZeroInRange(true);
        cpuYAxis.setAutoRanging(false);
        cpuYAxis.setUpperBound(100);
        cpuYAxis.setLowerBound(0);

        cpuChart = new LineChart<>(cpuXAxis, cpuYAxis);
        cpuChart.setLegendVisible(false);
        this.cpu.getChildren().add(cpuChart);
        cpuChart.prefWidthProperty().bind(this.cpu.widthProperty());
        cpuChart.prefHeightProperty().bind(this.cpu.heightProperty());
    }


}