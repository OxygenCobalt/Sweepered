// ResetButton
// Button that displays the GameState but also has the ability to restart the game.

package nodes.entities;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;

import javafx.geometry.Rectangle2D;

import java.util.HashMap;

import events.states.GameState;

import media.TextureAtlas;
import media.Sprite;
import media.Audio;

public class ResetButton extends Pane implements EventHandler<MouseEvent> {

    private final int x;
    private final int y;

    private final int width;
    private final int height;

    GameState state;

    private String currentFace;
    private Sprite faceSprite;

    private Rectangle2D mouseRect;

    private HashMap<String, ImageView> images;

    public ResetButton(final int x, final int offset) {

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

        // Load basic textures, and set up the face values.
        currentFace = "faceUNSTARTED";
        faceSprite = TextureAtlas.FACE_NORMAL;

        // mouseRect is used to detect when the mouse
        // has been released *outside* of the ResetButton,
        // albeit relative to StatPane as I cant get the mouse
        // position relative to the pane itself.
        mouseRect = new Rectangle2D(x + offset, y + offset, width, height);

        loadTexture("Normal", TextureAtlas.RESET_NORMAL);
        loadTexture(currentFace, faceSprite);

        setOnMousePressed(this);
        setOnMouseReleased(this);

    }

    public void handle(final MouseEvent event) {

        EventType type = event.getEventType();

        // Disable ResetButton if the game is already unstarted

        if (state.getState() != GameState.State.UNSTARTED) {

            if (type == MouseEvent.MOUSE_PRESSED) {

                onPress(event);

            }

            if (type == MouseEvent.MOUSE_RELEASED) {

                onRelease(event);

            }

        }

    }

    private void onPress(final MouseEvent event) {

        loadTexture("Pressed", TextureAtlas.RESET_PRESSED);

        loadTexture(currentFace, faceSprite);

    }

    private void onRelease(final MouseEvent event) {


        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

        if (isInBox) {

            // If so, change the state to UNSTARTED, notifying the listeners.

            state.setState(GameState.State.UNSTARTED);

        }

        // Either way, play the click sound

        Audio.CLICK_SOUND.play();

        updateFace(state.getState());

    }

    public void updateGameState(final GameState.State newState) {

        // Update the face and then set the state to the new value given.

        updateFace(newState);

        state.setStateSilent(newState);

    }

    public GameState getGameState() {

        return state;

    }

    private void updateFace(final GameState.State newState) {

        // First reload the normal button texture, in order
        // to hide the old face.

        loadTexture("resetNormal", TextureAtlas.RESET_NORMAL);

        // FIXME: This thing is a result of the janky way you wrote
        // TextureAtlas, FIX WHEN YOU REWRITE IT

        // Update the currentFace based off the gameState,
        // and then get the respective face texture for that.

        currentFace = "face" + String.valueOf(newState);

        switch (newState) {

            case UNSTARTED: faceSprite = TextureAtlas.FACE_NORMAL; break;

            case STARTED: faceSprite = TextureAtlas.FACE_NORMAL; break;

            case UNCERTAIN: faceSprite = TextureAtlas.FACE_UNCERTAIN; break;

            case EXPLOSION: faceSprite = TextureAtlas.FACE_EXPLOSION; break;

            case CLEARED: faceSprite = TextureAtlas.FACE_CLEARED; break;

        }

        loadTexture(currentFace, faceSprite);

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
