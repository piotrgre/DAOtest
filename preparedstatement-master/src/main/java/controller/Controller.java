package controller;

import database.DatabaseLogic;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Created by pwilkin on 15-Apr-20.
 */
public class Controller {

    @FXML
    public TextArea debug;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initialize() {
        debug.setEditable(false);
        DatabaseLogic logic = new DatabaseLogic();
        debug.textProperty().setValue("Wykonano zapytanie, wynik: " + logic.performTests());
    }
}
