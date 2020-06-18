// OptionPane
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

public class OptionPane extends Pane implements EventHandler<ActionEvent> {

    private final int height;
    private final int width;

    private ConfigState state;

    private final ConfigButton modeButton;

    public OptionPane(final int width, final int height) {

        // Subtract 8 from the height/width in order
        // to make up for the border of OptionPane
        this.height = height - 8;
        this.width = width - 8;

        relocate(4, 4);

        setPrefSize(this.width, this.height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        state = new ConfigState(ConfigState.State.MENU);

        modeButton = new ConfigButton("Mode", 17,  17, 91, 26);

        // Get the internal buttons from each configbutton
        // [As configbutton is a pane, not a button] and
        // the listen to any button presses
        modeButton.getInternalButton().setOnAction(this);

        getChildren().addAll(modeButton);

        Corner.generateCorners(this, false, false);

    }

    public void handle(final ActionEvent event) {

        // Every object added to this listener is a button, so
        // casting it to a Button is safe to do
        Button pressedButton = (Button) event.getSource();

        // Convert the text on the button itself [E.G "Mode"] to
        // an enum value for ConfigState and update the state to that
        state.setState(ConfigState.State.valueOf(

            pressedButton.getText().toUpperCase()

        ));

    }

    public void updateConfigState(final ConfigState.State newState) {

        state.setStateSilent(newState);

    }

    public ConfigState getConfigState() {

        return state;

    }

}

// OxygenCobalt
