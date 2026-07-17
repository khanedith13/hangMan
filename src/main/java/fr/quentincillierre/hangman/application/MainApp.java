package fr.quentincillierre.hangman.application;

import fr.quentincillierre.hangman.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        if (gameScene == null) {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("game-view.fxml"));
            Parent root = loader.load();
            GameController controller = loader.getController();
            Scene scene = new Scene(root, 650, 650);
            scene.setOnKeyTyped(event -> controller.handleKeyboardInput(event.getCharacter()));
            gameScene = scene;
        }
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
