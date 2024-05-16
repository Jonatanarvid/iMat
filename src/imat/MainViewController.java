package imat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainViewController implements Initializable {


    BackendController controller = new BackendController();

    public void initialize(URL url, ResourceBundle rb) {

        String iMatDirectory = controller.getIMatDirectory();

    }
}