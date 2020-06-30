// Audio
// Very small class holding static AudioClips.

package org.oxycblt.sweepered.media.audio;

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

    public static final AudioClip CLICK_SOUND = new AudioClip(

        Audio.class.getResource("/org/oxycblt/sweepered/audio/click.wav").toString()

    );

    public static final AudioClip FLAG_SOUND = new AudioClip(

        Audio.class.getResource("/org/oxycblt/sweepered/audio/tap.wav").toString()

    );

    public static final AudioClip EXPLODE_SOUND = new AudioClip(

        Audio.class.getResource("/org/oxycblt/sweepered/audio/explosion.wav").toString()

    );

    public static final AudioClip CLEAR_SOUND = new AudioClip(

        Audio.class.getResource("/org/oxycblt/sweepered/audio/arcade_start.wav").toString()

    );

}

// OxygenCobalt
