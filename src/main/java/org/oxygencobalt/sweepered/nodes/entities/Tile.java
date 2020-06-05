// Tile
// Main button-like object, may or may not contain a mine

package nodes.entities;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;

import javafx.geometry.Rectangle2D;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import events.WaveTimeline;

import generation.board.ChangePacket;
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

    public Tile(int simpleX, int simpleY, int paneX, int paneY) {

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

        // mouseRect is used to detect when the mouse has been released *outside* of the tile
        // The panes location is used to create a location relative to the scene, as I cant do that w/MouseEvent
        mouseRect = new Rectangle2D(x + paneX, y + paneY, 32, 32);

        // Create/Add the disabled states to their respective list
        // FIXME: Shorten this please
        disabledStates = Arrays.asList(
            TileState.State.DISABLED,
            TileState.State.EXPLODED,
            TileState.State.UNCOVERED,
            TileState.State.DISABLED_MINED
        );

        // Create image map and load the main tile texture
        images = new HashMap<String, ImageView>();
        loadTexture("Normal", TextureAtlas.tileNormal);

        setOnMousePressed(this);
        setOnMouseReleased(this);
    }

    // Mouse Input functions
    public void handle(MouseEvent event) {

        // TODO: Maybe you should use the JavaFX mouse enums.
        String button = String.valueOf(event.getButton());

        // Prevent tile from being clicked on if its in any state other than COVERED
        if (!disabledStates.contains(state.getState())) {

            switch (button) {

                case "PRIMARY": onPrimary(event); break;

                case "SECONDARY": onSecondary(event); break;

            }

        }

    }

    private void onPrimary(MouseEvent event) {

        Boolean isNotFlagged = !String.valueOf(state.getState()).contains("FLAGGED");
        String type = String.valueOf(event.getEventType());

        // Make sure that the tile is not flagged in any way before proceeding

        if (isNotFlagged) {

            // JavaFX polls the last pressed button, making switch statements impossible.

            if (type.equals("MOUSE_PRESSED")) {onPress(event);}
            if (type.equals("MOUSE_RELEASED")) {onRelease(event);}

        }

    }

    private void onSecondary(MouseEvent event) {

        String type = String.valueOf(event.getEventType());

        // Also check if the mouse is pressing or releasing, to prevent
        // a flag from being placed and then immediately removed
        if (type.equals("MOUSE_PRESSED")) {

            // Playing the flag sound is seperate from any function due to
            // how the invertFlagged() function does not always run

            Audio.flagSound.play();

            // Switch between two near-identical enums in order to notify the listeners repeatedly
            // FIXME: Once you write your own observer class, just change pulse() to fire an observer change.
            state.pulse(
                TileState.State.FLAG_QUERY,
                TileState.State.FLAG_QUERY_,
                "Flag"
            );

        }        

    }

    private void onPress(MouseEvent event) {

        String button = String.valueOf(event.getButton());
        Boolean isNotFlagged = state.getState() != TileState.State.FLAGGED;

        loadTexture("Pressed", TextureAtlas.tilePressed);

    }

    private void onRelease(MouseEvent event) {

        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

        if (isInBox) {

            // Play the click sound, but only if the state isnt MINED,
            // as exploding a tile has its own sound.

            if (state.getState() != TileState.State.MINED) {

                Audio.clickSound.play();

            }

            state.setState(TileState.State.UNCOVERED, "Uncover");

        } else { // Otherwise, revert to the normal covered tile appearence

            // Since being able to unpress a tile is universal, no need to check if its mined
            Audio.clickSound.play();

            loadTexture("Normal", TextureAtlas.tileNormal);
        }

    }

    // State management
    public void updateState(ChangePacket packet) {

        // NewState is declared just in case uncovered overwrites it.
        TileState.State newState = packet.getNewState();
        ChangePacket.Change change = packet.getChange();

        switch (change) {

            // No function needs to be ran when turning a tile into a mine
            case MINE: becomeMine(packet); break;

            // The board function can return two types of ChangePackets, so have cases for each of them
            case FLAG: invertFlagged(packet); break;

            // Game end cases
            case DISABLE: disableTile(packet); break;

            // These cases are for the tile itself, while the above cases are for the entire board.
            case EXPLODE: explodeMine(packet); break;
            case CLEAR: clearTile(packet); break;

            case UNCOVER: uncover(packet); break;

        }

        state.setStateSilent(newState);

    }

    private void uncover(ChangePacket packet) {

        TileState.State newState = packet.getNewState();

        int nearMines;

        // Get the auxillary element from nearMines and first check if its an actual instance of Integer

        // FIXME?: Im really not sure if this is a good way to write this.

        if (packet.getAuxillary() instanceof Integer) {

            // If so, cast it to integer as its safe to do so.
            nearMines = (int) packet.getAuxillary();

        }

        else {

            throw new IllegalArgumentException("Given nearMines is not an integer.");

        }

        // Determine which grid state to use depending on the Y coordinates,
        // to prevent odd grid borders at the top left tiles.
        Sprite gridSprite = new Sprite(TextureAtlas.gridAtlas, 1, 1);

        if (x == 0) {gridSprite.setX(0);}
        if (y == 0) {gridSprite.setY(0);}

        // Load both grid and uncovered textures.
        loadTexture("Uncovered", TextureAtlas.uncoveredNear[nearMines]);
        loadTexture("Grid", gridSprite);

    }

    private void becomeMine(ChangePacket packet) {

        // Simply make sure that the normal, covered tile is shown
        // In case that the tile is becoming a mine after being unflagged.
        loadTexture("Normal", TextureAtlas.tileNormal);

    }

    private void explodeMine(ChangePacket packet) {

        // Simply loaded the exploded texture, and then play the corresponding sound

        loadTexture("Exploded", TextureAtlas.uncoveredExploded);

        Audio.explodeSound.play();

    }

    private void invertFlagged(ChangePacket packet) {

        String stringState = String.valueOf(packet.getNewState());

        if (stringState.contains("FLAGGED")) { // If the tile should now be flagged, load its texture

            loadTexture("Flagged", TextureAtlas.tileFlagged);

        } 

        else { // Otherwise revert to a normal tile

            loadTexture("Normal", TextureAtlas.tileNormal);

        }

    }

    private void disableTile(ChangePacket packet) {

        int originX = packet.getOriginX();
        int originY = packet.getOriginY();

        String type;

        // Like uncover, check if this auxillary value is an instance of String.
        if (packet.getAuxillary() instanceof String) {

            // If so, cast it to String as its safe to do so.
            type = (String) packet.getAuxillary();

        }

        else {

            throw new IllegalArgumentException("Given type is not a String.");

        }


        WaveTimeline timeline = new WaveTimeline(
            this,

            new Point2D(simpleX, simpleY),
            new Point2D(originX, originY),

            type
        );

        timeline.getTimeline().play();

    }

    private void clearTile(ChangePacket packet) {

        // UNCOVERED_CLEARED is no different from UNCOVERED, so no code needs to be ran
        // Just play the clear sound instead.

        Audio.clearSound.play();

    }

    // Image loading
    public void loadTexture(String name, Sprite fallback) {

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

    // Getters
    public HashMap<String, ImageView> getImages() {return images;}
    public TileState getTileState() {return state;}

}

// OxygenCobalt