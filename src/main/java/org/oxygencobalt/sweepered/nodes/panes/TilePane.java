// MinePane
// Pane where Tiles and mines are generated,

package nodes.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.util.ArrayList;

import generation.board.Board;
import generation.board.ChangePacket;

import generation.states.GameState;
import generation.states.TileState;

import nodes.entities.Tile;
import nodes.entities.Corner;

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

    public TilePane(final int tileWidth, // w/h is given by number of tiles, not pixels.
                    final int tileHeight,
                    final int mineCount,
                    final int offset) {

        x = offset;
        y = (offset * 2) + 44; // 44 is added to account for StatPane and its border.

        // w/h is multiplied by the tile count to get the actual pixel size
        width = tileWidth * 32;
        height = tileHeight * 32;

        relocate(x, y);

        setPrefSize(width, height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        setStyle(

            "-fx-background-color: #3d3d3d;"
            +
            "-fx-border-width: 4px;"
            +
            "-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;"
            +
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

                tile.getTileState().addListener(this);

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

    public void propertyChange(final PropertyChangeEvent event) {

        // Cast the TileState corresponding to where the event took place to access its X/Y values
        TileState observable = (TileState) event.getSource();

        String message = observable.getMessage();

        final int originX = observable.getX();
        final int originY = observable.getY();

        ArrayList<ChangePacket> toChange = new ArrayList<ChangePacket>();

        switch (message) {

            case "Uncover": toChange = startUncover(originX, originY); break;

            case "Flag": toChange = startFlag(originX, originY); break;

        }

        // Get the last [The index where an exploded or cleared tile would be added to] and check if
        // its one of the special changes [EXPLODE or CLEAR]
        ChangePacket.Change originTile = toChange.get(toChange.size() - 1).getChange();

        // If so, run its respective function.
        switch (originTile) {

            case EXPLODE: toChange.addAll(startExplode(originX, originY)); break;

            case CLEAR: toChange.addAll(startClear(originX, originY)); break;

        }

        updateTiles(toChange);

    }

    private ArrayList<ChangePacket> startUncover(final int originX, final int originY) {

        ArrayList<ChangePacket> toChange = new ArrayList<ChangePacket>();

        if (state.getState() == GameState.State.UNSTARTED) {

            // If the game is unstarted, generate the mines before
            // uncovering the tile, while also starting the game.

            toChange.addAll(board.generateMines(originX, originY));

            state.setState(GameState.State.STARTED);

        }

        // In all cases, run uncoverTile on the given updated tile,
        // and uncover the remaining tile count [As uncoverTile cant do that itself.]

        toChange.addAll(board.uncoverTile(originX, originY));
        toChange.addAll(board.updateRemainingTiles(originX, originY));

        return toChange;

    }

    private ArrayList<ChangePacket> startFlag(final int originX, final int originY) {

        return board.flagTile(originX, originY);

    }

    private ArrayList<ChangePacket> startExplode(final int originX, final int originY) {

        // Since this is a game end scenario, change
        // the game state to EXPLOSION, which is the
        // loss condition.

        state.setState(GameState.State.EXPLOSION);

        return board.disableAllTiles("EXPLOSION", originX, originY);


    }

    private ArrayList<ChangePacket> startClear(final int originX, final int originY) {

        // Since this is a game end scenario, change
        // the game state to EXPLOSION, which is the
        // win condition.

        state.setState(GameState.State.CLEARED);

        return board.disableAllTiles("CLEARED", originX, originY);

    }

    private void updateTiles(final ArrayList<ChangePacket> toChange) {

        Tile tile;
        TileState.State newState;

        // Iterate through every ChangePacket, extract the referenced tile,
        // and pass the new state to its updateState function.

        for (ChangePacket change : toChange) {

            tile = tiles[change.getTargetX()][change.getTargetY()];

            tile.updateState(change);

        }

    }

    public GameState getGameState() {

        return state;

    }

}

// OxygenCobalt
