// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import nodes.Corner;

public class StatPane extends Pane {
	public final int height;
	public final int width;

	public StatPane(int argHeight, int argWidth, int offset, int mineCount) { // MineCount is given by MinePane.mines [To be added]
		height = argHeight;
		width = argWidth;

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing
		relocate(offset, offset);

		// Set Style for the background and the borders
		// TODO: Move style to a CSS document
		setStyle(
			"-fx-background-color: #3d3d3d;" +
			"-fx-border-width: 4px;" +
			"-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;" +
			"-fx-border-style: solid outside;"
		);

		generateCorners();
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