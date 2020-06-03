// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package generation;

import states.TileState;

public class ChangePacket {
	private final int x;
	private final int y;
	private final TileState.State newState;

	public ChangePacket(int x, int y, TileState.State newState) {
		this.x = x;
		this.y = y;

		this.newState = newState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TileState.State getNewState() {
		return newState;
	}
}