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

import events.observable.EventBoolean;

import generation.TileState;

import media.TextureAtlas;
import media.Sprite;

public class Tile extends Pane implements EventHandler<MouseEvent> {
    private final int width;
    private final int height;

    private final int x;
    private final int y;

    private final int simpleX;
    private final int simpleY;

    private EventBoolean isUncovered;
    private EventBoolean hasExploded;

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

        isUncovered = new EventBoolean(false, "isUncovered", simpleX, simpleY);

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

        String type = String.valueOf(event.getEventType());

        // JavaFX polls the last pressed button, making switch statements impossible.
        if (type.equals("MOUSE_PRESSED")) {onPress(event);}
        if (type.equals("MOUSE_RELEASED")) {onRelease(event);}

    }

    private void onPress(MouseEvent event) {

        loadTexture("Pressed", TextureAtlas.tilePressed);

    }

    private void onRelease(MouseEvent event) {
        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

        if (isInBox) {

            isUncovered.setValue(true);

        }

    }

    // State management
    public void updateState(TileState state) {
        switch (state) {
            case MINED: becomeMine(); break;

            // Due to UNCOVERED having multiple constants in TileState,
            // It is used as the default case.
            default: uncover(state);
        }
    }

    private void uncover(TileState state) {
        // TODO: Add grid + sounds

        // Get the amount of mines near the tile by parsing the
        // UNCOVERED constant for the last character
        int nearMines = Character.getNumericValue(
                String.valueOf(state).charAt(10)
        );

        loadTexture("Uncovered", TextureAtlas.uncoveredNear[nearMines]);

        // Set isUncovered to true silently for any tiles that were uncovered recursively
        if (!isUncovered.getValue()) {
            isUncovered.setValueSilent(true);
        }
    }

    private void becomeMine() {
        loadTexture("Mined", TextureAtlas.uncoveredMined);
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
    public EventBoolean getUncovered() {
        return isUncovered;
    }
}

// OxygenCobalt
