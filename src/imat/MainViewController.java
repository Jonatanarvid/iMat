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
    private CategoryView categoryView;
    private ShoppingCartView shoppingCartView;
    @FXML BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        controller = new BackendController(dataHandler);
        shoppingCartView = new ShoppingCartView(controller);
        categoryView = new CategoryView(dataHandler);
        mainBorderPane.setRight(shoppingCartView);
        mainBorderPane.setLeft(categoryView);
        categoryView.addSearchObserver(controller);
        controller.start(shopView);

        String iMatDirectory = controller.getIMatDirectory();
    }
}