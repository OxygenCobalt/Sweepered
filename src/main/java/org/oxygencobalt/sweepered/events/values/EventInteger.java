// EventInteger
// An observable integer value.

package events.values;

import events.observable.Observable;

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

    public void increment(final Integer count) {

        value = value + count;

        System.out.println(value);

    }

    public void deincrement(final Integer count) {

        value = value - count;

        fireChange(this);

    }

    public Integer getValue() {

        return value;

    }

}

// OxygenCobalt
