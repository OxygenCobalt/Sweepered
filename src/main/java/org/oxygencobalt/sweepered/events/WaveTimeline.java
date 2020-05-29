// ShockwaveTimeline
// Timeline that handles explosion shockwaves.

package events;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.scene.image.ImageView;
import javafx.geometry.Point2D;

import javafx.util.Duration;

public class WaveTimeline {
	private Timeline timeline;

	private ImageView waveTile;
	private ImageView minedTile;
	private ImageView flaggedTile;

	Boolean isFlagged;

	String type;

	// TODO: Trim these arguments down HARD.
	public WaveTimeline(ImageView waveTile, ImageView minedTile, ImageView flaggedTile, Boolean isFlagged, Point2D location, Point2D origin, String type) {
		// Distance is measured in the time it will take for the shockwave to reach the tile itself.
		double distanceTime = location.distance(origin) * 0.1;

		// Store given imageview for the other functions
		this.waveTile = waveTile;
		this.minedTile = minedTile;
		this.flaggedTile = flaggedTile;

		this.isFlagged = isFlagged;

		this.type = type;

		timeline = new Timeline(
			new KeyFrame(Duration.ZERO,                        event -> inactive()),
            new KeyFrame(Duration.seconds(distanceTime),       event -> active()),
            new KeyFrame(Duration.seconds(distanceTime + 0.1), event -> fade()),            
            new KeyFrame(Duration.seconds(distanceTime + 0.2), event -> inactive())
		);
	}
	
	private void active() {
		// Check if the tile is mined [If it isnt, then it would have never loaded minedTile]
		if (minedTile != null && !isFlagged) {
			switch (type) {
				case "Explosion": minedTile.toFront(); break; // Any mined tile should have their mine shown if a mine explodes
				case "Cleared": flaggedTile.toFront(); break; // Any remaining mines should be flagged if the board is cleared

				default: System.out.println("This shouldnt happen."); // > mfw this does happen
			}
		}

		waveTile.setOpacity(1);
		waveTile.toFront();
	}

	private void fade() {
		waveTile.setOpacity(0.5);
	}

	private void inactive() {
		// Once the mine is shown, theres no need to unshow it, so just hide waveTile's opacity.
		waveTile.setOpacity(0);
	}

	public Timeline getTimeline() {
		return timeline;
	}
}

// Oxygencobalt