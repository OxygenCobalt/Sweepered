// Sprite
// A class I wrote to have a Rect2D-like object that I could also place an image inside, for TextureAtlas.
// X-Y coordinates are stored as simplified representations, as in 32x 64y would become 1x 2y.

package media;

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Sprite {
	private final Image parentImage;

	private int x;
	private int y;

	private final int width;
	private final int height; 

	public Sprite(Image argParentImage, int argX, int argY, int... size) {
		parentImage = argParentImage;

		x = argX;
		y = argY;

		// Size is an optional argument, so if no argument is provided the w/h immediately defaults to 32
		if (size.length == 2) {
			width = size[0];
			height = size[1];

		} else {
			width = 32;
			height = 32;
		}
	}

	public void setX(int argX) {
		x = argX;
	}

	public void setY(int argY) {
		y = argY;
	}

	public Rectangle2D getRect2D() {
		return new Rectangle2D(x * width, y * height, width, height);
	}

	public Image getParentImage() {
		return parentImage;
	}
}