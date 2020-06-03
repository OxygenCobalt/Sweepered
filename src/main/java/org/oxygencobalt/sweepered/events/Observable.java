// ObservableBase
// Base class for all observable values.

package events;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Observable {

    protected PropertyChangeSupport propertyChangeSupport = null;

    public Observable() {}

    public void setObservableObject(Observable observable) {

        propertyChangeSupport = new PropertyChangeSupport(observable);

    }

    public void addListener(PropertyChangeListener listener){

        propertyChangeSupport.addPropertyChangeListener(listener);

    }

}