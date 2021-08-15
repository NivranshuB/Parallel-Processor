package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class MainController {

    @FXML
    private Label numOfTasks;

    @FXML
    private Label numOfProcessors;

    @FXML
    private Label numOfCores;

    public void initialize() {
        Config config = Config.getInstance();

        int numOfP = config.getNumOfProcessors();
        int numOfC = config.getNumOfCores();

        numOfProcessors.setText(String.valueOf(numOfP));
        numOfCores.setText(String.valueOf(numOfC));
    }
    
}
