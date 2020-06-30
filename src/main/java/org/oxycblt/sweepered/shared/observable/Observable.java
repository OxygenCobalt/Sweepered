// Observable
// Base class for all observable values.

package org.oxycblt.sweepered.shared.observable;

public abstract class Observable<T> {

    protected Listener<T> listener = null;

    public Observable() {

        // Not called.

    }

    public void addListener(final Listener<T> newListener) {

        // No need to check if the listener is of a valid type for this observable,
        // as the identifier handles that for us.

        listener = newListener;

    }

    public void fireChange(final T source) {

        // Check if a listener has been added

        if (listener != null) {

            listener.propertyChanged(source);

        }

    }

}

// OxygenCobalt
