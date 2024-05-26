package imat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class Spinner extends AnchorPane implements ShoppingItemObservable {
    @FXML
    private Button buttonCardMinus;
    @FXML
    private Button buttonCardPlus;
    @FXML
    private BorderPane spinnerBox;
    @FXML
    private Label buyCounter;
    @FXML
    private Button buyButton;

    private int amount = 0;
    private final Product product;
    private final IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private List<ShoppingItemObserver> shoppingItemObservers = new ArrayList<>();

    public Spinner(Product product, boolean small) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Spinner.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;

        // Apply the appropriate style based on whether the spinner is small or not
        applyStyles(small);
        initAmount();
    }

    private void applyStyles(boolean small) {
        /*if (small) {
            this.getStyleClass().addAll("custom-button-small", "button-content-small");
            buttonCardMinus.getStyleClass().addAll("spinner-sub-button-small");
            buttonCardPlus.getStyleClass().addAll("spinner-sub-button-small");
            buyCounter.getStyleClass().add("counter-label-small");
        }*/
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
        this.amount = max(amount, 0);
        this.buyCounter.setText(String.valueOf(this.amount));
        if (this.amount > 0) {
            this.spinnerBox.setVisible(true);
            this.buyButton.setVisible(false);
        } else {
            this.spinnerBox.setVisible(false);
            this.buyButton.setVisible(true);
        }
    }

    @FXML
    public void buyLabelClicked(ActionEvent event) {
        updateAmount(1);
        notifyShoppingItemObservers(true, false);
    }

    @FXML
    public void addProduct(ActionEvent event) {
        updateAmount(this.amount + 1);
        notifyShoppingItemObservers(true, false);
    }

    @FXML
    public void subtractProduct(ActionEvent event) {
        if (this.amount > 1) {
            updateAmount(this.amount - 1);
            notifyShoppingItemObservers(false, false);
        } else {
            updateAmount(0);
            notifyShoppingItemObservers(false, true);
        }
    }

    public void update(CartEvent event) {
        if (event.getShoppingItem().getProduct().equals(this.product)) {
            if (event.isAddEvent()) {
                for (ShoppingItem item : dataHandler.getShoppingCart().getItems()) {
                    if (item.getProduct().equals(this.product)) {
                        updateAmount((int) item.getAmount());
                        break;
                    }
                }
            } else {
                this.amount = 0;
                this.buyButton.setVisible(true);
                this.spinnerBox.setVisible(false);
            }
        }
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public void addShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {
        if (!shoppingItemObservers.contains(shoppingItemObserver)) {
            shoppingItemObservers.add(shoppingItemObserver);
        }
    }

    @Override
    public void removeShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {
        shoppingItemObservers.remove(shoppingItemObserver);
    }

    @Override
    public void notifyShoppingItemObservers(boolean isAdd, boolean isDelete) {
        for (ShoppingItemObserver observer : shoppingItemObservers) {
            observer.updateShoppingItemObserver(this.product, isAdd, isDelete);
        }
    }
}