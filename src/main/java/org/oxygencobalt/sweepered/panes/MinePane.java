// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.Group;

import nodes.Tile;

public class MinePane extends StackPane {
	public final int width;
	public final int height;

	private Group root;
	// private final Border border;
	// private Tile[][] tiles;

	public MinePane(int mineWidth, int mineHeight, int offset) { // Width/Height is given as the amount of tiles, not pixels
		width = mineWidth * 32; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
		height = mineHeight * 32;

		root = new Group();

		// tiles = new Tile[mineWidth][mineHeight];

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing
		relocate(offset, (offset * 2) + 40); // Offset is used to create "Gutters" between StatPane and MinePane
		setStyle("-fx-background-color: #565656");
		generateMines(mineWidth, mineHeight, offset);

		getChildren().add(root);
	}

	private void generateMines(int mineWidth, int mineHeight, int offset) {
		// Simply iterate through all the X and Y coordinates on the board, 
		// and generate a tile for them all
		for (int x = 0; x<mineWidth; x++) {
			for (int y = 0; y<mineHeight; y++) {
				root.getChildren().add(new Tile(x, y, offset, false));
			}
		}
	}
}

// OxygenCobalt