// EventInteger
// An observable integer value.

package shared.values;

import shared.observable.Observable;

public class EventInteger extends Observable<EventInteger> {

    private Integer value;

    public EventInteger(final Integer value) {

        this.value = value;
    }

    public void setValue(final Integer newValue) {

        // Only set the state and notify listeners if the value is actually new.

        if (value != newValue) {

            value = newValue;

            // Notify listeners of value change
            fireChange(this);

        }

    }

    // Used to set the value w/o notifying the listeners
    public void setValueSilent(final Integer newValue) {

        this.value = newValue;

    }

    // Pulse is used to notify the listeners w/o changing the value
    public final void pulse() {

        fireChange(this);

    }

    public Integer getValue() {

        return value;

    }

}

// OxygenCobalt
