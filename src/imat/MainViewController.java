package imat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;

public class MainViewController implements Initializable {
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private BackendController controller;
    private ShopView shopView = new ShopView();
    private ShoppingCartView shoppingCartView;
    @FXML private VBox centerVBox;
    @FXML private VBox rightVBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        centerVBox.getChildren().add(shopView);
        shoppingCartView = new ShoppingCartView(dataHandler);
        rightVBox.getChildren().clear();
        rightVBox.getChildren().add(shoppingCartView);
        controller = new BackendController(dataHandler);
        controller.start(shopView);

        String iMatDirectory = controller.getIMatDirectory();

    }
}