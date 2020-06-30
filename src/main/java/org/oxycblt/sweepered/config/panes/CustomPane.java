// CustomPane
// Pane that contains about page for sweepered

package org.oxycblt.sweepered.config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.Arrays;

import org.oxycblt.sweepered.config.values.ConfigState;
import org.oxycblt.sweepered.config.ui.ConfigButton;
import org.oxycblt.sweepered.config.ui.ConfigField;

import org.oxycblt.sweepered.shared.config.Configuration;

import org.oxycblt.sweepered.media.images.TextureAtlas;

public class CustomPane extends Pane implements EventHandler<ActionEvent> {

    private final int x;
    private final int y;

    private final int height;
    private final int width;

    private ConfigState state;

    private Boolean isShown;

    private ConfigField widthField;
    private ConfigField heightField;
    private ConfigField minesField;

    private ConfigButton confirm;

    public CustomPane(final int width, final int height, final int x, final int y) {

        this.x = x;
        this.y = y;

        this.height = height;
        this.width = width;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        state = new ConfigState(ConfigState.State.MENU);

        isShown = false;

        // Create each text field for setting the custom
        // values. Their current placeholder value is zero
        // until the Configuration values are loaded when
        // the pane itself is shown
        widthField = new ConfigField(

            "Width",
            0,
            2, 2,
            56, 24

        );

        heightField = new ConfigField(

            "Height",
            0,
            62, 2,
            56, 24

        );

        minesField = new ConfigField(

            "Mines",
            0,
            122, 2,
            56, 24

        );

        // Create the confirmation button that would
        // update the board with the new values
        confirm = new ConfigButton("", 183, 2, 24, 24);
        confirm.getInternalButton().setOnAction(this);
        confirm.getInternalButton().setGraphic(

            TextureAtlas.get(TextureAtlas.ICON_CONFIRM)

        );

        // By default, CustomPane is not shown, so dont add
        // any of the nodes created.

    }

    public void handle(final ActionEvent event) {

        int fieldWidth = widthField.getValue();
        int fieldHeight = heightField.getValue();
        int fieldMines = minesField.getValue();

        Boolean different = isDifferent(fieldWidth, fieldHeight, fieldMines);

        // If one or more of the values is different from
        // the Configuration values, then continue.
        if (different) {

            Boolean[] valid = isValid(fieldWidth, fieldHeight, fieldMines);

            // If every board value is correct, then actually
            // set the values and change the board
            if (valid[3]) {

                widthField.setValid(true);
                heightField.setValid(true);
                minesField.setValid(true);

                Configuration.setConfigValue("tileWidth", fieldWidth);
                Configuration.setConfigValue("tileHeight", fieldHeight);
                Configuration.setConfigValue("mineCount", fieldMines);

                // Pulse the Mode EventInteger created by GameScene, in
                // order to notify GameScene to reconstruct the board
                // without incremening the value past 4.
                Configuration.getEventConfigValue("mode").pulse();

            } else {

                // Otherwise Use the first 3 values given by isValid() to
                // set the valid/invalid status of each field and highlight the error
                widthField.setValid(valid[0]);
                heightField.setValid(valid[1]);
                minesField.setValid(valid[2]);

            }

        } else {

            widthField.setValid(true);
            heightField.setValid(true);
            minesField.setValid(true);

        }


    }

    private Boolean isDifferent(final int fieldWidth,
                                final int fieldHeight,
                                final int fieldMines) {

        // Compare the values given with the current
        // values to make sure that the fields have actually
        // changed, to prevent redundancy.
        int[] previousValues = new int[]{

            Configuration.getConfigValue("tileWidth"),
            Configuration.getConfigValue("tileHeight"),
            Configuration.getConfigValue("mineCount")

        };

        return !Arrays.equals(

            previousValues,

            new int[]{fieldWidth, fieldHeight, fieldMines}

        );

    }

    private Boolean[] isValid(final int fieldWidth,
                              final int fieldHeight,
                              final int fieldMines) {

        // Limit the dimensions of the board to be
        // higher than 6 [Where StatPane starts breaking]
        // but lower than 100 [Arbitrary number, thats it]
        Boolean isWidthValid = fieldWidth >= 6 && fieldWidth <= 100;
        Boolean isHeightValid = fieldHeight >= 6 && fieldHeight <= 100;

        // Limit the mineCount so that (w * h) - mineCount needs
        // to be over or equal to 9, so that the Board does not
        // enter a loop attempting to fill in mines that do not
        // exist.

        Boolean isMinesValid = ((fieldWidth * fieldHeight) - fieldMines) >= 9;

        // Combine the previous values into one in order to determine
        // if the board is valid or not
        Boolean isAllValid = isWidthValid && isHeightValid && isMinesValid;

        // Return all 4 values as an array
        return new Boolean[]{isWidthValid, isHeightValid, isMinesValid, isAllValid};

    }

    public void setShown(final Boolean newShown) {

        // Make sure that the new value given
        // isnt the same as the current value
        // to prevent redundancy
        if (newShown != isShown) {

            if (!newShown) {

                // If the pane should be hidden,
                // remove all the nodes from
                // CustomPane and mode the pane
                // to the back to prevent it from conflicting
                // with the other buttons
                getChildren().removeAll(

                    widthField,
                    heightField,
                    minesField,
                    confirm

                );

                toBack();

            } else {

                // If the pane should be shown, update the fields
                // configuration values and then readd all nodes to
                // CustomPane, before bringing the pane back up again
                widthField.setValue(Configuration.getConfigValue("tileWidth"));
                heightField.setValue(Configuration.getConfigValue("tileHeight"));
                minesField.setValue(Configuration.getConfigValue("mineCount"));

                widthField.setValid(true);
                heightField.setValid(true);
                minesField.setValid(true);

                getChildren().addAll(

                    widthField,
                    heightField,
                    minesField,
                    confirm

                );

                toFront();

            }

            isShown = newShown;

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
