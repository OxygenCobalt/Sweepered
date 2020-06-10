// GameScene
// Main Minesweeper game scene.

package nodes.scenes;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Rectangle2D;

import events.observable.Listener;
import events.states.GameState;

import media.Audio;

import nodes.panes.TilePane;
import nodes.panes.StatPane;

public class GameScene extends Scene implements Listener<GameState>, EventHandler<MouseEvent> {

    private Group root;

    private StatPane stats;
    private TilePane tiles;

    private GameState masterState;

    private final int offset;
    private final int mineCount;

    private Rectangle2D tileRect;

    private String[] owners;

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

        // Set up the gameStates and add listeners to GameScene
        masterState = new GameState(GameState.State.UNSTARTED, "GameScene");

        stats.getGameState().addListener(this);
        tiles.getGameState().addListener(this);
        masterState.addListener(this);

        root = group;
        root.getChildren().addAll(stats, tiles);

        // tileRect is used to detect if the mouse is outside of
        // TilePane, and then correcting the gameState accordingly.
        tileRect = new Rectangle2D(tiles.getLayoutX(),
                                   tiles.getLayoutY(),
                                   mineWidth * 32,
                                   mineHeight * 32);

        setOnMouseMoved(this);

    }

    public void propertyChanged(final GameState changed) {

        // Get information from changed object
        GameState.State newState = changed.getState();
        String owner = changed.getOwner();

        // Check the owner of the changed gamestate, and update the
        // other owners respectively to sync the new state.
        switch (owner) {

            case "TilePane": stats.updateGameState(newState); break;

            case "StatPane": tiles.updateGameState(newState); break;

            case "GameScene": stats.updateGameState(newState);
                              tiles.updateGameState(newState);
                              break;

        }

        // Update the master state once everything is done.
        masterState.setStateSilent(newState);

    }

    public void handle(final MouseEvent event) {

        // If the game is started, find if the pointer is
        // still within TilePane. If not, then correct the gamestate
        // to STARTED to prevent it from being stuck on UNCERTAIN

        if (masterState.isStarted()) {

            Boolean isInPane = tileRect.contains(event.getSceneX(), event.getSceneY());

            if (!isInPane) {

                masterState.setState(GameState.State.STARTED);

            }

        }

    }

}

// OxygenCobalt
