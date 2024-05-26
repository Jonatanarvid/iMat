package imat;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class PreviousOrdersView extends  AnchorPane{
    @FXML private VBox previousOrdersScrollPaneVBox;
    @FXML private VBox orderDetailScrollPaneVBox;
    @FXML private Label totalDetailPriceLabel;
    private MainViewController mainViewController;
    private ShoppingCartView shoppingCartView;

    public PreviousOrdersView(MainViewController mainViewController, ShoppingCartView shoppingCartView) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("previousOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (
                IOException exception) {
            throw new RuntimeException(exception);
        }
        update();
        this.mainViewController = mainViewController;
        this.shoppingCartView = shoppingCartView;
    }

    public void previousOrderClicked(PreviousOrder order) {
        this.orderDetailScrollPaneVBox.getChildren().clear();
        this.orderDetailScrollPaneVBox.setSpacing(20);
        double price = 0;
        for(ShoppingItem item : order.order.getItems()) {
            IMatDataHandler dataHandler = IMatDataHandler.getInstance();
            ProductLine productLine = new ProductLine(item.getProduct(), dataHandler.getFXImage(item.getProduct()), (int) item.getAmount());
            productLine.hideSpinner((int) item.getAmount());

            Scale scale = new Scale();
            scale.setY(1.68);
            scale.setX(1.68);
            productLine.getTransforms().add(scale);

            price += item.getTotal();
            orderDetailScrollPaneVBox.getChildren().add(productLine);
        }
        this.totalDetailPriceLabel.setText(String.valueOf(price) + " kr");
    }

    public void update() {
        IMatDataHandler dataHandler = IMatDataHandler.getInstance();
        previousOrdersScrollPaneVBox.getChildren().clear();
        for(Order order : dataHandler.getOrders()) {
            previousOrdersScrollPaneVBox.getChildren().add(new PreviousOrder(order, this));
        }
    }

    public void backToShop() {
        mainViewController.backToShop();
    }
}