package fr.quentincillierre.hangman.application;

import fr.quentincillierre.hangman.controller.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static Scene gameScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showIntroScene();
    }

    public static void showIntroScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("intro-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 650, 650);
        primaryStage.setTitle("Codebreaker: Freedom | Execution");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showGameScene() throws Exception {
        showGameSceneWithDifficulty("easy");
    }

    public static void showGameSceneWithDifficulty(String difficulty) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("game-view.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();
        Scene scene = new Scene(root, 650, 650);
        scene.setOnKeyTyped(event -> controller.handleKeyboardInput(event.getCharacter()));

        controller.setDifficulty(difficulty);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showExecutedScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("executed-view.fxml"));
        Parent root = loader.load();

        // set the execution image programmatically to ensure correct resource path
        ImageView imgView = (ImageView) root.lookup("#executedImage");
        if (imgView != null) {
            Image img = new Image(MainApp.class.getResourceAsStream("/pictures/10-hangman.png"));
            imgView.setImage(img);
        }

        Scene executedScene = new Scene(root, 650, 650);
        primaryStage.setScene(executedScene);
        primaryStage.show();

        // After short delay, force the player to the game scene
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(ev -> {
            try {
                showGameScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
