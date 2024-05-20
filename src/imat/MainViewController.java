package imat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainViewController implements Initializable {

    BackendController controller = new BackendController();
    private ShopView shopView = new ShopView();
    @FXML private BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        controller.start(shopView);

        String iMatDirectory = controller.getIMatDirectory();

    }
}