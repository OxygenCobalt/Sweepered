// Timer
// Counter that keeps track of the time that has elapsed in the game.

package nodes.entities.counter;

import javafx.animation.AnimationTimer;

import java.time.Instant;

import java.util.ArrayList;
import java.util.Collections;

import events.states.GameState;

public class Timer extends Counter {

    private Boolean started;
    private long startTime;

    private int elapsedCache;

    public Timer(final int x, final int digitCount) {

        super(x, digitCount);

        started = false;

    }

    private AnimationTimer countTime = new AnimationTimer() {

        @Override
        public void handle(final long now) {

            ArrayList<Integer> passedDigits = new ArrayList<Integer>();

            long currentTime;
            int elapsedTime;

            // Get the amount of seconds that has elapsed by
            // subtracting the new epoch time from the old epoch time.
            currentTime = Instant.now().getEpochSecond();

            elapsedTime = (int) (currentTime - startTime);

            // If the new time hasnt changed, then ignore this code.
            if (elapsedCache != elapsedTime) {

                // Fill the array of each value seperated into its individual digits,
                // and then reverse it to get the correct order of the digits
                while (elapsedTime > 0) {

                    passedDigits.add(elapsedTime % 10);

                    elapsedTime = elapsedTime / 10;

                }

                Collections.reverse(passedDigits);

                // Then update the counters digits
                // with the new value.
                updateDigits(passedDigits);

                elapsedCache = elapsedTime;

            }

        }

    };

    public void updateGameState(final GameState.State newState) {

        switch (newState) {

            // Reset the timer if the board is being reset w/UNSTARTED
            case UNSTARTED: resetTime(0); break;

            // Start the timer if the game is being started
            case STARTED: startTime(); break;

            // If its the game ends with a loss, reset the timer with the dash value
            case EXPLOSION: resetTime(10); break;

            // If the board is cleared, just stop the time.
            case CLEARED: stopTime(); break;

        }

    }

    private void startTime() {

        if (!started) {

            started = true;

            // Set up variables to be compared later on
            // such as the time this function was called
            // and the last stored time difference.
            startTime = Instant.now().getEpochSecond();
            elapsedCache = 0;

            countTime.start();

        }

    }

    private void stopTime() {

        started = false;

        countTime.stop();

    }

    // resetTime is similar to stopTime, but it also
    // reverts all digits to their disabled "Dash" state.
    private void resetTime(final int digitType) {

        stopTime();

        // Fill a new array of digits with the value 10 [The dash
        // value] and then update the counter with that array
        ArrayList<Integer> resetNumbers = new ArrayList<Integer>();

        for (int digit = 0; digit < getDigitCount(); digit++) {

            resetNumbers.add(digitType);

        }

        updateDigits(resetNumbers);

    }

}

// OxygenCobalt
