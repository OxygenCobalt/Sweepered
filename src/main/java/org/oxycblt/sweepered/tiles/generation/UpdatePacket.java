// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package org.oxycblt.sweepered.tiles.generation;

import org.oxycblt.sweepered.tiles.values.TileState;

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
        // Otherwise, there are too many auxillary arguments, so just pick the first one
        // and print a warning into the console.

        switch (auxillary.length) {

            case 0: this.auxillary = null; break;

            case 1: this.auxillary = auxillary[0]; break;

            default: System.out.println(

                        "Too many auxillary arguments, defaulting to the first entry."

                    ); this.auxillary = auxillary[0]; break;

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
