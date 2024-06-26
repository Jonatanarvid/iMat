package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.text.DecimalFormat;

public class ProductLine extends AnchorPane {
    @FXML private ImageView productImageView;
    @FXML private Label priceLabel;
    @FXML private Label nameLabel;
    @FXML private StackPane amountSpinnerStackPane;
    @FXML private Label amountLabel;
    private Product product;
    private final Spinner amountSpinner;

    public ProductLine(Product product, Image image, int amount) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_line.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.amountLabel.setVisible(false);
        this.product = product;
        this.nameLabel.setText(product.getName());
        this.productImageView.setImage(image);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        this.priceLabel.setText(numberFormat.format(product.getPrice() * amount) + " kr");
        Scale scale = new Scale();
        scale.setX(0.5);
        scale.setY(0.5);

        amountSpinner = new Spinner(this.product, true);
        amountSpinner.getTransforms().add(scale);
        amountSpinnerStackPane.setStyle("-fx-alignment: center;");
        amountSpinner.setTranslateY(5);

        amountSpinnerStackPane.setAlignment(Pos.BOTTOM_CENTER);
        amountSpinnerStackPane.getChildren().add(amountSpinner);
    }

    public void addShoppingItemObserver(ShoppingItemObserver observer) {
        amountSpinner.addShoppingItemObserver(observer);
    }

    public void updateProducts(CartEvent event) {
        amountSpinner.update(event);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        this.priceLabel.setText(numberFormat.format(this.product.getPrice() * amountSpinner.getAmount()) + " kr");
    }

    public void hideSpinner(int amount) {
        this.amountSpinner.setVisible(false);
        this.amountLabel.setText(String.valueOf(amount));
        this.amountLabel.setVisible(true);
        this.amountLabel.toFront();
    }
}