// Sweepered
// A Minesweeper clone in Java

package org.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;

import scenes.GameScene;

public class SweeperedApp extends Application {
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setResizable(false);
        window.setTitle("Sweepered");

        // Load scenes
        GameScene mainScene = new GameScene(new Group(), 15, 15);

        window.setScene(mainScene); // Currently the main scene loaded is just GameScene
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// OxygenCobalt