// TileBoolean
// Observable object that has added information about the tile, such as coordinates

package observer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TileBoolean extends SimpleBooleanProperty {
	private final int x;
	private final int y;

	public TileBoolean(boolean value, int x, int y) {
		super(value); // Give the boolean value to the inherited class

		this.x = x;
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}
}