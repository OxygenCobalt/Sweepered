// Board
// Multithreaded class that handles interactions between tiles

package generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

import states.TileState;

public class Board {
    private TileState.State[][] board;

    private final int width;
    private final int height;
    private final int mineCount;

    public Board(int width, int height, int mineCount) {
        board = new TileState.State[width][height];

        this.width = width;
        this.height = height;
        this.mineCount = mineCount;

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
                TileState.State.MINED
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
        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();
        ArrayList<int[]> recursiveList = new ArrayList<int[]>();

        int foundMines = 0;

        TileState.State tile;

        boolean isNotCenter;
        boolean isNotOutOfBounds;

        for (int nearX = (originX - 1); nearX <  (originX + 2); nearX++) {

            for (int nearY = (originY - 1); nearY < (originY + 2); nearY++) {

                // Also check if surrounding tile is not outside the bounds of the board
                // before running any functions on it
                // TODO: THERE HAS TO BE A BETTER WAY TO WRITE THIS
                isNotOutOfBounds = (nearX != width && nearX >= 0) && (nearY != height && nearY >= 0);

                if (isNotOutOfBounds) {
                    tile = board[nearX][nearY];

                    // Add to mineCount if tile does contain a mine
                    if (tile == TileState.State.MINED) {
                        foundMines++;
                    }

                    // Blacklist any tile that is not covered from the recursivelist,
                    // to prevent flags from being destroyed and an infinite loop
                    // w/uncovered tiles
                    if (tile == TileState.State.COVERED) {
                        recursiveList.add(new int[]{nearX, nearY});
                    }
                }

            }

        }

        // Change tile state to uncovered, with the amount of nearby tiles
        TileState.State state = TileState.State.valueOf("UNCOVERED_" + String.valueOf(foundMines));

        board[originX][originY] = state;

        // Also add the changed tile to the list
        changedTiles.add(new ChangePacket(
            originX,
            originY,
            state
        ));

        // If there are no nearby mines, iterate recursively and add the list of changed tiles
        if (foundMines == 0) {

            for (int[] coords : recursiveList) {

                changedTiles.addAll(uncoverTile(coords[0], coords[1]));

            }

        }

        return changedTiles;
    }

    public ArrayList<ChangePacket> flagTile(int originX, int originY) {

        ArrayList<ChangePacket> changedTiles = new ArrayList<ChangePacket>();

        // Get reference to tile
        TileState.State tile = board[originX][originY];

        // If tile is not already flagged, set it to flag and create a ChangePacket for that.
        // Otherwise, set it to Covered instead
        if (tile != TileState.State.FLAGGED) {
            tile = TileState.State.FLAGGED;
        }

        else {
            tile = TileState.State.COVERED;
        }

        // Update tile on board and create corresponding ChangePacket
        board[originX][originY] = tile;

        changedTiles.add(new ChangePacket(
            originX,
            originY,
            tile
        ));

        return changedTiles;

    }

}

// OxygenCobalt