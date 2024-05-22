package imat;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ShopView extends VBox implements ProductCardObserver {
    @FXML private GridPane productGrid;
    private BackendController controller;

    private MainViewController mainViewController;
    @FXML
    private ComboBox<String> sortComboBox;





    public ShopView() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("product_grid.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        // Add items to the ComboBox
        sortComboBox.getItems().addAll(
                "Alphabetical",
                "Reverse Alphabetical",
                "Price: Low to High",
                "Price: High to Low"
        );
    }

    private void setSortButtonDefault() {
        SortOrder sortOrder = controller.getSortOrder();
        String sortOrderString = switch (sortOrder) {
            case ALPHA -> "Alphabetical";
            case REVERSEALPHA -> "Reverse Alphabetical";
            case PRICELOWHIGH -> "Price: Low to High";
            case PRICEHIGHLOW -> "Price: High to Low";

        };
        // Set default value for ComboBox
        sortComboBox.setValue(sortOrderString);
    }

    public void setBackendController(BackendController controller) {
        this.controller = controller;
        setSortButtonDefault();
    }

    public void setMainViewController(MainViewController controller) {
        this.mainViewController = controller;

    }

    private void clearProductGrid() {
        productGrid.getChildren().clear(); // Remove all children
        productGrid.getRowConstraints().clear(); // Optional: Clear row constraints
    }

    private void populateProductGrid(List<ProductCard> productCards) {
        int counter = 0;
        int productAmount = productCards.size();
        System.out.println(productAmount);
        int rowCount = (int) (productCards.size() / 3 + 0.5);

        clearProductGrid();
        for (int i = 0; i < (productAmount); i++) {
            ProductCard productCard = productCards.get(i);
            int currentRow = i / 3;
            int currentColumn = i % 3;

            productGrid.add(productCard, currentColumn, currentRow);
        }
    }

    @FXML
    private void handleSortOrderChange() {
        String selectedSort = sortComboBox.getValue();
        SortOrder sortOrder;

        switch (selectedSort) {
            case "Alphabetical":
                sortOrder = SortOrder.ALPHA;
                break;
            case "Reverse Alphabetical":
                sortOrder = SortOrder.REVERSEALPHA;
                break;
            case "Price: Low to High":
                sortOrder = SortOrder.PRICELOWHIGH;
                break;
            case "Price: High to Low":
                sortOrder = SortOrder.PRICEHIGHLOW;
                break;
            default:
                sortOrder = SortOrder.ALPHA;
                break;
        }
        controller.setSortOrder(sortOrder);

        String searchText = mainViewController.getSearchText();
        if (searchText.equals("")) { // Om det inte är en sökning (om sökrutan ej är tom) som sort ska ändras för utan inom en kategori
            mainViewController.refreshSortedCategorySearch();
        } else {
            controller.newSearch(new Search(mainViewController.getSearchText(), controller.getSortOrder()));
        }


        //controller.newSearch(new Search(mainViewController.getSearchText(), controller.getSortOrder())); //searchField text here so you can sort when you're doing a textsearch too

    }


    @Override
    public void update(List<ProductCard> productCards) {
        populateProductGrid(productCards);
    }
}
