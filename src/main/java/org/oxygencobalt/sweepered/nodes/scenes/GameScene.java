// GameScene
// Main Minesweeper game scene, only mode as of now is 9x9, but more will be added.

package nodes.scenes;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import generation.states.GameState;

import media.Audio;

import nodes.panes.TilePane;
import nodes.panes.StatPane;

public class GameScene extends Scene implements PropertyChangeListener {
    private Group root;

    private StatPane stats;
    private TilePane board;

    private GameState masterState;

    private final int offset;
    private final int mineCount;

    public GameScene(Group group, int mineWidth, int mineHeight, int mineCount, int offset) {
        // Call super to construct Scene(), and then add passed group to class.
        super(group, (mineWidth * 32) + (18 + offset), (mineHeight * 32) + (76 + offset));
        setFill(Color.web("3d3d3d")); // Background color matches w/the tile color, mostly

        this.offset = offset + 4; // 4 is added to make up for borders
        this.mineCount = mineCount;

        // Run stop on every sound in order to cache them in memory, making everything else run faster
        // [Yeah, I dont know why either]
        Audio.clickSound.stop();
        Audio.flagSound.stop();
        Audio.explodeSound.stop();
        Audio.clearSound.stop();

        // Load the main panes
        stats = new StatPane(40, (mineWidth * 32), this.offset, 0);
        board = new TilePane(mineWidth, mineHeight, this.mineCount, this.offset);

        board.getGameState().addListener(this);

        root = group;
        root.getChildren().addAll(stats, board);

        masterState = new GameState(GameState.State.UNSTARTED);
    }

    public void propertyChange(PropertyChangeEvent event) {
        // Cast the changed state back to GameState, in order to be used
        GameState observable = (GameState) event.getSource();

        // TODO: Add GameState communication from MinePane/StatPane

        // Update the master state once everything is done.
        masterState.setStateSilent(observable.getState());

    }
}

// OxygenCobalt