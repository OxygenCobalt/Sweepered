// ShockwaveTimeline
// Timeline that handles explosion shockwaves.

package events;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.scene.image.ImageView;
import javafx.geometry.Point2D;

import javafx.util.Duration;

public class ShockwaveTimeline {
	private Timeline timeline;

	private ImageView waveTile;
	private ImageView minedTile;

	boolean isFlagged;

	public ShockwaveTimeline(ImageView waveTile, ImageView minedTile, boolean isFlagged, Point2D location, Point2D origin) {
		// Distance is measured in the time it will take for the shockwave to reach the tile itself.
		double distanceTime = location.distance(origin) * 0.1;

		// Store given imageview for the other functions
		this.waveTile = waveTile;
		this.minedTile = minedTile;

		this.isFlagged = isFlagged;

		timeline = new Timeline(
			new KeyFrame(Duration.ZERO,                        event -> inactive()),
            new KeyFrame(Duration.seconds(distanceTime), event -> active()),
            new KeyFrame(Duration.seconds(distanceTime + 0.1), event -> fade()),            
            new KeyFrame(Duration.seconds(distanceTime + 0.2), event -> inactive())
		);
	}
	
	private void fade() {
		waveTile.setOpacity(0.5);
	}

	private void active() {
		// Show tile's mine when the wave reaches it [if it has one]
		// Also check for the tiles flag status, as they should not change to reveal their mine status.
		if (minedTile != null && !isFlagged) {
			minedTile.toFront(); 
		}

		waveTile.setOpacity(1);
		waveTile.toFront();
	}

	private void inactive() {
		// Once the mine is shown, theres no need to unshow it, so just hide waveTile's opacity.
		waveTile.setOpacity(0);
	}

	public Timeline getTimeline() {
		return timeline;
	}
}