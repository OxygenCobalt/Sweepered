// Sweepered
// A Minesweeper clone in Java

// Make sure to use -S for all commits from now on :)

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
        GameScene mainScene = new GameScene(new Group(), 9, 9);

        window.setScene(mainScene); // Currently the main scene loaded is just GameScene
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// OxygenCobalt