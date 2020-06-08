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

    // Note: Tile coordinates are based off their actual x/y values divided by 32.

    private static final String RES_PATH = "file:src/main/resources/textures/";

    // Basic Tiles
    private static final Image TILE_ATLAS = new Image(RES_PATH + "tileAtlas.png");
    public static final Sprite TILE_NORMAL = new Sprite(TILE_ATLAS, 0, 0);
    public static final Sprite TILE_FLAGGED = new Sprite(TILE_ATLAS, 1, 0);
    public static final Sprite TILE_PRESSED = new Sprite(TILE_ATLAS, 2, 0);

    public static final Sprite CLEAR_WAVE = new Sprite(TILE_ATLAS, 0, 1);
    public static final Sprite INVALID_WAVE = new Sprite(TILE_ATLAS, 1, 1);
    public static final Sprite EXPLODE_WAVE = new Sprite(TILE_ATLAS, 2, 1);

    // Uncovered Tiles
    private static final Image UNCOVERED_ATLAS = new Image(RES_PATH + "uncoveredAtlas.png");
    public static final Sprite UNCOVERED_MINED = new Sprite(UNCOVERED_ATLAS, 0, 0);
    public static final Sprite UNCOVERED_EXPLODED = new Sprite(UNCOVERED_ATLAS, 0, 1);

    // Uncovered Number-Tiles
    // These are indexed by mineCount [See: MinePanes onUncover()], so its an array.
    public static final Sprite[] UNCOVERED_NEAR = new Sprite[]{

        // TODO: If you add anims, please point
        // this sprite to another blank tile.

        new Sprite(UNCOVERED_ATLAS, 3, 1),
        new Sprite(UNCOVERED_ATLAS, 0, 2),
        new Sprite(UNCOVERED_ATLAS, 1, 2),
        new Sprite(UNCOVERED_ATLAS, 2, 2),
        new Sprite(UNCOVERED_ATLAS, 3, 2),
        new Sprite(UNCOVERED_ATLAS, 0, 3),
        new Sprite(UNCOVERED_ATLAS, 1, 3),
        new Sprite(UNCOVERED_ATLAS, 2, 3),
        new Sprite(UNCOVERED_ATLAS, 3, 3)

    };

    // Grid/Corners are special cases, as their respective nodes generate their own sprite objects
    public static final Image GRID_ATLAS = new Image(RES_PATH + "gridAtlas.png");
    public static final Image CORNER_ATLAS = new Image(RES_PATH + "cornerAtlas.png");

}

// OxygenCobalt
