// MenuPane
// Pane that contains the mode selector and the about button

package config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;

import events.states.ConfigState;

import config.ui.ConfigButton;
import config.Configuration;

import media.images.TextureAtlas;
import media.images.Sprite;

import game.decor.Corner;

public class MenuPane extends Pane {

    private final int height;
    private final int width;

    private ConfigState state;

    private ConfigButton modeButton;
    private ConfigButton aboutButton;

    private final Sprite[] modeGraphics;
    private final String[] modeNames;

    public MenuPane(final int width, final int height) {

        // Subtract 8 from the height/width in order
        // to make up for the border of MenuPane
        this.height = height - 8;
        this.width = width - 8;

        relocate(4, 4);

        setPrefSize(this.width, this.height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        state = new ConfigState(ConfigState.State.MENU);

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
        modeButton = new ConfigButton("Mode", 17, 17, 201, 26);

        updateModeButton();

        // Then, create the about button and apply its graphic as well
        aboutButton = new ConfigButton("About Sweepered", 17, 62, 201, 26);
        aboutButton.getInternalButton().setGraphic(

            TextureAtlas.get(TextureAtlas.ICON_ABOUT)

        );

        // Bind each listeners to their respective button
        modeButton.getInternalButton().setOnAction(modeButtonListener);
        aboutButton.getInternalButton().setOnAction(aboutButtonListener);

        getChildren().addAll(modeButton, aboutButton);

        Corner.generateCorners(this, false, false);

    }

    EventHandler<ActionEvent> modeButtonListener = event -> {

        // All node added to this listener is a button, so its safe to cast as such
        Button button = (Button) event.getSource();
        String buttonText = button.getText();

        // Get the current mode value, and increment it up
        // if the value is over 4 [Custom Mode], then roll over
        // back to zero [Easy]
        int mode = Configuration.getConfigValue("Mode");
        Configuration.setConfigValue("Mode", (mode + 1) % 5);

        updateModeButton();

    };

    EventHandler<ActionEvent> aboutButtonListener = event -> {

        state.setState(ConfigState.State.ABOUT);

    };

    public void updateModeButton() {

        // Get the internal button of the mode button, and
        // the  current/new value of Mode
        Button internalButton = modeButton.getInternalButton();

        int mode = Configuration.getConfigValue("Mode");

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

    public void updateConfigState(final ConfigState.State newState) {

        state.setStateSilent(newState);

    }

    public ConfigState getConfigState() {

        return state;

    }

}

// OxygenCobalt
