// Observable
// Base class for all observable values.

package events.observable;

public abstract class Observable {

    protected Listener listener = null;

    public Observable() {

        // Not called.

    }

    public void addListener(final Listener newListener) {

        listener = newListener;

    }

    public void fireChange(final Object source) {

        // Check if a listener has been added

        if (listener != null) {

            listener.propertyChanged(source);

        } else { // If not, throw an exception.

            throw new RuntimeException("Listener must be added before fireChange can be run.");

        }

    }

}

// OxygenCobalt
