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

import components.TextureAtlas;

public class Tile extends StackPane implements EventHandler<MouseEvent> {
	private final int width = 32;
	private final int height = 32;

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

	public Tile(int x, int y, int offset, Boolean isMine) {
		setPrefSize(32, 32);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		relocate(x * 32, y * 32);

		// mouseRect is used to detect when the mouse has been released *outside* of the tile
		// Offset is applied in order to create a rect relative to the scene, as I cant poll mouse positions relative to MinePane
		mouseRect = new Rectangle2D(offset + (x * 32), (offset * 2) + (y * 32) + 40, 32, 32);

		// These tiles are common, so theyre loaded in the class constructor
		normalTile = TextureAtlas.get("tiles_ss/tiles/tile_normal");
		flaggedTile = TextureAtlas.get("tiles_ss/tiles/tile_flagged");
		pressedTile = TextureAtlas.get("tiles_ss/tiles/tile_pressed");

		getChildren().addAll(pressedTile, flaggedTile, normalTile);

		setOnMousePressed(this);
		setOnMouseReleased(this);
	}

	// TODO: Add sounds to these presses/releases
	private void uncover() {
		nearTile = TextureAtlas.get("tiles_ss/tiles_near/0");
		nearTile.toFront();

		getChildren().addAll(nearTile);

		isUncovered = true; // Now that the tile is uncovered, prevent any more clicks on it
	}

	private void onPress(MouseEvent event) {
		pressedTile.toFront();
	}

	private void onRelease(MouseEvent event) {
		// Find if the mouse pointer is still within the rect2d
		Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

		if (isInBox) {
			uncover();
		} else {
			normalTile.toFront(); // If not, revert to normal tile appearance.
		}
	}

	@Override
	public void handle(MouseEvent event) {
		String type = String.valueOf(event.getEventType());

		if (!isUncovered) {
			// JavaFX polls the last pressed button, making switch/else ifs impossible.
			if (type.equals("MOUSE_PRESSED")) {onPress(event);}
			if (type.equals("MOUSE_RELEASED")) {onRelease(event);}
		}
	}
}

// OxygenCobalt