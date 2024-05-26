package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.Date;

public class PreviousOrder extends AnchorPane {
    @FXML private Button customButton;
    @FXML private Label dateLabel;
    @FXML private Label totalLabel;
    Order order;
    private PreviousOrdersView previousOrdersView;
    private double totalCost = 0;

    public PreviousOrder(Order order, PreviousOrdersView previousOrdersView) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("previous_order.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.previousOrdersView = previousOrdersView;
        this.order = order;
        String date = "";
        Date orderDate = order.getDate();
        date += orderDate.getDate() + "/" + (orderDate.getMonth() + 1) + " " + (orderDate.getYear() + 1900);
        this.dateLabel.setText(date);
        for (ShoppingItem item : order.getItems()) {
            this.totalCost += item.getTotal();
        }
        this.totalLabel.setText(String.valueOf(totalCost) + " kr");

        this.setOnMouseClicked(event -> previousOrderClicked());
    }

    public void previousOrderClicked() {
        previousOrdersView.previousOrderClicked(this);
    }

    public void select() {
        customButton.getStyleClass().add("selected");
    }

    public void deselect() {
        customButton.getStyleClass().remove("selected");
    }
}
