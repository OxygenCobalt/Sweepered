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

	public Sprite(Image argParentImage, int argX, int argY) {
		x = argX;
		y = argY;

		parentImage = argParentImage;
	}

	public void setX(int argX) {
		x = argX;
	}

	public void setY(int argY) {
		y = argY;
	}

	public Rectangle2D getRect2D() {
		return new Rectangle2D(x * 32, y * 32, 32, 32);
	}

	public Image getParentImage() {
		return parentImage;
	}
}