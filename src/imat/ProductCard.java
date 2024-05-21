package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductCard extends AnchorPane implements FavouriteObservable, ShoppingItemObservable {
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

    private final Product product;
    private boolean isFavourite;
    private List<FavouriteObserver> favouriteObservers = new ArrayList<FavouriteObserver>();
    private List<ShoppingItemObserver> shoppingItemObservers = new ArrayList<ShoppingItemObserver>();

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
        initialize();
    }

    private void initialize(){
        favouriteImageView.setImage(notFavouriteImage);
        isFavourite = false; // This should not exist, buttonImages should always reflect the state that is kept in backend
        //Fine for temporary visual implementation I guess though
    }

    @FXML
    private void favouriteButtonSelected(){
        isFavourite = !isFavourite;
        if(isFavourite){
            favouriteImageView.setImage(isFavouriteImage);
        }
        else{
            favouriteImageView.setImage(notFavouriteImage);
        }
        notifyFavouriteObservers();
    }

    public void buyLabelClicked() {
        notifyShoppingItemObservers();
    }

    @Override
    public void addFavouriteObserver(FavouriteObserver observer) {
        favouriteObservers.add(observer);
    }

    @Override
    public void removeFavouriteObserver(FavouriteObserver observer) {
        if(favouriteObservers.contains(observer)) {
            favouriteObservers.remove(observer);
        }
    }

    @Override
    public void notifyFavouriteObservers() {
        for(FavouriteObserver observer : favouriteObservers) {
            observer.updateFavouriteObserver(this.product, this.isFavourite);
        }
    }

    @Override
    public void addShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {
        shoppingItemObservers.add(shoppingItemObserver);
    }

    @Override
    public void removeShoppingItemObserver(ShoppingItemObserver shoppingItemObserver) {
        if(shoppingItemObservers.contains(shoppingItemObserver)) {
            shoppingItemObservers.remove(shoppingItemObserver);
        }
    }

    @Override
    public void notifyShoppingItemObservers() {
        for(ShoppingItemObserver shoppingItemObserver : shoppingItemObservers) {
            shoppingItemObserver.updateShoppingItemObserver(this.product);
        }
    }
}