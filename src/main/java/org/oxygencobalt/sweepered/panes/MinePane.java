// MinePane
// Pane where Tiles and mines are generated,

package panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;

import java.util.Random;
import java.util.ArrayList;

import events.EventBoolean;

import nodes.Tile;
import nodes.Corner;

public class MinePane extends Pane implements ChangeListener<Boolean> {
    private final int width;
    private final int height;

    private final int mineWidth;
    private final int mineHeight;

    private final int x;
    private final int y;

    private final int mineCount;

    private boolean gameStarted;

    private Tile[][] tiles;

    public MinePane(int mineWidth, int mineHeight, int mineCount, int offset) { // Width/Height is given as the amount of tiles, not pixels
        x = offset;
        y = (offset * 2) + 44; // The added 44 is to account for the width of stackpane and the border of minepane.

        width = mineWidth * 32; // Since w/h is based on mine count, its multiplied by the dimensions of the tiles to get the size in pixels
        height = mineHeight * 32;

        this.mineWidth = mineWidth;
        this.mineHeight = mineHeight;

        this.mineCount = mineCount;

        gameStarted = false;

        tiles = new Tile[this.mineWidth][this.mineHeight];

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

        generateTiles();
        generateCorners();
    }

    // Node Generation
    private void generateTiles() {
        // Simply iterate through all the X and Y coordinates on the board, 
        // and generate a tile for them all
        for (int tX = 0; tX < mineWidth; tX++) {

            for (int tY = 0; tY < mineHeight; tY++) {
                tiles[tX][tY] = new Tile(
                        // Pass mine positions
                        tX, 
                        tY,
                        // Pass pane positions for MouseRect creation
                        x,
                        y
                );

                tiles[tX][tY].getUncovered().addListener(this);

                getChildren().add(tiles[tX][tY]);
            }

        }

    }

    private void generateMines(int[] constraintsX, int[] constraintsY) {
        int generatedMines = 0;

        // Ratio is used to have an even-ish distribution of mines
        // Simply the factor of 1 and the width of the board,
        // multiplied by 100 to match up w/random
        double ratio = (1.0 / mineWidth) * 100;
        Random randGenerator = new Random();

        boolean inSafeZone;
        boolean isMine;

        while (generatedMines != mineCount) {

            gridLoop:
            for (Tile[] tileColumn : tiles) {

                for (Tile tile : tileColumn) {
                    // Find status of invalidating factors,
                    // such as if its around the first uncovered tile and if its already a mine

                    inSafeZone = tile.isInConstraints(constraintsX, constraintsY);
                    isMine = tile.getMineStatus();

                    if (!inSafeZone && !isMine) {

                        if (randGenerator.nextInt(100) < ratio) {
                            tile.becomeMine();

                            // GeneratedMines breaks the loop as soon as it reaches the mineCount,
                            // ending the entire generation loop
                            generatedMines++;
                            if (generatedMines == mineCount) {break gridLoop;}
                        }

                    }

                }

            }

        }
    }

    private void generateCorners() {
        // Like generateTiles, iterate through every corner of the pane
        // and generate a corner for them all
        for (int cX = 0; cX < 2; cX++) {

            for (int cY = 0; cY < 2; cY++) {
                getChildren().add(new Corner(cX, cY, width, height));
            }
        }
    }

    // Event handling
    public void changed(ObservableValue<? extends Boolean> rawObservable, Boolean oldValue, Boolean newValue) {
        // Cast observable to EventBoolean to get access to its stored values
        EventBoolean observable = (EventBoolean) rawObservable;

        switch (observable.getType()) {
            case "isUncovered": onUncover(observable); break;
            case "hasExploded": onExplode(); break;

            default: throw new UnsupportedOperationException("Invalid EventBoolean type");
        }
    }

    private void onUncover(EventBoolean observable) {
        // Find the stored x/y values from whatever tile was activated
        int obsX = observable.getX();
        int obsY = observable.getY();

        ArrayList<Tile> recursiveList = new ArrayList<Tile>();

        if (gameStarted) {
            int mineCount = 0;

            Tile tile;
            boolean isMine;
            boolean isNotCenter;
            boolean isNotOutOfBounds;

            recursiveList.clear();

            for (int uX = (obsX - 1); uX < (obsX + 2); uX++) {

                for (int uY = (obsY - 1); uY < (obsY + 2); uY++) {

                    isNotOutOfBounds = (uX != mineWidth && uX >= 0) && (uY != mineHeight && uY >= 0);

                    if (isNotOutOfBounds) {
                        tile = tiles[uX][uY];

                        isMine = tile.getMineStatus();
                        isNotCenter = !(uX == obsX && uY == obsY);

                        if (isMine && isNotCenter) {

                            isMine = tile.getMineStatus();
                            if (isMine) {mineCount++;}
                        }

                        recursiveList.add(tile);

                    }

                }

            }

            tiles[obsX][obsY].uncover(mineCount);

            if (mineCount == 0) {
                recursiveList.forEach((t) -> t.setUncovered(true));
            }

        } else { // Start the game otherwise
            gameStarted = true;

            // Constraints are calculated by creating a 3x3 "Rectangle" around the
            // tile that was uncovered, but in the from of their maximum and minimum values.
            int[] constraintsX = new int[]{obsX - 2, obsX + 2};
            int[] constraintsY = new int[]{obsY - 2, obsY + 2};

            // Generate mines and then run the function again to activate
            // the recursive looping for the first tile.
            generateMines(constraintsX, constraintsY);
            onUncover(observable);
        }
    }

    private void onExplode() {}
}

// OxygenCobalt