package imat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductDetail;

public class MainViewController implements Initializable {
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private BackendController controller;
    private ShopView shopView = new ShopView();
    private CategoryView categoryView;
    private ShoppingCartView shoppingCartView;
    private DetailView detailView;

    @FXML TextArea marke;
    @FXML TextArea ursprung;
    @FXML TextArea beskrivning;
    @FXML TextArea innehall;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    AnchorPane detailPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        controller = new BackendController(dataHandler, this);
        shoppingCartView = new ShoppingCartView(controller);
        categoryView = new CategoryView(dataHandler);
        mainBorderPane.setRight(shoppingCartView);
        mainBorderPane.setLeft(categoryView);
        categoryView.addSearchObserver(controller);
        controller.start(shopView);

        shopView.setBackendController(controller);
        String iMatDirectory = controller.getIMatDirectory();
        detailPane.setVisible(false);


    }
    public void openDetailView(Product product){
        populateRecipeDetailView(product);
        detailPane.setVisible(true);
        detailPane.toFront();

    }
    public void populateRecipeDetailView(Product product){
        ProductDetail productDetail= dataHandler.getDetail(product);
        marke.setText("Märke: "+ productDetail.getBrand());
        ursprung.setText("Ursprung: " + productDetail.getOrigin());
        beskrivning.setText("Beskrivning: " + productDetail.getDescription());
        innehall.setText("Innehåll: " + productDetail.getDescription());

    }
}