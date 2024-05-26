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
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductDetail;

public class MainViewController implements Initializable {
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private BackendController controller;
    private final ShopView shopView = new ShopView();
    private CategoryView categoryView;
    private ShoppingCartView shoppingCartView;
    private DetailView detailView;
    private PaymentScreen paymentScreen;
    private PreviousOrdersView previousOrdersView;

    @FXML
    TextArea marke;
    @FXML
    TextArea ursprung;
    @FXML
    TextArea beskrivning;
    @FXML
    TextArea innehall;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    AnchorPane detailPane;
    @FXML
    StackPane rootStackPane;
    @FXML
    AnchorPane previousOrdersPane;
    @FXML
    private BorderPane detailViewPane;
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainBorderPane.setCenter(shopView);
        controller = new BackendController(dataHandler, this);
        shoppingCartView = new ShoppingCartView(controller, this);
        categoryView = new CategoryView(dataHandler);
        mainBorderPane.setRight(shoppingCartView);
        mainBorderPane.setLeft(categoryView);
        categoryView.addSearchObserver(controller);
        categoryView.setMainController(this);
        controller.start(shopView);

        previousOrdersView = new PreviousOrdersView(this, this.shoppingCartView);
        previousOrdersPane.getChildren().clear();
        previousOrdersPane.getChildren().add(this.previousOrdersView);
        previousOrdersPane.setVisible(false);
        previousOrdersPane.toBack();

        shopView.setBackendController(controller);
        shopView.setMainViewController(this);
        String iMatDirectory = controller.getIMatDirectory();
        detailPane.setVisible(false);

        // Set an event handler for the search field
        searchField.setOnKeyPressed(event -> handleSearchFieldKeyPressed(event));
        this.paymentScreen = new PaymentScreen(this, new ShoppingCartView(controller, this));
        this.rootStackPane.getChildren().add(paymentScreen);
        paymentScreen.toBack();
    }

    public void openDetailView(Product product) {
        ProductCard productCard = new ProductCard(product, dataHandler.getFXImage(product), this);
        productCard.addShoppingItemObserver(controller);
        productCard.addFavouriteObserver(controller);
        dataHandler.getShoppingCart().addShoppingCartListener(productCard);
        detailView = new DetailView(productCard, this);
        productCard.addFavouriteObserver(detailView);
        detailPane.getChildren().clear();
        detailPane.getChildren().add(detailView);
        detailPane.setVisible(true);
        detailPane.toFront();

    }

    public void closeDetailView() {
        detailPane.setVisible(false);
        detailPane.toBack();

    }

    public void populateRecipeDetailView(Product product) {
        ProductDetail productDetail = dataHandler.getDetail(product);
        marke.setText("Märke: " + productDetail.getBrand());
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
            categoryView.selectNode(categoryView.getCurrentValue());
        } else {
            categoryView.clearSelection(); // Unselecta categories om det görs en textsökning
            controller.newSearch(new Search(searchText, controller.getSortOrder()));
        }
    }

    public void toPayment(ShoppingCartView shoppingCartView) {
        this.paymentScreen.toFront();
        this.mainBorderPane.setVisible(false);
        this.paymentScreen.toChosenProducts();
    }

    public void backToShop() {
        this.paymentScreen.toBack();
        this.previousOrdersPane.toBack();
        this.previousOrdersPane.setVisible(false);
        this.mainBorderPane.setVisible(true);
    }

    public void toStartPage() {

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

    public void openPreviousOrdersPane() {
        previousOrdersView.update();
        previousOrdersPane.toFront();
        previousOrdersPane.setVisible(true);
    }
}