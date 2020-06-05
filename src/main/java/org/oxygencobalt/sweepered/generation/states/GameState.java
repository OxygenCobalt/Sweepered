// GameState
// An Observable enum for the game state

package generation.states;

import events.Observable;

public class GameState extends Observable {

    // Possible game states
    public enum State {

        UNSTARTED, STARTED,

        CLEARED, EXPLOSION // Cleared is the win case, explosion is the loss case

    }

    private State state;

    public GameState(State state) {

        this.state = state;

        // Initialize propertyChangeSupport before allowing a listener to be added
        setObservableObject(this);

    }

    public void setState(State state) {

        State oldState = this.state;
        this.state = state;

        // Notify listeners of value change
        propertyChangeSupport.firePropertyChange("State", oldState, state);

    }

    // Used to set the value of this boolean w/o notifying the listeners
    public void setStateSilent(State state) {this.state = state;}

    public State getState() {return state;}

}

// OxygenCobalt