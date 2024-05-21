package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCartView extends VBox implements ShoppingCartListener {
    @FXML
    private VBox shoppingViewScrollPaneVBox;
    private List<ShoppingItem> items;
    private HashMap<Product, ProductLine> productLines = new HashMap<Product, ProductLine>();
    private IMatDataHandler dataHandler;

    public ShoppingCartView(IMatDataHandler dataHandler) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shopping_cart_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.dataHandler = dataHandler;
        dataHandler.getShoppingCart().addShoppingCartListener(this);
        for(ShoppingItem item : dataHandler.getShoppingCart().getItems()) {
            productLines.put(item.getProduct(), new ProductLine(item.getProduct(), dataHandler.getFXImage(item.getProduct()), item.getAmount(), item.getTotal()));
        }
        updateShoppingViewScrollPaneVBox();
    }

    private void updateShoppingViewScrollPaneVBox() {
        shoppingViewScrollPaneVBox.getChildren().clear();
        for(Product product : productLines.keySet()) {
            shoppingViewScrollPaneVBox.getChildren().add(productLines.get(product));
        }
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        Product product = cartEvent.getShoppingItem().getProduct();
        double amount = cartEvent.getShoppingItem().getAmount();
        double total = cartEvent.getShoppingItem().getTotal();
        if(!cartEvent.isAddEvent()) {
            productLines.remove(product);
        } else {
            if(productLines.containsKey(product)) {
                productLines.put(product, productLines.get(product).updateLabels(amount, total));
            }
            else {
                productLines.put(product, new ProductLine(product, dataHandler.getFXImage(product), amount, total));
            }
        }
        updateShoppingViewScrollPaneVBox();
    }
}
