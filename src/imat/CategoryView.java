package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryView extends VBox implements SearchObservable{
    @FXML
    private TreeView<String> categoryTreeView;
    private List<SearchObserver> searchObservers = new ArrayList<SearchObserver>();
    private HashMap<String, List<ProductCategory>> categoryHashMap = new HashMap<String, List<ProductCategory>>();

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

        TreeItem<String> rootItem = new TreeItem<>("Kategorier");
        rootItem.setExpanded(true);
        rootItem.getChildren().add(new TreeItem<String>("Favoriter"));
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
                if(!selectedItem.getValue().equals(new String("Favoriter"))) {
                    notifySearchObservers(categoryHashMap.get(selectedItem.getValue()));
                } else {
                    notifySearchObservers(new ArrayList<ProductCategory>());
                }
            }
        });

    }

    @Override
    public void addSearchObserver(SearchObserver observer) {
        searchObservers.add(observer);
    }

    @Override
    public void removeSearchObserver(SearchObserver observer) {
        if(searchObservers.contains(observer)) {
            searchObservers.remove(observer);
        }
    }

    @Override
    public void notifySearchObservers(List<ProductCategory> categories) {
        for(SearchObserver observer : searchObservers) {
            observer.updateSearchObserver(categories);
        }
    }
}
