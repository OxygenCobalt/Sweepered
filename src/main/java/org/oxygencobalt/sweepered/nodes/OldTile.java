// Tile
// Main button-like object, may or may not contain a mine

/*
package nodes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Point2D;

import events.EventBoolean;
import events.WaveTimeline;

import media.TextureAtlas;
import media.Sprite;
import media.Audio;

public class OldTile extends Pane implements EventHandler<MouseEvent> {
    private final int width;
    private final int height;

    private final int x;
    private final int y;

    private final int simpleX;
    private final int simpleY;

    private Rectangle2D mouseRect;

    private WaveTimeline shockwave;

    private Boolean isMine;
    private Boolean isFlagged;
    private Boolean isDisabled;

    private EventBoolean isUncovered;
    private EventBoolean hasExploded;

    // ImageViews are left uninitialized to optimize the game
    // Images are only loaded when they are needed
    private ImageView normalTile;
    private ImageView flaggedTile;
    private ImageView pressedTile;
    private ImageView waveTile;
    private ImageView minedTile;
    private ImageView explodedTile;
    private ImageView nearTile;
    private ImageView gridTile;

    public OldTile(int simpleX, int simpleY, int paneX, int paneY) {
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
        isDisabled = false;

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

    public void notifyOfGameEnd(String type, int explodeX, int explodeY) {
        // Use type to find the correct wave tile to use, use the invalid wave [Yellow] if the type is invalid.
        switch(type) {
            case "Cleared": waveTile = TextureAtlas.get(TextureAtlas.tileClearWave); break;
            case "Explosion": waveTile = TextureAtlas.get(TextureAtlas.tileExplodeWave); break;

            default: waveTile = TextureAtlas.get(TextureAtlas.tileInvalidWave);
        }

        getChildren().add(waveTile);

        shockwave = new WaveTimeline(
            waveTile,
            minedTile,
            flaggedTile,
            isFlagged,
            new Point2D(simpleX, simpleY),
            new Point2D(explodeX, explodeY),
            type
        );

        // Timeline is final, so WaveTimeline does not extend it, rather store it internally.
        // So to play it, first use a getter to retrieve the timeline, and then play it normally.
        shockwave.getTimeline().play();

        isDisabled = true; // Prevent tile from being clicked again, as the game has ended.
    }

    // Mouse detection functions
    public void handle(MouseEvent event) {
        if (!isDisabled) { // Only take mouse events if the game is still active/unstarted
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

                // Mine has its own sound when uncovered, so dont play the click sound if it is one.
                if (!isMine) {Audio.clickSound.play();}

                // TODO: Find a way to remove the click sound for when the last tile get cleared [a.k.a when the board gets cleared]
                // AFAIK, the only way to do that is to rewrite THE ENTIRE UNCOVERING SYSTEM due to the janky, procedural way ive designed it
                // If anything, this class is due for a rewrite anyway, its awful.

            } 

            else {normalTile.toFront();} // If not, revert to normal tile appearance.

        }

    }

    // Variable manipulation functions
    // A bunch of getters and setters used by MinePane to get information w/o making the actual variables public.
    public Boolean isInConstraints(int[] constraintsX, int[] constraintsY) {
        // Constraints are usually given as simple coordinates, so tile uses their simple coordinates as well
        Boolean inXConstraint = constraintsX[0] < simpleX && simpleX < constraintsX[1];
        Boolean inYConstraint = constraintsY[0] < simpleY && simpleY < constraintsY[1];

        // Both booleans are added together as the overal constraints are meant to be a 3x3 square.
        return inXConstraint && inYConstraint;
    }

    public void setUncovered(Boolean value) {
        // Flagged tiles should not be removed recursively, by nature.
        if (!isFlagged) {isUncovered.setValue(value);}
    } 

    public EventBoolean getUncovered() {
        return isUncovered;
    }

    public Boolean getMined() {
        return isMine;
    }

    public EventBoolean getExploded() {
        return hasExploded;
    }
}

// OxygenCobalt
*/
