// Sweepered
// A Minesweeper clone in Java

package org.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.Group;

import scenes.MineScene;

public class SweeperedApp extends Application {
    private Stage window;
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setResizable(false);
        window.setTitle("Sweepered");

        // Load scenes [MineScene is the main game scene loaded for now]
        MineScene mainScene = new MineScene(new Group(), 400, 500);

        mainScene.setFill(Color.BLACK);

        window.setScene(mainScene);
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// OxygenCobalt