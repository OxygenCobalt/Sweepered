// Observable
// Base class for all observable values.

package events.observable;

public abstract class Observable<T> {

    protected Listener<T> listener = null;

    public Observable() {

        // Not called.

    }

    public void addListener(final Listener<T> newListener) {

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
