// Sweepered
// A Minesweeper clone in Java 

package org.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;

import nodes.scenes.GameScene;

public class SweeperedApp extends Application {
    private Stage window;

    @Override 
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setResizable(false);
        window.setTitle("Sweepered");
        
        // Arguments are passed to GameScene as followed:
        // group - for the super() constructor to function
        // width, height - specified board size [in tiles, not pixels]
        // mine count - number of mines on the board
        // offset - the spacing between each pane in GameScene
        GameScene mainScene = new GameScene(new Group(), 9, 9, 35, 10);

        window.setScene(mainScene); // Currently the main scene loaded is just GameScene
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// OxygenCobalt