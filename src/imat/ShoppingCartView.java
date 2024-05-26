package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCartView extends VBox implements ShoppingCartListener {
    @FXML
    VBox shoppingViewScrollPaneVBox;
    @FXML
    private Label totalPriceLabel;
    private List<ShoppingItem> items;
     HashMap<Product, ProductLine> productLines = new HashMap<Product, ProductLine>();
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private BackendController backendController;
    private MainViewController mainViewController;

    public ShoppingCartView(BackendController backendController, MainViewController mainViewController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shopping_cart_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        shoppingViewScrollPaneVBox.setSpacing(-20);


        this.backendController = backendController;
        dataHandler.getShoppingCart().addShoppingCartListener(this);
        for(ShoppingItem item : dataHandler.getShoppingCart().getItems()) {
            ProductLine productLine = new ProductLine(item.getProduct(), dataHandler.getFXImage(item.getProduct()), (int) item.getAmount());
            productLine.addShoppingItemObserver(backendController);
            productLines.put(item.getProduct(), productLine);
        }
        updateShoppingViewScrollPaneVBox();
        this.mainViewController = mainViewController;
    }

    public void updateShoppingViewScrollPaneVBox() {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        this.totalPriceLabel.setText(numberFormat.format(dataHandler.getShoppingCart().getTotal()) + " kr");
        shoppingViewScrollPaneVBox.getChildren().clear();
        System.out.println(productLines.size());
        for(Product product : productLines.keySet()) {
            shoppingViewScrollPaneVBox.getChildren().add(productLines.get(product));
        }
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        this.totalPriceLabel.setText(String.valueOf(dataHandler.getShoppingCart().getTotal()) + " kr");
        if(cartEvent.getShoppingItem() != null) {
            Product product = cartEvent.getShoppingItem().getProduct();
            double amount = cartEvent.getShoppingItem().getAmount();
            double total = cartEvent.getShoppingItem().getTotal();
            if (!cartEvent.isAddEvent()) {
                productLines.remove(product);
                updateShoppingViewScrollPaneVBox();
            } else {
                if (productLines.containsKey(product)) {
                    productLines.get(product).updateProducts(cartEvent);
                } else {
                    ProductLine productLine = new ProductLine(product, dataHandler.getFXImage(product), 1);
                    productLine.addShoppingItemObserver(backendController);
                    productLines.put(product, productLine);
                }
            }
            updateShoppingViewScrollPaneVBox();
        }
    }

    public void toPayment() {
        mainViewController.toPayment(this);
    }
}