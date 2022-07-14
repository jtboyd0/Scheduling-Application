package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This is the main class of the application
 * @author Jacob Boyd
 */
public class QAM1 extends Application {

    /**
     * start loads and shows the login form.
     * @param stage The stage
     * @throws IOException if the login form does not exist or is misplaced.
     */
    @Override public void start(Stage stage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginForm1.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * main is the Main method of this application.
     * @param args The arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
