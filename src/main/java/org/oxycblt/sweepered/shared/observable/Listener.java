// Listener
// Base interface to listen to value changes w/observable.

package org.oxycblt.sweepered.shared.observable;

public interface Listener<T> {

    void propertyChanged(T changed);

}

// OxygenCobalt
