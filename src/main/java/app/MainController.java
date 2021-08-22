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
import javafx.scene.layout.HBox;
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

	public Graph sg;

	private static MainController mainController = null;

	private Config config;

	private Scheduler scheduler;

	private final Timeline[] timeline = new Timeline[1];

	private int nodeCounter = 0;
	private int edgeCounter = 0;

	private final String[] COLORS = {"fill-color: rgb(41,168,41);",
			"fill-color: rgb(125,191,255);",
			"fill-color: rgb(80,60,240);",
			"fill-color: rgb(0,255,0);",
			"fill-color: rgb(255,0,0);",
			"fill-color: rgb(107,107,255);",
			"fill-color: rgb(30,124,120);",
			"fill-color: rgb(215,69,152);",
	"fill-color: rgb(107,255,221);"};

	private List<String> lastOptimalNode = new ArrayList<>();

	@FXML
	private Label numOfTasks;

	@FXML
	private Label currentBest;

	@FXML
	private Label numOfProcessors;

	@FXML
	private Label numOfCores;

	@FXML
	private VBox n_graph;

	@FXML
	private VBox o_graph;

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

	private static final String GraphstreamStyleSheet = "" +
			"node {" +
			"fill-color: red;" +
			"text-offset: 10;" +
			"text-size: 16;" +
			"size-mode: dyn-size;" +
			"text-color: white;" +
			"}" +
			"node.marked {" +
			"fill-color: green;" +
			"}" +
			"graph {" +
			"fill-color: rgb(2, 4, 16), rgb(5, 21, 34);" +
			"fill-mode: gradient-vertical;" +
			"}" +
			"edge {" +
			"fill-color: white;" +
			"}";

	public void initialize() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
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


        g.setAttribute("ui.stylesheet", GraphstreamStyleSheet);

        Stage primaryStage = Main.getPrimaryStage();

        n_graph.getChildren().add(panel);

        initialiseScheduleGraph();

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
        
        // Start the memory info monitor in another thread
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
                            // if task starts at time = 0
                            if (node.getStart() == 0) {
                                taskData = new XYChart.Data<>(node.getWeight(), String.valueOf(node.getProcessor()));
                                oList.add(taskData);
                                sbc.getData().add(new XYChart.Series<>(oList));
                                currentTime = node.getWeight();
                            } else {
                                ObservableList<XYChart.Data<Number, String>> otherList = FXCollections.observableArrayList();
                                // if task is first task on processor when time != 0
                                if (currentTime == 0 && node.getStart() != 0) {
                                    XYChart.Data<Number, String> data = new XYChart.Data<>(node.getStart(), String.valueOf(node.getProcessor()));
                                    oList.add(data);
                                    invisibleList.add(data);
                                    XYChart.Series<Number, String> emptyTask = new XYChart.Series<>(oList);
                                    sbc.getData().add(emptyTask);
                                // if task is not first task on processor and time != 0
                                } else if (node.getStart() != currentTime) {
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
                    if (evt.getPropertyName().equals("update progress")) {
                        // checks if multi-threading
                        if (Integer.parseInt(numOfCores.getText()) > 1) {
                            boolean newOptimal = true;
                            // looks through potential optimal schedule of each thread
                            for (String n : lastOptimalNode) {
                                n = n.split("_")[0];
                                // checks that new value is less than all threads
                                if (Integer.parseInt(n) < (int) (evt.getNewValue())) {
                                    newOptimal = false;
                                }
                            }
                            if (newOptimal) { // if true, updates value
                                bestTime.setText(String.valueOf(evt.getNewValue()));
                            }
                        } else { // always updates value if single threaded
                            bestTime.setText(String.valueOf(evt.getNewValue()));
                        }
                        // runs when final optimal schedule is found
                    } else if (evt.getPropertyName().equals("optimal schedule")) {
                        status.setText("COMPLETE");
                        bestTime.setText(String.valueOf(evt.getNewValue()));
                        timeline[0].stop();
                    }
                }
            });

        });
    }

    /**
	 * Update the memory graph using the memory usage data and time elapsed.
	 * @param memoryUsage The total memory currently in use.
	 */
	public void updateMemory(double memoryUsage) {
		// store starting time
		if (this.memoryStartTime < 1) {
			memoryStartTime = System.currentTimeMillis();
		}
		double timeElapsed = (System.currentTimeMillis() - this.memoryStartTime) / 1000;

		// update memory usage and time axis
		Platform.runLater(() -> {
			this.memorySeries.getData().add(new XYChart.Data<>(timeElapsed, memoryUsage / (1024 * 1024)));
		});
	}

	/**
	 * Create a blank memory graph with the appropriate axis and sizing.
	 */
	private void initialiseMemory() {

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
	
	/**
	 * Update the CPU graph using the CPU usage data for each core and time elapsed.
	 * @param perCoreUsage A list containing the CPU usage for each core in use.
	 */
	public void updateCPU(List<Double> perCoreUsage) {

		Platform.runLater(() -> {
			for (int i = 0; i < perCoreUsage.size(); i++) {
				// store starting time
				if (this.cpuStartTime < 1) {
					this.cpuStartTime = System.currentTimeMillis();
				}
				double timeElapsed = (System.currentTimeMillis() - this.cpuStartTime) / 1000;
				// add the correct number of cpu lines to graph depending on number of cores
				if (this.cpuSeries.size() <= i) {
					XYChart.Series<Number, Number> trend = new XYChart.Series<>();
					this.cpuChart.getData().add(trend);
					this.cpuSeries.add(trend);
				}
				// update cpu usage and time axis
				this.cpuSeries.get(i).getData().add(new XYChart.Data<>(timeElapsed, perCoreUsage.get(i) * 100));
			}
		});
	}

	/**
	 * Create a blank CPU graph with the appropriate axis and sizing.
	 */
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

    private void initialiseScheduleGraph() {
        sg = new SingleGraph("test_optimals");
        sg.setStrict(true);

        FxViewer v = new FxViewer(sg, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());

        sg.addNode("root");
        sg.getNode("root").setAttribute("ui.class", "root");

        String styleSheet = "node {" +
                "size: 35px, 20px;" +
                "shape: box;" +
                "stroke-color: rgb(2, 4, 16);" +
                "stroke-mode: plain;" +
                "stroke-width: 1px;" +
                "}" +
                "node.marked {" +
                "stroke-color: yellow;" +
                "stroke-width: 4px;" +
                "stroke-mode: plain;" +
                "z-index: 4;" +
                "text-size: 12;" +
                "size: 40px, 25px;" +
                "}" +
                "node.root {" +
                "fill-color: grey;" +
                "shape: circle;" +
                "size: 20px;" +
                "}" +
                "graph {fill-color: rgb(2, 4, 16), rgb(5, 21, 34);" +
                "fill-mode: gradient-vertical;" +
                "}" +
                "edge {fill-color: grey;}";

        sg.setAttribute("ui.stylesheet", styleSheet);

        Stage primaryStage = Main.getPrimaryStage();

        o_graph.getChildren().add(panel);
    }

    public void instantiateOptimalNodes(int tCores) {
        for (int i = 0; i < tCores; i++) {
            lastOptimalNode.add("");
        }
    }

    public synchronized void addOptimalToSearchGraph(int criticalLength, int coreNm) {
        sg.addNode(criticalLength + "_" + nodeCounter);
        sg.addEdge("Edge-" + edgeCounter, "root", criticalLength + "_" + nodeCounter);
        nodeCounter++;
        edgeCounter++;

        Node currentNode = sg.getNode(criticalLength + "_" + (nodeCounter - 1));
        currentNode.setAttribute("ui.label", criticalLength);
        currentNode.setAttribute("core", coreNm);
        currentNode.setAttribute("ui.class", "marked");

        if (coreNm >= COLORS.length) {
            currentNode.setAttribute("ui.style", COLORS[0]);
        } else {
            currentNode.setAttribute("ui.style", COLORS[coreNm]);
        }

        String previousCoreBest = lastOptimalNode.get(coreNm);

        if (!(previousCoreBest.equals(""))) {
            Node previousOptimalNode = sg.getNode(previousCoreBest);
            previousOptimalNode.removeAttribute("ui.class");
        }
        lastOptimalNode.set(coreNm, currentNode.toString());

    }

}