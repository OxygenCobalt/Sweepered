// Corner
// Largely decorative object

package nodes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.image.ImageView;

import media.TextureAtlas;
import media.Sprite;

public class Corner extends Pane {
	private final int x;
	private final int y;

	private final int width;
	private final int height;

	private Sprite cornerSprite;
	private ImageView cornerImage;

	public Corner(int simpleX, int simpleY, int paneWidth, int paneHeight) {
		// 
		x = -4 + (simpleX * (paneWidth + 4));
		y = -4 + (simpleY * (paneHeight + 4));

		width = 4;
		height = 4;

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		relocate(x, y);

		cornerSprite = new Sprite(
			TextureAtlas.cornerAtlas,
			simpleX,
			simpleY,
			width,
			height
		);

		cornerImage = TextureAtlas.get(cornerSprite);
		getChildren().add(cornerImage);
	}
}