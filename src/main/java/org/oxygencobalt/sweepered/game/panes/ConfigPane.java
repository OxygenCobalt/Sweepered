// ConfigPane
// Pane that houses the buttons/menus for the game/its settings.
// I could have used Menubar, but it was too much hassle.

package game.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;

import javafx.stage.Stage;
import javafx.scene.Group;

import game.scenes.AboutScene;

public class ConfigPane extends Pane {

    private final int width;
    private final int height;

    private Boolean aboutIsShown;

    public ConfigPane(final int sceneWidth) {

        this.height = 20;

        // The width of GameScene is used to allow ConfigPane
        // to stretch across the length of the window.
        this.width = sceneWidth;

        relocate(0, 0);

        setPrefSize(width, height);

        // Lock Size to prevent unintentional resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("config-pane");

        aboutIsShown = false;

        // Create each button, bind their respective listener,
        // and then add them to the pane

        // TODO: Swap this out for a ConfigButton and change
        // this to a help menu instead of just "About"
        Button aboutButton = new Button("About");

        aboutButton.setOnAction(aboutListener);
        aboutButton.getStyleClass().add("config-button");

        getChildren().addAll(aboutButton);

    }

    EventHandler<ActionEvent> aboutListener = actionEvent -> {

        // Disallow any more about windows to be
        // opened until the current one is closed
        if (!aboutIsShown) {

            aboutIsShown = true;

            Stage aboutStage = new Stage();
            AboutScene aboutScene = new AboutScene(new Group());

            aboutStage.setTitle("About Sweepered");
            aboutStage.setScene(aboutScene);

            // Center the window using the coords given by aboutScene
            aboutStage.setX(aboutScene.getCenterX());
            aboutStage.setY(aboutScene.getCenterY());

            aboutStage.show();

            // Make sure to reenable opening a window when the about window is closed
            aboutStage.setOnHidden(closeEvent -> {

                aboutIsShown = false;

            });

        }

    };

}

// OxygenCobalt
