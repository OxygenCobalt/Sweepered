// TextureAtlas
// Class containing all image coordinates and the function to turn them into imageviews.

package media;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class TextureAtlas {

    private TextureAtlas() {

        // Isnt called.

    }

    // Main image fetching function
    // Returns an imageview w/a viewport as I cant directly crop an Image

    public static ImageView get(final Sprite sprite) {

        ImageView toReturn = new ImageView(sprite.getParentImage());
        toReturn.setViewport(sprite.getRect2D());

        return toReturn;

    }

    // FIXME: Rewrite once you add different themes

    // Image Data
    // Abandon all hope ye who enter here

    // Note: Tile coordinates are based off their actual x/y values divided by their width/height.

    private static final String RES_PATH = "file:src/main/resources/textures/";

    // Basic UI elements
    private static final Image UI_ATLAS = new Image(RES_PATH + "uiAtlas.png");
    public static final Sprite TILE_NORMAL = new Sprite(UI_ATLAS, 0, 0);
    public static final Sprite TILE_PRESSED = new Sprite(UI_ATLAS, 1, 0);

    public static final Sprite RESET_NORMAL = new Sprite(UI_ATLAS, 0, 1, 36, 36);
    public static final Sprite RESET_PRESSED = new Sprite(UI_ATLAS, 1, 1, 36, 36);

    // Tile States
    private static final Image STATE_ATLAS = new Image(RES_PATH + "stateAtlas.png");
    public static final Sprite STATE_FLAGGED = new Sprite(STATE_ATLAS, 0, 0);
    public static final Sprite STATE_BAD_FLAG = new Sprite(STATE_ATLAS, 1, 0);
    public static final Sprite STATE_MINED = new Sprite(STATE_ATLAS, 0, 1);
    public static final Sprite STATE_EXPLODED = new Sprite(STATE_ATLAS, 0, 2);

    // Uncovered Number-Tiles
    // These are indexed by mineCount [See: Tile's uncover()], so its an array.
    public static final Sprite[] STATE_UNCOVERED = new Sprite[]{

        // TODO: If you add anims, please point
        // this sprite to another blank tile.

        new Sprite(STATE_ATLAS, 1, 2),
        new Sprite(STATE_ATLAS, 0, 3),
        new Sprite(STATE_ATLAS, 1, 3),
        new Sprite(STATE_ATLAS, 2, 3),
        new Sprite(STATE_ATLAS, 3, 3),
        new Sprite(STATE_ATLAS, 0, 4),
        new Sprite(STATE_ATLAS, 1, 4),
        new Sprite(STATE_ATLAS, 2, 4),
        new Sprite(STATE_ATLAS, 3, 4)

    };

    // Waves
    private static final Image WAVE_ATLAS = new Image(RES_PATH + "waveAtlas.png");
    public static final Sprite CLEAR_WAVE = new Sprite(WAVE_ATLAS, 0, 0);
    public static final Sprite INVALID_WAVE = new Sprite(WAVE_ATLAS, 1, 0);
    public static final Sprite EXPLODE_WAVE = new Sprite(WAVE_ATLAS, 2, 0);

    // Faces [For the reset button]
    public static final Image FACE_ATLAS = new Image(RES_PATH + "faceAtlas.png");
    public static final Sprite FACE_NORMAL = new Sprite(FACE_ATLAS, 0, 0, 36, 36);
    public static final Sprite FACE_UNCERTAIN = new Sprite(FACE_ATLAS, 1, 0, 36, 36);
    public static final Sprite FACE_EXPLOSION = new Sprite(FACE_ATLAS, 2, 0, 36, 36);
    public static final Sprite FACE_CLEARED = new Sprite(FACE_ATLAS, 3, 0, 36, 36);

    // Digits [Used w/the timer and flag count]
    public static final Image DIGIT_ATLAS = new Image(RES_PATH + "digitAtlas.png");
    public static final Sprite[] DIGITS = new Sprite[]{

        new Sprite(DIGIT_ATLAS, 0, 0, 19, 32),
        new Sprite(DIGIT_ATLAS, 1, 0, 19, 32),
        new Sprite(DIGIT_ATLAS, 2, 0, 19, 32),
        new Sprite(DIGIT_ATLAS, 3, 0, 19, 32),
        new Sprite(DIGIT_ATLAS, 0, 1, 19, 32),
        new Sprite(DIGIT_ATLAS, 1, 1, 19, 32),
        new Sprite(DIGIT_ATLAS, 2, 1, 19, 32),
        new Sprite(DIGIT_ATLAS, 3, 1, 19, 32),
        new Sprite(DIGIT_ATLAS, 0, 2, 19, 32),
        new Sprite(DIGIT_ATLAS, 1, 2, 19, 32),
        new Sprite(DIGIT_ATLAS, 2, 2, 19, 32),
        new Sprite(DIGIT_ATLAS, 3, 2, 19, 32),

    };

    // Grid/Corners are special cases, as their respective nodes generate their own sprite objects
    public static final Image GRID_ATLAS = new Image(RES_PATH + "gridAtlas.png");
    public static final Image CORNER_ATLAS = new Image(RES_PATH + "cornerAtlas.png");

}

// OxygenCobalt
