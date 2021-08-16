package app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.graphstream.graph.Graph;
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

import java.io.IOException;

public class MainController {

    @FXML
    private Label numOfTasks;

    @FXML
    private Label numOfProcessors;

    @FXML
    private Label numOfCores;

    @FXML
    private VBox graph;

    public void initialize() {
        Config config = Config.getInstance();

        int numOfT = config.getNumOfTasks();
        int numOfP = config.getNumOfProcessors();
        int numOfC = config.getNumOfCores();

        numOfTasks.setText(String.valueOf(numOfT));
        numOfProcessors.setText(String.valueOf(numOfP));
        numOfCores.setText(String.valueOf(numOfC));

        System.setProperty("org.graphstream.ui", "javafx");
//        System.setProperty("org.graphstream.debug", "true");

        Graph g = new SingleGraph("test");

        FxViewer v = new FxViewer(g, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());

//        ViewerPipe pipeIn = v.newViewerPipe();

//        FxDefaultView view = (FxDefaultView)v.addView("view1", new FxGraphRenderer());

//        DefaultApplication.init(view, g);
//        new Thread(() -> Application.launch(DefaultApplication.class)).start();

        FileSource fs = new FileSourceDOT();

        fs.addSink(g);

//        System.out.println(config.getInputFile().getName());

        try {
            fs.readAll(config.getInputFile().getName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }
//        GraphicGraph g = new GraphicGraph("test");

//        FxViewer v = new FxViewer(g);
//        Viewer viewer = new FxViewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);


//        v.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
//            v.close();

//        Scene scene = new Scene(panel, 800, 600);

        Stage primaryStage = Main.getPrimaryStage();
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        primaryStage.showAndWait();

        graph.getChildren().add(panel);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                v.close();
//                v.disableAutoLayout();
            }
        });

//        viewer.addDefaultView(true);
//        g.display();
    }
    
}
