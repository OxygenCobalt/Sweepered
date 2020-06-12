// GameState
// An Observable enum for the Game State

package events.states;

import events.observable.Observable;

public class GameState extends Observable<GameState> {

    // Possible game states
    public enum State {

        UNSTARTED, STARTED,

        // This is used to communicate to StatPane when
        // the mouse is hovering over a tile.
        UNCERTAIN,

        CLEARED, EXPLOSION // Cleared is the win case, explosion is the loss case

    }

    private State state;

    private final String owner;

    public GameState(final State state, final String owner) {

        this.state = state;

        this.owner = owner;

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

    public final String getOwner() {

        return owner;

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


    public Boolean isStarted() {

        return isState(State.STARTED, State.UNCERTAIN);

    }

}

// OxygenCobalt
