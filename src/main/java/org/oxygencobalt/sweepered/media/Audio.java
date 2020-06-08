// Audio
// Very small class holding static Audioclips.

package media;

import javafx.scene.media.AudioClip;

public final class Audio {

    private Audio() {

        // Isnt called.

    }

    public static void loadSounds() {

        // Run stop on every sound in order to cache them in memory,
        // preventing delays when the sound is played for the first time.

        // [Yeah, I dont know why either]

        CLICK_SOUND.stop();
        FLAG_SOUND.stop();
        EXPLODE_SOUND.stop();
        CLEAR_SOUND.stop();

    }

    private static final String RES_PATH = "file:src/main/resources/audio/";

    public static final AudioClip CLICK_SOUND = new AudioClip(RES_PATH + "click.wav");
    public static final AudioClip FLAG_SOUND = new AudioClip(RES_PATH + "flag.wav");

    public static final AudioClip EXPLODE_SOUND = new AudioClip(RES_PATH + "explode.wav");
    public static final AudioClip CLEAR_SOUND = new AudioClip(RES_PATH + "cleared.wav");

}

// OxygenCobalt
