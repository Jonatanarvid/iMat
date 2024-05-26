package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class PreviousOrder extends AnchorPane {
    @FXML private Label dateLabel;
    @FXML private Label totalLabel;
    private Order order;
    private PreviousOrdersView previousOrdersView;
    private double totalCost = 0;

    public PreviousOrder(Order order, PreviousOrdersView previousOrdersView) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("previous_order.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (
                IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;
        this.dateLabel.setText(String.valueOf(order.getDate()));
        for(ShoppingItem item : order.getItems()) {
            this.totalCost += item.getTotal();
        }
        this.totalLabel.setText(String.valueOf(totalCost) + " kr");
    }

    public void previousOrderClicked() {
        previousOrdersView.previousOrderClicked(this);
    }
}
