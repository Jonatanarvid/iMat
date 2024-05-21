package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryView extends VBox implements SearchObservable {
    @FXML
    private TreeView<String> categoryTreeView;
    private List<SearchObserver> searchObservers = new ArrayList<>();
    private HashMap<String, List<ProductCategory>> categoryHashMap = new HashMap<>();

    public CategoryView(IMatDataHandler dataHandler) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("category_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        TreeItem<String> rootItem = new TreeItem<>("Kategorier");
        rootItem.setExpanded(true);
        rootItem.getChildren().add(new TreeItem<String>("Favoriter"));
        // Iterate over Categories enum to create categories and subcategories
        for (Categories category : Categories.values()) {
            TreeItem<String> categoryItem = createTreeItem(category.name());
            categoryHashMap.put(category.name(), category.convertToListOfProductCategory());
            List<ProductCategory> subcategories = category.convertToListOfProductCategory();
            for (ProductCategory subcategory : subcategories) {
                List<ProductCategory> productCategories = new ArrayList<>();
                productCategories.add(subcategory);
                categoryHashMap.put(subcategory.name(), productCategories);
                TreeItem<String> subcategoryItem = createTreeItem(subcategory.name());
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
                if(!selectedItem.getValue().equals(new String("Favoriter"))) {
                    notifySearchObservers(categoryHashMap.get(selectedItem.getValue()));
                } else {
                    notifySearchObservers(new ArrayList<ProductCategory>());
                }
            }
        });

        // Expand selected item on single click
        categoryTreeView.setOnMouseClicked(event -> handleMouseClick(event));
    }

    private void handleMouseClick(MouseEvent event) {
        TreeItem<String> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && event.getClickCount() == 1) {
            TreeItem<String> clickedItem = getTreeItem(event);
            if (clickedItem != null && !clickedItem.isLeaf()) {
                boolean isExpanded = clickedItem.isExpanded();
                collapseOtherItems(clickedItem); // Collapse other items first
                clickedItem.setExpanded(!isExpanded); // Toggle expand/collapse
                categoryTreeView.getSelectionModel().select(clickedItem); // Ensure the item is selected
                event.consume(); // Consume the event to prevent further propagation
            }
        }
    }

    private TreeItem<String> getTreeItem(MouseEvent event) {
        TreeItem<String> item = categoryTreeView.getSelectionModel().getSelectedItem();
        if (item != null && categoryTreeView.getRow(item) == categoryTreeView.getSelectionModel().getSelectedIndex()) {
            return item;
        }
        return null;
    }

    private TreeItem<String> createTreeItem(String name) {
        TreeItem<String> item = new TreeItem<>(name);
        item.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                collapseOtherItems(item);
            }
        });
        return item;
    }

    private void collapseOtherItems(TreeItem<String> item) {
        TreeItem<String> parent = item.getParent();
        if (parent != null) {
            for (TreeItem<String> sibling : parent.getChildren()) {
                if (sibling != item) {
                    sibling.setExpanded(false);
                }
            }
        }
    }

    @Override
    public void addSearchObserver(SearchObserver observer) {
        searchObservers.add(observer);
    }

    @Override
    public void removeSearchObserver(SearchObserver observer) {
        searchObservers.remove(observer);
    }

    @Override
    public void notifySearchObservers(List<ProductCategory> categories) {
        for (SearchObserver observer : searchObservers) {
            observer.updateSearchObserver(categories);
        }
    }
}
