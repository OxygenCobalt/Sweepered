// Audio
// Very small class holding static AudioClips.

package media.audio;

import javafx.scene.media.AudioClip;

public final class Audio {

    private Audio() {

        // Isn't called.

    }

    public static void loadSounds() {

        // Run stop on every sound in order to cache them in memory,
        // preventing delays when the sound is played for the first time.

        // [Yeah, I don't know why either]

        CLICK_SOUND.stop();
        FLAG_SOUND.stop();
        EXPLODE_SOUND.stop();
        CLEAR_SOUND.stop();

    }

    private static final String RES_PATH = "file:src/main/resources/audio/";

    public static final AudioClip CLICK_SOUND = new AudioClip(RES_PATH + "click.wav");
    public static final AudioClip FLAG_SOUND = new AudioClip(RES_PATH + "tap.wav");

    public static final AudioClip EXPLODE_SOUND = new AudioClip(RES_PATH + "explosion.wav");
    public static final AudioClip CLEAR_SOUND = new AudioClip(RES_PATH + "arcade_start.wav");

}

// OxygenCobalt
