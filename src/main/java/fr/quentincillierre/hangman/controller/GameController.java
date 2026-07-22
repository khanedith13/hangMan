package fr.quentincillierre.hangman.controller;

import java.util.stream.Collectors;

import fr.quentincillierre.hangman.model.HangmanModel;
import fr.quentincillierre.hangman.model.WordRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GameController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label difficultyLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label wrongGuessesLabel;

    @FXML
    private Label executionProgressLabel;

    @FXML
    private Label guessedLettersLabel;

    @FXML
    private Label wordLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private TextField guessTextField;

    @FXML
    private Button guessButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button nextLevelButton;

    @FXML
    private ImageView hangmanImageView;

    private HangmanModel model;
    private Timeline countdownTimeline;
    private int timeLeftSeconds;
    private String difficulty;

    @FXML
    public void initialize() {
        resultLabel.setVisible(false);
        restartButton.setVisible(false);
        nextLevelButton.setVisible(false);
        guessTextField.setText("");
        guessTextField.setDisable(true);
        guessButton.setDisable(true);
        wrongGuessesLabel.setText("Wrong guesses: 0 / 10");
        executionProgressLabel.setText("Execution progress: 0 / 10");
        guessedLettersLabel.setText("Guessed: ");
    }

    public void initializeGame() {
        startGame();
    }

    private void startGame() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        if (difficulty == null) {
            difficulty = "easy";
        }

        WordRepository wordRepository = new WordRepository();
        String resource;
        switch (difficulty.toLowerCase()) {
            case "average":
                resource = "/average-words.txt";
                timeLeftSeconds = 60;
                break;
            case "hard":
                resource = "/hard-words.txt";
                timeLeftSeconds = 30;
                break;
            default:
                resource = "/easy-words.txt";
                timeLeftSeconds = 90;
                break;
        }

        model = new HangmanModel(wordRepository.getRandomWord(resource));

        levelLabel.setText("LEVEL: " + difficulty.toUpperCase());
        difficultyLabel.setText("Difficulty: " + difficulty.toUpperCase());
        resultLabel.setVisible(false);
        resultLabel.setText("");
        guessTextField.setDisable(false);
        guessButton.setDisable(false);
        guessTextField.setText("");
        guessTextField.requestFocus();
        restartButton.setVisible(false);
        nextLevelButton.setVisible(false);

        startCountdown();
        refreshUI();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    private void startCountdown() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            timeLeftSeconds--;
            timerLabel.setText(formatTime(timeLeftSeconds));
            if (timeLeftSeconds <= 0) {
                countdownTimeline.stop();
                model.forceLose();
                handleGameOver("TIME UP");
            }
        }));
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        timerLabel.setText(formatTime(timeLeftSeconds));
        countdownTimeline.playFromStart();
    }

    private String formatTime(int seconds) {
        int minutes = Math.max(0, seconds) / 60;
        int remainder = Math.max(0, seconds) % 60;
        return String.format("%02d:%02d", minutes, remainder);
    }

    private void refreshUI() {
        wordLabel.setText(formatHiddenWord(model.getHiddenWord()));
        wrongGuessesLabel.setText("Wrong guesses: " + model.getCurrentWrongs() + " / 10");
        executionProgressLabel.setText("Execution progress: " + model.getCurrentWrongs() + " / 10");
        guessedLettersLabel.setText("Guessed: " + formatGuessedLetters());

        String imagePath = "/pictures/%s-hangman.png".formatted(model.getCurrentWrongs());
        hangmanImageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));

        if (model.isWin() || model.isLose()) {
            guessTextField.setDisable(true);
            guessButton.setDisable(true);
            restartButton.setVisible(true);
            nextLevelButton.setVisible(model.isWin() && hasNextLevel());
            resultLabel.setVisible(true);
            resultLabel.setText(model.isWin() ? "CODE CRACKED!" : "EXECUTED!");
            wordLabel.setText(formatHiddenWord(model.getWordToGuess()));
            if (countdownTimeline != null) {
                countdownTimeline.stop();
            }
        }
    }

    private String formatHiddenWord(String hidden) {
        return hidden.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining(" "));
    }

    private String formatGuessedLetters() {
        if (model.getGuessedLetter().isEmpty()) {
            return "none";
        }
        return model.getGuessedLetter().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    public void handleKeyboardInput(String character) {
        if (model == null || model.isWin() || model.isLose()) {
            return;
        }
        if (character != null && character.length() > 0) {
            char letter = Character.toUpperCase(character.charAt(0));
            if (letter >= 'A' && letter <= 'Z') {
                model.tryLetter(letter);
                guessTextField.clear();
                refreshUI();
            }
        }
    }

    @FXML
    private void onGuess() {
        if (guessTextField == null) {
            return;
        }
        String input = guessTextField.getText().trim();
        if (input.length() > 0) {
            handleKeyboardInput(input.substring(0, 1));
        }
    }

    @FXML
    private void onRestart() {
        startGame();
    }

    @FXML
    private void onNextLevel() {
        if ("easy".equalsIgnoreCase(difficulty)) {
            difficulty = "average";
        } else if ("average".equalsIgnoreCase(difficulty)) {
            difficulty = "hard";
        }
        startGame();
    }

    private boolean hasNextLevel() {
        return "easy".equalsIgnoreCase(difficulty) || "average".equalsIgnoreCase(difficulty);
    }

    private void handleGameOver(String message) {
        resultLabel.setVisible(true);
        resultLabel.setText(message);
        refreshUI();
    }
}
