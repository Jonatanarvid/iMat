package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;

public class ProductLine extends AnchorPane {
    @FXML private ImageView productImageView;
    @FXML private Label priceLabel;
    @FXML private Label nameLabel;
    @FXML private StackPane amountSpinnerStackPane;

    private double price;
    private Product product;
    private final Spinner amountSpinner;

    public ProductLine(Product product, Image image) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_line.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.product = product;
        this.nameLabel.setText(product.getName());
        this.productImageView.setImage(image);


        this.price = product.getPrice();
        this.priceLabel.setText(String.valueOf(this.price));


        amountSpinner = new Spinner(this.product, true);
        amountSpinnerStackPane.getChildren().add(amountSpinner);
    }

    public void addShoppingItemObserver(ShoppingItemObserver observer) {
        amountSpinner.addShoppingItemObserver(observer);
    }

    /*
    public ProductLine updateLabels(double amount, double total) {
        this.amount += amount;
        price += total;
        this.amountLabel.setText(String.valueOf(this.amount));
        this.priceLabel.setText(String.valueOf(price));
        return this;
    }
     */
}