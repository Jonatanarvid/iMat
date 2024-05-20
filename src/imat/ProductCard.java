package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductCard extends AnchorPane implements FavouriteObservable {
    @FXML
    private ImageView productImageView;
    @FXML
    private Label priceLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView favouriteImageView;

    private Product product;
    private boolean isFavourite;
    private List<FavouriteObserver> favouriteObservers = new ArrayList<FavouriteObserver>();

    Image notFavouriteImage = new Image((getClass().getResourceAsStream("resources/imat/egnabilder/unfilled_star.png")));
    Image isFavouriteImage = new Image((getClass().getResourceAsStream("resources/imat/egnabilder/filled_star.png")));

    public ProductCard(Product product, Image image) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_card.fxml"));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.nameLabel.setText(product.getName());
        this.priceLabel.setText(String.valueOf(product.getPrice()));
        this.productImageView.setImage(image);


    }

    private void initialize(){
        favouriteImageView.setImage(notFavouriteImage);
        isFavourite = false;

    }
    public void FavouriteButtonSelected(){
        isFavourite = !isFavourite;
        if(isFavourite){
            favouriteImageView.setImage(isFavouriteImage);

        }
        else{
            favouriteImageView.setImage(notFavouriteImage);
        }
        notifyObservers();
    }

    @Override
    public void addObserver(FavouriteObserver observer) {
        favouriteObservers.add(observer);
    }

    @Override
    public void removeObserver(FavouriteObserver observer) {
        if(favouriteObservers.contains(observer)) {
            favouriteObservers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for(FavouriteObserver observer : favouriteObservers) {
            observer.update(this.product, this.isFavourite);
        }
    }
}
