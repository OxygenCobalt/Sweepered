// ConfigField
// UI object that primarily acts as a TextField with some extra effects.

package org.oxycblt.sweepered.config.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import org.oxycblt.sweepered.shared.ui.Corner;

public class ConfigField extends Pane implements EventHandler<KeyEvent> {

    private final int x;
    private final int y;

    private final int height;
    private final int width;

    private final TextField internalField;

    public ConfigField(final String prompt,
                       final int initValue,
                       final int x,
                       final int y,
                       final int width,
                       final int height) {

        this.x = x;
        this.y = y;

        // This pane should be no bigger than the Field it contains.
        // [Excluding the change to accommodate for borders]
        this.height = height - 4;
        this.width = width - 4;

        relocate(x, y);
        setPrefSize(this.width, this.height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        internalField = new TextField(String.valueOf(initValue));

        internalField.setMinSize(this.width, this.height);
        internalField.setPrefSize(this.width, this.height);
        internalField.setMaxSize(this.width, this.height);

        internalField.setPromptText(prompt);
        internalField.setOnKeyReleased(this);

        // Make sure that the internal field is
        // limited to numbers only by adding a listener
        // to any text change in the field
        internalField.textProperty().addListener(

            (observable, oldText, newText) -> {

                if (!newText.matches("\\d*")) {

                    internalField.setText(oldText);

                }

            }

        );

        getChildren().add(internalField);

        Corner.generateCorners(this, true, false);

    }

    @Override
    public void handle(final KeyEvent event) {

        // If ESCAPE is pressed, then defocus the ConfigField
        // [In a very hacky way]
        // by requesting the focus of the parent pane instead.
        if (event.getCode() == KeyCode.ESCAPE) {

            requestFocus();

        }

    }

    public void setValid(final Boolean valid) {

        if (valid) {

            internalField.getStyleClass().remove("invalid-field");

        } else {

            internalField.getStyleClass().add("invalid-field");

        }

    }

    public void setValue(final int newValue) {

        internalField.setText(String.valueOf(newValue));

    }

    public int getValue() {

        String text = internalField.getText();

        // Make sure that the field isnt empty
        // before converting it to an integer
        if (!text.equals("")) {

            return Integer.parseInt(text);

        } else {

            // If it is empty, just return zero
            return 0;

        }

    }

}

// OxygenCobalt
