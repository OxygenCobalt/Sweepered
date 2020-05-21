// TextureAtlas
// Parses spritesheets and returns a selected image

package components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import javafx.geometry.Rectangle2D;

public class Media {
	private static final String resPath = "file:src/main/resources/";

	// Image data
	public static final Object[] tiles_ss = new Object[]{ // Tiles
		new Image(resPath + "spritesheets/tiles_ss.png"), 
		"tileNormal", "tileFlagged", "tilePressed", "tileMined", 
		"tileExploded", "tileNear0", "tileNear1", "tileNear2", 
		"tileNear3",  "tileNear4", "tileNear5", "tileNear6", 
		"tileNear7", "tileNear8"
	};

	public static final Object[] grid_ss = new Object[]{ // Underlying grid
		new Image(resPath + "spritesheets/grid_ss.png"),
		"grid0x0y", "grid1x0y", "grid0x1y", "grid1x1y" 
	};

	// Audio data
	public static final AudioClip releaseSound = new AudioClip("file:src/main/resources/audio/release.wav");

	// Returns a texture from the spritesheets
	public static ImageView getTexture(Object[] spriteSheet, String spriteName) {
		ImageView crop = new ImageView((Image) spriteSheet[0]); // Typecasts are needed to convert ambigious Objects into workable variables
		Rectangle2D viewPort;
		int index = 0;

		// Search given spritesheet for the name specified
		for (int i = 1; i < spriteSheet.length; i++) {
			if (((String) spriteSheet[i]).contains(spriteName)) {
				index = i - 1;
			}
		}

		// Convert index of sprite to actual rectangle2d coordinates
		int x = index % 4;
		int y = (index - (index % 4)) / 4;

		viewPort = new Rectangle2D(x * 32, y * 32, 32, 32);

		// Set viewport and return the imageview
		crop.setViewport(viewPort);

		return crop;
	}
}

// OxygenCobalt