// Main MineSweeper game scene

package scenes;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class MineScene extends Scene {
	private Group root;

	public MineScene(Group group, int width, int height) {
		// Call super to construct Scene(), and then add passed group to class.
		super(group, width, height);
		root = group;

		root.getChildren().add(new Rectangle(100, 100, Color.WHITE));
	}
}

// OxygenCobalt