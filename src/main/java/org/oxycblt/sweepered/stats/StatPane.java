// StatPane
// Pane where information about the current game and the reset button is shown

package org.oxycblt.sweepered.stats;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import org.oxycblt.sweepered.shared.ui.Corner;
import org.oxycblt.sweepered.shared.values.GameState;
import org.oxycblt.sweepered.shared.observable.Listener;

import org.oxycblt.sweepered.stats.counter.Timer;
import org.oxycblt.sweepered.stats.counter.FlagCounter;

import org.oxycblt.sweepered.stats.ui.ResetButton;

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

        // Lock Size to prevent unintentional resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("game-pane");

        state = new GameState(GameState.State.UNSTARTED);

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

    public void updateBoardValues(final int newTileWidth) {

        // Only the width needs to be updated during
        // a mode change, as StatPanes height is static
        // and the mineCount isn't needed
        width = newTileWidth * 32;

        setPrefWidth(width);
        setMaxWidth(Region.USE_PREF_SIZE);

        // Update the positioning of only the ResetButton and the
        // timer, as they are dependent on the dimensions of StatPane.
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
