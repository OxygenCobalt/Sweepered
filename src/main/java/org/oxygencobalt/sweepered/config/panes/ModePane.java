// ModePane
// Pane that houses the buttons that open other Config Menu panes

package config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;

import config.ui.ConfigButton;

import events.states.ConfigState;

import game.decor.Corner;

import media.images.TextureAtlas;
import media.images.Sprite;

public class ModePane extends Pane implements EventHandler<ActionEvent> {

    private int height;
    private int width;

    private ConfigState state;

    private final ConfigButton[] buttons;

    private final String[] buttonNames;
    private final Sprite[] buttonSprites;

    public ModePane() {

        // Dont resize the pane [Yet], just move it
        relocate(4, 4);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        // fixme couldnt you just iterate through an array?

        state = new ConfigState(ConfigState.State.MENU);

        buttons = new ConfigButton[4];
        buttonNames = new String[]{

            "Back",
            "Easy",
            "Normal",
            "Hard",
            "Expert",
            "Custom"

        };

        buttonSprites = new Sprite[]{

            TextureAtlas.ICON_BACK,
            TextureAtlas.ICON_ONE_MINE,
            TextureAtlas.ICON_TWO_MINE,
            TextureAtlas.ICON_THREE_MINE,
            TextureAtlas.ICON_EXPLODE,
            TextureAtlas.ICON_CUSTOM

        };

        generateButtons();

        Corner.generateCorners(this, false, false);

    }

    private void generateButtons() {

        ConfigButton configButton;
        Button internalButton;

        int i = 0;

        int x = 0;
        int y = 0;

        for (int buttonY = 0; buttonY < 3; buttonY++) {

            for (int buttonX = 0; buttonX < 2; buttonX++) {

                // Create a new button with the x/y value
                // calculated by the loop values and a name
                // chosen from the array of possible names
                configButton = new ConfigButton(

                    buttonNames[i],

                    17 + (218 * buttonX),
                    17 + (47 * buttonY),

                    199, 28

                );

                internalButton = configButton.getInternalButton();

                // Configure the button graphic, change the alignment to LEFT,
                // and add the action listener to the new button
                internalButton.setGraphic(TextureAtlas.get(buttonSprites[i]));
                internalButton.getStyleClass().add("icon-button");
                internalButton.setOnAction(this);

                getChildren().add(configButton);

                i++;

                x = buttonX;

            }

            y = buttonY;

        }

        // Now calculate the width since
        // the buttons have been generated

        this.width = 17 + (218 * (x + 1)) - 2;
        this.height = 17 + (47 * (y + 1)) - 2;

        setPrefSize(width, height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    }

    public void handle(final ActionEvent event) {

        // Every object added to this listener is a button, so
        // casting it to a Button is safe to do
        Button pressedButton = (Button) event.getSource();
        String buttonText = pressedButton.getText();

        // If the back button was clicked, simply exit back to the settings menu
        if (buttonText.equals("Back")) {

            state.setState(ConfigState.State.MENU);

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
