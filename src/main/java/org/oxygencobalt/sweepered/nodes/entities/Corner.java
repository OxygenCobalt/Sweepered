// Corner
// Largely decorative object

package nodes.entities;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.image.ImageView;

import media.TextureAtlas;
import media.Sprite;

public class Corner extends Pane {

    private final int x;
    private final int y;

    private final int width;
    private final int height;

    private Sprite cornerSprite;
    private ImageView cornerImage;

    public Corner(final int simpleX,
                  final int simpleY,
                  final int paneWidth,
                  final int paneHeight,
                  final Boolean small) {

        int offset = 4;
        int textureX = simpleX;
        int textureY = simpleY;

        if (small) {

            // If the corner should be small, subtract 2 from the
            // offset to get the correct coordinate for a half-size corner
            offset = offset - 2;

            // Also increment the texture coordinates
            // to index cornerAtlas correctly.
            textureX = textureX + 4;

        }

        // The paneWidth is multiplied by a simple coordinate [which is 0 or 1] to
        // find a real coordinate, whether -4/-2 [Left] or the width of the pane [Right]
        x = -offset + (simpleX * (paneWidth + offset));
        y = -offset + (simpleY * (paneHeight + offset));

        // The offset also applies to the corners w/h, so set that accordingly.
        width = offset;
        height = offset;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Instead of creating sprites in TextureAtlas for corners,
        // Corner generates its own instead.
        cornerSprite = new Sprite(

            TextureAtlas.CORNER_ATLAS,
            textureX,
            textureY,
            width,
            height

        );

        cornerImage = TextureAtlas.get(cornerSprite);

        getChildren().add(cornerImage);

    }

    // This function allows a pane to generate their own corners
    // Without a function of their own.

    public static void generateCorners(final Pane pane, final Boolean small) {

        // Get the preferred with of the panes
        int paneWidth = (int) pane.getPrefWidth();
        int paneHeight = (int) pane.getPrefHeight();

        // Then, iterate through every corner of the pane
        // and generate a corner for them all
        for (int cornerX = 0; cornerX < 2; cornerX++) {

            for (int cornerY = 0; cornerY < 2; cornerY++) {

                pane.getChildren().add(

                    new Corner(
                        cornerX,
                        cornerY,

                        paneWidth,
                        paneHeight,

                        // Small is used to return smaller corners
                        // to be used by the counter
                        small)

                );

            }

        }

    }

}

// OxygenCobalt
