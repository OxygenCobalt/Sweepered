// CustomPane
// Pane that contains about page for sweepered

package config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import config.values.ConfigState;
import config.ui.ConfigButton;
import config.ui.ConfigField;

import shared.config.Configuration;

import media.images.TextureAtlas;

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

        widthField = new ConfigField(

            "Width",
            Configuration.getConfigValue("tileWidth"),
            2, 2,
            56, 24

        );

        heightField = new ConfigField(

            "Height",
            Configuration.getConfigValue("tileHeight"),
            62, 2,
            56, 24

        );

        minesField = new ConfigField(

            "Mines",
            Configuration.getConfigValue("mineCount"),
            122, 2,
            56, 24

        );

        // Create the confirmation button that would
        // update the board with the new values
        confirm = new ConfigButton("", 183, 2, 24, 24);
        confirm.getInternalButton().setOnAction(this);
        confirm.getInternalButton().setGraphic(

            TextureAtlas.get(TextureAtlas.ICON_BACK)

        );

        // By default, CustomPane is not shown, so dont add
        // any of the nodes created.

    }

    public void handle(final ActionEvent event) {



    }

    public void setShown(final Boolean newShown) {

        // Make sure that the new value given
        // isnt the same as the current value
        // to prevent redundancy
        if (newShown != isShown) {

            // Bring the pane to the back and remove
            // all the nodes from the pane if
            // the pane should be hidden, do the opposite
            // if not
            if (!newShown) {

                getChildren().removeAll(

                    confirm,
                    widthField,
                    heightField,
                    minesField

                );

                toBack();

            } else {

                getChildren().addAll(

                    confirm,
                    widthField,
                    heightField,
                    minesField

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
