package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCartListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductCard extends AnchorPane implements FavouriteObservable, ShoppingCartListener {
    @FXML
    private ImageView productImageView;
    @FXML
    private Label priceLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView favouriteImageView;
    @FXML
    private Button favouriteButton;
    @FXML
    private Label buyLabel;
    @FXML
    private Label withSuffix;
    @FXML
    private AnchorPane mainPainProductCard;

    @FXML
    private StackPane spinnerStackPane;
    @FXML
    private Rectangle cardFeedback;


    private final Product product;
    private boolean isFavourite;
    private List<FavouriteObserver> favouriteObservers = new ArrayList<FavouriteObserver>();
    private List<ShoppingItemObserver> shoppingItemObservers = new ArrayList<ShoppingItemObserver>();
    private final Spinner spinner;

    Image notFavouriteImage = new Image((getClass().getResourceAsStream("resources/imat/egnabilder/unfilled_star.png")));
    Image isFavouriteImage = new Image((getClass().getResourceAsStream("resources/imat/egnabilder/filled_star.png")));

    public ProductCard(Product product, Image image) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.nameLabel.setText(product.getName());
        this.priceLabel.setText(String.valueOf(product.getPrice()));
        this.productImageView.setImage(image);
        this.product = product;
        this.productImageView.setImage(image);
        this.spinner = new Spinner(this.product, false);
        this.spinnerStackPane.getChildren().clear();
        this.spinnerStackPane.getChildren().add(spinner);
        this.withSuffix.setText(String.valueOf(product.getPrice()+product.getUnit()));
        initialize();

        // Apply scaling transformation to the loaded AnchorPane
        //Scale scale = new Scale();
        //scale.setX(0.85); // Scale X axis by 0.75 (make it smaller)
        //scale.setY(0.85); // Scale Y axis by 0.75 (make it smaller)

        // Apply the Scale transformation to the root element of the loaded FXML
        //this.getTransforms().add(scale);
    }

    private void initialize() {
        isFavourite = IMatDataHandler.getInstance().favorites().contains(this.product);
        if (isFavourite) {
            this.favouriteImageView.setImage(isFavouriteImage);
        } else {
            this.favouriteImageView.setImage(notFavouriteImage);
        }
    }


    @FXML
    private void favouriteButtonSelected() {
        isFavourite = !isFavourite;
        if (isFavourite) {
            favouriteImageView.setImage(isFavouriteImage);
        } else {
            favouriteImageView.setImage(notFavouriteImage);
        }
        notifyFavouriteObservers();
    }

    @Override
    public void addFavouriteObserver(FavouriteObserver observer) {
        favouriteObservers.add(observer);
    }

    @Override
    public void removeFavouriteObserver(FavouriteObserver observer) {
        if (favouriteObservers.contains(observer)) {
            favouriteObservers.remove(observer);
        }
    }

    @Override
    public void notifyFavouriteObservers() {
        for (FavouriteObserver observer : favouriteObservers) {
            observer.updateFavouriteObserver(this.product, this.isFavourite);
        }
    }
    public void addShoppingItemObserver(ShoppingItemObserver observer) {
        spinner.addShoppingItemObserver(observer);
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        if(cartEvent.getShoppingItem().getProduct().equals(this.product)) {
            spinner.update(cartEvent);
        }
    }
    @FXML
    public void cardEntered(){
        cardFeedback.setVisible(true);
    }
    @FXML
    public void cardExited(){
        cardFeedback.setVisible(false);
    }
}