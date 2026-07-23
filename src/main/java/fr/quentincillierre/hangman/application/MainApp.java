package fr.quentincillierre.hangman.application;

import fr.quentincillierre.hangman.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static Scene gameScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showMainMenuScene();
    }

    public static void showMainMenuScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("main-menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 650, 650);
        applyStylesheet(scene);
        primaryStage.setTitle("Codebreaker: Freedom | Execution");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showIntroScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("intro-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 650, 650);
        applyStylesheet(scene);
        primaryStage.setTitle("Codebreaker: Freedom | Execution");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showGameScene() throws Exception {
        showGameSceneWithDifficulty("easy");
    }

    public static void showGameSceneWithDifficulty(String difficulty) throws Exception {
        String viewName;
        switch (difficulty.toLowerCase()) {
            case "average":
                viewName = "average-level.fxml";
                break;
            case "hard":
                viewName = "hard-level.fxml";
                break;
            default:
                viewName = "easy-level.fxml";
                break;
        }

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(viewName));
        Parent root = loader.load();
        GameController controller = loader.getController();
        controller.setDifficulty(difficulty);
        controller.initializeGame();

        Scene scene = new Scene(root, 650, 650);
        applyStylesheet(scene);
        scene.setOnKeyTyped(event -> controller.handleKeyboardInput(event.getCharacter()));

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
        applyStylesheet(executedScene);
        primaryStage.setScene(executedScene);
        primaryStage.show();
    }

    public static void showVictoryScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("victory-view.fxml"));
        Parent root = loader.load();
        Scene victoryScene = new Scene(root, 650, 650);
        applyStylesheet(victoryScene);
        primaryStage.setScene(victoryScene);
        primaryStage.show();
    }

    private static void applyStylesheet(Scene scene) {
        String cssUrl = MainApp.class.getResource("design.css").toExternalForm();
        if (!scene.getStylesheets().contains(cssUrl)) {
            scene.getStylesheets().add(cssUrl);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
