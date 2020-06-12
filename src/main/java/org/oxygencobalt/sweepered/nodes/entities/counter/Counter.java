// Counter
// Abstract class that serves as the base of Timer & FlagCount

package nodes.entities.counter;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.image.ImageView;

import java.util.Arrays;

import media.TextureAtlas;

import nodes.entities.Corner;

public abstract class Counter extends Pane {

    private final int x;
    private final int y;

    private final int width;
    private final int height;

    public int[] digits;
    private int[] digitCache;

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
        // Despite being an entity, Counter follows the same
        // border pattern as the panes.
        setStyle(

            "-fx-background-color: #000000;"
            +
            "-fx-border-width: 2px;"
            +
            "-fx-border-color: #1d1d1d #565656 #565656 #1d1d1d;"
            +
            "-fx-border-style: solid outside;"

        );

        // Create the digit array, its cache, and the list of digit ImageViews
        digits = new int[digitCount];
        digitCache = new int[digitCount];
        digitViews = new ImageView[digitCount];

        // Then, fill digits/digitCache with all zeros, the base value
        Arrays.fill(digits, 0);
        Arrays.fill(digitCache, 0);

        updateDigits(true);

        Corner.generateCorners(this, true);

    }

    public void updateDigits(final Boolean doInitGeneration) {

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

}

// OxygenCobalt
