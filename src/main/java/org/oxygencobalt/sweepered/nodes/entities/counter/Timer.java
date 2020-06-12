// Timer
// Counter that keeps track of the time that has elapsed in the game.

package nodes.entities.counter;

import javafx.animation.AnimationTimer;

import java.time.Instant;

import java.util.Arrays;
import java.util.ArrayList;

import events.states.GameState;

public class Timer extends Counter {

    private Boolean started;
    private long startTime;

    private String elapsedCache;

    public Timer(final int x, final int digitCount) {

        super(x, digitCount);

        started = false;

    }

    private AnimationTimer countTime = new AnimationTimer() {

        @Override
        public void handle(final long now) {

            long currentTime;

            String elapsedTime;

            ArrayList<String> elapsedDigits;
            int elapsedSize;

            // Get the amount of seconds that has elapsed by
            // subtracting the new epoch time from the old epoch time.
            currentTime = Instant.now().getEpochSecond();

            elapsedTime = String.valueOf(currentTime - startTime);

            // If the new time hasnt changed, then ignore this code.
            if (!elapsedCache.equals(elapsedTime)) {

                // elapsedDigits is converted into an array to fit more easily w/the digits array
                elapsedDigits = new ArrayList<>(Arrays.asList(elapsedTime.split("(?!^)")));

                // Correct the contents of elapsed if it does
                // not match up w/the length of digits
                while (elapsedDigits.size() != digits.length) {

                    elapsedSize = elapsedDigits.size();

                    // If its too short, pad the front of
                    // elapsed with zeroes
                    if (elapsedSize < digits.length) {

                        elapsedDigits.add(0, "0");

                    // If its too long, trunicate any digits outside
                    // of it to create a rollover-type effect.
                    } else if (elapsedSize > digits.length) {

                        elapsedDigits.remove(0);

                    }

                }

                // For every digit in the list, update them
                // with the new digits from elapsed.
                for (int digit = 0; digit < digits.length; digit++) {

                    digits[digit] = Integer.parseInt(elapsedDigits.get(digit));

                }

                updateDigits(false);

                elapsedCache = elapsedTime;

            }

        }

    };

    public void updateGameState(final GameState.State newState) {

        switch (newState) {

            // Reset the timer if the board is being reset w/UNSTARTED
            case UNSTARTED: resetTime(); break;

            // Start the timer if the game is being started
            case STARTED: startTime(); break;

            // If its a game end condition, stop the timer;
            case EXPLOSION: stopTime(); break;

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
            elapsedCache = "";

            countTime.start();

        }

    }

    private void stopTime() {

        started = false;

        countTime.stop();

    }

    private void resetTime() {

        for (int digit = 0; digit < digits.length; digit++) {

            digits[digit] = 0;

        }

        updateDigits(false);

    }

}

// OxygenCobalt
