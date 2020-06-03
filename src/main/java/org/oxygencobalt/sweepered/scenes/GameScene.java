// GameScene
// Main Minesweeper game scene, only mode as of now is 9x9, but more will be added.

package scenes;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import panes.TilePane;
import panes.StatPane;

public class GameScene extends Scene implements PropertyChangeListener {
    private Group root;

    private StatPane stats;
    private TilePane board;

    private final int offset;
    private final int mineCount;

    public GameScene(Group group, int mineWidth, int mineHeight, int mineCount, int offset) {
        // Call super to construct Scene(), and then add passed group to class.
        super(group, (mineWidth * 32) + (18 + offset), (mineHeight * 32) + (76 + offset));
        setFill(Color.web("3d3d3d")); // Background color matches w/the tile color, mostly

        this.offset = offset + 4; // 4 is added to make up for borders
        this.mineCount = mineCount;

        // Load the main panes
        stats = new StatPane(40, (mineWidth * 32), this.offset, 0);
        board = new TilePane(mineWidth, mineHeight, this.mineCount, this.offset);

        board.getGameState().addListener(this);

        root = group;
        root.getChildren().addAll(stats, board);
    }

    public void propertyChange(PropertyChangeEvent event) {
        // TODO: Add GameState communication from MinePane/StatPane
    }
}

// OxygenCobalt