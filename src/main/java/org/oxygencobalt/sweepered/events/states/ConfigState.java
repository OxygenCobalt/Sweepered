// ConfigState
// An Observable enum for the menu currently shown in the config window

package events.states;

import events.observable.Observable;

public class ConfigState extends Observable<ConfigState> {

    // Possible game states
    public enum State {

        MENU, MODE, ABOUT

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

    public final Boolean isState(final State... states) {

        // Iterate through all given states and return
        // true if one matches the current state,
        // otherwise return false.

        for (State compareState : states) {

            if (state == compareState) {

                return true;

            }

        }

        return false;

    }

}

// OxygenCobalt
