// TileState
// Observable enum used for managing the state of tiles

package generation.states;

import events.Observable;

public class TileState extends Observable {

    public enum State {

        COVERED, MINED, 

        FLAGGED, FLAGGED_MINED,

        FLAG_QUERY, FLAG_QUERY_, // Flag values used to pulse listeners

        EXPLODED, UNCOVERED_CLEARED, // Game end scenarios

        UNCOVERED,

        DISABLED, DISABLED_MINED, DISABLED_FLAGGED

    }

    private State state;

    private String message;

    private final int x;
    private final int y;

    public TileState(State state, int x, int y) {

        this.state = state;

        // Store auxillary information
        this.message = null;
        this.x = x;
        this.y = y;

        // Initialize propertyChangeSupport before allowing a listener to be added
        setObservableObject(this);

    }

    public final void setState(State state, String message) {

        State oldState = this.state;
        this.state = state;

        this.message = message;

        // Notify listeners of value change
        propertyChangeSupport.firePropertyChange("TileState", oldState, this.state);

    }

    // Pulse is used to notify listeners w/o changing the value itself.
    public final void pulse(State state, State altState, String message) {

        if (this.state != state) {

            setState(state, message);

        } else {

            setState(altState, message);
            
        }

    }

    // Used to set the value of this boolean w/o notifying the listeners
    public final void setStateSilent(State state) {this.state = state;}

    // Getters
    public final State getState() {return state;}

    public final String getMessage() {return message;}

    public final int getX() {return x;}

    public final int getY() {return y;}

}

// OxygenCobalt