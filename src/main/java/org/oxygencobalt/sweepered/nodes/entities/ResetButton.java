// ResetButton
// Button that displays the GameState but also has the ability to restart the game.

package nodes.entities;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.image.ImageView;

import java.util.HashMap;

import events.states.GameState;

import media.TextureAtlas;
import media.Sprite;

public class ResetButton extends Pane {

    private final int x;
    private final int y;

    private final int width;
    private final int height;

    GameState state;

    private String currentFace;

    private HashMap<String, ImageView> images;

    public ResetButton(final int x) {

        // X is the only param needed, to center ResetButton
        this.x = x;
        this.y = 2;

        width = 36;
        height = 36;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        state = new GameState(GameState.State.UNSTARTED, "ResetButton");

        currentFace = "Normal";

        images = new HashMap<String, ImageView>();

        loadTexture("resetNormal", TextureAtlas.RESET_NORMAL);
        loadTexture("faceNormal", TextureAtlas.FACE_NORMAL);

    }

    public void updateGameState(final GameState.State newState) {

        // First reload the normal button texture, in order
        // to hide the old face.

        loadTexture("resetNormal", TextureAtlas.RESET_NORMAL);

        // Then, convert the new GameState to a face to load.

        // FIXME: This thing is a result of the janky way you wrote
        // TextureAtlas, FIX WHEN YOU REWRITE IT

        switch (newState) {

            case UNSTARTED: loadTexture("faceNormal", TextureAtlas.FACE_NORMAL); break;

            case STARTED: loadTexture("faceNormal", TextureAtlas.FACE_NORMAL); break;

            case UNCERTAIN: loadTexture("faceUncertain", TextureAtlas.FACE_UNCERTAIN); break;

            case EXPLOSION: loadTexture("faceExplosion", TextureAtlas.FACE_EXPLOSION); break;

            case CLEARED: loadTexture("faceCleared", TextureAtlas.FACE_CLEARED); break;

        }

        // Finally update the state once everything is done.

        state.setStateSilent(newState);

    }

    public GameState getGameState() {

        return state;

    }

    private void loadTexture(final String name, final Sprite fallback) {

        // Check if the image already exists in the map
        if (images.containsKey(name)) {

            images.get(name).toFront();

        // Otherwise, load it and add it to the map
        } else {

            images.put(

                name,
                TextureAtlas.get(fallback)

            );

            getChildren().add(images.get(name)); // Add it to the pane

        }

    }

}
