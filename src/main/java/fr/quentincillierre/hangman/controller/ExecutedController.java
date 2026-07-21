package fr.quentincillierre.hangman.controller;

import fr.quentincillierre.hangman.application.MainApp;
import javafx.fxml.FXML;

public class ExecutedController {

    @FXML
    private void onRestart() {
        try {
            MainApp.showIntroScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
