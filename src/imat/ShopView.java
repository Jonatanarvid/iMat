package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ShopView extends VBox implements ProductCardObserver {
    @FXML private GridPane productGrid;
    private BackendController controller;

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
    }

    public void setBackendController(BackendController controller) {
        this.controller = controller;
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
        
        controller.newSearch(new Search("", controller.getSortOrder()));

    }

    @Override
    public void update(List<ProductCard> productCards) {
        populateProductGrid(productCards);
    }
}
