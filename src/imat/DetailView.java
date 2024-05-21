package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductDetail;

import java.io.IOException;


public class DetailView {
    @FXML
    private TextArea marke;
    @FXML
    private TextArea ursprung;
    @FXML
    private TextArea beskrivning;
    @FXML
    private TextArea innehall;
    public DetailView() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("detailView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

    }
    public void populateDetailView(Product product, ProductDetail productDetail) {
        marke.setText(productDetail.getBrand());
        ursprung.setText(productDetail.getOrigin());
        beskrivning.setText(productDetail.getDescription());
        innehall.setText(productDetail.getContents());

    }

}
