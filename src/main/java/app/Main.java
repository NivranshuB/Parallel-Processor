package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Author: Team UNTESTED
 * Main method where the implementation of the program begins.
 */
public class Main extends Application {

    private static Stage primaryStage;

    private static DotFileReader dotFileReader;

    private static Config config;


    public static void main(String[] args) {
        //Parses command line input arguments to extract the number of processors, DOT file and other args

        ArgumentParser parser = new ArgumentParser();

        try {
            config = parser.parse(args);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to parse arguments " + e.getMessage());
            return;
        }

        dotFileReader = new DotFileReader(config.getInputFile());

        config.setNumOfTasks(dotFileReader.getNodeMap().size());

        if (config.getVisualise()) {
            launch(args);
        } else {
            ApplicationThread thread = new ApplicationThread();
            thread.start();
        }

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static DotFileReader getDotFileReader() {
        return dotFileReader;
    }

    @Override
    public void start(Stage stage) throws Exception {

        primaryStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

        stage.setTitle("Task Scheduler");
        stage.setScene(new Scene(root, 1300, 760));
        stage.setResizable(false);
        if (config.getVisualise()) {
            stage.show();
        }

        System.out.println("is start first??");

        ApplicationThread thread = new ApplicationThread();
        thread.start();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
