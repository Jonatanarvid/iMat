package imat;


import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class iMatApp extends Application {
    private final BackendController backend = new BackendController();
    
    @Override
    public void start(Stage stage) throws Exception {

        ResourceBundle bundle = java.util.ResourceBundle.getBundle("imat/resourcesImages/iMat");
        
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("imat_app.fxml")), bundle);
        
        Scene scene = new Scene(root, 1800, 1000);
        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        backend.start();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
