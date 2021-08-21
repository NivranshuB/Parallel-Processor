package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
    public void start(Stage arg0) throws Exception {
    	
    		primaryStage = arg0;
//          arg0.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png")));
//          arg0.getIcons().add(new Image("file:logo.png"));
//          URL url = Paths.get("./src/main/java/app/Main.fxml").toUri().toURL();
//          Parent root = FXMLLoader.load(url);
  		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
//          Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
          arg0.setTitle("Task Scheduler");
          arg0.setScene(new Scene(root));
          
          if (config.getVisualise()) {
        	  arg0.show();
          }
        

        System.out.println("is start first??");

        MyThread thread = new MyThread();
        thread.start();
    }

    public static DotFileReader getDotFileReader() { return dotFileReader; }
}
