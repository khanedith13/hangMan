package fr.quentincillierre.hangman.controller;

import fr.quentincillierre.hangman.application.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    private void onStartGame() {
        try {
            MainApp.showIntroScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }
}
