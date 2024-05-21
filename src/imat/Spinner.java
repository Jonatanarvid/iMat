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

import static java.lang.Math.max;

public class Spinner extends AnchorPane implements ShoppingItemObservable{
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
        this.buy.toFront();
        this.product = product;
        Scale scale = new Scale();
        if(isProductLine) {
            // Apply scaling transformation to the loaded AnchorPane
            scale.setX(0.4); // Scale X axis by 0.75 (make it smaller)
            scale.setY(0.4); // Scale Y axis by 0.75 (make it smaller)

            // Apply the Scale transformation to the root element of the loaded FXML
            moreThan0Product.toFront();
        } else {
            scale.setX(0.8);
            scale.setY(0.8);
        }
        this.getTransforms().add(scale);
        initAmount();
    }

    public void initAmount() {
        for (ShoppingItem item : dataHandler.getShoppingCart().getItems()) {
            if (this.product.equals(item.getProduct()) && item.getAmount() != 0) {
                this.amount = (int) item.getAmount();
                updateAmount(amount);
            }
        }
        this.buyCounter.setText(String.valueOf(this.amount));
    }

    private void updateAmount(int amount) {
        this.amount = amount;
        this.buyCounter.setText(String.valueOf(this.amount));
        this.moreThan0Product.toFront();
    }

    @FXML
    public void buyLabelClicked(Event event) {
        System.out.println("buyLabelClicked");
        notifyShoppingItemObservers(true);
        event.consume();
    }

    public void addProduct(Event event) {
        System.out.println("addProduct");
        notifyShoppingItemObservers(true);
        event.consume();
    }

    public void subtractProduct(Event event) {
        System.out.println("subtractProduct");
        if(amount > 0) {
            notifyShoppingItemObservers(false);
        }
        event.consume();
    }

    public void update(CartEvent event) {
        System.out.println("Spinner Updated!");
        if(event.getShoppingItem().getProduct().equals(this.product)) {
            System.out.println("Match");
            for(ShoppingItem item: dataHandler.getShoppingCart().getItems()) {
                if(item.getProduct().equals(this.product)) {
                    System.out.println("Found product in cart");
                    System.out.println(item.getAmount() == amount);
                    updateAmount((int) item.getAmount());
                    break;
                }
            }
        }
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public void addShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {
        if(!shoppingItemObservers.contains(shoppingItemObserver)) {
            shoppingItemObservers.add(shoppingItemObserver);
        }
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
