package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductDetail;

import java.io.IOException;


public class DetailView extends AnchorPane{
    @FXML private AnchorPane productCardAnchorPane;
    @FXML private ImageView favouriteImageView;
    @FXML private Label brand;
    @FXML private Label origin;
    @FXML private Label description;
    @FXML private Label ingredients;
    private ProductCard productCard;
    private MainViewController mainViewController;

    public DetailView(ProductCard productCard, MainViewController mainViewController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detailView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.productCard = productCard;
        Scale scale = new Scale();
        scale.setX(2);
        scale.setY(2);
        this.productCard.getTransforms().add(scale);

        this.productCardAnchorPane.getChildren().clear();
        this.productCardAnchorPane.getChildren().add(productCard);
        this.mainViewController = mainViewController;
        this.favouriteImageView.setImage(productCard.getFavouriteImage());

        ProductDetail details = IMatDataHandler.getInstance().getDetail(productCard.getProduct());
        this.brand.setText("Märke: " + details.getBrand());
        this.origin.setText("Ursprung: " + details.getOrigin());
        this.description.setText("Beskrivning: " + details.getDescription());
        this.ingredients.setText("Innehåll: " + details.getContents());
    }

    public void favouriteButtonSelected() {
        this.productCard.favouriteButtonSelected();
        this.favouriteImageView.setImage(this.productCard.getFavouriteImage());
    }
    public void closeDetailView() {
        mainViewController.closeDetailView();
    }
}
