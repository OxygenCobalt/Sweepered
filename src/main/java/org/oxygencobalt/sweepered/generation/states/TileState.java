// TileState
// Observable enum for the Tile State

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

    public TileState(final State state, final int x, final int y) {

        this.state = state;

        // Store auxillary information
        this.message = null;
        this.x = x;
        this.y = y;

        // Initialize propertyChangeSupport before allowing a listener to be added
        setObservableObject(this);

    }

    public final void setState(final State newState, final String newMessage) {

        State oldState = state;
        state = newState;

        message = newMessage;

        // Notify listeners of value change
        propertyChangeSupport.firePropertyChange("TileState", oldState, state);

    }

    // Pulse is used to notify listeners w/o changing the value itself.
    public final void pulse(final State newState, final State altState, final String newMessage) {

        if (this.state != newState) {

            setState(newState, newMessage);

        } else {

            setState(altState, newMessage);

        }

    }

    // Used to set the value of this boolean w/o notifying the listeners
    public final void setStateSilent(final State newState) {

        this.state = newState;

    }

    // Getters
    public final State getState() {

        return state;

    }

    public final String getMessage() {

        return message;

    }

    public final int getX() {

        return x;

    }

    public final int getY() {

        return y;

    }

}

// OxygenCobalt
