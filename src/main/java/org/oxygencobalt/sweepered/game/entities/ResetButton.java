// ResetButton
// Button that displays the GameState but also has the ability to restart the game.

package game.entities;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import javafx.scene.image.ImageView;

import javafx.geometry.Rectangle2D;

import java.util.HashMap;

import config.ConfigStage;

import events.states.GameState;

import media.images.TextureAtlas;
import media.images.Sprite;
import media.audio.Audio;

public class ResetButton extends Pane implements EventHandler<MouseEvent> {

    private final int x;
    private final int y;

    private final int width;
    private final int height;

    private GameState state;
    private GameState.State stateCache;

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
        mouseRect = new Rectangle2D(x + paneX, y + paneY, width, height);

        loadTexture("Normal", TextureAtlas.RESET_NORMAL, false);
        loadTexture(currentFace, faceSprite, false);

        setOnMousePressed(this);
        setOnMouseReleased(this);

    }

    public void handle(final MouseEvent event) {

        EventType type = event.getEventType();
        MouseButton button = event.getButton();

        // Disable ResetButton if the game is already unstarted

        if (state.getState() != GameState.State.UNSTARTED || button == MouseButton.SECONDARY) {

            if (type == MouseEvent.MOUSE_PRESSED) {

                onPress(event);

            }

            if (type == MouseEvent.MOUSE_RELEASED) {

                onRelease(event);

            }

        }

    }

    private void onPress(final MouseEvent event) {

        MouseButton button = event.getButton();

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

    private void onRelease(final MouseEvent event) {

        // Find if the mouse pointer is still within the Rect2D
        Boolean isInBox = mouseRect.contains(event.getSceneX(), event.getSceneY());
        MouseButton button;

        if (isInBox) {

            button = event.getButton();

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

    private void resetGame() {

        state.setState(GameState.State.UNSTARTED);

    }

    private void openConfigMenu() {

        // TODO: Make this stage its own class

        stateCache = state.getState();

        state.setState(GameState.State.DISABLED);

        ConfigStage configStage = new ConfigStage();

        // Make sure that the Game State is reverted to the original
        // state that was stored earlier when the window is closed.
        configStage.setOnHidden(event -> {

            state.setState(stateCache);

        });

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

        loadTexture("resetNormal", TextureAtlas.RESET_NORMAL, false);

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

        // FIXME: This probably wont work with any theme other than dark.

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

}

// OxygenCobalt
