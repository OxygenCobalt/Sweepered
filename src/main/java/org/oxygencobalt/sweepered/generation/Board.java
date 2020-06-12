// Board
// Object that handles interactions between tiles

package generation;

import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

import events.states.TileState;

public class Board {

    private TileState.State[][] board;

    private final int width;
    private final int height;
    private final int mineCount;

    private Integer flagCount;

    private int remainingTiles;

    private final List<TileState.State> minedTiles;
    private final List<TileState.State> coveredTiles;

    public Board(final int width, final int height, final int mineCount) {

        board = new TileState.State[width][height];

        this.width = width;
        this.height = height;
        this.mineCount = mineCount;

        flagCount = mineCount;

        this.remainingTiles = (width * height) - mineCount;

        // Having a size that exceeds (width * height) - 9 is
        // impossible, as the program will enter an infinite loop
        // trying to fill in a tile that doesnt exist. There also
        // cannot be a size lower than 9x9, as that will break
        // StatBar [May be subject to change.]

        if (remainingTiles < 9 || (width < 9 || height < 9)) {

            // FIXME: Very unfriendly, may just add a restriction to the custom game option.

            throw new IllegalArgumentException(

                "Size W"
                +
                String.valueOf(width)
                +
                " H"
                +
                String.valueOf(height)
                +
                " with MineCount of "
                +
                String.valueOf(mineCount)
                +
                " is not valid."

            );

        }

        // Create and fill the lists of tiles
        // used in some if functions to shorten them,
        // so x != y & x != y would just become !a.contains(x)
        minedTiles = Arrays.asList(
            TileState.State.MINED,
            TileState.State.FLAGGED_MINED,
            TileState.State.DISABLED_MINED
        );

        coveredTiles = Arrays.asList(
            TileState.State.FLAGGED,
            TileState.State.COVERED
        );

        // Set every tile in the board to uncovered
        // Since this array is 2-dimensional, I have to iterate
        // through each array and then run Arrays.fill on each one.
        for (TileState.State[] column : board) {

            Arrays.fill(column, TileState.State.COVERED);

        }

    }

    // Board Functions

    // Mine generation is HEAVILY INSPIRED by the way Simon Tartham wrote it in mines.c
    // Check out his puzzle collection here: https://www.chiark.greenend.org.uk/~sgtatham/puzzles/
    public final ArrayList<UpdatePacket> generateMines(final int originX, final int originY) {

        ArrayList<UpdatePacket> changedTiles = new ArrayList<UpdatePacket>();

        int generatedMines = 0;
        int locationWidth;
        int index;

        Random rand = new Random();

        ArrayList<int[]> mineLocations = new ArrayList<int[]>();

        // Fill in a list of valid mine locations
        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                // Blacklist values within a 1-tile radius of the origin
                if (Math.abs(x - originX) > 1 || Math.abs(y - originY) > 1) {

                    // Create an array with the X and Y coordinate of
                    // the tile, and add it to the list
                    int[] location = new int[]{x, y};
                    mineLocations.add(location);

                }

            }

        }

        locationWidth = mineLocations.toArray().length;

        // Then, pick out a random entry and label it as a mine.
        do {

            index = rand.nextInt(locationWidth);

            // Get value from list, and split it up into its X/Y values
            int[] coords = mineLocations.get(index);
            int coordX = coords[0];
            int coordY = coords[1];

            // Update tile state on the board to MINED
            board[coordX][coordY] = TileState.State.MINED;

            // Also add it to changedTiles in order for board to change the actual tile node.
            changedTiles.add(new UpdatePacket(

                UpdatePacket.Change.MINE,
                originX,
                originY,

                TileState.State.MINED,
                coordX,
                coordY

            ));

            // Remove the value from the list, to prevent it from becoming a mine again.
            mineLocations.remove(index);

            // Update list length now that an item has been removed
            locationWidth = mineLocations.toArray().length;

            generatedMines++;

        } while (generatedMines != mineCount);

        return changedTiles;

    }

    public final ArrayList<UpdatePacket> uncoverTile(final int originX, final int originY) {

        ArrayList<UpdatePacket> changedTiles = new ArrayList<UpdatePacket>();
        ArrayList<int[]> recursiveList = new ArrayList<int[]>();

        TileState.State nearTile;

        TileState.State originTile;
        UpdatePacket.Change originChange;

        int foundMines = 0;

        Boolean isNotOutOfBounds;

        originTile = board[originX][originY];

        // First, check if the tile is mined
        if (originTile == TileState.State.MINED) {

            // If thats the case, set the origin tile/type to
            // EXPLODED and drop the other instructions

            originChange = UpdatePacket.Change.EXPLODE;

            originTile = TileState.State.EXPLODED;

            board[originX][originY] = originTile;

        } else { // Otherwise, continue.

            for (int nearX = (originX - 1); nearX <  (originX + 2); nearX++) {

                for (int nearY = (originY - 1); nearY < (originY + 2); nearY++) {

                    // Also check if surrounding tile is not outside the bounds of the board
                    // before running any functions on it
                    // TODO: THERE HAS TO BE A BETTER WAY TO WRITE THIS
                    isNotOutOfBounds = (
                        (nearX != width && nearX >= 0) && (nearY != height && nearY >= 0)
                    );

                    if (isNotOutOfBounds) {
                        nearTile = board[nearX][nearY];

                        // Add to mineCount if tile does contain a mine
                        // [Or its flagged or disabled equivelents]
                        if (minedTiles.contains(nearTile)) {
                            foundMines++;
                        }

                        // Blacklist any tile that is not covered from the recursivelist,
                        // to prevent flags from being destroyed and an infinite loop
                        // w/uncovered tiles

                        // FIXME: The mines are being removed recursively
                        // multiple times, but im not sure if thats possible to be fixed.

                        if (nearTile == TileState.State.COVERED) {
                            recursiveList.add(new int[]{nearX, nearY});
                        }

                    }

                }

            }

            originChange = UpdatePacket.Change.UNCOVER;
            originTile = TileState.State.UNCOVERED;

            // To prevent a StackOverflowError, the board tile
            // has to be updated seperately in both cases.
            board[originX][originY] = originTile;

            // If there are no nearby mines, iterate
            // recursively and add the list of changed tiles
            if (foundMines == 0) {

                for (int[] coords : recursiveList) {

                    changedTiles.addAll(uncoverTile(coords[0], coords[1]));

                }

            }
        }

        // Finally, create a UpdatePacket for the original updated tile.

        changedTiles.add(

            new UpdatePacket(

                originChange,
                originX,
                originY,

                originTile,
                originX,
                originY,

                // foundMines is passed as an auxillary argument,
                // even if it wont be used if a tile explodes.
                foundMines

            )

        );

        return changedTiles;

    }

    public final ArrayList<UpdatePacket> flagTile(final int originX, final int originY) {

        ArrayList<UpdatePacket> changedTiles = new ArrayList<UpdatePacket>();

        // Get reference to tile
        TileState.State tile = board[originX][originY];

        String stringTile = String.valueOf(tile);

        // If the tile is not flagged, change its state to
        // the corresponding FLAGGED state depending on its MINED status

        // If not, change the tiles state in reverse, converting its
        // FLAGGED status to its corresponding MINED status

        if (!stringTile.contains("FLAGGED")) {

            // Once the amount of flags exceeds mineCount,
            // then flagging should be disabled, albeit
            // unflagging should still be possible.
            if (flagCount > 0) {

                if (tile == TileState.State.MINED) {

                    tile = TileState.State.FLAGGED_MINED;

                } else {

                    tile = TileState.State.FLAGGED;

                }

                flagCount--;

            }

        } else {

            if (tile == TileState.State.FLAGGED_MINED) {

                tile = TileState.State.MINED;


            } else {

                tile = TileState.State.COVERED;

            }

            flagCount++;

        }

        // Due to the possibility that the flagging operation could not happen,
        // dont create a new changepacket if no change has been made.

        if (tile != board[originX][originY]) {

            // Update tile on board and create corresponding UpdatePacket
            board[originX][originY] = tile;

            changedTiles.add(new UpdatePacket(

                UpdatePacket.Change.FLAG,
                originX,
                originY,

                tile,
                originX,
                originY

            ));

        }

        return changedTiles;

    }

    public final ArrayList<UpdatePacket> disableAllTiles(final String reason,
                                                         final int originX,
                                                         final int originY) {

        ArrayList<UpdatePacket> changedTiles = new ArrayList<UpdatePacket>();

        TileState.State tile;

        // Iterate through every tile, and set it to DISABLED,
        // as notifyAllTiles is only activated at a Game End scenario.

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                tile = board[x][y];

                switch (tile) {

                    case MINED: tile = TileState.State.DISABLED_MINED; break;

                    case EXPLODED: tile = TileState.State.DISABLED_EXPLODED; break;

                    // This flagged state only allows WaveTimeline to differentiate from
                    // tiles that were flagged correctly and ones that werent.
                    case FLAGGED: tile = TileState.State.DISABLED_BAD_FLAG; break;

                    // Tiles that were flagged correctly have their own state.
                    case FLAGGED_MINED: tile = TileState.State.DISABLED_FLAGGED; break;

                    default: tile = TileState.State.DISABLED;

                }

                board[x][y] = tile;

                // Create a UpdatePacket w/the reason for the disabling as
                // an auxillary value.
                changedTiles.add(new UpdatePacket(

                    UpdatePacket.Change.DISABLE,
                    originX,
                    originY,

                    tile,
                    x,
                    y,

                    reason

                ));

            }

        }

        return changedTiles;

    }

    public final ArrayList<UpdatePacket> updateRemainingTiles(final int originX,
                                                              final int originY) {

        ArrayList<UpdatePacket> changedTiles = new ArrayList<UpdatePacket>();

        remainingTiles = 0; // Reset remainingTiles, for the new count.

        // Iterate through all tiles in board, and increment
        // remainingTiles for every one still covered
        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                if (coveredTiles.contains(board[x][y])) {

                    remainingTiles++;

                }

            }

        }

        // If every tile is uncovered [or some other state, such as MINED]
        // Create a UpdatePacket for the originally pressed tile that
        // has the special state of UNCOVERED_CLEARED.
        if (remainingTiles == 0) {

            board[originX][originY] = TileState.State.UNCOVERED;

            changedTiles.add(new UpdatePacket(

                UpdatePacket.Change.CLEAR,
                originX,
                originY,

                TileState.State.UNCOVERED,
                originX,
                originY

            ));

        }

        return changedTiles;

    }

    public ArrayList<UpdatePacket> resetBoard() {

        ArrayList<UpdatePacket> changedTiles = new ArrayList<UpdatePacket>();

        TileState.State tile;

        // Reset flagCount to mineCount
        flagCount = mineCount;

        // Iterate through the entire board and reset every tile to COVERED

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                board[x][y] = TileState.State.COVERED;

                changedTiles.add(new UpdatePacket(

                    UpdatePacket.Change.COVER,
                    0,
                    0,

                    TileState.State.COVERED,
                    x,
                    y

                ));

            }

        }

        return changedTiles;

    }

    public TileState.State getTileAt(final int x, final int y) {

        return board[x][y];

    }

    public Integer getFlagCount() {

        return flagCount;

    }

}

// OxygenCobalt
