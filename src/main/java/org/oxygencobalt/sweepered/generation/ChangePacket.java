// ChangePacket
// Small class for communicating changed tiles from board to tilepane

package generation;

public class ChangePacket {
	private final int x;
	private final int y;
	private final TileState newState;

	public ChangePacket(int x, int y, TileState newState) {
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

	public TileState getNewState() {
		return newState;
	}
}