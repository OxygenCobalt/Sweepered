// GameScene
// Main Minesweeper game scene.

package nodes.scenes;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.paint.Color;

import events.observable.Listener;
import events.states.GameState;

import media.Audio;

import nodes.panes.TilePane;
import nodes.panes.StatPane;

public class GameScene extends Scene implements Listener<GameState> {

    private Group root;

    private StatPane stats;
    private TilePane tiles;

    private GameState masterState;

    private final int offset;
    private final int mineCount;

    public GameScene(final Group group,
                     final int mineWidth,
                     final int mineHeight,
                     final int mineCount,
                     final int offset) {

        // Call super to construct Scene(), and then add passed group to class.
        super(group, (mineWidth * 32) + (18 + offset), (mineHeight * 32) + (76 + offset));

        setFill(Color.web("3d3d3d")); // Background color matches w/the tile color, mostly

        this.offset = offset + 4; // 4 is added to make up for some of the borders
        this.mineCount = mineCount;

        // Load the sounds in from Audio
        Audio.loadSounds();

        // Load the main panes
        stats = new StatPane(40, (mineWidth * 32), this.offset, 0);
        tiles = new TilePane(mineWidth, mineHeight, this.mineCount, this.offset);

        stats.getGameState().addListener(this);
        tiles.getGameState().addListener(this);

        root = group;
        root.getChildren().addAll(stats, tiles);

        masterState = new GameState(GameState.State.UNSTARTED, "GameScene");

    }

    public void propertyChanged(final GameState changed) {

        // Get information from changed object
        GameState.State newState = changed.getState();
        String owner = changed.getOwner();

        // Update the master state once everything is done.
        masterState.setStateSilent(newState);

        System.out.println(changed.getState());

    }
}

// OxygenCobalt
