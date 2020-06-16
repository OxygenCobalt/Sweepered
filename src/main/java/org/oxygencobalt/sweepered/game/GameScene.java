// GameScene
// Main Minesweeper game scene.

package game;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Rectangle2D;

import events.observable.Listener;

import events.states.GameState;
import events.values.EventInteger;

import media.audio.Audio;

import game.panes.ConfigPane;
import game.panes.TilePane;
import game.panes.StatPane;

public class GameScene extends Scene implements EventHandler<MouseEvent> {

    private Group root;

    private ConfigPane config;

    private StatPane stats;
    private TilePane tiles;

    private GameState masterState;

    private final int offset;
    private final int mineCount;

    private Rectangle2D tileRect;

    private String[] owners;

    public GameScene(final Group group,
                     final int tileWidth,
                     final int tileHeight,
                     final int mineCount,
                     final int offset) {

        // Call super to construct Scene(), and then add passed group to class.
        super(group, (tileWidth * 32) + (18 + offset), (tileHeight * 32) + (96 + offset));

        // Also set up the stylesheet that is used throughout the program
        getStylesheets().add("file:src/main/resources/stylesheets/main.css");

        setFill(Color.web("3d3d3d")); // Background color matches w/the tile color, mostly

        this.offset = offset + 4; // 4 is added to make up for some of the borders
        this.mineCount = mineCount;

        // Load the sounds in from Audio
        Audio.loadSounds();

        // Load the main nodes
        config = new ConfigPane((int) getWidth());
        stats = new StatPane(40, (tileWidth * 32), this.offset, this.mineCount);
        tiles = new TilePane(tileWidth, tileHeight, this.mineCount, this.offset);

        // Set up the gameStates and add listeners to GameScene
        masterState = new GameState(GameState.State.UNSTARTED, "GameScene");

        stats.getGameState().addListener(gameStateListener);
        tiles.getGameState().addListener(gameStateListener);
        masterState.addListener(gameStateListener);

        tiles.getFlagCount().addListener(flagCountListener);

        root = group;
        root.getChildren().addAll(config, stats, tiles);

        // tileRect is used to detect if the mouse is outside of
        // TilePane, and then correcting the gameState accordingly.
        tileRect = new Rectangle2D(tiles.getLayoutX(),
                                   tiles.getLayoutY(),
                                   tileWidth * 32,
                                   tileHeight * 32);

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

        // If a change is detected, just pass it off to statpane
        // as only TilePane does anything w/flagCount
        stats.updateFlagCount(changed.getValue());

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
