// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;

import java.util.Random;

import nodes.Tile;
import nodes.Corner;

import observer.TileBoolean;

public class MinePane extends Pane implements ChangeListener<Boolean> {
	private final int width;
	private final int height;

	private final int mineWidth;
	private final int mineHeight;

	private final int x;
	private final int y;

	private final int mineCount;

	private boolean[][] mines;

	public MinePane(int mineWidth, int mineHeight, int mineCount, int offset) { // Width/Height is given as the amount of tiles, not pixels
		x = offset;
		y = (offset * 2) + 44; // The added 44 is to account for the width of stackpane and the border of minepane.

		width = mineWidth * 32; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
		height = mineHeight * 32;

		this.mineWidth = mineWidth;
		this.mineHeight = mineHeight;

		this.mineCount = mineCount;

		mines = new boolean[this.mineWidth][this.mineHeight];

		relocate(x, y);
		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing

		// Set Style for the background and the borders
		setStyle(
			"-fx-background-color: #3d3d3d;" +
			"-fx-border-width: 4px;" +
			"-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;" +
			"-fx-border-style: solid outside;"
		);

		generateTiles();
		generateCorners();
	}

	/*
	private void generateMines() {
		int generatedMines = 0;

		// Ratio is used to have an even-ish distribution of mines
		// Simply the factor of 1 and the width of the board,
		// multiplied by 100 to match up w/random
		double ratio = (1.0 / mineWidth) * 100;
		Random randGenerator = new Random();

		while (generatedMines != mineCount) {

			gridLoop: // gridLoop is established to allow a simple break once every mine is generated
			for (int mX = 0; mX < mineWidth; mX++) {

				for (int mY = 0; mY < mineHeight; mY++) {

					if (!mines[mX][mY]) { // Check if slot in mines hasnt already been occupied by a mine

						mines[mX][mY] = randGenerator.nextInt(100) < ratio;

						if (mines[mX][mY]) {
							generatedMines++; // Add to generatedMines

							if (generatedMines == mineCount) {break gridLoop;}

						}

					}

				}

			}

		}

		generateTiles();
	}
	*/

	private void generateTiles() {
		// Simply iterate through all the X and Y coordinates on the board, 
		// and generate a tile for them all
		for (int tX = 0; tX < mineWidth; tX++) {

			for (int tY = 0; tY < mineHeight; tY++) {
				Tile tile = new Tile(
						// Pass mine positions
						tX, 
						tY,
						// Pass pane positions for MouseRect creation
						x,
						y
				);

				tile.uncoveredStart.addListener(this);

				getChildren().add(tile);
			}

		}

	}

	private void generateCorners() {
		// Like generateTiles, iterate through every corner of the pane
		// and generate a corner for them all
		for (int cX = 0; cX < 2; cX++) {

			for (int cY = 0; cY < 2; cY++) {
				getChildren().add(new Corner(cX, cY, width, height));
			}
		}
	}

    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
   		// Cast observable to TileBoolean to get access to its x/y values
    	TileBoolean tb = (TileBoolean) observable;

   		// TODO: Add game start function
    }
}

// OxygenCobalt