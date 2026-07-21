package fr.quentincillierre.hangman.controller;

import fr.quentincillierre.hangman.model.HangmanModel;
import fr.quentincillierre.hangman.model.WordRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class GameController {

    @FXML
    private Label storyLabel;

    @FXML
    private Label wordLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private ImageView hangmanImageView;

    @FXML
    private GridPane keyboardGrid;

    private HangmanModel model;
    private Timeline storyTimeline;
    private String storyText;
    private Timeline countdownTimeline;
    private int timeLeftSeconds;
    private String difficulty;

    // Automatically call by JavaFX when FXML file is loaded
    @FXML
    public void initialize() {
        storyText = "You have been captured by an unknown organization." +
                "\n\nTo earn your freedom, you must crack three security codes." +
                "\n\nEach failed guess brings you one step closer to execution." +
                "\n\nCrack every code before time runs out..." +
                "\n\n...or face execution.";

        storyLabel.setText("");
        storyLabel.setVisible(true);
        wordLabel.setVisible(false);
        resultLabel.setVisible(false);
        hangmanImageView.setVisible(false);
        keyboardGrid.setVisible(false);
        keyboardGrid.setDisable(true);

        generateKeyboard();
        animateStoryIntro();
    }

    private void animateStoryIntro() {
        storyTimeline = new Timeline();
        int totalChars = storyText.length();
        double interval = 10000.0 / Math.max(totalChars, 1);

        for (int i = 0; i < totalChars; i++) {
            int index = i + 1;
            storyTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(interval * index), event -> {
                storyLabel.setText(storyText.substring(0, index));
            }));
        }

        storyTimeline.setOnFinished(event -> startGame());
        storyTimeline.play();
    }

    private void startGame() {
        storyLabel.setVisible(false);
        wordLabel.setVisible(true);
        resultLabel.setVisible(true);
        hangmanImageView.setVisible(true);
        keyboardGrid.setVisible(true);
        keyboardGrid.setDisable(false);

        WordRepository wordRepository = new WordRepository();
        String resource = "/words.txt";
        int seconds = 10;
        if (this.difficulty != null){
            switch (this.difficulty.toLowerCase()){
                case "easy":
                    resource = "/easy-words.txt"; seconds = 10; break;
                case "average":
                    resource = "/average-words.txt"; seconds = 15; break;
                case "hard":
                    resource = "/hard-words.txt"; seconds = 20; break;
                default:
                    resource = "/words.txt"; seconds = 10;
            }
        }

        this.model = new HangmanModel(wordRepository.getRandomWord(resource));
        this.timeLeftSeconds = seconds;
        startCountdown();

        refreshUI();
    }

    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }

    private void startCountdown(){
        if (countdownTimeline != null){
            countdownTimeline.stop();
        }
        countdownTimeline = new Timeline();
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), ev -> {
            timeLeftSeconds--;
            timerLabel.setText(String.format("%02d", timeLeftSeconds));
            if (timeLeftSeconds <= 0){
                countdownTimeline.stop();
                // force lose
                model.forceLose();
                resultLabel.setText("Time's up!");
                keyboardGrid.setDisable(true);
                refreshUI();
            }
        }));
        // update initial display
        timerLabel.setText(String.format("%02d", timeLeftSeconds));
        countdownTimeline.playFromStart();
    }

    private void refreshUI() {
        wordLabel.setText(model.getHiddenWord());

        hangmanImageView.setImage(new Image(getClass().getResource( "/pictures/%s-hangman.png".formatted(model.getCurrentWrongs())).toExternalForm()));

        if (model.isLose() || model.isWin()){
            keyboardGrid.setDisable(true);
            wordLabel.setText(model.getWordToGuess());
            resultLabel.setOpacity(1);
            resultLabel.setAlignment(Pos.CENTER);
            if (model.isWin()){
                resultLabel.setText("Victory !");
            }
            else {
                resultLabel.setText("Game Over !");
            }
        }
    }

    private void generateKeyboard() {
        keyboardGrid.getChildren().clear();
        String[] rows = {"QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM"};

        for (int row = 0; row < rows.length; row++) {
            String rowLetters = rows[row];
            int offset = row == 0 ? 0 : row == 1 ? 1 : 2;

            for (int col = 0; col < rowLetters.length(); col++) {
                char c = rowLetters.charAt(col);
                Button letterButton = new Button(String.valueOf(c));
                letterButton.setPrefWidth(40);
                letterButton.setPrefHeight(40);
                letterButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                letterButton.setOnAction(event -> {
                    if (!letterButton.isDisable()) {
                        handleKeyboardInput(letterButton.getText());
                        letterButton.setDisable(true);
                    }
                });
                keyboardGrid.add(letterButton, offset + col, row);
            }
        }
    }

    public void handleKeyboardInput(String character){

        if (model.isWin() || model.isLose()){
            return;
        }
        if (character != null && character.length() == 1){

            char letter = Character.toUpperCase(character.charAt(0));

            if ('A' <= letter && letter <= 'Z'){
                model.tryLetter(letter);

                refreshUI();
            }
        }
    }
}