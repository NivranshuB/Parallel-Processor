package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class MainController {

    @FXML
    private Label numOfProcessors;

    public void initialize() {
        Config config = Config.getInstance();
        int numOfP = config.getNumOfProcessors();
        numOfProcessors.setText(String.valueOf(numOfP));
    }
    
}
