// ConfigPane
// Pane that houses the buttons/menus for the game/its settings.
// I could have used Menubar, but it was too much hassle.

package game.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class ConfigPane extends Pane {

    private final int width;
    private final int height;

    public ConfigPane(final int sceneWidth) {

        this.height = 17;

        // The width of GameScene is used to allow ConfigPane
        // to strech across the length of the window.
        this.width = sceneWidth;

        relocate(0, 0);

        setPrefSize(width, height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("config-pane");

    }

}

// OxygenCobalt
