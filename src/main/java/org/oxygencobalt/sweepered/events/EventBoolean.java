// EventBoolean
// Observer boolean that can also store other information, such as coordinates.

package events;

import javafx.beans.property.SimpleBooleanProperty;

public class EventBoolean extends SimpleBooleanProperty {
	private final String type;

	private final int x;
	private final int y;

	public EventBoolean(Boolean value, String type, int x, int y) {
		super(value); // Give the boolean value to the inherited class

		this.type = type;
		this.x = x;
		this.y = y;
	}

	public final String getType() {
		return type;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}
}

// OxygenCobalt