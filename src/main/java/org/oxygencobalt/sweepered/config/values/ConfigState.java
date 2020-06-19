// ConfigState
// An Observable enum for the state of the Config Menu

package config.values;

import shared.observable.Observable;

public class ConfigState extends Observable<ConfigState> {

    // Possible config states
    public enum State {

        MENU, ABOUT

    }

    private State state;

    public ConfigState(final State state) {

        this.state = state;

    }

    public void setState(final State newState) {

        // Only set the state and notify listeners if the value is actually new.

        if (state != newState) {

            state = newState;

            // Notify listeners of value change
            fireChange(this);

        }

    }

    // Used to set the value w/o notifying the listeners
    public void setStateSilent(final State newState) {

        this.state = newState;

    }

    public State getState() {

        return state;

    }

}

// OxygenCobalt
