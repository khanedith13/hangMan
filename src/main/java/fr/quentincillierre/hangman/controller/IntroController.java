package fr.quentincillierre.hangman.controller;

import fr.quentincillierre.hangman.application.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class IntroController {

    @FXML
    private Label storyLabel;

    @FXML
    private Button continueButton;

    @FXML
    private Button exitButton;

    private final String storyText = "Year 2084...\n\n" +
            "You have been captured.\n\n" +
            "Three encrypted security systems block your escape.\n\n" +
            "Crack the codes before your execution.";

    @FXML
    public void initialize() {
        storyLabel.setText("");
        continueButton.setDisable(true);
        animateStoryIntro();
    }

    private void animateStoryIntro() {
        Timeline storyTimeline = new Timeline();
        int totalChars = storyText.length();
        double intervalMillis = 35.0;

        for (int i = 0; i < totalChars; i++) {
            int index = i + 1;
            storyTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(intervalMillis * index), event -> {
                storyLabel.setText(storyText.substring(0, index));
            }));
        }

        storyTimeline.setOnFinished(event -> continueButton.setDisable(false));
        storyTimeline.play();
    }

    @FXML
    private void onContinue() throws Exception {
        MainApp.showGameScene();
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }
}
