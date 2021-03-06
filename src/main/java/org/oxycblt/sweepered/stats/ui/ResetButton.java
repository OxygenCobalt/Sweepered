// ResetButton
// Button that displays the GameState but also has the ability to restart the game.

package org.oxycblt.sweepered.stats.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.image.ImageView;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import javafx.geometry.Rectangle2D;

import java.util.HashMap;

import org.oxycblt.sweepered.config.ConfigStage;

import org.oxycblt.sweepered.media.images.TextureAtlas;
import org.oxycblt.sweepered.media.images.Sprite;
import org.oxycblt.sweepered.media.audio.Audio;

import org.oxycblt.sweepered.shared.values.GameState;

public class ResetButton extends Pane {

    private int x;
    private int y;

    private final int width;
    private final int height;

    private GameState state;
    private GameState.State stateCache;

    private ConfigStage configStage;

    private String currentFace;
    private Sprite faceSprite;

    private Rectangle2D mouseRect;

    private HashMap<String, ImageView> images;

    public ResetButton(final int x, final int paneX, final int paneY) {

        this.x = x;
        this.y = 2;

        width = 36;
        height = 36;

        relocate(x, y);
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        state = new GameState(GameState.State.UNSTARTED);

        configStage = new ConfigStage();

        images = new HashMap<String, ImageView>();

        // Load basic textures, and set up the face values.
        currentFace = "faceUNSTARTED";
        faceSprite = TextureAtlas.FACE_NORMAL;

        // mouseRect is used to detect when the mouse
        // has been released *outside* of the ResetButton,
        // albeit relative to StatPane as I cant get the mouse
        // position relative to the pane itself.
        mouseRect = new Rectangle2D(x + paneX, y + paneY, width, height);

        loadTexture("Normal", TextureAtlas.RESET_NORMAL, false);
        loadTexture(currentFace, faceSprite, false);

        setOnMousePressed(pressListener);
        setOnMouseReleased(releaseListener);

    }

    EventHandler<MouseEvent> pressListener = event -> {

        // Disable ResetButton if the game is already unstarted

        MouseButton button = event.getButton();

        if (!state.isUnstarted() || button == MouseButton.SECONDARY) {

            loadTexture("Pressed", TextureAtlas.RESET_PRESSED, false);

            // If the left mouse button is being pressed, then do not show
            // the normal face when pressing down, instead the WAITING icon
            // to indicate the action about to be performed
            if (button == MouseButton.SECONDARY) {

                loadTexture("faceDISABLED", TextureAtlas.FACE_WAITING, true);

            } else {

                loadTexture(currentFace, faceSprite, true);

            }

        }

    };

    EventHandler<MouseEvent> releaseListener = event -> {

        // Disable ResetButton if the game is already unstarted

        MouseButton button = event.getButton();

        if (!state.isUnstarted() || button == MouseButton.SECONDARY) {

            // Find if the mouse pointer is still within the Rect2D
            Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());

            if (isInBox) {

                // Check which mouse button has been released.
                // If its the right click, then reset the board by setting the state
                // to UNSTARTED. If its a left click, open up the config menu and
                // disable the game by setting the state to DISABLED

                switch (button) {

                    case PRIMARY: resetGame(); break;

                    case SECONDARY: openConfigMenu(); break;

                }

            }

            // Either way, play the click sound

            Audio.CLICK_SOUND.play();

            updateFace(state.getState());

        }

    };

    private void resetGame() {

        state.setState(GameState.State.UNSTARTED);

    }

    private void openConfigMenu() {

        // Check if the configStage isnt already being
        // shown before proceeding.
        if (!configStage.isShowing()) {

            configStage.show();

            stateCache = state.getState();

            state.setState(GameState.State.DISABLED);

            // Make sure that the Game State is reverted to the original
            // state that was stored earlier when the window is closed.
            configStage.setOnHidden(event -> {

                state.setState(stateCache);

            });

        }

    }

    private void updateFace(final GameState.State newState) {

        // First reload the normal button texture, in order
        // to hide the old face.

        loadTexture("resetNormal", TextureAtlas.RESET_NORMAL, false);

        // Update the currentFace based off the gameState,
        // and then get the respective face texture for that.

        currentFace = "face" + String.valueOf(newState);

        switch (newState) {

            case UNSTARTED: faceSprite = TextureAtlas.FACE_NORMAL; break;

            case STARTED: faceSprite = TextureAtlas.FACE_NORMAL; break;

            case UNCERTAIN: faceSprite = TextureAtlas.FACE_UNCERTAIN; break;

            case EXPLOSION: faceSprite = TextureAtlas.FACE_EXPLOSION; break;

            case CLEARED: faceSprite = TextureAtlas.FACE_CLEARED; break;

            case DISABLED: faceSprite = TextureAtlas.FACE_WAITING; break;

        }

        loadTexture(currentFace, faceSprite, false);

    }

    private void loadTexture(final String name, final Sprite fallback, final Boolean dark) {

        ImageView image;

        double opacity;

        // If dark is set to true, then the texture will
        // be loaded slightly transparent, to make it
        // look darker. This is used with the faces when
        // ResetButton is pressed, to make it more natural.

        if (dark) {

            opacity = 0.5;

        } else {

            opacity = 1;
        }

        // Check if the image already exists in the map
        if (images.containsKey(name)) {

            image = images.get(name);

            image.setOpacity(opacity);

            image.toFront();


        // Otherwise, load it and add it to the map
        } else {

            image = TextureAtlas.get(fallback);

            image.setOpacity(opacity);

            images.put(

                name,
                image

            );

            getChildren().add(image); // Add it to the pane

        }

    }

    public void updatePosition(final int newX, final int paneX, final int paneY) {

        // Given the new values, update the X position and the mouseRect position
        this.x = newX;

        relocate(x, y);

        mouseRect = new Rectangle2D(x + paneX, y + paneY, width, height);

    }

    public void updateGameState(final GameState.State newState) {

        // Update the face and then set the state to the new value given.

        updateFace(newState);

        state.setStateSilent(newState);

    }

    public GameState getGameState() {

        return state;

    }

}

// OxygenCobalt
