// GameScene
// Main Minesweeper game scene, only mode as of now is 9x9, but more will be added.
// TODO: Make GameScene w/h dependent on MINE COUNT, not pixels

package scenes;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import panes.MinePane;
import panes.StatPane;

public class GameScene extends Scene {
	private Group root;

	private StatPane bar;
	private MinePane board;
    private final Color backgroundColor = Color.rgb(192, 192, 192);

	public GameScene(Group group, int width, int height) {
		// Call super to construct Scene(), and then add passed group to class.
		super(group, width, height);
		setFill(backgroundColor);

		// Load the main panes
		bar = new StatPane(40, width, 10, 0);
		board = new MinePane(9, 9, 10);

		root = group;
		root.getChildren().addAll(bar, board);
	}
}

// OxygenCobalt