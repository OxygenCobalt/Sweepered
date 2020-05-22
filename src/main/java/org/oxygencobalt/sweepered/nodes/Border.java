// Border
// Decorative object thats suprisingly hard to set up.

package nodes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.image.ImageView;

import media.TextureAtlas;

public class Border extends Pane {
	private double width;
	private double height;

	private final double sceneWidth; // Scene size is stored here
	private final double sceneHeight;

	private double x;
	private double y;

	private final double simpleX; // "Simplified" coordinates [a.k.a divided by 32, so 64 -> 2]
	private final double simpleY;

	private ImageView borderImage;

	public Border(String type, int argX, int argY, double argSceneWidth, double argSceneHeight) {
		sceneWidth = argSceneWidth;
		sceneHeight = argSceneHeight;

		simpleX = argX;
		simpleY = argY;

		switch (type) { // Check each type, and if not found throw an invalid type
			case "corner": generateCorner(); break;
			case "vertical": generateVertical(); break;
			case "horizontal": generateHorizontal(); break;

			default: throw new RuntimeException("Invalid Border Type");
		}

		// Location/Size are handled after the corners are generated, as they initialize the variables needed.
		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		relocate(x, y);

		getChildren().add(borderImage);
	}

	// Border generation functions
	// Pretty awful, but at least Ive tried to explain it w/comments.
	private void generateCorner() {
		double rotation = 0;

		// The basic calculation involves a scene dimension [subtracted by 10 to make up for the top-left corner]
		// being multiplied by the X or Y value. This can either produce a 0 value [Left Side] or a value 
		// consistant w/the scene dimension [Right Side]
		x = (sceneWidth - 10) * simpleX;

		// Middle corners case
		// These corners have different images, and different placement patterns,
		// so they have their own case
		if (simpleY == 1) {
			borderImage = TextureAtlas.get(TextureAtlas.borderDualCorner);
			if (simpleX == 1) {rotation = 180;} // Flip image if on right side
			y = 50;

		} else {
			borderImage = TextureAtlas.get(TextureAtlas.borderCorner);
			rotation = (simpleX * 90);

			// Add 270 to the negative variant of rotation to get the upside-down values for the bottom corners
			// I would have subtracted this, but that messes up one of the corners for some reason.
			if (simpleY == 2) {rotation = -rotation + 270;}
			y = ((sceneHeight - 10) * simpleY) / 2; // Due to the nature of the Y value, the result is divided by 2 to prevent the border from going off-screen
		}

		width = 10;
		height = 10;

		borderImage.setRotate(rotation);
	}

	private void generateVertical() {
		borderImage = TextureAtlas.get(TextureAtlas.borderNormal);

		// 155 is apparently the magic number that allows the vertical borders to be placed correctly
		// Yeah, I dont know either.
		x = (((sceneWidth - 10) * simpleX) - 155); 
		y = (simpleY * 320);

		width = 10;
		height = 320;

		// 90 is initally used to make the border vertical, and then its incremented by 180 to flip it depending on the X coordinate
		borderImage.setRotate(90 + (180 * simpleX));
	}

	private void generateHorizontal() {
		double rotation = 0;

		x = simpleX * 320;

		// Similarly to middle corners, middle horizontal borders have their own case as well.
		if (simpleY == 1) {
			borderImage = TextureAtlas.get(TextureAtlas.borderBetween);
			y = 50;

		} else {
			borderImage = TextureAtlas.get(TextureAtlas.borderNormal);
			if (simpleY == 0) {borderImage.setRotate(180);}
			y = ((sceneHeight - 10) * simpleY) / 2;
		}
	}	
}