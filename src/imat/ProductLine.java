package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCartListener;

import java.io.IOException;

public class ProductLine implements ShoppingCartListener {
    @FXML private Label amountLabel;
    @FXML private ImageView productImageView;
    @FXML private Label priceLabel;
    @FXML private Label nameLabel;
    private Product product;
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
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        if(this.product.equals(cartEvent.getShoppingItem().getProduct())) {
            this.amountLabel.setText(String.valueOf(cartEvent.getShoppingItem().getAmount()));
            this.priceLabel.setText(String.valueOf(cartEvent.getShoppingItem().getTotal()));
        }
    }
}
