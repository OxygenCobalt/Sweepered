// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import nodes.Tile;
import nodes.Corner;

public class MinePane extends Pane {
	public final int width;
	public final int height;

	public final int x;
	public final int y;

	// private Tile[][] tiles;

	public MinePane(int mineWidth, int mineHeight, int offset) { // Width/Height is given as the amount of tiles, not pixels
		width = mineWidth * 32; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
		height = mineHeight * 32;

		x = offset;
		y = (offset * 2) + 44; // The added 44 is to account for the width of stackpane and the border of minepane.

		// tiles = new Tile[mineWidth][mineHeight];

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing
		relocate(x, y);

		// Set Style for the background and the borders
		setStyle(
			"-fx-background-color: #3d3d3d;" +
			"-fx-border-width: 4px;" +
			"-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;" +
			"-fx-border-style: solid outside;"
		);

		generateMines(mineWidth, mineHeight, offset);
		generateCorners();
	}

	private void generateMines(int mineWidth, int mineHeight, int offset) {
		// Simply iterate through all the X and Y coordinates on the board, 
		// and generate a tile for them all
		for (int mX = 0; mX < mineWidth; mX++) {
			for (int mY = 0; mY < mineHeight; mY++) {
				getChildren().add(
					new Tile(
						// Pass mine positions
						mX, 
						mY,
						// Pass pane positions for MouseRect creation
						x,
						y,
						false
					)
				);
			}
		}
	}

	private void generateCorners() {
		// Like minepane, iterate through every corner of the pane
		// and generate a corner for them all
		for (int cX = 0; cX < 2; cX++) {
			for (int cY = 0; cY < 2; cY++) {
				getChildren().add(new Corner(cX, cY, width, height));
			}
		}
	}
}

// OxygenCobalt