// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.Group;

import nodes.Border;

public class BorderPane extends Pane {
	public final double width;
	public final double height;

	private Group root;

	public BorderPane(double screenWidth, double screenHeight) { // Width/Height is given as the amount of tiles, not pixels
		width = screenWidth; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
		height = screenHeight;

		root = new Group();

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing

		generateBorders();
	}

	private void generateBorders() {
		// Counts are the amount of bars that can fit in one axis [excluding decimals]
		// W/Hs are made divisble by 32 using modulo, and then divided by 32 to get the value
		double horizontalCount = (width + (width % 32)) / 32;
		double verticalCount = (height + (height % 32)) / 32;

		// Horizontal Borders
		for (int hX = 0; hX < horizontalCount; hX++) {
			for (int hY = 0; hY < 3; hY++) {
				getChildren().add(new Border("horizontal", hX, hY, width, height));				
			}
		}

		// Vertical Borders
		for (int vX = 0; vX < 2; vX++) {
			for (int vY = 0; vY < verticalCount; vY++) {
				getChildren().add(new Border("vertical", vX, vY, width, height));
			}
		}

		// Corner Borders
		for (int cX = 0; cX < 2; cX++) {
			for (int cY = 0; cY < 3; cY++) {
				getChildren().add(new Border("corner", cX, cY, width, height));
			}
		}
	}
}