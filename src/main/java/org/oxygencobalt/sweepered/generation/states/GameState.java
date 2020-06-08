// GameState
// An Observable enum for the Game State

package generation.states;

import events.Observable;

public class GameState extends Observable {

    // Possible game states
    public enum State {

        UNSTARTED, STARTED,

        CLEARED, EXPLOSION // Cleared is the win case, explosion is the loss case

    }

    private State state;

    public GameState(final State state) {

        this.state = state;

        // Initialize propertyChangeSupport before allowing a listener to be added
        setObservableObject(this);

    }

    public void setState(final State newState) {

        State oldState = state;
        state = newState;

        // Notify listeners of value change
        propertyChangeSupport.firePropertyChange("State", oldState, state);

    }

    // Used to set the value of this boolean w/o notifying the listeners
    public void setStateSilent(final State newState) {

        this.state = newState;

    }

    public State getState() {

        return state;

    }

}

// OxygenCobalt
