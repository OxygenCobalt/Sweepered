// MenuPane
// Pane that contains the mode selector and the about button

package config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import config.values.ConfigState;
import config.ui.ConfigButton;

import media.images.TextureAtlas;
import media.images.Sprite;

import shared.config.Configuration;
import shared.ui.Corner;

public class MenuPane extends Pane {

    private int height;
    private int width;

    private final int initialHeight;

    private ConfigState state;

    private int mode;

    private final Sprite[] modeGraphics;
    private final String[] modeNames;

    private ConfigButton modeButton;
    private CustomPane custom;

    private ConfigButton aboutButton;

    private Corner[] corners;

    public MenuPane(final int width, final int height) {

        // Subtract 8 from the height/width in order
        // to make up for the border of MenuPane
        this.height = height - 8;
        this.width = width - 8;

        // Set the initial height to be reverted to later
        initialHeight = this.height;

        relocate(4, 4);

        setPrefSize(this.width, this.height);

        // Lock Size to prevent unintentional resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        state = new ConfigState(ConfigState.State.MENU);

        mode = Configuration.getConfigValue("mode");

        // modeNames and modeGraphics are values used to
        // set the buttons text based of the current mode
        modeNames = new String[] {

            "Easy",
            "Normal",
            "Hard",
            "Expert"

        };

        modeGraphics = new Sprite[]{

            TextureAtlas.ICON_ONE_MINE,
            TextureAtlas.ICON_TWO_MINE,
            TextureAtlas.ICON_THREE_MINE,
            TextureAtlas.ICON_EXPLODE

        };

        // Create the mode button, and then apply the correct graphic and name
        modeButton = new ConfigButton("Mode", 17, 17, 205, 30);

        updateModeButton();

        // Then, create the about button and apply its graphic as well
        aboutButton = new ConfigButton("About Sweepered", 17, 62, 205, 30);
        aboutButton.getInternalButton().setGraphic(

            TextureAtlas.get(TextureAtlas.ICON_ABOUT)

        );

        // Bind each listeners to their respective button
        modeButton.getInternalButton().setOnAction(modeButtonListener);
        aboutButton.getInternalButton().setOnAction(aboutButtonListener);

        corners = Corner.generateCorners(this, false, false);

        custom = new CustomPane(205, 24, 15, 48);

        // If the mode is already determined to be custom, then
        // update the custom pane to be shown. Otherwise this
        // does not need to be run

        // This is only ran after every other node has been initialized,
        // as updating MenuPane to accommodate CustomPane requires the
        // movement/relocation of other nodes, which would throw
        // a NullPointerException if not already initialized

        if (mode > 3) {

            updateCustomPane();

        }

        getChildren().addAll(modeButton, custom, aboutButton);

    }

    EventHandler<ActionEvent> modeButtonListener = event -> {

        // All node added to this listener is a button, so its safe to cast as such
        Button button = (Button) event.getSource();
        String buttonText = button.getText();

        mode = Configuration.getConfigValue("mode") + 1;

        // If the mode is over 4 [Custom] or just invalid, then revert it back to
        // the first mode [Easy]. While a modulo could be used, using an if statement
        // is more versatile as negative values or values above 4 can be loaded in
        if (mode < 0 || mode > 4) {

            mode = 0;

        }

        Configuration.setConfigValue("mode", mode);

        // Now update the mode button with the new mode value
        updateModeButton();

        // If the mode has just become "Custom" or rolled over
        // to 0, then also update the Custom pane.
        if (mode > 3 || mode == 0) {

            updateCustomPane();

        }

    };

    EventHandler<ActionEvent> aboutButtonListener = event -> {

        // If switching the state to ABOUT, make sure that the
        // CUSTOM flag persists in order to prevent sizing
        // conflicts in ConfigStage.

        switch (state.getState()) {

            case MENU: state.setState(ConfigState.State.ABOUT); break;

            case MENU_CUSTOM: state.setState(ConfigState.State.ABOUT_CUSTOM); break;

        }

    };

    public void updateModeButton() {

        // Get the internal button of the mode button
        Button internalButton = modeButton.getInternalButton();

        // If the mode is within the preset values, load a
        // name/graphic from the list of presets
        if (mode >= 0 && mode <= 3) {

            internalButton.setText("Mode: " + modeNames[mode]);

            internalButton.setGraphic(

                TextureAtlas.get(modeGraphics[mode])

            );

        } else {

            // If invalid, load the "Custom" values as those would
            // be the fallback values loaded by Configuration

            internalButton.setText("Mode: Custom");

            internalButton.setGraphic(

                TextureAtlas.get(TextureAtlas.ICON_CUSTOM)

            );

        }

    }

    private void updateCustomPane() {

        // Check if the current mode is out-of-bounds
        // a.k.a a custom mode

        if (mode > 3) {

            // If so, extend the Panes height and set
            // the custom menu to shown
            height = height + 28;

            setPrefHeight(height);

            custom.setShown(true);

            // In either case, relocate the about button
            // to be in line with the additon/removal of the custom menu
            aboutButton.relocate(17, 90);

            state.setState(ConfigState.State.MENU_CUSTOM);

        } else {

            // Otherwise, revert the height to the initial
            // value and hide the custom menu
            height = initialHeight;

            setPrefHeight(height);

            custom.setShown(false);

            aboutButton.relocate(17, 62);

            state.setState(ConfigState.State.MENU);

        }

        // Also destroy all corners and regenerate them with the new dimensions
        for (Corner corner : corners) {

            getChildren().remove(corner);

        }

        corners = Corner.generateCorners(this, false, false);

    }

    public void updateConfigState(final ConfigState.State newState) {

        state.setStateSilent(newState);

        custom.updateConfigState(newState);

    }

    public ConfigState getConfigState() {

        return state;

    }

}

// OxygenCobalt
