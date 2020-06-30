// Sweepered
// A Minesweeper clone in Java

package org.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;

import media.images.TextureAtlas;

import shared.config.Configuration;
import shared.observable.Listener;
import shared.values.EventInteger;

public class SweeperedApp extends Application {

    private Stage window;

    @Override
    public void start(final Stage primaryStage) {

        window = primaryStage;

        window.setResizable(false);
        window.setTitle("Sweepered");

        // Read the configuration file [To be used across the game]
        Configuration.init();

        // Create the main game scene
        GameScene mainScene = new GameScene(new Group());

        // Get the calculated w/h from GameScene and
        // set the window dimensions to that.
        EventInteger sceneWidth = mainScene.getObservableWidth();
        EventInteger sceneHeight = mainScene.getObservableHeight();

        sceneWidth.addListener(sceneWidthListener);
        sceneHeight.addListener(sceneHeightListener);

        window.getIcons().add(TextureAtlas.WINDOW_ICON);

        window.setWidth(sceneWidth.getValue());
        window.setHeight(sceneHeight.getValue() + 26);

        window.setScene(mainScene);
        window.show();

        // Make sure to write any config changes when the window is closed
        window.setOnHidden(event -> {

            Configuration.end();

        });

    }

    // Listeners that update the Stages height whenever the scenes
    // height is updated, as a Scenes dimensions are normally Read-Only
    Listener<EventInteger> sceneWidthListener = changed -> {

        Integer newValue = changed.getValue();

        window.setWidth(newValue);

    };

    Listener<EventInteger> sceneHeightListener = changed -> {

        Integer newValue = changed.getValue();

        window.setHeight(newValue + 26);

    };

    public static void main(final String[] args) {

        launch();

    }

}

// OxygenCobalt
