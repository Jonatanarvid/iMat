package imat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    @FXML
    private BorderPane detailViewPane;
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        controller = new BackendController(dataHandler, this);
        shoppingCartView = new ShoppingCartView(controller);
        categoryView = new CategoryView(dataHandler);
        mainBorderPane.setRight(shoppingCartView);
        mainBorderPane.setLeft(categoryView);
        categoryView.addSearchObserver(controller);
        categoryView.setMainController(this);
        controller.start(shopView);

        shopView.setBackendController(controller);
        shopView.setMainViewController(this);
        String iMatDirectory = controller.getIMatDirectory();
        detailPane.setVisible(false);

        // Set an event handler for the search field
        searchField.setOnKeyPressed(event -> handleSearchFieldKeyPressed(event));
    }
    public void openDetailView(Product product){
        populateRecipeDetailView(product);
        detailPane.setVisible(true);
        detailPane.toFront();

    }
    public void closeDetailView(){
        detailPane.setVisible(false);
        detailPane.toBack();

    }
    public void populateRecipeDetailView(Product product){
        ProductDetail productDetail= dataHandler.getDetail(product);
        marke.setText("Märke: "+ productDetail.getBrand());
        ursprung.setText("Ursprung: " + productDetail.getOrigin());
        beskrivning.setText("Beskrivning: " + productDetail.getDescription());
        innehall.setText("Innehåll: " + productDetail.getDescription());



    }

    private void handleSearchFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            performSearch();
        }
    }

    private void performSearch() {
        String searchText = getSearchText();
        if (searchText.equals("")) { // Om det inte är en sökning som sort ska ändras för utan inom en kategori
            refreshSortedCategorySearch();
        } else {
            categoryView.clearSelection(); // Unselecta categories om det görs en textsökning
            controller.newSearch(new Search(searchText, controller.getSortOrder()));
        }
    }

    public void refreshSortedCategorySearch() {
        categoryView.updateViewByCategories(categoryView.getCurrentValue());
    }

    public String getSearchText() {
        return searchField.getText();
    }
    public void clearSearchText() {
        searchField.setText("");
    }
}
