package imat;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import se.chalmers.cse.dat216.project.Product;

import java.util.List;

public class ShopView {
    @FXML private GridPane productGrid;




    private void clearProductGrid() {
        productGrid.getChildren().clear(); // Remove all children
        productGrid.getRowConstraints().clear(); // Optional: Clear row constraints
        //gridPane.getColumnConstraints().clear();  Optional: Clear column constraints
    }

    private void populateProductGrid(List<Product> products) {
        int counter = 0;
        int productAmount = products.size();
        int rowCount = (int) (products.size() /3 + 0.5);

        clearProductGrid();
        for (int i = 0; i< (productAmount); i++) {
            Product product = products.get(i);
            int currentRow = i / 3;
            int currentColumn = i % 3;

            productGrid.add(productCard, currentRow, currentColumn);

        }
    }
}
