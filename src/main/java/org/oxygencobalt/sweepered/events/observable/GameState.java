// GameState
// An ObservableValue for the game state [With an internal enum]

package events.observable;

public class GameState extends Observable {

    // Possible game states
    public enum State {

        UNSTARTED, STARTED,

        CLEARED, EXPLOSION // Cleared is the win case, explosion is the loss case

    }

    private State state;

    public GameState(State value) {

        state = value;

        // Initialize propertyChangeSupport before allowing a listener to be added
        setObservableObject(this);

    }

    public State getValue() {

        return state;

    }

    public void setValue(State value) {

        State oldState = state;
        this.state = value;

        // Notify listeners of value change
        propertyChangeSupport.firePropertyChange("State", oldState, state);

    }

}

// OxygenCobalt