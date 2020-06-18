// AboutStage
// Window for the settings of this game

package config;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.stage.Screen;

import javafx.scene.paint.Color;

import javafx.geometry.Rectangle2D;

import config.panes.OptionPane;

public class ConfigStage extends Stage {

    private Scene internalScene;
    private Group root;

    private final int width;
    private final int height;

    private final OptionPane options;

    public ConfigStage() {

        width = 243;
        height = 113;

        // Since this object is a stage, create the internal
        // Group and the Scene instead of calling the super constructor
        root = new Group();
        internalScene = new Scene(root, width, height);
        internalScene.getStylesheets().add("file:src/main/resources/style/main.css");
        internalScene.setFill(Color.web("3d3d3d"));

        setTitle("Settings");
        setScene(internalScene);
        setResizable(false);

        // Screenbounds is the given size of the screen,
        // used to get a centered position of this window
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        setX((screenBounds.getWidth() - width) / 2);
        setY((screenBounds.getHeight() - height) / 2);

        // Create all child nodes for this window
        options = new OptionPane(width, height);

        root.getChildren().add(options);

        show();

    }

}

// OxygenCobalt
