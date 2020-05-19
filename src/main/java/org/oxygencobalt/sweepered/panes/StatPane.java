// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package panes;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;

public class StatPane extends StackPane {
	public final int height;
	public final int width;

	public StatPane(int argHeight, int argWidth, int offset, int mineCount) { // MineCount is given by MinePane.mines [To be added]
		height = argHeight;
		width = argWidth - (offset * 2);

		setPrefSize(width, height);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing by HBox/VBox

		// Offset is used to create "Gutters" between StatPane and MinePane, similar to the UI of the old minesweeper game
		setLayoutX(offset);
		setLayoutY(offset);

		setStyle("-fx-background-color: white;");
	}
}