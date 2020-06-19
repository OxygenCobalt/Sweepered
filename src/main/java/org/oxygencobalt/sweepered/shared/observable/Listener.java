// Listener
// Base interface to listen to value changes w/observable.

package shared.observable;

public interface Listener<T> {

    void propertyChanged(T changed);

}

// OxygenCobalt
