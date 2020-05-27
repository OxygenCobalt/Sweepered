// Tile
// Main button-like object, may or may not contain a mine

package nodes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import media.TextureAtlas;
import media.Sprite;
import media.Audio;

import events.EventBoolean;

// TODO: Find a way to get tiles to communicate w/one another
public class Tile extends Pane implements EventHandler<MouseEvent> {
    private final int width;
    private final int height;

    private final int x;
    private final int y;

    public final int simpleX;
    public final int simpleY;

    private Rectangle2D mouseRect;

    private boolean isMine;
    private EventBoolean isUncovered;

    // ImageViews are left uninitialized to optimize the game
    // Images are only loaded when they are needed
    private ImageView normalTile;
    private ImageView flaggedTile;
    private ImageView pressedTile;
    private ImageView minedTile;
    private ImageView explodedTile;
    private ImageView nearTile;
    private ImageView gridTile;

    public Tile(int simpleX, int simpleY, int paneX, int paneY) {
        x = (simpleX * 32);
        y = (simpleY * 32);

        this.simpleX = simpleX;
        this.simpleY = simpleY;

        width = 32;
        height = 32;

        // mouseRect is used to detect when the mouse has been released *outside* of the tile
        // The panes location is used to create a location relative to the scene, as I cant do that w/MouseEvent
        mouseRect = new Rectangle2D(x + paneX, y + paneY, 32, 32);

        isMine = false;
        isUncovered = new EventBoolean(false, "isUncovered", simpleX, simpleY);

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        loadTextures();

        setOnMousePressed(this);
        setOnMouseReleased(this);
    }

    private void loadTextures() {
        normalTile = TextureAtlas.get(TextureAtlas.tileNormal);
        flaggedTile = TextureAtlas.get(TextureAtlas.tileFlagged);
        pressedTile = TextureAtlas.get(TextureAtlas.tilePressed);

        getChildren().addAll(pressedTile, flaggedTile, normalTile);
    }

    // Interfacing functions
    // Usually activated by MinePane instead of the Tile itself.
    public void uncover(int nearMines) {
        Sprite gridSprite = new Sprite(TextureAtlas.gridAtlas, 1, 1);

        if (x == 0) {gridSprite.setX(0);}
        if (y == 0) {gridSprite.setY(0);}

        gridTile = TextureAtlas.get(gridSprite);
        nearTile = TextureAtlas.get(TextureAtlas.uncoveredNear[nearMines]);

        getChildren().addAll(nearTile, gridTile);

        isUncovered.setValue(true); // Now that the tile is uncovered, prevent any more clicks on it
    }

    public void becomeMine() {
        // Load mine-specific tiles
        minedTile = TextureAtlas.get(TextureAtlas.uncoveredMined);
        getChildren().add(minedTile);

        isMine = true; // Set isMine to true to prevent this from becoming a mine again
    }

    // Mouse detection functions
    @Override
    public void handle(MouseEvent event) {
        String type = String.valueOf(event.getEventType());

        if (!isUncovered.getValue()) {
            // JavaFX polls the last pressed button, making switch statements impossible.
            if (type.equals("MOUSE_PRESSED")) {onPress(event);}
            if (type.equals("MOUSE_RELEASED")) {onRelease(event);}
        }
    }

    private void onPress(MouseEvent event) {
        Audio.clickSound.stop();

        pressedTile.toFront();
    }

    private void onRelease(MouseEvent event) {
        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

        if (isInBox) {isUncovered.setValue(true);} 
        else {normalTile.toFront();} // If not, revert to normal tile appearance.

        Audio.clickSound.play();
    }

    // Variable obtaining functions
    public boolean isInConstraints(int[] constraintsX, int[] constraintsY) {
        // Constraints are usually given as simple coordinates, so tile uses their simple coordinates as well
        boolean inXConstraint = constraintsX[0] < simpleX && simpleX < constraintsX[1];
        boolean inYConstraint = constraintsY[0] < simpleY && simpleY < constraintsY[1];

        // Both booleans are added togrether as the overal constraints are meant to be a 3x3 square.
        return inXConstraint && inYConstraint;
    }

    public void setUncovered(boolean value) {
        isUncovered.setValue(value);
    }

    public EventBoolean getUncovered() {
        return isUncovered;
    }

    public boolean getMineStatus() {
        return isMine;
    }
}

// OxygenCobalt