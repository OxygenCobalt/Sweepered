// Board
// Multithreaded class that handles interactions between tiles

package generation;

// TODO: Maybe ditch ArrayList
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

import states.TileState;

public class Board {
    private TileState.State[][] board;

    private final int width;
    private final int height;
    private final int mineCount;

    private int remainingTiles;

    private final List<TileState.State> minedTiles;
    private final List<TileState.State> coveredTiles;

    public Board(int width, int height, int mineCount) {
        board = new TileState.State[width][height];

        this.width = width;
        this.height = height;
        this.mineCount = mineCount;

        this.remainingTiles = (width * height) - mineCount;

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
    public ArrayList<ChangePacket> generateMines(int originX, int originY) {
        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();

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

                    // Create an array with the X and Y coordinate of the tile, and add it to the list
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
            changedTiles.add(new ChangePacket(
                coordX,
                coordY,
                TileState.State.MINED,
                null
            ));

            // Remove the value from the list, to prevent it from becoming a mine again.
            mineLocations.remove(index);

            // Update list length now that an item has been removed
            locationWidth = mineLocations.toArray().length;

            generatedMines++;

        } while (generatedMines != mineCount);

        return changedTiles;

    }

    public ArrayList<ChangePacket> uncoverTile(int originX, int originY) {

        // Declare the variables used.
        
        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();
        ArrayList<int[]> recursiveList = new ArrayList<int[]>();

        TileState.State nearTile;
        TileState.State originTile;

        int foundMines = 0;

        Boolean isNotOutOfBounds;

        originTile = board[originX][originY];

        // First, check if the tile is mined
        if (originTile == TileState.State.MINED) {

            // If thats the case, set the origin tile to EXPLODED and drop the other instructions
            originTile = TileState.State.EXPLODED;

            board[originX][originY] = originTile;

        }

        else { // Otherwise, continue.

            for (int nearX = (originX - 1); nearX <  (originX + 2); nearX++) {

                for (int nearY = (originY - 1); nearY < (originY + 2); nearY++) {

                    // Also check if surrounding tile is not outside the bounds of the board
                    // before running any functions on it
                    // TODO: THERE HAS TO BE A BETTER WAY TO WRITE THIS
                    isNotOutOfBounds = (nearX != width && nearX >= 0) && (nearY != height && nearY >= 0);

                    if (isNotOutOfBounds) {
                        nearTile = board[nearX][nearY];

                        // Add to mineCount if tile does contain a mine [Or its flagged or disabled equivelents]
                        if (minedTiles.contains(nearTile)) {
                            foundMines++;
                        }

                        // Blacklist any tile that is not covered from the recursivelist,
                        // to prevent flags from being destroyed and an infinite loop
                        // w/uncovered tiles

                        // FIXME: The mines are being removed recursively multiple times, but im not sure if thats possible to be fixed.

                        if (nearTile == TileState.State.COVERED) {
                            recursiveList.add(new int[]{nearX, nearY});
                        }

                    }

                }

            }

            // Change the origin tile to uncovered, with the amount of nearby tiles
            originTile = TileState.State.valueOf("UNCOVERED_" + String.valueOf(foundMines));

            // To prevent a StackOverflowError, the board tile has to be updated seperately in both cases.
            board[originX][originY] = originTile;

            // If there are no nearby mines, iterate recursively and add the list of changed tiles
            if (foundMines == 0) {

                for (int[] coords : recursiveList) {

                    changedTiles.addAll(uncoverTile(coords[0], coords[1]));

                }

            }
        }

        // Finally, create a ChangePacket for the original updated tile.

        changedTiles.add(

            new ChangePacket(
                originX,
                originY,
                originTile,
                null
            )

        );

        return changedTiles;

    }

    public ArrayList<ChangePacket> flagTile(int originX, int originY) {

        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();

        // Get reference to tile
        TileState.State tile = board[originX][originY];

        String stringTile = String.valueOf(tile);


        // Check if the tile is not already flagged
        // If so, change the flagged status to the corresponding flag status for the tiles mined state
        // If not, convert the flagged status back into the mined state
        if (!stringTile.contains("FLAGGED")) {

            if (tile == TileState.State.MINED) { // Check if theres an underlying mine before changing the state

                tile = TileState.State.FLAGGED_MINED; // If so, change the tile to its corresponding value

            }

            else {

                tile = TileState.State.FLAGGED; // Otherwise, change it to the plain flagged value

            }

        }

        else {

            // Perform a similar operation as above, but in reverse.
            if (tile == TileState.State.FLAGGED_MINED) {

                tile = TileState.State.MINED;


            } 

            else {

                tile = TileState.State.COVERED;

            }

        }

        // Update tile on board and create corresponding ChangePacket
        board[originX][originY] = tile;

        changedTiles.add(new ChangePacket(
            originX,
            originY,
            tile,
            null
        ));

        return changedTiles;

    }

    public ArrayList<ChangePacket> notifyAllTiles(String type, int originX, int originY) {

        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();

        TileState.State tile;

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                tile = board[x][y];

                if (tile == TileState.State.MINED) {

                    tile = TileState.State.DISABLED_MINED;

                }

                else {

                    tile = TileState.State.DISABLED;

                }

                board[x][y] = tile;

                changedTiles.add(new ChangePacket(
                    x,
                    y,
                    tile,
                    type,
                    originX,
                    originY
                ));

            }

        }

        return changedTiles;

    }

    public ArrayList<ChangePacket> updateRemainingTiles(int originX, int originY) {

        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();

        remainingTiles = 0; // Reset remainingTiles, for the new count.

        // Iterate through all tiles in board, and increment remainingTiles for every one still covered
        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                if (coveredTiles.contains(board[x][y])) {

                    remainingTiles++;

                }

            }

        }

        // If every tile is uncovered [or some other state, such as MINED]
        // Create a changePacket for the originally pressed tile that has the special state of UNCOVERED_CLEARED.
        if (remainingTiles == 0) {

            changedTiles.add(new ChangePacket(

                originX,
                originY,
                TileState.State.UNCOVERED_CLEARED,
                null

            ));

        } 

        return changedTiles;

    }

}

// OxygenCobalt