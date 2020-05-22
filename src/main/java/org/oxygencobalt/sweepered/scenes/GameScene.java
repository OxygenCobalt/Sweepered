// GameScene
// Main Minesweeper game scene, only mode as of now is 9x9, but more will be added.

package scenes;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import panes.MinePane;
import panes.StatPane;
import panes.BorderPane;

import media.TextureAtlas;
import media.Sprite;

public class GameScene extends Scene {
	private Group root;

	private StatPane bar;
	private MinePane board;
	private BorderPane border;

	private final Color backgroundColor = Color.rgb(0, 0, 0);

	public GameScene(Group group, int mineWidth, int mineHeight) {
		// Call super to construct Scene(), and then add passed group to class.
		super(group, (mineWidth * 32) + 20, (mineHeight * 32) + 70);
		setFill(backgroundColor);

		// Load the main panes
		bar = new StatPane(40, (mineWidth * 32), 10, 0);
		board = new MinePane(mineWidth, mineHeight, 10);
		border = new BorderPane(getWidth(), getHeight());

		root = group;
		root.getChildren().addAll(border, bar, board);
	}
}

// OxygenCobalt