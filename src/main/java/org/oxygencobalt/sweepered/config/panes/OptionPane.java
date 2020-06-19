// OptionPane
// Pane that houses the buttons that open other Config Menu panes

package config.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import game.decor.Corner;

public class OptionPane extends Pane {

    private final int height;
    private final int width;

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

        Corner.generateCorners(this, false, false);

    }

}

// OxygenCobalt
