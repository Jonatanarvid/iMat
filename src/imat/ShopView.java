package imat;

import javafx.fxml.FXML;
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
        for (Product product: products) {

        }
    }
}
