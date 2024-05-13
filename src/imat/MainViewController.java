package imat;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

public class MainViewController implements Initializable {

    @FXML
    Label pathLabel;

    BackendController controller = new BackendController();

    public void initialize(URL url, ResourceBundle rb) {

        String iMatDirectory = controller.getIMatDirectory();

        pathLabel.setText(iMatDirectory);
    }
}