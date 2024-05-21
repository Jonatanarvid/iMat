package imat;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spinner extends AnchorPane implements ShoppingCartListener, ShoppingItemObservable{
    @FXML
    private Button buttonCardMinus;
    @FXML
    private Button buttonCardPlus;
    @FXML
    private AnchorPane moreThan0Product;
    @FXML
    private Label buyCounter;
    @FXML
    private AnchorPane buy;

    private int amount = 0;
    private final Product product;
    private final IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private List<ShoppingItemObserver> shoppingItemObservers = new ArrayList<>();
    ;
    public Spinner(Product product, boolean isProductLine) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Spinner.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        dataHandler.getShoppingCart().addShoppingCartListener(this);
        this.buy.toFront();
        this.product = product;
        if(isProductLine) {
            // Apply scaling transformation to the loaded AnchorPane
            Scale scale = new Scale();
            scale.setX(0.4); // Scale X axis by 0.75 (make it smaller)
            scale.setY(0.4); // Scale Y axis by 0.75 (make it smaller)

            // Apply the Scale transformation to the root element of the loaded FXML
            this.getTransforms().add(scale);
            moreThan0Product.toFront();
        }
        updateAmount();
    }

    public void buyLabelClicked(Event event) {
        System.out.println("Hello!");
        addProduct(event);
        updateAmount();
    }

    public void addProduct(Event event) {
        notifyShoppingItemObservers(true);
        event.consume();
    }

    public void subtractProduct(Event event) {
        notifyShoppingItemObservers(false);
        event.consume();
    }

    public void updateAmount() {
        for (ShoppingItem item : dataHandler.getShoppingCart().getItems()) {
            if (this.product.equals(item.getProduct()) && item.getAmount() != 0) {
                this.moreThan0Product.toFront();
                this.amount = (int) item.getAmount();
            }
        }
        this.buyCounter.setText(String.valueOf(this.amount));
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        updateAmount();
    }

    @Override
    public void addShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {
        shoppingItemObservers.add(shoppingItemObserver);
    }

    @Override
    public void removeShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {

    }

    @Override
    public void notifyShoppingItemObservers(boolean isAdd) {
        for (ShoppingItemObserver observer : shoppingItemObservers) {
            observer.updateShoppingItemObserver(this.product, isAdd);
        }
    }
}
