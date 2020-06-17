// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package game.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import events.observable.Listener;
import events.states.GameState;

import game.counter.Timer;
import game.counter.FlagCounter;

import game.entities.ResetButton;
import game.decor.Corner;

public class StatPane extends Pane implements Listener<GameState> {

    private final int x;
    private final int y;

    public final int height;
    public final int width;

    private final GameState state;

    private final FlagCounter flags;
    private final ResetButton reset;
    private final Timer timer;

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

        flags = new FlagCounter(6, (width / 32), 3, mineCount);
        reset = new ResetButton((width - 36) / 2, x, y);
        timer = new Timer(width - ((19 * 3) + 6), 3);

        reset.getGameState().addListener(this);

        getChildren().addAll(

            flags, reset, timer

        );

        Corner.generateCorners(this, false, false);

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

    public GameState getGameState() {

        return state;

    }

}

// OxygenCobalt
