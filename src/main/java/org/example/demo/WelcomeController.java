package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WelcomeController {

    @FXML
    public void switchToLoginScene(ActionEvent event){
        DBUtils.changeScene(event, "caRRent", "sample.fxml", null);
    }
}
