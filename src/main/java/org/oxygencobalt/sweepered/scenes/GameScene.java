// GameScene
// Main Minesweeper game scene, only mode as of now is 9x9, but more will be added.

// TODO: Readd corner tiles, but as self-contained MinePane and statpane objects [?]

package scenes;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import panes.MinePane;
import panes.StatPane;

public class GameScene extends Scene {
	private Group root;

	private StatPane bar;
	private MinePane board;

	private final int offset;

	public GameScene(Group group, int mineWidth, int mineHeight, int argOffset) {
		// Call super to construct Scene(), and then add passed group to class.
		super(group, (mineWidth * 32) + (18 + argOffset), (mineHeight * 32) + (76 + argOffset));
		setFill(Color.web("3d3d3d")); // Background color matches w/the tile color, mostly

		offset = argOffset + 4; // 4 is added to make up for borders

		// Load the main panes
		bar = new StatPane(40, (mineWidth * 32), offset, 0);
		board = new MinePane(mineWidth, mineHeight, offset);

		root = group;
		root.getChildren().addAll(bar, board);
	}
}

// OxygenCobalt