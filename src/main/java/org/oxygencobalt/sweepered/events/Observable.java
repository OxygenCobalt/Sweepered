// ObservableBase
// Base class for all observable values.

package events;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// FIXME: This class is from stackoverflow, just write your own observable at this point lmao

public abstract class Observable {

    protected PropertyChangeSupport propertyChangeSupport = null;

    public Observable() {}

    public void setObservableObject(Observable observable) {

        // Load the observable object [Whatever inherits it]
        // into the support that will actually notify the listeners
        propertyChangeSupport = new PropertyChangeSupport(observable);

    }

    public void addListener(PropertyChangeListener listener){

        propertyChangeSupport.addPropertyChangeListener(listener);

    }

}

// OxygenCobalt