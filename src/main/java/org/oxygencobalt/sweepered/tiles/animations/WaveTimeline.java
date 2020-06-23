// ShockwaveTimeline
// Timeline that handles game end event shockwaves.

package tiles.animations;

import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.geometry.Point2D;

import media.images.TextureAtlas;
import media.images.Sprite;

import tiles.values.TileState;
import tiles.ui.Tile;

public class WaveTimeline {

    private Timeline timeline;

    private Tile tile;
    private TileState tileState;

    private String type;

    private Sprite waveSprite;

    public WaveTimeline(final Tile tile,
                        final Point2D location,
                        final Point2D origin,
                        final String type) {

        // Distance is measured in the time it will take for the shockwave to reach the tile itself.
        double distanceTime = location.distance(origin) * 0.1;

        // Store auxillary information
        this.tile = tile;
        this.type = type;
        this.tileState = tile.getTileState();

        // Determine wave tile based on given type.

        switch (type) {

            case "EXPLOSION": waveSprite = TextureAtlas.EXPLODE_WAVE; break;

            case "CLEARED": waveSprite = TextureAtlas.CLEAR_WAVE; break;

            default: waveSprite = TextureAtlas.INVALID_WAVE; break;

        }

        tile.loadTexture("Wave", waveSprite);

        // Main timeline used
        timeline = new Timeline(

            new KeyFrame(Duration.ZERO,                        event -> inactive()),
            new KeyFrame(Duration.seconds(distanceTime),       event -> active()),
            new KeyFrame(Duration.seconds(distanceTime + 0.1), event -> fade()),
            new KeyFrame(Duration.seconds(distanceTime + 0.2), event -> inactive())

        );

    }

    private void active() {

        TileState.State state = tileState.getState();

        switch (state) {

            case DISABLED_MINED: showTileState(); break;

            case DISABLED_BAD_FLAG: showBadFlag(); break;

        }

        // Bring waveTile back to front if another tile is loaded from above,
        // but only if the tile is DISABLED in any way to prevent the wave from
        // persisting past a reset.

        if (tileState.isDisabled()) {

            tile.loadTexture("Wave", waveSprite);

            tile.getImages().get("Wave").setOpacity(1);

        }

    }

    private void showTileState() {

        switch (type) {

            // Any mined tile should have their mine shown if a mine explodes
            case "EXPLOSION": tile.loadTexture("Mined", TextureAtlas.STATE_MINED);
                              break;

            // Any remaining mines should be flagged if the board is cleared
            case "CLEARED":   tile.loadTexture("Flagged", TextureAtlas.STATE_FLAGGED);
                              break;

            default: System.out.println("This shouldnt happen."); // > mfw this does happen

        }

    }

    private void showBadFlag() {

        // If the tile is flagged incorrectly [Not under a mine],
        // then show an X on the tile to correct it.

        tile.loadTexture("Incorrect", TextureAtlas.STATE_BAD_FLAG);

    }

    private void fade() {

        TileState.State state = tileState.getState();

        if (tileState.isDisabled()) {

            // After the wave is shown, set the wave to partially
            // transparent to make it look like its fading out
            tile.getImages().get("Wave").setOpacity(0.5);

        }

    }

    private void inactive() {

        TileState.State state = tileState.getState();

        if (tileState.isDisabled()) {

            // Once the mine is shown, theres no need to unshow it, so
            // just remove the Wave texture instead.
            tile.removeTexture("Wave");

        }

    }

    public Timeline getTimeline() {

        return timeline;

    }

}

// Oxygencobalt
