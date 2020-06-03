// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.util.ArrayList;

import events.observable.EventBoolean;
import events.observable.GameState;

import generation.Board;
import generation.ChangePacket;

import nodes.Tile;
import nodes.Corner;

public class TilePane extends Pane implements PropertyChangeListener {
    private final int width;
    private final int height;

    private final int x;
    private final int y;

    private final int tileWidth;
    private final int tileHeight;

    private final int mineCount;

    private final GameState state;

    private Board board;
    private Tile[][] tiles;

    public TilePane(int tileWidth, int tileHeight, int mineCount, int offset) { // Width/Height is given as the amount of tiles, not pixels
        x = offset;
        y = (offset * 2) + 44; // The added 44 is to account for the width of stackpane and the border of minepane.

        width = tileWidth * 32; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
        height = tileHeight * 32;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Lock Size to prevent unintential resizing

        // Set Style for the background and the borders
        setStyle(
            "-fx-background-color: #3d3d3d;" +
            "-fx-border-width: 4px;" +
            "-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;" +
            "-fx-border-style: solid outside;"
        );

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.mineCount = mineCount;

        state = new GameState(GameState.State.UNSTARTED);

        board = new Board(tileWidth, tileHeight, mineCount);
        tiles = new Tile[tileWidth][tileHeight];

        generateTiles();
        generateCorners();
    }

    private void generateTiles() {
        Tile tile;

        // Simply iterate through all the X and Y coordinates on the board, 
        // and generate a tile for them all
        for (int tileX = 0; tileX < tileWidth; tileX++) {

            for (int tileY = 0; tileY < tileHeight; tileY++) {
                tile = new Tile(
                        // Pass tile positions
                        tileX, 
                        tileY,
                        // Pass pane positions for MouseRect creation
                        x,
                        y
                );

                tile.getUncovered().addListener(this);

                tiles[tileX][tileY] = tile;

                getChildren().add(tiles[tileX][tileY]);
            }

        }

    }

    private void generateCorners() {

        // Iterate through every corner of the pane
        // and generate a corner for them all
        for (int cornerX = 0; cornerX < 2; cornerX++) {

            for (int cornerY = 0; cornerY < 2; cornerY++) {
                getChildren().add(new Corner(cornerX, cornerY, width, height));
            }

        }

    }

    public void propertyChange(PropertyChangeEvent event) {
        // TODO: Should probably fragment this into multiple functions, I dont know

        // Case the object where the event took place to access its X/Y values
        EventBoolean observable = (EventBoolean) event.getSource();

        int originX = observable.getX();
        int originY = observable.getY();

        // Get results of both generateMines() and uncoverTile(), and record them appropriately
        ArrayList<ChangePacket> toChange = new ArrayList<ChangePacket>();

        if (state.getValue() == GameState.State.UNSTARTED) {
            toChange.addAll(board.generateMines(originX, originY));

            state.setValue(GameState.State.STARTED);
        }

        toChange.addAll(board.uncoverTile(originX, originY));

        updateTiles(toChange);
    }

    private void updateTiles(ArrayList<ChangePacket> toChange) {
        Tile tile;

        for (ChangePacket change : toChange) {

            tile = tiles[change.getX()][change.getY()];

            tile.updateState(change.getNewState());

        }

    }

    public GameState getGameState() {
        return state;
    }

}

// OxygenCobalt