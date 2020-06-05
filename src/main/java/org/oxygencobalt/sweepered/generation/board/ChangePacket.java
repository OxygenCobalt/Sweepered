// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package generation.board;

import generation.states.TileState;

public class ChangePacket {

	public enum Change {

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

	public ChangePacket(Change change, 
						int originX, 
						int originY, 

						TileState.State newState, 
						int targetX, 
						int targetY,

						Object... auxillary) {

		this.change = change;

		this.originX = originX;
		this.originY = originY;

		this.targetX = targetX;
		this.targetY = targetY;

		this.newState = newState;

		// Auxillary is used for any extra information that is special for the specific change
		// Ex. The amount of nearby mines to a tile.

		// Check if the length is more than one, and throw an error if so as
		// auxillary [As an Object type] should only be one item.

		if (auxillary.length == 1) {

			this.auxillary = auxillary[0];

		} else {

			this.auxillary = null;

		}

	}
	public Change getChange() {
		return change;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	public TileState.State getNewState() {
		return newState;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public Object getAuxillary() {
		return auxillary;
	}

}

// OxygenCobalt