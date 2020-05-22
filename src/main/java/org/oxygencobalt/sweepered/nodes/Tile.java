// Tile
// Main button-like object, may or may not contain a mine

package nodes;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.Group;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import media.TextureAtlas;
import media.Sprite;
import media.Audio;

public class Tile extends StackPane implements EventHandler<MouseEvent> {
	private final int width;
	private final int height;

	private final int x;
	private final int y;

	private Rectangle2D mouseRect;

	private Boolean isUncovered = false;

	// ImageViews are left uninitialized to optimize the game
	// Images are only loaded when they are needed
	private ImageView normalTile;
	private ImageView flaggedTile;
	private ImageView pressedTile;
	private ImageView mineTile;
	private ImageView explodedTile;
	private ImageView nearTile;
	private ImageView gridTile;

	public Tile(int argX, int argY, int offset, Boolean isMine) {
		width = 32;
		height = 32;

		x = argX * 32;
		y = argY * 32;

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		relocate(x, y);

		// mouseRect is used to detect when the mouse has been released *outside* of the tile
		// Offset is applied in order to create a rect relative to the scene, as I cant poll mouse positions relative to MinePane
		mouseRect = new Rectangle2D(offset + x, (offset * 2) + 40 + y, 32, 32);
		loadTextures();

		setOnMousePressed(this);
		setOnMouseReleased(this);
	}

	private void loadTextures() {
		// normalTile = new ImageView(TextureAtlas.tileAtlas);
		normalTile = TextureAtlas.get(TextureAtlas.tileNormal);
		flaggedTile = TextureAtlas.get(TextureAtlas.tileFlagged);
		pressedTile = TextureAtlas.get(TextureAtlas.tilePressed);

		getChildren().addAll(pressedTile, flaggedTile, normalTile);
	}

	private void uncover() {
		Sprite gridSprite = new Sprite(TextureAtlas.gridAtlas, 1, 1);

		if (x == 0) {gridSprite.setX(0);}
		if (y == 0) {gridSprite.setY(0);}

		nearTile = TextureAtlas.get(TextureAtlas.uncoveredNear0);
		gridTile = TextureAtlas.get(gridSprite);

		getChildren().addAll(nearTile, gridTile);

		isUncovered = true; // Now that the tile is uncovered, prevent any more clicks on it
	}

	private void onPress(MouseEvent event) {
		Audio.clickSound.stop();

		pressedTile.toFront();
	}

	private void onRelease(MouseEvent event) {
		// Find if the mouse pointer is still within the Rect2D
		Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

		if (isInBox) {
			uncover();	
		} else {
			normalTile.toFront(); // If not, revert to normal tile appearance.
		}
		Audio.clickSound.play();
	}

	@Override
	public void handle(MouseEvent event) {
		String type = String.valueOf(event.getEventType());

		if (!isUncovered) {
			// JavaFX polls the last pressed button, making switch statements impossible.
			if (type.equals("MOUSE_PRESSED")) {onPress(event);}
			if (type.equals("MOUSE_RELEASED")) {onRelease(event);}
		}
	}
}

// OxygenCobalt