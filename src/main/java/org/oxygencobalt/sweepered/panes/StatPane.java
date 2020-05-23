// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class StatPane extends Pane {
	public final int height;
	public final int width;

	public StatPane(int argHeight, int argWidth, int offset, int mineCount) { // MineCount is given by MinePane.mines [To be added]
		height = argHeight;
		width = argWidth;

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing
		relocate(offset, offset); // Offset is used to create "Gutters" between StatPane and MinePane

		setStyle(
			"-fx-background-color: #3d3d3d;"
			+ "-fx-border-width: 4px;"
			+ "-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;" 
			+ "-fx-border-style: solid outside line-join miter;"
			+ "-fx-border-radius: 5;"
		);
	}
}

// OxygenCobalt