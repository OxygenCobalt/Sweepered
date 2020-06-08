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
                  final int paneHeight) {

        // The paneWidth is multiplied by a simple coordinate [which is 0 or 1] to
        // find a real coordinate, whether -4 [Left] or the width of the pane [Right]
        x = -4 + (simpleX * (paneWidth + 4));
        y = -4 + (simpleY * (paneHeight + 4));

        width = 4;
        height = 4;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Instead of creating sprites in TextureAtlas for corners,
        // Corner generates its own instead.
        cornerSprite = new Sprite(

            TextureAtlas.CORNER_ATLAS,
            simpleX,
            simpleY,
            width,
            height

        );

        cornerImage = TextureAtlas.get(cornerSprite);

        getChildren().add(cornerImage);

    }

    // This function allows a pane to generate their own corners
    // Without a function of their own.

    public static void generateCorners(final Pane pane) {

        // Get the preferred with of the panes
        int paneWidth = (int) pane.getPrefWidth();
        int paneHeight = (int) pane.getPrefHeight();

        // Then, iterate through every corner of the pane
        // and generate a corner for them all
        for (int cornerX = 0; cornerX < 2; cornerX++) {

            for (int cornerY = 0; cornerY < 2; cornerY++) {

                pane.getChildren().add(

                    new Corner(cornerX, cornerY, paneWidth, paneHeight)

                );

            }

        }

    }

}

// OxygenCobalt
