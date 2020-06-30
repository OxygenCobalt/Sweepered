// ConfigState
// An Observable enum for the state of the Config Menu

package config.values;

import shared.observable.Observable;

public class ConfigState extends Observable<ConfigState> {

    // Possible config states
    public enum State {

        MENU, MENU_CUSTOM,

        ABOUT, ABOUT_CUSTOM

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

    // State checking
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

    public final Boolean isMenu() {

        return isState(State.MENU, State.MENU_CUSTOM);

    }

    public final Boolean isAbout() {

        return isState(State.ABOUT, State.ABOUT_CUSTOM);

    }

}

// OxygenCobalt
