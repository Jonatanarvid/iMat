package imat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

    BackendController controller = new BackendController();
    private ShopView shopView = new ShopView();
    @FXML private VBox centerVBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        centerVBox.getChildren().add(shopView);
        controller.start(shopView);

        String iMatDirectory = controller.getIMatDirectory();

    }
}