package imat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

public class MainViewController implements Initializable {
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private BackendController controller;
    private ShopView shopView = new ShopView();
    private CategoryView categoryView;
    private ShoppingCartView shoppingCartView;
    @FXML BorderPane mainBorderPane;
    @FXML
    private AnchorPane detailViewAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        shoppingCartView = new ShoppingCartView(dataHandler);
        categoryView = new CategoryView(dataHandler);
        mainBorderPane.setRight(shoppingCartView);
        mainBorderPane.setLeft(categoryView);
        controller = new BackendController(dataHandler);
        categoryView.addSearchObserver(controller);
        controller.start(shopView);

        String iMatDirectory = controller.getIMatDirectory();
    }

    public void showDetailVeiw(){

    }








}