// EventBoolean
// Observer boolean that can also store other information, such as coordinates.

package events.observable;

import javafx.beans.property.SimpleBooleanProperty;

public class EventBoolean extends Observable {
	private Boolean value;

	private final String name;

	private final int x;
	private final int y;

	public EventBoolean(Boolean value, String name, int x, int y) {
		this.value = value;

		// Store auxillary information
		this.name = name;
		this.x = x;
		this.y = y;

		// Initialize propertyChangeSupport before allowing a listener to be added
		setObservableObject(this);
	}

	public final void setValue(Boolean value) {
        Boolean oldValue = this.value;
        this.value = value;

        // Notify listeners of value change
        propertyChangeSupport.firePropertyChange("Value", oldValue, this.value);
	}

	// Used to set the value of this boolean w/o notifying the listeners
	public final void setValueSilent(Boolean value) {this.value = value;}

	// Getters
	public final Boolean getValue() {return value;}

	public final String getName() {return name;}

	public final int getX() {return x;}

	public final int getY() {return y;}
}

// OxygenCobalt