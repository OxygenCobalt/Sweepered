// ShockwaveTimeline
// Timeline that handles explosion shockwaves.

package events;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.scene.image.ImageView;
import javafx.geometry.Point2D;

import javafx.util.Duration;

import media.TextureAtlas;
import media.Sprite;

import generation.states.TileState;

import nodes.entities.Tile;

public class WaveTimeline {

    private Timeline timeline;

    private Tile tile;
    private String type;

    private Sprite waveSprite;

    public WaveTimeline(Tile tile, Point2D location, Point2D origin, String type) {

        // Distance is measured in the time it will take for the shockwave to reach the tile itself.
        double distanceTime = location.distance(origin) * 0.1;

        // Store auxillary information
        this.tile = tile;
        this.type = type;

        // Determine wave tile based on given type.

        switch (type) {

            case "EXPLOSION": waveSprite = TextureAtlas.tileExplodeWave; break;

            case "CLEARED": waveSprite = TextureAtlas.tileClearWave; break;

            case "INVALID": waveSprite = TextureAtlas.tileInvalidWave; break;

        }

        tile.loadTexture("Wave", waveSprite);

        timeline = new Timeline(
            new KeyFrame(Duration.ZERO,                        event -> inactive()),
            new KeyFrame(Duration.seconds(distanceTime),       event -> active()),
            new KeyFrame(Duration.seconds(distanceTime + 0.1), event -> fade()),            
            new KeyFrame(Duration.seconds(distanceTime + 0.2), event -> inactive())
        );

    }
    
    private void active() {

        TileState state = tile.getTileState();

        switch (state.getState()) {

            case DISABLED_MINED: {

                switch (type) {

                    // Any mined tile should have their mine shown if a mine explodes
                    case "EXPLOSION": tile.loadTexture("Mined", TextureAtlas.uncoveredMined);
                                      break;

                    // Any remaining mines should be flagged if the board is cleared
                    case "CLEARED":   tile.loadTexture("Flagged", TextureAtlas.tileFlagged);
                                      break;

                    default: System.out.println("This shouldnt happen."); // > mfw this does happen

                }

                break;

            }

            case DISABLED_FLAGGED: {

                // If the tile is flagged incorrectly [Not under a mine], then recover it to show the error.
                // TODO: May change the texture to a flag w/an X mark over it.
                tile.loadTexture("Normal", TextureAtlas.tileNormal);

                break;

            }

        }

        // Bring waveTile back to front if another tile is loaded from above
        tile.loadTexture("Wave", waveSprite);

        tile.getImages().get("Wave").setOpacity(1);

    }

    private void fade() {

        tile.getImages().get("Wave").setOpacity(0.5);

    }

    private void inactive() {

        // Once the mine is shown, theres no need to unshow it, so just hide waveTile's opacity.
        tile.getImages().get("Wave").setOpacity(0);

    }

    public Timeline getTimeline() {return timeline;}

}

// Oxygencobalt