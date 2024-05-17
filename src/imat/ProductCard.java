package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class ProductCard extends AnchorPane {
    @FXML
    private ImageView productImageView;
    @FXML
    private Label priceLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView favouriteImageVeiw;

    private Product product;
    private boolean isFavourite;


    Image notFavouriteImage = new Image((getClass().getResourceAsStream("src/imat/imat/egnabilder/unfilled_star.png")));
    Image isFavouriteImage = new Image((getClass().getResourceAsStream("src/imat/imat/egnabilder/filled_stpr.png")));

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


    }

    private void initialize(){
        favouriteImageVeiw.setImage(notFavouriteImage);
        isFavourite = false;

    }
    public void FavouriteButtonSelected(){
        if(isFavourite){
            favouriteImageVeiw.setImage(isFavouriteImage);
        }
        else{
            favouriteImageVeiw.setImage(notFavouriteImage);
        }


    }
}
