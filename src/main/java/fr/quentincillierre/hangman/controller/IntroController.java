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
    private Button easyButton;

    @FXML
    private Button averageButton;

    @FXML
    private Button hardButton;

    @FXML
    private Button exitButton;

    private final String storyText = "Year 2084...\n\n" +
            "You have been captured.\n\n" +
            "Three encrypted security systems block your escape.\n\n" +
            "Crack the codes before your execution.";

    @FXML
    public void initialize() {
        storyLabel.setText("");
        // hide buttons until narration finishes
        easyButton.setVisible(false);
        averageButton.setVisible(false);
        hardButton.setVisible(false);
        exitButton.setVisible(false);
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

        storyTimeline.setOnFinished(event -> {
            easyButton.setVisible(true);
            averageButton.setVisible(true);
            hardButton.setVisible(true);
            exitButton.setVisible(true);
        });
        storyTimeline.play();
    }

    @FXML
    private void onStartEasy() throws Exception {
        MainApp.showGameSceneWithDifficulty("easy");
    }

    @FXML
    private void onStartAverage() throws Exception {
        MainApp.showGameSceneWithDifficulty("average");
    }

    @FXML
    private void onStartHard() throws Exception {
        MainApp.showGameSceneWithDifficulty("hard");
    }

    @FXML
    private void onExit() {
        try {
            MainApp.showExecutedScene();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

}
