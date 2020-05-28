package database;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by pwilkin on 15-Apr-20.
 */
public class DatabaseTester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dbtest.fxml"));
        Parent root = loader.load();
        Controller ctrl = loader.getController();
        primaryStage.setTitle("PPA PreparedStatement");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        ctrl.setStage(primaryStage);
    }
}
