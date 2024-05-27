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
    private MainViewController mainViewController;
    private List<SearchObserver> searchObservers = new ArrayList<>();
    private HashMap<String, List<ProductCategory>> categoryHashMap = new HashMap<>();
    private HashMap<String, String> displayTextMap = new HashMap<>();
    TreeItem<String> rootItem = new TreeItem<>("Kategorier");
    TreeItem<String> currentValue;

    public CategoryView(IMatDataHandler dataHandler) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("category_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        // Initialize the display text map with Swedish translations
        initDisplayTextMap();

        rootItem.setExpanded(true);

        // Add the "Alla Produkter" category first
        TreeItem<String> allProductsItem = new TreeItem<>(displayTextMap.get("ALL_PRODUCTS"));
        rootItem.getChildren().add(allProductsItem);
        categoryHashMap.put("ALL_PRODUCTS", Categories.ALL_PRODUCTS.convertToListOfProductCategory());

        // Add the "Favoriter" category second
        TreeItem<String> favoritesItem = new TreeItem<>("Favoriter");
        rootItem.getChildren().add(favoritesItem);

        // Iterate over Categories enum to create remaining categories and subcategories
        for (Categories category : Categories.values()) {
            if (category != Categories.ALL_PRODUCTS) { // Skip "ALL_PRODUCTS" as it is already added
                String categoryDisplayName = displayTextMap.getOrDefault(category.name(), category.name());
                TreeItem<String> categoryItem = new TreeItem<>(categoryDisplayName);
                categoryHashMap.put(category.name(), category.convertToListOfProductCategory());
                if (category != Categories.ALL_PRODUCTS) { // Skip subcategories for ALL_PRODUCTS
                    List<ProductCategory> subcategories = category.convertToListOfProductCategory();
                    for (ProductCategory subcategory : subcategories) {
                        String subcategoryDisplayName = displayTextMap.getOrDefault(subcategory.name(), subcategory.name());
                        List<ProductCategory> productCategories = new ArrayList<>();
                        productCategories.add(subcategory);
                        categoryHashMap.put(subcategory.name(), productCategories);
                        TreeItem<String> subcategoryItem = new TreeItem<>(subcategoryDisplayName);
                        categoryItem.getChildren().add(subcategoryItem);
                    }
                }
                rootItem.getChildren().add(categoryItem);
            }
        }

        // Set the root item to the TreeView
        categoryTreeView.setRoot(rootItem);

        // Hide the root item
        categoryTreeView.setShowRoot(false);

        // Handle selections
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateViewByCategories(newValue);
                currentValue = newValue;
            }
        });

        // Expand selected item on single click and collapse others
        categoryTreeView.setOnMouseClicked(event -> handleMouseClick(event, rootItem));
    }

    private void initDisplayTextMap() {
        displayTextMap.put("MEAT_AND_FISH", "Kött och Fisk");
        displayTextMap.put("DRINKS", "Drycker");
        displayTextMap.put("DAIRY_AND_EGG", "Mejeri och Ägg");
        displayTextMap.put("FRUIT", "Frukt");
        displayTextMap.put("VEG", "Grönsaker");
        displayTextMap.put("BAKERY", "Bageri");
        displayTextMap.put("SHELF", "Hylla");
        displayTextMap.put("ALL_PRODUCTS", "Alla Produkter");

        displayTextMap.put("MEAT", "Kött");
        displayTextMap.put("FISH", "Fisk");
        displayTextMap.put("COLD_DRINKS", "Kalla Drycker");
        displayTextMap.put("HOT_DRINKS", "Varma Drycker");
        displayTextMap.put("DAIRIES", "Mejeriprodukter");
        displayTextMap.put("BERRY", "Bär");
        displayTextMap.put("EXOTIC_FRUIT", "Exotiska Frukter");
        displayTextMap.put("VEGETABLE_FRUIT", "Grönsaksfrukt");
        displayTextMap.put("MELONS", "Meloner");
        displayTextMap.put("POD", "Baljväxter");
        displayTextMap.put("CITRUS_FRUIT", "Citrusfrukt");
        displayTextMap.put("CABBAGE", "Kål");
        displayTextMap.put("ROOT_VEGETABLE", "Rotfrukter");
        displayTextMap.put("HERB", "Örter");
        displayTextMap.put("BREAD", "Bröd");
        displayTextMap.put("SWEET", "Sötsaker");
        displayTextMap.put("POTATO_RICE", "Potatis och Ris");
        displayTextMap.put("PASTA", "Pasta");
        displayTextMap.put("NUTS_AND_SEEDS", "Nötter och Frön");
        displayTextMap.put("FLOUR_SUGAR_SALT", "Mjöl, Socker och Salt");
    }

    public void setMainController(MainViewController controller) {
        this.mainViewController = controller;
    }

    public TreeItem<String> getCurrentValue() {
        return currentValue;
    }

    public void selectNode(TreeItem<String> node) {
        if (node != null) {
            categoryTreeView.getSelectionModel().select(node);
            mainViewController.setResultInfoLabel("Visar kategori: " + getCurrentValue().getValue());
            if (!node.getChildren().isEmpty()) {
                node.setExpanded(true);
            }
        }
    }

    public void selectFirstChildNode() {
        if (rootItem.getChildren() != null && !rootItem.getChildren().isEmpty()) {
            selectNode(rootItem.getChildren().get(0));
        }
    }



    public void clearSelection() {
        SelectionModel<TreeItem<String>> selectionModel = categoryTreeView.getSelectionModel();
        selectionModel.clearSelection();
        collapseAll(rootItem);
    }

    public void updateViewByCategories(TreeItem<String> value) {
        TreeItem<String> selectedItem = value;
        System.out.println("Selected item: " + selectedItem.getValue());
        String selectedKey = displayTextMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedItem.getValue()))
                .map(HashMap.Entry::getKey)
                .findFirst()
                .orElse(selectedItem.getValue());

        if (selectedKey.equals("ALL_PRODUCTS")) {
            notifySearchObservers(categoryHashMap.get(selectedKey));
            collapseAll(rootItem); // Collapse all categories when "ALL_PRODUCTS" is selected
        } else if (!selectedItem.getValue().equals("Favoriter")) {
            notifySearchObservers(categoryHashMap.get(selectedKey));
        } else {
            notifySearchObservers(new ArrayList<>());
            collapseAll(rootItem);
        }
    }

    private void handleMouseClick(MouseEvent event, TreeItem<String> rootItem) {
        if (event.getClickCount() == 1) {
            TreeItem<String> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                mainViewController.setResultInfoLabel("Visar kategori: " + selectedItem.getValue());
                mainViewController.clearSearchText();
                String selectedKey = displayTextMap.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(selectedItem.getValue()))
                        .map(HashMap.Entry::getKey)
                        .findFirst()
                        .orElse(selectedItem.getValue());

                if (selectedKey.equals("ALL_PRODUCTS")) {
                    collapseAll(rootItem); // Collapse all when "ALL_PRODUCTS" is selected
                } else if (!selectedItem.getValue().equals("Favoriter") && !selectedItem.isLeaf()) {
                    boolean isExpanded = selectedItem.isExpanded();
                    collapseOtherItems(selectedItem);
                    selectedItem.setExpanded(!isExpanded);
                    categoryTreeView.getSelectionModel().select(selectedItem);
                } else if (selectedItem.getValue().equals("Favoriter")) {
                    collapseAll(rootItem);
                }
                event.consume();
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
            observer.updateListSearchObserver(categories);
        }
    }
}