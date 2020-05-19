// Sweepered
// A Minesweeper clone in Java

package org.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;

import scenes.GameScene;

public class SweeperedApp extends Application {
    private Stage window;
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setResizable(false);
        window.setTitle("Sweepered");

        // Load scenes
        GameScene mainScene = new GameScene(new Group(), 308, 358);

        window.setScene(mainScene); // Currently the main scene loaded is just GameScene
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// OxygenCobalt