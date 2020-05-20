// TextureAtlas
// Parses spritesheets and returns a selected image

package components;

import java.util.Map;
import static java.util.Map.entry;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.geometry.Rectangle2D;

public class TextureAtlas {
	private static String resPath = "file:src/main/resources/";
	// Images
	// These are loaded instead of a new image  being generated to save memory
	private static Image tiles_ss = new Image(resPath + "tiles_ss.png");
	private static Image grid_ss = new Image(resPath + "grid_ss.png");

	// Coordinate list
	// This contains every coordinate for every image on the spritesheet, albeit very ugly
	// I wish I had a better way to set this up.
	private static Map<String, Map<String, Map<String, Rectangle2D>>> textureCoords = Map.ofEntries(
		entry("tiles_ss", Map.ofEntries(
			entry("tiles", Map.ofEntries(
				entry("tile_normal", new Rectangle2D(0, 0, 32, 32)),
				entry("tile_flagged", new Rectangle2D(32, 0, 32, 32)),
				entry("tile_pressed", new Rectangle2D(64, 0, 32, 32)),
				entry("tile_mineshown", new Rectangle2D(96, 0, 32, 32)),
				entry("tile_exploded", new Rectangle2D(0, 32, 32, 32))
			)),

			entry("tiles_near", Map.ofEntries(
				entry("0", new Rectangle2D(32, 32, 32, 32)),
				entry("1", new Rectangle2D(32, 32, 32, 32)),
				entry("2", new Rectangle2D(64, 32, 32, 32)),
				entry("3", new Rectangle2D(96, 32, 32, 32)),
				entry("4", new Rectangle2D(0, 64, 32, 32)),
				entry("5", new Rectangle2D(32, 64, 32, 32)),
				entry("6", new Rectangle2D(64, 64, 32, 32)),
				entry("7", new Rectangle2D(96, 64, 32, 32)),	
				entry("8", new Rectangle2D(0, 96, 32, 32))
			))
		))
	); // TODO: Could possibly iterate through a different list to generate the Map

	public static ImageView get(String path) { // To retreive an image, you format your argument as this/and/this, like a file path
		ImageView crop = new ImageView();
		Rectangle2D viewPort;

		String[] pathSplit = path.split("\\/");

		// Use first "Directory" to find the image meant to be used
		switch (pathSplit[0]) {
			case "tiles_ss": crop.setImage(tiles_ss); break;

			default: throw new RuntimeException("Path " + path + " is invalid");
		}

		viewPort = textureCoords.get(pathSplit[0]).get(pathSplit[1]).get(pathSplit[2]); // AAAAAAAAAA

		crop.setViewport(viewPort);

		return crop; // Return the imageView, now cropped
	}
}

// OxygenCobalt