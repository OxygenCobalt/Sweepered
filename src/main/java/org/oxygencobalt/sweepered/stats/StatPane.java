// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package stats;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import shared.ui.Corner;
import shared.values.GameState;
import shared.observable.Listener;

import stats.counter.Timer;
import stats.counter.FlagCounter;

import stats.ui.ResetButton;

public class StatPane extends Pane implements Listener<GameState> {

    private final int x;
    private final int y;

    public int height;
    public int width;

    private final GameState state;

    private final FlagCounter flags;
    private final ResetButton reset;
    private final Timer timer;

    private Corner[] corners;

    public StatPane(final int height,
                    final int width,
                    final int offset,
                    final int mineCount) {

        x = offset;
        y = offset;

        this.height = height;
        this.width = width;

        relocate(x, y);

        setPrefSize(width, height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        state = new GameState(GameState.State.UNSTARTED, "StatPane");

        flags = new FlagCounter(6, 3, mineCount);
        reset = new ResetButton((width - 36) / 2, x, y);
        timer = new Timer(width - ((19 * 3) + 6), 3);

        reset.getGameState().addListener(this);

        getChildren().addAll(

            flags, reset, timer

        );

        corners = Corner.generateCorners(this, false, false);

    }

    public void propertyChanged(final GameState changed) {

        state.setState(changed.getState());

    }

    public void updateGameState(final GameState.State newState) {

        // Update the state of ResetButton & the timer
        reset.updateGameState(newState);
        timer.updateGameState(newState);

        // Then update itself.
        state.setStateSilent(newState);

    }

    public void updateFlagCount(final Integer newFlagCount) {

        // Simply update the flag counter with the
        // new count, no need to store it otherwise.

        flags.updateFlagCount(newFlagCount);

    }

    public void updateBoardValues(final int newTileWidth,
                                  final int newTileHeight) {

        width = newTileWidth * 32;

        setPrefWidth(width);

        // Lock Size to prevent unintential resizing
        setMaxWidth(Region.USE_PREF_SIZE);

        // Update the positoning of only the resetbutton and the
        // timer, as they are dependent on the dimensions of statpane.
        reset.updatePosition((width - 36) / 2, x, y);
        timer.updatePosition(width - ((19 * 3) + 6));

        timer.resetTime(0);

        // Also destroy all corners and regenerate them with the new dimensions
        for (Corner corner : corners) {

            getChildren().remove(corner);

        }

        corners = Corner.generateCorners(this, false, false);

    }

    public GameState getGameState() {

        return state;

    }

}

// OxygenCobalt
