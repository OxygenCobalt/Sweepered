// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package generation;

import states.TileState;

public class ChangePacket {
	private final int x;
	private final int y;

	private int originX;
	private int originY;

	private String originType;

	private final TileState.State newState;

	public ChangePacket(int x, int y, TileState.State newState, String optionalType, int... optionalOrigin) {

		this.x = x;
		this.y = y;

		this.newState = newState;

		if (optionalOrigin.length == 2) {

			originX = optionalOrigin[0];
			originY = optionalOrigin[1];

			originType = optionalType;

		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	public String getOriginType() {
		return originType;
	}

	public TileState.State getNewState() {
		return newState;
	}

}