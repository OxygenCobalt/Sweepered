// AboutScene
// Scene for the about page of this game

package config;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.stage.Screen;

import javafx.scene.paint.Color;

import javafx.geometry.Rectangle2D;

import config.panes.OptionPane;

public class ConfigScene extends Scene {

    private Group root;

    private final int width;
    private final int height;

    private final OptionPane options;

    private final Rectangle2D screenBounds;

    public ConfigScene(final Group group) {

        super(group, 243, 113);

        getStylesheets().add("file:src/main/resources/style/main.css");

        setFill(Color.web("3d3d3d"));

        width = 243;
        height = 113;

        options = new OptionPane(width, height);

        root = group;
        root.getChildren().add(options);

        // Screenbounds is the given size of the screen,
        // used to return a centered position of this pane
        // with getCenterX() and getCenterY()
        screenBounds = Screen.getPrimary().getVisualBounds();

    }

    public double getCenterX() {

        return (screenBounds.getWidth() - width) / 2;

    }

    public double getCenterY() {

        return (screenBounds.getHeight() - height) / 2;

    }

}

// OxygenCobalt
