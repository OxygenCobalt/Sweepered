// Corner
// Largely decorative object

package org.oxycblt.sweepered.shared.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.image.ImageView;

import org.oxycblt.sweepered.media.images.TextureAtlas;
import org.oxycblt.sweepered.media.images.Sprite;

public class Corner extends Pane {

    private final int x;
    private final int y;

    private int textureX;
    private int textureY;

    private final int width;
    private final int height;

    private Sprite cornerSprite;
    private ImageView cornerImage;

    public Corner(final int simpleX,
                  final int simpleY,
                  final int paneWidth,
                  final int paneHeight,
                  final Boolean small,
                  final Boolean inverted) {

        int offset = 4;
        textureX = simpleX;
        textureY = simpleY;

        if (inverted) {

            // If inverted, change the Y value to index the second
            // spritesheet that has the inverted corner images

            textureY = textureY + 2;

        }

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

    // This function inverts/uninverts the corner image, in a similar
    // way of setting "invert" to true in the class constructor.

    public void setInverted(final Boolean isInverted) {

        // Either move the texture index to the second
        // corner spritesheet if the corner needs to be inverted,
        // or move the texture index back to the original sprites
        // it it needs to be uninverted

        if (isInverted) {

            textureY = textureY + 2;

        } else {

            textureY = textureY - 2;

        }

        // Then. load the texture like in the class constructor,
        // but remove the cornerImage until the new one is loaded in.

        cornerSprite = new Sprite(

            TextureAtlas.CORNER_ATLAS,
            textureX,
            textureY,
            width,
            height

        );

        getChildren().remove(cornerImage);

        cornerImage = TextureAtlas.get(cornerSprite);

        getChildren().add(cornerImage);

    }

    // This function allows a pane to generate their own corners
    // Without a function of their own.

    public static Corner[] generateCorners(final Pane pane,
                                           final Boolean small,
                                           final Boolean inverted) {

        Corner[] toReturn = new Corner[4];

        Corner newCorner;

        // Get the preferred with of the panes
        int paneWidth = (int) pane.getPrefWidth();
        int paneHeight = (int) pane.getPrefHeight();

        int i = 0;

        // Then, iterate through every corner of the pane
        // and generate a corner for them all
        for (int cornerX = 0; cornerX < 2; cornerX++) {

            for (int cornerY = 0; cornerY < 2; cornerY++) {

                newCorner = new Corner(

                    cornerX,
                    cornerY,

                    paneWidth,
                    paneHeight,

                    // Small is used to return smaller corners
                    // to be used by the Counter/ConfigButtons
                    small,

                    // Inverted is used to return the opposite-colored
                    // corner, used by ConfigButton
                    inverted

                );

                pane.getChildren().add(newCorner);

                toReturn[i] = newCorner;

                i++;

            }

        }

        return toReturn;

    }

}

// OxygenCobalt
