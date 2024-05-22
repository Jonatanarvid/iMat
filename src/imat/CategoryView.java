package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionModel;
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
        TreeItem<String> favoritesItem = new TreeItem<>("Favoriter");
        rootItem.getChildren().add(favoritesItem);

        // Iterate over Categories enum to create categories and subcategories
        for (Categories category : Categories.values()) {
            TreeItem<String> categoryItem = new TreeItem<>(category.name());
            categoryHashMap.put(category.name(), category.convertToListOfProductCategory());
            List<ProductCategory> subcategories = category.convertToListOfProductCategory();
            for (ProductCategory subcategory : subcategories) {
                List<ProductCategory> productCategories = new ArrayList<>();
                productCategories.add(subcategory);
                categoryHashMap.put(subcategory.name(), productCategories);
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
                if (!selectedItem.getValue().equals("Favoriter")) {
                    notifySearchObservers(categoryHashMap.get(selectedItem.getValue()));
                } else {
                    notifySearchObservers(new ArrayList<>());
                    collapseAll(rootItem);
                }
            }
        });
        selectFirstChildOfRoot();
        // Expand selected item on single click and collapse others
        categoryTreeView.setOnMouseClicked(event -> handleMouseClick(event, rootItem));
    }

    private void selectFirstChildOfRoot() {
        // Check if the root item has children
        if (!categoryTreeView.getRoot().getChildren().isEmpty()) {
            // Get the first child of the root item
            TreeItem<String> firstChild = categoryTreeView.getRoot().getChildren().get(0);

            // Get the selection model of the TreeView
            SelectionModel<TreeItem<String>> selectionModel = categoryTreeView.getSelectionModel();

            // Select the first child of the root item
            selectionModel.select(firstChild);
        }
    };

    private void handleMouseClick(MouseEvent event, TreeItem<String> rootItem) {
        if (event.getClickCount() == 1) {
            TreeItem<String> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (!selectedItem.getValue().equals("Favoriter") && !selectedItem.isLeaf()) {
                    boolean isExpanded = selectedItem.isExpanded();
                    collapseOtherItems(selectedItem); // Collapse other items first
                    selectedItem.setExpanded(!isExpanded); // Toggle expand/collapse
                    categoryTreeView.getSelectionModel().select(selectedItem); // Ensure the item is selected
                } else if (selectedItem.getValue().equals("Favoriter")) {
                    collapseAll(rootItem);
                }
                event.consume(); // Consume the event to prevent further propagation
            }
        }
    }

    private void collapseAll(TreeItem<String> rootItem) {
        for (TreeItem<String> item : rootItem.getChildren()) {
            if (item.isExpanded()) {
                item.setExpanded(false);
                collapseAllChildren(item);
            }
        }
    }

    private void collapseAllChildren(TreeItem<String> item) {
        for (TreeItem<String> child : item.getChildren()) {
            if (child.isExpanded()) {
                child.setExpanded(false);
                collapseAllChildren(child);
            }
        }
    }

    private void collapseOtherItems(TreeItem<String> selectedItem) {
        TreeItem<String> parent = selectedItem.getParent();
        if (parent != null) {
            for (TreeItem<String> sibling : parent.getChildren()) {
                if (sibling != selectedItem) {
                    sibling.setExpanded(false);
                    collapseAllChildren(sibling);
                }
            }
        }
    }
// WEEEEEEEEEEE
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
