// Tile
// Main button-like object, may or may not contain a mine

package nodes.entities;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import javafx.scene.image.ImageView;

import javafx.geometry.Rectangle2D;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Arrays;

import java.util.HashMap;

import events.WaveTimeline;

import generation.board.UpdatePacket;
import generation.states.TileState;

import media.TextureAtlas;
import media.Sprite;
import media.Audio;

public class Tile extends Pane implements EventHandler<MouseEvent> {

    private final int width;
    private final int height;

    private final int x;
    private final int y;

    private final int simpleX;
    private final int simpleY;

    private TileState state;

    private Rectangle2D mouseRect;

    private HashMap<String, ImageView> images;

    private final List<TileState.State> disabledStates;

    public Tile(final int simpleX,
                final int simpleY,
                final int paneX,
                final int paneY) {

        x = (simpleX * 32);
        y = (simpleY * 32);

        width = 32;
        height = 32;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        this.simpleX = simpleX;
        this.simpleY = simpleY;

        state = new TileState(TileState.State.COVERED, simpleX, simpleY);

        // mouseRect is used to detect when the mouse
        // has been released *outside* of the tile
        // The panes location is used to create a location
        // relative to the scene, as I cant do that w/MouseEvent
        mouseRect = new Rectangle2D(x + paneX, y + paneY, 32, 32);

        // Create/Add the disabled states to their respective list
        disabledStates = Arrays.asList(

            TileState.State.DISABLED,
            TileState.State.EXPLODED,
            TileState.State.UNCOVERED,
            TileState.State.DISABLED_MINED

        );

        // Create image map and load the main tile texture
        images = new HashMap<String, ImageView>();
        loadTexture("Normal", TextureAtlas.TILE_NORMAL);

        setOnMousePressed(this);
        setOnMouseReleased(this);

    }

    // Mouse Input functions
    public void handle(final MouseEvent event) {

        MouseButton button = event.getButton();

        // Prevent tile from being clicked on if its in any state other than COVERED
        if (!disabledStates.contains(state.getState())) {

            switch (button) {

                case PRIMARY: onPrimary(event); break;

                case SECONDARY: onSecondary(event); break;

            }

        }

    }

    private void onPrimary(final MouseEvent event) {

        Boolean isNotFlagged = !String.valueOf(state.getState()).contains("FLAGGED");
        EventType type = event.getEventType();

        // Make sure that the tile is not flagged in any way before proceeding

        if (isNotFlagged) {

            // JavaFX polls the last pressed button, making switch statements impossible.

            if (type == MouseEvent.MOUSE_PRESSED) {

                onPress(event);

            }

            if (type == MouseEvent.MOUSE_RELEASED) {

                onRelease(event);

            }

        }

    }

    private void onSecondary(final MouseEvent event) {

        EventType type = event.getEventType();

        // First, check if the mouse is pressing or releasing, to prevent
        // a flag from being placed and then immediately removed
        if (type == MouseEvent.MOUSE_PRESSED) {

            // Playing the flag sound is seperate from any function due to
            // how the invertFlagged() function does not always run

            Audio.FLAG_SOUND.play();

            // Run the pulse function in order to notify the listeners
            // that the tile needs to be flagged, without changing the value.
            state.pulse("Flag");

        }

    }

    private void onPress(final MouseEvent event) {

        // Simply load the pressed texture, as there is no dedicated PRESSED state.
        loadTexture("Pressed", TextureAtlas.TILE_PRESSED);

    }

    private void onRelease(final MouseEvent event) {

        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

        if (isInBox) {

            // Play the click sound, but only if the state isnt MINED,
            // as exploding a tile has its own sound.

            if (state.getState() != TileState.State.MINED) {

                Audio.CLICK_SOUND.play();

            }

            state.setState(TileState.State.UNCOVERED, "Uncover");

        } else { // Otherwise, revert to the normal covered tile appearence

            // Since being able to unpress a tile is universal, no need to check if its mined
            Audio.CLICK_SOUND.play();

            loadTexture("Normal", TextureAtlas.TILE_NORMAL);

        }

    }

    // State management
    public void updateState(final UpdatePacket packet) {

        // Get the change intended by the UpdatePacket, and run it through the switch statement.
        UpdatePacket.Change change = packet.getChange();

        switch (change) {

            case MINE: becomeMine(packet); break;

            case FLAG: invertFlagged(packet); break;

            // Game end cases
            case DISABLE: disableTile(packet); break;

            // These cases are for the tile itself, but do have consequences for the entire board.
            case EXPLODE: explodeMine(packet); break;
            case CLEAR: clearTile(packet); break;

            case UNCOVER: uncover(packet); break;

        }

        state.setStateSilent(packet.getNewState());

    }

    private void uncover(final UpdatePacket packet) {

        TileState.State newState = packet.getNewState();

        int nearMines;

        // Get the auxillary element from nearMines and
        // first check if its an actual instance of Integer

        if (packet.getAuxillary() instanceof Integer) {

            // If so, cast it to integer as its safe to do so.
            nearMines = (int) packet.getAuxillary();

        } else {

            throw new IllegalArgumentException("Given nearMines is not an integer.");

        }

        // Determine which grid state to use depending on the Y coordinates,
        // to prevent odd grid borders at the top left tiles.
        Sprite gridSprite = new Sprite(TextureAtlas.GRID_ATLAS, 1, 1);

        if (x == 0) {

            gridSprite.setX(0);

        }
        if (y == 0) {

            gridSprite.setY(0);

        }

        // Wipe every texture, to prevent conflicts when the new textures are loaded.
        wipeTextures();

        // Load both grid and uncovered textures.
        loadTexture("Uncovered", TextureAtlas.STATE_UNCOVERED[nearMines]);
        loadTexture("Grid", gridSprite);

    }

    private void becomeMine(final UpdatePacket packet) {

        // Nothing needs to be run when a tile becomes a mine, so this function remains empty.

    }

    private void explodeMine(final UpdatePacket packet) {

        // Simply load the exploded texture, and then play the corresponding sound

        loadTexture("Exploded", TextureAtlas.STATE_EXPLODED);

        Audio.EXPLODE_SOUND.play();

    }

    private void invertFlagged(final UpdatePacket packet) {

        String stringState = String.valueOf(packet.getNewState());

        // If the tile should now be flagged, load its texture
        if (stringState.contains("FLAGGED")) {

            loadTexture("Flagged", TextureAtlas.STATE_FLAGGED);

        } else { // Otherwise revert to a normal tile

            loadTexture("Normal", TextureAtlas.TILE_NORMAL);

        }

    }

    private void disableTile(final UpdatePacket packet) {

        int originX = packet.getOriginX();
        int originY = packet.getOriginY();

        String type;

        // Like uncover(), check if this auxillary value is an instance of String.
        if (packet.getAuxillary() instanceof String) {

            // If so, cast it to String as its safe to do so.
            type = (String) packet.getAuxillary();

        } else {

            throw new IllegalArgumentException("Given type is not a String.");

        }


        // Create a wavetimeline w/the given type [Usually EXPLOSION
        // or CLEARED] and the object itself, and then play it
        WaveTimeline timeline = new WaveTimeline(

            this,

            new Point2D(simpleX, simpleY),
            new Point2D(originX, originY),

            type

        );

        timeline.getTimeline().play();

    }

    private void clearTile(final UpdatePacket packet) {

        // UNCOVERED_CLEARED is no different from UNCOVERED, so no code needs to be ran
        // Just play the clear sound instead.

        Audio.CLEAR_SOUND.play();

    }

    // Image loading
    public void loadTexture(final String name, final Sprite fallback) {

        // Check if the image already exists in the map
        if (images.containsKey(name)) {

            images.get(name).toFront();

        // Otherwise, load it and add it to the map
        } else {

            images.put(

                name,
                TextureAtlas.get(fallback)

            );

            getChildren().add(images.get(name)); // Add it to the pane

        }

    }

    public void wipeTextures() {

        // Iterate through the entire images dictionary and remove every entry
        // This is only used on uncover(), when every texture needs to be removed

        for (String key : images.keySet()) {

            getChildren().remove(images.get(key));

        }

        images.clear();

    }

    // Getters
    public HashMap<String, ImageView> getImages() {

        return images;

    }

    public TileState getTileState() {

        return state;

    }

}

// OxygenCobalt
