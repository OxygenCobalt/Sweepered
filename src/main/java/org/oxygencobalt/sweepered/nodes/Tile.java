// Tile
// Main button-like object, may or may not contain a mine

package nodes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.util.HashMap;

import media.TextureAtlas;
import media.Sprite;

import states.TileState;

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

        // Create image map and load the main tile texture
        images = new HashMap<String, ImageView>();
        loadTexture("Normal", TextureAtlas.tileNormal);

        setOnMousePressed(this);
        setOnMouseReleased(this);
    }

    // Mouse Input functions
    public void handle(MouseEvent event) {

        // TODO: Maybe you should use the JavaFX mouse enums.
        String type = String.valueOf(event.getEventType());
        String button = String.valueOf(event.getButton());

        // Prevent tile from being clicked on if its in any state other than COVERED
        if (state.getState() != TileState.State.DISABLED) {

            switch (button) {

                case "PRIMARY": {

                    if (type.equals("MOUSE_PRESSED")) {onPress(event);}
                    if (type.equals("MOUSE_RELEASED")) {onRelease(event);}

                    break;   

                }

                case "SECONDARY": {

                    // Also check if the mouse is pressing or releasing, to prevent
                    // a flag from being placed and then immediately removed
                    if (type.equals("MOUSE_PRESSED")) {

                        // Switch between two near-identical enums in order to notify the listeners repeatedly
                        // FIXME: Find a better way to pulse flag queries
                        state.pulse(
                            TileState.State.FLAG_QUERY,
                            TileState.State.FLAG_QUERY_,
                            "Flag"
                        );

                        break;

                    }

                }

            }

        }

    }

    private void onPress(MouseEvent event) {

        String button = String.valueOf(event.getButton());
        TileState.State rawState = state.getState();
        
        if (rawState != TileState.State.FLAGGED) {

            loadTexture("Pressed", TextureAtlas.tilePressed);

        }

    }

    private void onRelease(MouseEvent event) {
        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());
        Boolean isNotFlagged = state.getState() != TileState.State.FLAGGED;

        if (isInBox && isNotFlagged) {

            state.setState(TileState.State.UNCOVERED, "Uncover");

        }

    }

    // State management
    public void updateState(TileState.State newState) {

        switch (newState) {

            case MINED: becomeMine(newState); break;

            // The board function can return two types of ChangePackets, so have cases for each of them
            case COVERED: invertFlagged(newState); break;
            case FLAGGED: invertFlagged(newState); break;

            // Due to UNCOVERED having multiple constants in TileState,
            // It is used as the default case.
            default: uncover(newState);

        }

    }

    private void uncover(TileState.State newState) {

        // TODO: Add grid + sounds

        // Get the amount of mines near the tile by parsing the
        // UNCOVERED constant for the last character
        int nearMines = Character.getNumericValue(
                String.valueOf(newState).charAt(10)
        );

        loadTexture("Uncovered", TextureAtlas.uncoveredNear[nearMines]);

        // Disable tile once everything is shown [As dealing with the many iterations of UNCOVERED would be frustrating]
        state.setStateSilent(TileState.State.DISABLED);

    }

    private void becomeMine(TileState.State newState) {

        loadTexture("Mined", TextureAtlas.uncoveredMined);

    }

    private void invertFlagged(TileState.State newState) {

        // Set tiles new state [Just in case]
        state.setStateSilent(newState);

        if (state.getState() == TileState.State.FLAGGED) { // If the tile is flagged, load its texture
            loadTexture("Flagged", TextureAtlas.tileFlagged);
        } 

        else { // Otherwise revert to a normal tile

            loadTexture("Normal", TextureAtlas.tileNormal);

        }

    }

    // Image loading
    private void loadTexture(String name, Sprite fallback) {

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
    public TileState getState() {
        return state;
    }
}

// OxygenCobalt
