// Sprite
// A class I wrote to have a Rect2D-like object that I could also place an image inside, for TextureAtlas.
// X-Y coordinates are stored as simplified representations based on their width and height, 
// So, a 32x32 sprite with coordinates at 32x/64y would become 1x/2y.

package media;

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Sprite {
    private final Image parentImage;

    private int x;
    private int y;

    private final int width;
    private final int height; 

    public Sprite(Image parentImage, int x, int y, int... size) {
        this.parentImage = parentImage;

        this.x = x;
        this.y = y;

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
        this.x = x;
    }

    public void setY(int argY) {
        this.y = y;
    }

    public Image getParentImage() {
        return parentImage;
    }

    public Rectangle2D getRect2D() {
        return new Rectangle2D(x * width, y * height, width, height);
    }
}

// OxygenCobalt