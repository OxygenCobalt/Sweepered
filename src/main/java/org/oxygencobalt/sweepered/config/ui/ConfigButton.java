// ConfigButton
// Entity that primarily acts as a button with some extra effects.

package config.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import javafx.scene.control.Button;

import game.decor.Corner;

public class ConfigButton extends Pane implements EventHandler<MouseEvent> {

    private final int x;
    private final int y;

    private final int height;
    private final int width;

    private final Button internalButton;

    private final Corner[] corners;

    public ConfigButton(final String name,
                        final int x,
                        final int y,
                        final int width,
                        final int height) {

        this.x = x;
        this.y = y;

        // This pane should be no bigger than the button it contains.
        this.height = height;
        this.width = width;

        relocate(x, y);

        setPrefSize(this.width, this.height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        internalButton = new Button(name);

        internalButton.setMinSize(width, height);
        internalButton.setPrefSize(width, height);
        internalButton.setMaxSize(width, height);

        internalButton.setOnMousePressed(this);
        internalButton.setOnMouseReleased(this);

        getChildren().add(internalButton);

        corners = Corner.generateCorners(this, true, true);

    }

    @Override
    public void handle(final MouseEvent event) {

        EventType type = event.getEventType();
        MouseButton button = event.getButton();

        Boolean isUninverted;

        if (button == MouseButton.PRIMARY) {

            // If the mouse is pressed on the button, then
            // uninvert the corners to make the button look
            // pushed in, otherwise invert them if the mouse
            // is not pressing on the button
            isUninverted = (type == MouseEvent.MOUSE_RELEASED);

            for (Corner corner : corners) {

                corner.setInverted(isUninverted);

            }

        }

    }

    public Button getInternalButton() {

        return internalButton;

    }


}

// OxygenCobalt
