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
    private CategoryView categoryView = new CategoryView();
    private ShoppingCartView shoppingCartView;
    @FXML BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        shoppingCartView = new ShoppingCartView(dataHandler);
        mainBorderPane.setRight(shoppingCartView);
        controller = new BackendController(dataHandler);
        controller.start(shopView);

        String iMatDirectory = controller.getIMatDirectory();

    }
}