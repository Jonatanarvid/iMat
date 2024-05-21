package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;
import java.util.List;

public class CategoryView extends VBox {
    private IMatDataHandler dataHandler;
    @FXML
    private TreeView<String> categoryTreeView;

    public CategoryView(IMatDataHandler dataHandler) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("category_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (
                IOException exception) {
            throw new RuntimeException(exception);
        }

        this.dataHandler = dataHandler;

        TreeItem<String> rootItem = new TreeItem<>("Kategorier");
        rootItem.setExpanded(true);

        // Iterate over Categories enum to create categories and subcategories
        for (Categories category : Categories.values()) {
            TreeItem<String> categoryItem = new TreeItem<>(category.name());
            List<ProductCategory> subcategories = category.convertToListOfProductCategory();
            for (ProductCategory subcategory : subcategories) {
                TreeItem<String> subcategoryItem = new TreeItem<>(subcategory.name());
                categoryItem.getChildren().add(subcategoryItem);
            }
            rootItem.getChildren().add(categoryItem);
        }

        // Set the root item to the TreeView
        categoryTreeView.setRoot(rootItem);

        // Hide the root item
        categoryTreeView.setShowRoot(false);

        // Handle selections
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem<String> selectedItem = newValue;
                System.out.println("Selected item: " + selectedItem.getValue());
                // Handle the selection (category or subcategory)
                // Add your custom logic here
            }
        });

    }
}
