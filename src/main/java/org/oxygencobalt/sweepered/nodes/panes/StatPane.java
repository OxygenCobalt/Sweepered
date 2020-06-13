// StatPane
// Pane where items such as the timer, number of mines remaining, and reset button are

package nodes.panes;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import events.observable.Listener;
import events.states.GameState;

import nodes.entities.counter.Timer;
import nodes.entities.counter.FlagCounter;

import nodes.entities.ResetButton;
import nodes.entities.Corner;

public class StatPane extends Pane implements Listener<GameState> {

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

        flags = new FlagCounter(6, (width / 32), 3, mineCount);
        reset = new ResetButton((width - 36) / 2, offset);
        timer = new Timer(width - ((19 * 3) + 6), 3);

        reset.getGameState().addListener(this);

        getChildren().addAll(flags, reset, timer);

        Corner.generateCorners(this, false);

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
