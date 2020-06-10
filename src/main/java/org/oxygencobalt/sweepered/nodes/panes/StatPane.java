// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package nodes.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import events.states.GameState;

import nodes.entities.Corner;

public class StatPane extends Pane {

    public final int height;
    public final int width;

    private final GameState state;

    public StatPane(final int height,
                    final int width,
                    final int offset,
                    final int mineCount) {

        this.height = height;
        this.width = width;

        relocate(offset, offset);

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

        state = new GameState(GameState.State.UNSTARTED, "StatPane");

        generateCorners();

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

    public void updateGameState(final GameState.State newState) {



    }

    public GameState getGameState() {

        return state;

    }

}

// OxygenCobalt
