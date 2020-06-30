// FlagCounter
// Counter that keeps track of the amount of flags have been placed.

package stats.counter;

import java.util.ArrayList;
import java.util.Collections;

public class FlagCounter extends Counter {

    private int flagCount;

    public FlagCounter(final int x,
                       final int digitCount,
                       final int flagCount) {

        super(x, digitCount);

        updateFlagCount(flagCount);

    }

    public void updateFlagCount(final Integer newFlagCount) {

        ArrayList<Integer> flagDigits = new ArrayList<Integer>();

        flagCount = newFlagCount;

        // flagClone is used to split up the number
        // into digits w/o affecting flagCount.
        Integer flagClone = newFlagCount;

        int flagDigit;

        // Fill the array of each value separated into its individual digits,
        // and then reverse it to get the correct order of the digits
        while (flagClone > 0) {

            flagDigit = flagClone % 10;

            flagDigits.add(flagDigit);

            flagClone = flagClone / 10;

        }

        Collections.reverse(flagDigits);

        // Then update the counters digits
        // with the new value.
        updateDigits(flagDigits);

    }

}

// OxygenCobalt
