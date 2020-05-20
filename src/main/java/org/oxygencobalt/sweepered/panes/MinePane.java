// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;

import nodes.Tile;

public class MinePane extends StackPane {
	public final int width;
	public final int height;

	public MinePane(int mineWidth, int mineHeight, int offset) { // Width/Height is given as the amount of tiles, not pixels
		width = mineWidth * 32; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
		height = mineHeight * 32;

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing by HBox/VBox

		// Offset is used to create "Gutters" between StatPane and MinePane, similar to the UI of the old minesweeper game
		setLayoutX(offset);
		setLayoutY((offset * 2) + 40); // Will be improved if Im able to get MinePane to fetch StatPane's height value

		setStyle("-fx-background-color: black;");
	}

	private void generateMines(int mineWidth, int mineHeight) {
		return; // TODO: Actually generate the mines.
	}
}

// OxygenCobalt