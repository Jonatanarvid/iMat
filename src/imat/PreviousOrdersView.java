package imat;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;

import java.awt.*;
import java.io.IOException;

public class PreviousOrdersView extends  AnchorPane{
    @FXML private VBox previousOrdersScrollPaneVBox;
    @FXML private VBox orderDetailScrollPaneVBox;
    private MainViewController mainViewController;

    public PreviousOrdersView(MainViewController mainViewController) {
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
    }

    public void previousOrderClicked(PreviousOrder order) {
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