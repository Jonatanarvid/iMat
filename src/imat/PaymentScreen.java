package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.text.DecimalFormat;

public class PaymentScreen extends StackPane implements ShoppingCartListener {
    @FXML private BorderPane chosenProductsBorderPane;
    @FXML private ScrollPane chosenProductsScrollPane;
    @FXML private Label totalPriceLabel;

    @FXML private BorderPane deliveryBorderPane;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postAddressTextField;
    @FXML private TextField postCodeTextField;

    @FXML private BorderPane cardInfoBorderPane;
    @FXML private TextField firstNameCardTextField;
    @FXML private TextField lastNameCardTextField;
    @FXML private TextField cardNumberTextField;
    @FXML private TextField cardTypeTextField;
    @FXML private TextField validMonthTextField;
    @FXML private TextField validYearTextField;
    @FXML private TextField securityCodeTextField;

    @FXML private BorderPane paymentDonePane;

    private MainViewController mainViewController;
    private ShoppingCartView shoppingCartView;
    private VBox chosenProductsVBox = new VBox();
    private boolean isChosenInited = false;


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
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        this.totalPriceLabel.setText(numberFormat.format(IMatDataHandler.getInstance().getShoppingCart().getTotal()) + " kr");
        this.shoppingCartView = shoppingCartView;
    }

    public void toChosenProducts() {
        updateChosenProducts();
        chosenProductsScrollPane.setFitToWidth(true);
        chosenProductsBorderPane.setVisible(true);
        chosenProductsBorderPane.toFront();
    }

    private void updateChosenProducts() {
        chosenProductsVBox.getChildren().clear();
        chosenProductsVBox.setSpacing(10);
        for(ShoppingItem item: IMatDataHandler.getInstance().getShoppingCart().getItems()) {
            ProductLine productLine = new ProductLine(item.getProduct(), IMatDataHandler.getInstance().getFXImage(item.getProduct()), (int) item.getAmount());
            Scale scale = new Scale();
            scale.setY(1.68);
            scale.setX(1.68);
            productLine.getTransforms().add(scale);
            productLine.addShoppingItemObserver(mainViewController.controller);
            chosenProductsVBox.getChildren().add(productLine);
        }
        this.chosenProductsScrollPane.setContent(chosenProductsVBox);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        this.totalPriceLabel.setText(numberFormat.format(IMatDataHandler.getInstance().getShoppingCart().getTotal()) + " kr");
    }

    public void backToShop() {
        hideBorderPanes();
        shoppingCartView.updateShoppingViewScrollPaneVBox();
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

    public void toCardInfo() {
        hideBorderPanes();
        cardInfoBorderPane.toFront();
        CreditCard creditCard = IMatDataHandler.getInstance().getCreditCard();
        String[] name = creditCard.getHoldersName().split("<");
        try {
            firstNameCardTextField.setText(name[0]);
            lastNameCardTextField.setText(name[1]);
        } catch (Exception ignored) {}
        cardNumberTextField.setText(creditCard.getCardNumber());
        cardTypeTextField.setText(creditCard.getCardType());
        validMonthTextField.setText(String.valueOf(creditCard.getValidMonth()));
        validYearTextField.setText(String.valueOf(creditCard.getValidYear()));
        securityCodeTextField.setText(String.valueOf(creditCard.getVerificationCode()));
        cardInfoBorderPane.setVisible(true);
    }

    public void saveCustomer() {
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        customer.setFirstName(firstNameTextField.getText());
        customer.setLastName(lastNameTextField.getText());
        customer.setEmail(emailTextField.getText());
        customer.setPhoneNumber(phoneNumberTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setPostAddress(postAddressTextField.getText());
        customer.setPostCode(postCodeTextField.getText());
    }

    public void saveCard() {
        CreditCard creditCard = IMatDataHandler.getInstance().getCreditCard();
        String name = firstNameCardTextField.getText() + "<" + lastNameCardTextField.getText();
        creditCard.setHoldersName(name);
        creditCard.setCardNumber(cardNumberTextField.getText());
        creditCard.setCardType(cardTypeTextField.getText());
        Integer validMonth = null;
        Integer validYear = null;
        Integer securityCode = null;
        try {
            validMonth = new Integer(validMonthTextField.getText());
            validYear = new Integer(validYearTextField.getText());
            securityCode = new Integer(securityCodeTextField.getText());
        } catch (Exception ignored) {}

        if (validMonth != null) {
            creditCard.setValidMonth(validMonth);
        }
        if (validYear != null) {
            creditCard.setValidYear(validYear);
        }
        if (securityCode != null) {
            creditCard.setVerificationCode(securityCode);
        }

        deliveryBorderPane.setVisible(true);
    }

    public void toPaymentDone() {
        IMatDataHandler.getInstance().placeOrder();
        hideBorderPanes();
        this.paymentDonePane.setVisible(true);
        this.paymentDonePane.toFront();
    }

    private void hideBorderPanes() {
        this.chosenProductsBorderPane.setVisible(false);
        this.deliveryBorderPane.setVisible(false);
        this.cardInfoBorderPane.setVisible(false);
        this.paymentDonePane.setVisible(false);
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        this.totalPriceLabel.setText(numberFormat.format(IMatDataHandler.getInstance().getShoppingCart().getTotal()) + " kr");
        updateChosenProducts();
    }
}
