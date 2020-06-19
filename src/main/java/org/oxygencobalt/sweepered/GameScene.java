// GameScene
// Main Minesweeper game scene.

package org.oxygencobalt.sweepered;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Rectangle2D;

import media.audio.Audio;

import shared.Configuration;
import shared.values.GameState;
import shared.values.EventInteger;
import shared.observable.Listener;

import stats.StatPane;
import tiles.TilePane;

public class GameScene extends Scene implements EventHandler<MouseEvent> {

    private EventInteger width;
    private EventInteger height;

    private EventInteger mode;

    private int tileWidth;
    private int tileHeight;
    private int mineCount;

    private Group root;

    private StatPane stats;
    private TilePane tiles;

    private Pane coverPane;

    private GameState masterState;
    private GameState.State stateCache;

    private int offset;

    private Rectangle2D tileRect;

    private String[] owners;

    public GameScene(final Group group) {

        // Call super to construct Scene()
        super(group);

        mode = Configuration.getEventConfigValue("Mode");
        mode.addListener(modeListener);
        setModeValues(mode.getValue());

        // The offset is simply a value used to space out
        // the different panes in GameScene
        offset = 14;

        // Width is only stored after the scene is set up
        // as the super constructor has to be called first
        width = new EventInteger((tileWidth * 32) + (18 + offset), "Width");
        height = new EventInteger((tileHeight * 32) + (76 + offset), "Height");

        // Also set up the stylesheet that is used throughout the program
        getStylesheets().add("file:src/main/resources/style/main.css");

        setFill(Color.web("3d3d3d")); // Background color matches w/the tile color, mostly

        // Load the sounds in from Audio
        Audio.loadSounds();

        // Load the main nodes
        stats = new StatPane(40, (tileWidth * 32), this.offset, this.mineCount);
        tiles = new TilePane(tileWidth, tileHeight, this.mineCount, this.offset);

        // coverPane is a pane used to disable the entire game
        // if the config menu is ever shown.
        coverPane = new Pane();

        // Set up the gameStates and add listeners to GameScene
        masterState = new GameState(GameState.State.UNSTARTED, "GameScene");

        stats.getGameState().addListener(gameStateListener);
        tiles.getGameState().addListener(gameStateListener);
        masterState.addListener(gameStateListener);

        tiles.getFlagCount().addListener(flagCountListener);

        root = group;
        root.getChildren().addAll(coverPane, stats, tiles);

        // tileRect is used to detect if the mouse is outside of
        // TilePane, and then correcting the gameState accordingly.
        tileRect = new Rectangle2D(tiles.getLayoutX(),
                                   tiles.getLayoutY(),
                                   tileWidth * 32,
                                   tileHeight * 32);

        setOnMouseMoved(this);

    }

    private void setModeValues(final int newMode) {

        // Each mode has a different mine count, so create an array to be indexed
        // to be indexed later on
        int[] mineCounts = new int[]{10, 35, 40, 99};

        // All modes below 2 operate on a 9x9 board, so set that as such
        if (newMode >= 0 && newMode < 2) {

            tileWidth = 9;
            tileHeight = 9;

            mineCount = mineCounts[newMode];

        // Otherwise, create a 16x16 board
        } else if (newMode >= 2 && newMode <= 3) {

            tileWidth = 16;
            tileHeight = 16;

            mineCount = mineCounts[newMode];

        // Otherwise, the mode is invalid and the width/height/mineCount are loaded
        // from the alternate values loaded by configuration. This is also a custom
        // value would be created.
        } else {

            tileWidth = Configuration.getConfigValue("tileWidth");
            tileHeight = Configuration.getConfigValue("tileHeight");

            mineCount = Configuration.getConfigValue("mineCount");

        }

    }

    // Used to monitor changes in the Game State
    Listener<GameState> gameStateListener = changed -> {

        // Get information from changed object
        GameState.State newState = changed.getState();
        String owner = changed.getOwner();

        // Update every nodes state indescriminately
        stats.updateGameState(newState);
        tiles.updateGameState(newState);

        if (changed.isDisabled()) {

            // If the game is disabled, then bring the coverPane
            // to the front in order to disable the game, as placing
            // a blank pane over other panes will cause those panes
            // to stop registering mouse input.
            coverPane.toFront();
            coverPane.setPrefSize(getWidth(), getHeight());

            // Store the previous state, in order to be loaded later
            // if the settings menu is exited, and then set the state to
            // DISABLED
            stateCache = masterState.getState();

            masterState.setStateSilent(newState);

        } else {

            // If the disabled state is being replaced with any
            // other state [While the masterState is still DISABLED,
            // as it hasnt been updated set], then move the coverPane
            // to the back in order to reenable game functions again.

            if (masterState.isDisabled()) {

                coverPane.toBack();

                masterState.setState(stateCache);

            } else {

                // Update the master state once everything is done.
                masterState.setStateSilent(newState);

            }

        }

    };

    // Used to monitor changes in the flag count
    Listener<EventInteger> flagCountListener = changed -> {

        // If a change is detected, just pass it off to statpane
        // as only TilePane does anything w/flagCount
        stats.updateFlagCount(changed.getValue());

    };

    Listener<EventInteger> modeListener = changed -> {

        int newMode = changed.getValue();

        setModeValues(newMode);

        stats.updateBoardValues(tileWidth, tileHeight);
        tiles.updateBoardValues(tileWidth, tileHeight, mineCount);

        width.setValue((tileWidth * 32) + (18 + offset));
        height.setValue((tileHeight * 32) + (76 + offset));

        coverPane.setPrefSize(getWidth(), getHeight());

        // If the mode is changed, make sure to change the game state to
        // UNSTARTED as effectively the entire game is being restarted
        stateCache = GameState.State.UNSTARTED;

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

    public EventInteger getObservableWidth() {

        return width;

    }

    public EventInteger getObservableHeight() {

        return height;

    }


}

// OxygenCobalt