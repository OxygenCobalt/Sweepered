// AboutStage
// Window for the settings of this game

package config;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.scene.layout.Pane;

import javafx.stage.Screen;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

import config.panes.MenuPane;
import config.panes.AboutPane;
import config.values.ConfigState;

import media.images.TextureAtlas;

import shared.observable.Listener;

public class ConfigStage extends Stage implements Listener<ConfigState> {

    private Scene internalScene;
    private Group root;

    private double width;
    private double height;

    private ConfigState masterState;

    private Pane frontPane;

    private MenuPane menu;
    private AboutPane about;

    public ConfigStage() {

        // Since this object is a stage instead of a scene, create an internal
        // Group and the Scene instead of calling the super constructor
        root = new Group();
        internalScene = new Scene(root);
        internalScene.getStylesheets().add("file:src/main/resources/style/main.css");
        internalScene.setFill(Color.web("3d3d3d"));

        setTitle("Settings");
        setScene(internalScene);
        setResizable(false);

        getIcons().add(TextureAtlas.WINDOW_ICON);

        // Screenbounds is the given size of the screen,
        // used to get a centered position of this window
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        setX((screenBounds.getWidth() - width) / 2);
        setY((screenBounds.getHeight() - height) / 2);

        masterState = new ConfigState(ConfigState.State.MENU);

        menu = new MenuPane(243, 113);
        about = new AboutPane(243, 203);

        menu.getConfigState().addListener(this);
        about.getConfigState().addListener(this);

        root.getChildren().addAll(about, menu);

        updateFrontPane(masterState);

        show();

    }

    public void propertyChanged(final ConfigState changed) {

        ConfigState.State newState = changed.getState();

        updateFrontPane(changed);

        menu.updateConfigState(newState);
        about.updateConfigState(newState);

        masterState.setState(newState);

    }

    private void updateFrontPane(final ConfigState changed) {

        // Set the currently shown pane to the
        // states respective pane

        if (changed.isMenu()) {

            setTitle("Settings");
            frontPane = menu;

        } else if (changed.isAbout()) {

            setTitle("About Sweepered");
            frontPane = about;

        }

        // Bring that pane to the front, and adjust the
        // windows with to fit that pane
        frontPane.toFront();

        width = frontPane.getPrefWidth();
        height = frontPane.getPrefHeight();

        // Add 12/38 to the size, to make up for the borders
        // and something else when it comes to height
        setWidth(width + 12);
        setHeight(height + 38);

    }

}

// OxygenCobalt
