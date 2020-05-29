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
	private ImageView shockwaveTile;
	private ImageView minedTile;

	public ShockwaveTimeline(ImageView shockwaveTile, ImageView minedTile, Point2D location, Point2D origin) {
		// Distance is measured in the time it will take for the shockwave to reach the tile itself.
		double distanceTime = location.distance(origin) * 0.1;

		// Store given imageview for the other functions
		this.shockwaveTile = shockwaveTile;
		this.minedTile = minedTile;

		timeline = new Timeline(
			new KeyFrame(Duration.ZERO, event -> setInactive()),
            new KeyFrame(Duration.seconds(distanceTime), event -> setActive()),
            new KeyFrame(Duration.seconds(distanceTime + 0.1), event -> setInactive())
		);
	}

	public Timeline getTimeline() {
		return timeline;
	}

	private void setActive() {
		// Show tile's mine when shockwave reaches it [if it has one]
		if (minedTile != null) {minedTile.toFront(); shockwaveTile.toFront();}

		shockwaveTile.setOpacity(1);
	}

	private void setInactive() {
		// Once the mine is shown, theres no need to unshow it, so just hide shockwaveTiles opacity.
		shockwaveTile.setOpacity(0);
	}
}