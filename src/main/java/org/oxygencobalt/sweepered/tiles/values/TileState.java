// TileState
// Observable enum for the Tile State

package tiles.values;

import shared.observable.Observable;

public class TileState extends Observable<TileState> {

    public enum State {

        COVERED, MINED,

        FLAGGED, FLAGGED_MINED,

        EXPLODED, UNCOVERED_CLEARED, // Game end triggers

        UNCOVERED,

        DISABLED,

        DISABLED_MINED, DISABLED_BAD_FLAG

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

    }

    public final void setState(final State newState, final String newMessage) {

        if (state != newState) {

            state = newState;

            message = newMessage;

            // Notify listeners of value change
            fireChange(this);

        }

    }

    // Pulse is used to notify listeners w/o changing the value itself.
    public final void pulse(final String newMessage) {

        message = newMessage;

        fireChange(this);

    }

    // Used to set the value w/o notifying the listeners
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

    public final Boolean isDisabled() {

        return isState(

            State.UNCOVERED,
            State.DISABLED,

            State.DISABLED_MINED,
            State.DISABLED_BAD_FLAG

        );

    }

    public final Boolean isMined() {

        return isState(

            State.MINED,
            State.DISABLED_MINED,

            State.EXPLODED

        );

    }

}

// OxygenCobalt
