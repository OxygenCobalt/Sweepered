// Audio
// Very small class holding static Audioclips for now.

package media;

import javafx.scene.media.AudioClip;

public class Audio {

    public static void loadSounds() {

        // Run stop on every sound in order to cache them in memory,
        // preventing delays when the sound is played for the first time.

        // [Yeah, I dont know why either]

        clickSound.stop();
        flagSound.stop();
        explodeSound.stop();
        clearSound.stop();

    }

    private static final String resPath = "file:src/main/resources/audio/";

    public static final AudioClip clickSound = new AudioClip(resPath + "click.wav");
    public static final AudioClip flagSound = new AudioClip(resPath + "flag.wav");

    public static final AudioClip explodeSound = new AudioClip(resPath + "explode.wav");
    public static final AudioClip clearSound = new AudioClip(resPath + "cleared.wav"); // TODO: Maybe change this sound.

}

// OxygenCobalt