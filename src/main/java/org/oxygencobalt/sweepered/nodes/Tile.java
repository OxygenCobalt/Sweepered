// Tile
// Main button-like object, may or may not contain a mine

package nodes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Point2D;

import events.EventBoolean;
import events.ShockwaveTimeline;

import media.TextureAtlas;
import media.Sprite;
import media.Audio;

// TODO: Find a way to get tiles to communicate w/one another
public class Tile extends Pane implements EventHandler<MouseEvent> {
    private final int width;
    private final int height;

    private final int x;
    private final int y;

    public final int simpleX;
    public final int simpleY;

    private Rectangle2D mouseRect;

    private ShockwaveTimeline shockwave;

    private boolean isMine;
    private boolean isFlagged;
    private boolean isUnclickable;

    private EventBoolean isUncovered;
    private EventBoolean hasExploded;

    // ImageViews are left uninitialized to optimize the game
    // Images are only loaded when they are needed
    private ImageView normalTile;
    private ImageView flaggedTile;
    private ImageView pressedTile;
    private ImageView shockwaveTile;
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
        isFlagged = false;
        isUnclickable = false;

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

        // shockwaveTile gets its opacity set to 0 for the shockwave transition that may happen later.
        shockwaveTile = TextureAtlas.get(TextureAtlas.tileShockwave);
        shockwaveTile.setOpacity(0);

        getChildren().addAll(pressedTile, flaggedTile, normalTile, shockwaveTile);
    }

    // State functions
    // State-controlling functions usually only activated by tile, but sometimes minepane uses them.
    private void press() {
        if (!isFlagged) {
            pressedTile.toFront();
        }
    } 

    private void flag() {
        if (!isFlagged) {flaggedTile.toFront();} 
        else {normalTile.toFront();}

        isFlagged = !isFlagged;

        Audio.flagSound.play();
    }

    public void uncover(int nearMines) {
        // First, check if mine is safe to uncover
        if (!isMine) {
            // Determine which grid state to use depending on the Y coordinates,
            // to prevent odd grid borders at the top left tiles.
            Sprite gridSprite = new Sprite(TextureAtlas.gridAtlas, 1, 1);

            if (x == 0) {gridSprite.setX(0);}
            if (y == 0) {gridSprite.setY(0);}

            // Create grid tile and calcualate nearTile based off number of surrounding mines [given by MinePane]
            gridTile = TextureAtlas.get(gridSprite);
            nearTile = TextureAtlas.get(TextureAtlas.uncoveredNear[nearMines]);

            getChildren().addAll(nearTile, gridTile);
        }
    }

    public void becomeMine() {
        // Load mine-specific tiles
        minedTile = TextureAtlas.get(TextureAtlas.uncoveredMined);

        getChildren().add(minedTile);

        normalTile.toFront(); // Prevent mines from being shown by bring normaltile back up

        // Set isMine to true to prevent this from becoming a mine again
        // Also initialize hasExploded in the case that this tile explodes
        isMine = true;
        hasExploded = new EventBoolean(false, "hasExploded", simpleX, simpleY);
    }

    public void explode() {
        // Load the exploded tile and add it
        explodedTile = TextureAtlas.get(TextureAtlas.uncoveredExploded);
        getChildren().add(explodedTile);

        // Remove minetile to prevent it from being "Revealed" by the shockwave.
        getChildren().remove(minedTile);
        minedTile = null;

        hasExploded.setValue(true);

        Audio.explodeSound.play();        
    }

    public void notifyOfExplosion(int explodeX, int explodeY) {
        shockwave = new ShockwaveTimeline(
            shockwaveTile,
            minedTile,
            new Point2D(simpleX, simpleY),
            new Point2D(explodeX, explodeY)
        );

        shockwave.getTimeline().play();
    }

    // Mouse detection functions
    @Override
    public void handle(MouseEvent event) {
        if (!isUnclickable) { // Only take mouse events if the game is still active/unstarted
            String type = String.valueOf(event.getEventType());

            if (!isUncovered.getValue()) {
                // JavaFX polls the last pressed button, making switch statements impossible.
                if (type.equals("MOUSE_PRESSED")) {onPress(event);}
                if (type.equals("MOUSE_RELEASED")) {onRelease(event);}
            }
        }
    }

    private void onPress(MouseEvent event) {
        String button = String.valueOf(event.getButton());

        switch (button) { 
            case "PRIMARY":   press(); break;
            case "SECONDARY": flag(); break;
        }
    }

    private void onRelease(MouseEvent event) {
        String button = String.valueOf(event.getButton()); 

        // Ignore all mouse pressed outside of left-click
        // Right-Click is used for flagging tiles, and middle click isnt used.
        // Also check if isFlagged is true/false, as flagging a tile disables it.
        if (button.equals("PRIMARY") && !isFlagged) {

            // Find if the mouse pointer is still within the Rect2D
            Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

            if (isInBox) {

                isUncovered.setValue(true);

                // A mine exploding has its own sound, so exclude it from the click sound
                if (!isMine) {Audio.clickSound.play();}

            } 
            else {normalTile.toFront();} // If not, revert to normal tile appearance.

        }

    }

    // Variable manipulation functions
    // A bunch of getters and setters used by MinePane to get information w/o making the actual variables public.
    public boolean isInConstraints(int[] constraintsX, int[] constraintsY) {
        // Constraints are usually given as simple coordinates, so tile uses their simple coordinates as well
        boolean inXConstraint = constraintsX[0] < simpleX && simpleX < constraintsX[1];
        boolean inYConstraint = constraintsY[0] < simpleY && simpleY < constraintsY[1];

        // Both booleans are added togrether as the overal constraints are meant to be a 3x3 square.
        return inXConstraint && inYConstraint;
    }

    public void setUncovered(boolean value) {
        // Flagged tiles should not be removed recursively, by nature.
        if (!isFlagged) {isUncovered.setValue(value);}
    }

    public void setUnclickable(boolean value) { // Would be setDisabled if that wasnt already a method of node.
        isUnclickable = value;
    }

    public EventBoolean getUncovered() {
        return isUncovered;
    }

    public boolean getMined() {
        return isMine;
    }

    public EventBoolean getExploded() {
        return hasExploded;
    }
}

// OxygenCobalt