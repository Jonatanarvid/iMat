package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCartListener;

import java.io.IOException;

public class ProductLine extends AnchorPane {
    @FXML private Label amountLabel;
    @FXML private ImageView productImageView;
    @FXML private Label priceLabel;
    @FXML private Label nameLabel;
    private double amount;
    private double price;
    private Product product;
    public ProductLine(Product product, Image image, double amount, double price) {
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

        this.amount = amount;
        this.price = price;
        this.amountLabel.setText(String.valueOf(this.amount));
        this.priceLabel.setText(String.valueOf(this.price));
    }

    public ProductLine updateLabels(double amount, double total) {
        this.amount += amount;
        price += total;
        this.amountLabel.setText(String.valueOf(this.amount));
        this.priceLabel.setText(String.valueOf(price));
        return this;
    }
}