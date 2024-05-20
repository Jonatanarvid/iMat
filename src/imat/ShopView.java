package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.util.List;

public class ShopView extends ScrollPane implements ProductCardObserver {
    @FXML private GridPane productGrid;

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

    private void clearProductGrid() {
        productGrid.getChildren().clear(); // Remove all children
        productGrid.getRowConstraints().clear(); // Optional: Clear row constraints
        //gridPane.getColumnConstraints().clear();  Optional: Clear column constraints
    }

    private void populateProductGrid(List<ProductCard> productCards) {
        int counter = 0;
        int productAmount = productCards.size();
        System.out.println(productAmount);
        int rowCount = (int) (productCards.size() /3 + 0.5);

        clearProductGrid();
        for (int i = 0; i< (productAmount); i++) {
            ProductCard productCard = productCards.get(i);
            int currentRow = i / 3;
            int currentColumn = i % 3;

            productGrid.add(productCard, currentColumn, currentRow);
        }
    }

    @Override
    public void update(List<ProductCard> productCards) {
        populateProductGrid(productCards);
    }
}