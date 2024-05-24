package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.ShoppingCartListener;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentScreen extends StackPane implements ShoppingCartListener {
    @FXML private BorderPane chosenProductsBorderPane;
    @FXML private BorderPane deliveryBorderPane;
    @FXML private ScrollPane chosenProductsScrollPane;
    @FXML private Label totalPriceLabel;

    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postAddressTextField;
    @FXML private TextField postCodeTextField;

    private MainViewController mainViewController;
    private ShoppingCartView shoppingCartView;

    public PaymentScreen(MainViewController mainViewController, ShoppingCartView shoppingCartView) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment_screen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.mainViewController = mainViewController;
        this.chosenProductsBorderPane.toFront();
        IMatDataHandler.getInstance().getShoppingCart().addShoppingCartListener(this);
        this.totalPriceLabel.setText(String.valueOf(IMatDataHandler.getInstance().getShoppingCart().getTotal()) + " kr");
        this.shoppingCartView = shoppingCartView;
    }

    public void toChosenProducts() {
        this.chosenProductsScrollPane.setContent(shoppingCartView.shoppingViewScrollPaneVBox);
        hideBorderPanes();
        chosenProductsBorderPane.setVisible(true);
        chosenProductsBorderPane.toFront();
    }

    public void backToShop() {
        hideBorderPanes();
        mainViewController.backToShop();
    }

    public void toDelivery() {
        hideBorderPanes();
        deliveryBorderPane.toFront();
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        firstNameTextField.setText(customer.getFirstName());
        System.out.println(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        emailTextField.setText(customer.getEmail());
        phoneNumberTextField.setText(customer.getPhoneNumber());
        addressTextField.setText(customer.getAddress());
        postAddressTextField.setText(customer.getPostAddress());
        postCodeTextField.setText(customer.getPostCode());
        deliveryBorderPane.setVisible(true);
    }

     public void saveCustomer() {
        System.out.println("Customer saved!");
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        customer.setFirstName(firstNameTextField.getText());
        customer.setLastName(lastNameTextField.getText());
        customer.setEmail(emailTextField.getText());
        customer.setPhoneNumber(phoneNumberTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setPostAddress(postAddressTextField.getText());
        customer.setPostCode(postCodeTextField.getText());
    }

    private void hideBorderPanes() {
        this.chosenProductsBorderPane.setVisible(false);
        this.deliveryBorderPane.setVisible(false);
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        this.totalPriceLabel.setText(String.valueOf(IMatDataHandler.getInstance().getShoppingCart().getTotal()) + " kr");
    }
}
