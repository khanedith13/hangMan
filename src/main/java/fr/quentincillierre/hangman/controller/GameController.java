package fr.quentincillierre.hangman.controller;

import fr.quentincillierre.hangman.model.HangmanModel;
import fr.quentincillierre.hangman.model.WordRepository;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private Label wordLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private ImageView hangmanImageView;

    @FXML
    private GridPane keyboardGrid;

    private HangmanModel model;

    // Automatically call by JavaFX when FXML file is loaded
    @FXML
    public void initialize() {
        WordRepository wordRepository = new WordRepository();

        this.model = new HangmanModel(wordRepository.getRandomWord());

        //UI update with "_____"
        refreshUI();

        //Loading letters buttons
        generateKeyboard();

        resultLabel.setOpacity(0);
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