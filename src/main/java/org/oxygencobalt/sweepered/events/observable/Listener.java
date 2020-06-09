// Listener
// Base interface to listen to value changes w/observable.

package events.observable;

public interface Listener<T> {

    void propertyChanged(T changed);

}

// OxygenCobalt
