// TextureAtlas
// Class containing all image coordinates and the function to turn them into ImageViews.

package media.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class TextureAtlas {

    private TextureAtlas() {

        // Isn't called.

    }

    // Main image fetching function
    // Returns an ImageView w/a viewport as I cant directly crop an Image

    public static ImageView get(final Sprite sprite) {

        ImageView toReturn = new ImageView(sprite.getParentImage());
        toReturn.setViewport(sprite.getRect2D());

        return toReturn;

    }

    // Image Data
    // Abandon all hope ye who enter here

    // Note: Tile coordinates are based off their actual x/y values divided by their width/height.

    // Basic UI elements
    private static final Image UI_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/uiAtlas.png").toString()

    );
    public static final Sprite TILE_NORMAL = new Sprite(UI_ATLAS, 0, 0);
    public static final Sprite TILE_PRESSED = new Sprite(UI_ATLAS, 1, 0);

    public static final Sprite RESET_NORMAL = new Sprite(UI_ATLAS, 0, 1, 36, 36);
    public static final Sprite RESET_PRESSED = new Sprite(UI_ATLAS, 1, 1, 36, 36);

    // Tile States
    private static final Image STATE_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/stateAtlas.png").toString()

    );
    public static final Sprite STATE_FLAGGED = new Sprite(STATE_ATLAS, 0, 0);
    public static final Sprite STATE_BAD_FLAG = new Sprite(STATE_ATLAS, 1, 0);
    public static final Sprite STATE_MINED = new Sprite(STATE_ATLAS, 2, 0);
    public static final Sprite STATE_EXPLODED = new Sprite(STATE_ATLAS, 3, 0);

    // Uncovered Number-Tiles
    // These are indexed by mineCount [See: Tile's uncover()], so its an array.
    public static final Sprite[] STATE_UNCOVERED = new Sprite[]{

        // The 0th uncovered tile points to an out of bounds
        // location in order to appear transparent
        new Sprite(STATE_ATLAS, 4, 0),

        new Sprite(STATE_ATLAS, 0, 1),
        new Sprite(STATE_ATLAS, 1, 1),
        new Sprite(STATE_ATLAS, 2, 1),
        new Sprite(STATE_ATLAS, 3, 1),
        new Sprite(STATE_ATLAS, 0, 2),
        new Sprite(STATE_ATLAS, 1, 2),
        new Sprite(STATE_ATLAS, 2, 2),
        new Sprite(STATE_ATLAS, 3, 2)

    };

    // Waves
    private static final Image WAVE_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/waveAtlas.png").toString()

    );
    public static final Sprite CLEAR_WAVE = new Sprite(WAVE_ATLAS, 0, 0);
    public static final Sprite INVALID_WAVE = new Sprite(WAVE_ATLAS, 1, 0);
    public static final Sprite EXPLODE_WAVE = new Sprite(WAVE_ATLAS, 2, 0);

    // Faces [For the reset button]
    public static final Image FACE_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/faceAtlas.png").toString()

    );
    public static final Sprite FACE_NORMAL = new Sprite(FACE_ATLAS, 0, 0, 36, 36);
    public static final Sprite FACE_UNCERTAIN = new Sprite(FACE_ATLAS, 1, 0, 36, 36);
    public static final Sprite FACE_EXPLOSION = new Sprite(FACE_ATLAS, 2, 0, 36, 36);
    public static final Sprite FACE_CLEARED = new Sprite(FACE_ATLAS, 3, 0, 36, 36);
    public static final Sprite FACE_WAITING = new Sprite(FACE_ATLAS, 0, 1, 36, 36);

    // Digits [Used w/the timer and flag count]
    public static final Image DIGIT_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/digitAtlas.png").toString()

    );
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

    // Icons [Used in the ConfigMenu]
    public static final Image ICON_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/iconAtlas.png").toString()

    );
    public static final Sprite ICON_BACK = new Sprite(ICON_ATLAS, 0, 0);
    public static final Sprite ICON_CONFIRM = new Sprite(ICON_ATLAS, 1, 0);
    public static final Sprite ICON_ONE_MINE = new Sprite(ICON_ATLAS, 2, 0);
    public static final Sprite ICON_TWO_MINE = new Sprite(ICON_ATLAS, 3, 0);
    public static final Sprite ICON_THREE_MINE = new Sprite(ICON_ATLAS, 0, 1);
    public static final Sprite ICON_EXPLODE = new Sprite(ICON_ATLAS, 1, 1);
    public static final Sprite ICON_CUSTOM = new Sprite(ICON_ATLAS, 2, 1);
    public static final Sprite ICON_ABOUT = new Sprite(ICON_ATLAS, 3, 1);

    // Grid/Corners are special cases, as their respective nodes generate their own sprite objects
    public static final Image GRID_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/gridAtlas.png").toString()

    );;
    public static final Image CORNER_ATLAS = new Image(

        TextureAtlas.class.getResource("/textures/cornerAtlas.png").toString()

    );

    // Or in the case of WINDOW_ICON, its loaded as an icon
    public static final Image WINDOW_ICON = new Image(

        TextureAtlas.class.getResource("/textures/windowIcon.png").toString()

    );;

}

// OxygenCobalt
