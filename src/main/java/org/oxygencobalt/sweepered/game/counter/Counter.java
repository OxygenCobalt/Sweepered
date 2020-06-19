// Counter
// Abstract class that serves as the base of Timer & FlagCount

package game.counter;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.ArrayList;

import media.images.TextureAtlas;

import game.decor.Corner;

public abstract class Counter extends Pane {

    private int x;
    private int y;

    private final int width;
    private final int height;

    private int[] digits;
    private int[] digitCache;

    private int digitCount;

    private ImageView[] digitViews;

    public Counter(final int x, final int digitCount) {

        this.x = x;
        this.y = 4;

        // The width of a counter is determined by the digit
        // count multiplied by the width of a digit image.
        // Height is static.
        this.width = digitCount * 19;
        this.height = 32;

        relocate(x, y);

        setPrefSize(width, height);

        // Lock Size to prevent unintential resizing
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set Style for the background and the borders
        getStyleClass().add("counter");

        // Create the digit array, its cache, and the list of digit ImageViews
        digits = new int[digitCount];
        digitCache = new int[digitCount];

        this.digitCount = digitCount;

        digitViews = new ImageView[digitCount];

        // Then, fill digits/digitCache with 10, the disabled value [Simply a dash]
        Arrays.fill(digits, 0);
        Arrays.fill(digitCache, 0);

        updateDigitDisplay(true);

        Corner.generateCorners(this, true, false);

    }

    public void updateDigits(final ArrayList<Integer> digitArgs) {

        int digitSize;

        // First, correct the list of digits if its too large/small to be displayed.
        while (digitArgs.size() != digits.length) {

            digitSize = digitArgs.size();

            // If its too short, pad the front of
            // elapsed with zeroes
            if (digitSize < digits.length) {

                digitArgs.add(0, 0);

            // If its too long, trunicate any digits outside
            // of it to create a rollover effect.
            } else if (digitSize > digits.length) {

                digitArgs.remove(0);

            }

        }

        // For every digit in the list, update them
        // with the new digits from elapsed.
        for (int digit = 0; digit < digits.length; digit++) {

            digits[digit] = digitArgs.get(digit);

        }

        updateDigitDisplay(false);

    }

    private void updateDigitDisplay(final Boolean doInitGeneration) {

        Boolean isUpdated;
        ImageView newDigit;

        for (int i = 0; i < digits.length; i++) {

            // First, check if the digit has actually changed by comparing it
            // to digitCache. This can be disabled by setting doInitGeneration to
            // true, but that is reserved to when a Counter is first initialized.
            if (digits[i] != digitCache[i] || doInitGeneration) {

                newDigit = TextureAtlas.get(

                    // Query textureAtlas for the specific digit
                    // image based on the updated value in digits
                    TextureAtlas.DIGITS[digits[i]]

                );

                newDigit.setX(i * 19);

                // Remove the old digit from the pane,
                // update its entry with the new digit,
                // and re-add the entry.

                // Removing the old digit does not happen when
                // the digits are first updated.

                if (!doInitGeneration) {

                    getChildren().remove(digitViews[i]);

                }

                digitViews[i] = newDigit;

                getChildren().add(digitViews[i]);

                digitCache[i] = digits[i];

            }

        }

    }

    public void updatePosition(final int newX) {

        x = newX;

        relocate(x, y);

    }

    public int getDigitCount() {

        return digitCount;

    }

}

// OxygenCobalt
