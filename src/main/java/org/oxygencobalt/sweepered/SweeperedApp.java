// Sweepered
// A Minesweeper clone in Java

// TODO: Create a textureatlas for
// Mines/Tiles
// Timer [6-Segments]
// Maybe Title Screen

// TextureAtlas has constants for every tile
// Class calls function get() from TextureAtlas with static constant, returns typecasted image

package org.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;

import scenes.GameScene;

import components.TextureAtlas;

public class SweeperedApp extends Application {
    private Stage window;

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