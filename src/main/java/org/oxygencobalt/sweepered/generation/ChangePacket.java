// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package generation;

import states.TileState;

public class ChangePacket {
	private final String type;

	private final int originX;
	private final int originY;

	private final int targetX;
	private final int targetY;

	private final TileState.State newState;

	public ChangePacket(String type, int originX, int originY, TileState.State newState, int targetX, int targetY) {

		this.type = type;

		this.originX = originX;
		this.originY = originY;

		this.targetX = targetX;
		this.targetY = targetY;

		this.newState = newState;

	}
	public String getType() {
		return type;
	}

	public int getOriginX() {
		return originX;
	}

	public TileState.State getNewState() {
		return newState;
	}

	public int getOriginY() {
		return originY;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetY() {
		return targetY;
	}

}

// OxygenCobalt