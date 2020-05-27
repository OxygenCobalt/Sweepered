// TextureAtlas
// Class containing all image coordinates and the function to turn them into imageviews.

package media;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TextureAtlas {
	// Main image fetching function
	// Returns an imageview w/a viewport as I cant directly crop an Image
	public static ImageView get(Sprite sprite) {
		ImageView toReturn = new ImageView(sprite.getParentImage());
		toReturn.setViewport(sprite.getRect2D());

		return toReturn;
	}

	// Image Data
	// Abandon all hope ye who enter here
	private static final String resPath = "file:src/main/resources/textures/";

	// Basic Tiles
	private static final Image tileAtlas = new Image(resPath + "tileAtlas.png");
	public static final Sprite tileNormal = new Sprite(tileAtlas, 0, 0); // Last two arguments represent the sprites simplified x/y values in the atlas
	public static final Sprite tileFlagged = new Sprite(tileAtlas, 1, 0); // They are multiplied by 32 to get the actual coordinates of the sprite
	public static final Sprite tilePressed = new Sprite(tileAtlas, 2, 0);

	// Uncovered Tiles
	private static final Image uncoveredAtlas = new Image(resPath + "uncoveredAtlas.png");
	public static final Sprite uncoveredMined = new Sprite(uncoveredAtlas, 0, 0);
	public static final Sprite uncoveredExploded = new Sprite(uncoveredAtlas, 0, 1);

	// Uncovered Number-Tiles
	public static final Sprite uncoveredNear0 = new Sprite(uncoveredAtlas, 3, 1); // TODO: If you add anims, please point this sprite to another blank tile.
	public static final Sprite uncoveredNear1 = new Sprite(uncoveredAtlas, 0, 2);
	public static final Sprite uncoveredNear2 = new Sprite(uncoveredAtlas, 1, 2);
	public static final Sprite uncoveredNear3 = new Sprite(uncoveredAtlas, 2, 2);
	public static final Sprite uncoveredNear4 = new Sprite(uncoveredAtlas, 3, 2);
	public static final Sprite uncoveredNear5 = new Sprite(uncoveredAtlas, 0, 3);
	public static final Sprite uncoveredNear6 = new Sprite(uncoveredAtlas, 1, 3);
	public static final Sprite uncoveredNear7 = new Sprite(uncoveredAtlas, 2, 3);
	public static final Sprite uncoveredNear8 = new Sprite(uncoveredAtlas, 3, 3);

	// Grid/Corners are special cases, as their respective nodes generate their own sprite objects
	public static final Image gridAtlas = new Image(resPath + "gridAtlas.png");
	public static final Image cornerAtlas = new Image(resPath + "cornerAtlas.png");
}

// OxygenCobalt