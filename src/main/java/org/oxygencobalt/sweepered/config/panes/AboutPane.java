// AboutPane
// Pane that contains about page for sweepered

package config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import config.ui.ConfigButton;
import config.values.ConfigState;

import media.images.TextureAtlas;

import shared.ui.Corner;

public class AboutPane extends Pane implements EventHandler<ActionEvent> {

    private final int height;
    private final int width;

    private ConfigState state;

    private final String[] words;

    private ImageView logo;
    private ConfigButton backButton;

    public AboutPane(final int width, final int height) {

        relocate(4, 4);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        state = new ConfigState(ConfigState.State.MENU);

        // List of label texts used on the about page
        // This is used to generate labels during generateText
        words = new String[]{

            "A Minesweeper Clone in Java",
            "", // Blank strings act as spaces
            "All Code/Art by OxygenCobalt",
            "",
            "Sounds from Freesound.org:",
            "\"Click.m4a\" by SpiceProgram",
            "\"Tap 1\" by Splatez07",
            "\"8-bit explosion_4.wav\" by",
            "Soundholder",
            "\"8-bit Arcade Start Sound",
            "Effect\" by FartBiscuit1700"

        };

        // Now that words is defined, set the dimensions of the pane
        // based off the height of the combined labels
        this.height = 84 + (20 * words.length);
        this.width = width - 8;

        setPrefSize(this.width, this.height);

        // Lock Size to prevent unintentional resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Create the logo and the back button
        logo = new ImageView(new Image("file:src/main/resources/textures/logo.png"));
        logo.relocate(55, 5);

        backButton = new ConfigButton("Back", 17, 51 + (20 * words.length), 205, 30);
        backButton.getInternalButton().setOnAction(this);
        backButton.getInternalButton().setGraphic(

            TextureAtlas.get(TextureAtlas.ICON_BACK)

        );

        getChildren().addAll(logo, backButton);

        generateText();

        Corner.generateCorners(this, false, false);

    }

    public void handle(final ActionEvent event) {

        // Make sure that the custom menu flag persists
        // when the state is switched back to menu
        switch (state.getState()) {

            case ABOUT: state.setState(ConfigState.State.MENU); break;

            case ABOUT_CUSTOM: state.setState(ConfigState.State.MENU_CUSTOM); break;

        }

    }

    private void generateText() {

        Label label;

        // Iterate through every sentence in the
        // words array and generate a label for it
        for (int i = 0; i < words.length; i++) {

            label = new Label(words[i]);
            label.relocate(0, 40 + (20 * i));

            getChildren().add(label);

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
