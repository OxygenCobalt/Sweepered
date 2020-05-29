// Audio
// Very small class holding static Audioclips for now.

package media;

import javafx.scene.media.AudioClip;

public class Audio {
    private static final String resPath = "file:src/main/resources/audio/";

    public static final AudioClip clickSound = new AudioClip(resPath + "click.wav");
   	public static final AudioClip flagSound = new AudioClip(resPath + "flag.wav");

    public static final AudioClip explodeSound = new AudioClip(resPath + "explode.wav");
    public static final AudioClip clearSound = new AudioClip(resPath + "cleared.wav");
}

// OxygenCobalt