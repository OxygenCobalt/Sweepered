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
import events.values.EventInteger;

import media.Audio;

import nodes.panes.TilePane;
import nodes.panes.StatPane;

public class GameScene extends Scene implements EventHandler<MouseEvent> {

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

        stats.getGameState().addListener(gameStateListener);
        tiles.getGameState().addListener(gameStateListener);
        masterState.addListener(gameStateListener);

        tiles.getFlagCount().addListener(flagCountListener);

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

    // Used to monitor changes in the Game State
    Listener<GameState> gameStateListener = changed -> {

        // Get information from changed object
        GameState.State newState = changed.getState();
        String owner = changed.getOwner();

        // Update every nodes state indescriminately
        stats.updateGameState(newState);
        tiles.updateGameState(newState);

        // Update the master state once everything is done.
        masterState.setStateSilent(newState);

    };

    // Used to monitor changes in the flag count
    Listener<EventInteger> flagCountListener = changed -> {

        System.out.println(changed.getValue());

    };

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
