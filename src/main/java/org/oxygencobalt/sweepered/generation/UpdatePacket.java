// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package generation;

import events.states.TileState;

public class UpdatePacket {

    public enum Change {

        COVER,

        MINE, FLAG,

        UNCOVER, EXPLODE,

        CLEAR, DISABLE

    }

    private final Change change;

    private final int originX;
    private final int originY;

    private final int targetX;
    private final int targetY;

    private final TileState.State newState;

    private final Object auxillary;

    public UpdatePacket(final Change change,
                        final int originX,
                        final int originY,

                        final TileState.State newState,
                        final int targetX,
                        final int targetY,

                        final Object... auxillary) {

        this.change = change;

        this.originX = originX;
        this.originY = originY;

        this.targetX = targetX;
        this.targetY = targetY;

        this.newState = newState;

        // Auxillary is used for any extra information that is special for the specific change
        // Ex. The amount of nearby mines to a tile.

        // Run a switch statement w/the length of auxillary.
        // If the length is 0, then the argument was not passed, and auxillary should just be null.
        // If the length is 1, then an auxillary arg was passed, so set the value to that
        // Otherwise, there are too many auxillary arguments, and an exception must be thrown.

        // FIXME: Avoid undefined objects, use <T> :P

        switch (auxillary.length) {

            case 0: this.auxillary = null; break;

            case 1: this.auxillary = auxillary[0]; break;

            default: throw new IllegalArgumentException("Too many auxillary arguments.");

        }

    }

    // Getters

    public final Change getChange() {

        return change;

    }

    public final int getOriginX() {

        return originX;

    }

    public final int getOriginY() {

        return originY;

    }

    public final TileState.State getNewState() {

        return newState;

    }

    public final int getTargetX() {

        return targetX;

    }

    public final int getTargetY() {

        return targetY;

    }

    public final Object getAuxillary() {

        return auxillary;

    }

}

// OxygenCobalt
