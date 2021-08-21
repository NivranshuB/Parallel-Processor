package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



/**
 * Author: Team UNTESTED
 * Main method where the implementation of the program begins.
 */
public class Main extends Application {

	private static Stage primaryStage;

	private static DotFileReader dotFileReader;

	private Schedule optimalSchedule;
	private static Config config;
	

	public static void main(String[] args) {
		//Parses command line input arguments to extract the number of processors, DOT file and other args

		ArgumentParser parser = new ArgumentParser();



		try {
			config = parser.parse(args);
		} catch (IllegalArgumentException e) {
			System.out.println("Unable to parse arguments "+ e.getMessage());
			return;
		}

		System.out.println("input file = " + config.getInputFile());
		System.out.println("number of processors = " + config.getNumOfProcessors());
		System.out.println("number of cores = " + config.getNumOfCores());
		System.out.println("visualise = " + config.getVisualise());
		System.out.println("output file = " + config.getOutputFile());

		//		Scheduler scheduler = Scheduler.getInstance();

		Scheduler scheduler = Scheduler.getInstance();

		//Uses the DOT filepath from the args to create a new DotFileReader object/graph representation
		//        DotFileReader dotFileReader = new DotFileReader(config.getInputFile());
		dotFileReader = new DotFileReader(config.getInputFile());

		config.setNumOfTasks(dotFileReader.getNodeMap().size());


		launch(args);

		// debugging

		//Joe's code that uses the DOT filepath from the args to create a new
		//DotFileReader object/graph representation

		//        DotFileReader dotFileReader = new DotFileReader(config.getInputFile());
		//
		//        //debugging
		//        System.out.println("graph name = " + dotFileReader.getGraphName());
		//
		////        Schedule optimalSchedule = scheduler.getOptimalSchedule(dotFileReader.getNodeMap(), dotFileReader.getEdgeMap(), config.getNumOfProcessors());
		////		System.out.println("Here is optimal: \n" + optimalSchedule);
		//
		//        BnBScheduler optimalScheduler = BnBScheduler.getInstance(dotFileReader, config);
		//
		//        BnBSchedule optimalSchedule = optimalScheduler.getSchedule();
		//        System.out.println(optimalSchedule);
		//        System.out.println("We reached here");
		//        optimalSchedule.printSchedule();
		//
		////        //optimalSchedule = scheduler.getOptimalSchedule(nodeMap, edgeMap, numberOfProcessors);
		//        String graphName = dotFileReader.getGraphName();
		////        //Corban's code to parse the optimal schedule to the output DOT file
		//        OutputParser op = new OutputParser(graphName, config, optimalSchedule, optimalScheduler);
		//
		//        op.writeFile();
	}



	public static Stage getPrimaryStage() {
		return primaryStage;

	}

	@Override
	public void start(Stage stage) throws Exception {

		primaryStage = stage;
		//      stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png")));
		//      stage.getIcons().add(new Image("file:logo.png"));
		//      URL url = Paths.get("./src/main/java/app/Main.fxml").toUri().toURL();
		//      Parent root = FXMLLoader.load(url);
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
		//      Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
		stage.setTitle("Task Scheduler");
		stage.setScene(new Scene(root, 1280, 960));
        stage.setResizable(false);
		if (config.getVisualise()) {
			stage.show();
		}

		System.out.println("is start first??");

		MyThread thread = new MyThread();
		thread.start();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
	}



	public static DotFileReader getDotFileReader() { return dotFileReader; }
}
