// AboutStage
// Window for the settings of this game

package config;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.scene.layout.Pane;

import javafx.stage.Screen;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

import java.util.Map;

import config.panes.OptionPane;

import config.panes.ModePane;

import events.observable.Listener;
import events.states.ConfigState;

public class ConfigStage extends Stage implements Listener<ConfigState> {

    private Scene internalScene;
    private Group root;

    private final int width;
    private final int height;

    private ConfigState masterState;

    private OptionPane options;
    private ModePane mode;

    private final Map<ConfigState.State, String> configTitles;
    private final Map<ConfigState.State, double[]> configSizes;
    private final Map<ConfigState.State, Pane> configPanes;

    public ConfigStage() {

        width = 243;
        height = 113;

        // Since this object is a stage, create the internal
        // Group and the Scene instead of calling the super constructor
        root = new Group();
        internalScene = new Scene(root, width, height);
        internalScene.getStylesheets().add("file:src/main/resources/style/main.css");
        internalScene.setFill(Color.web("3d3d3d"));

        setTitle("Settings");
        setScene(internalScene);
        setResizable(false);

        // Screenbounds is the given size of the screen,
        // used to get a centered position of this window
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        setX((screenBounds.getWidth() - width) / 2);
        setY((screenBounds.getHeight() - height) / 2);

        masterState = new ConfigState(ConfigState.State.MENU);

        options = new OptionPane(243, 113);
        mode = new ModePane();

        options.getConfigState().addListener(this);
        mode.getConfigState().addListener(this);

        root.getChildren().addAll(mode, options);

        // configTitles is a map of window titles that is used
        // whenever the current menu [ConfigState] is changed
        configTitles = Map.of(

            ConfigState.State.MENU, "Settings",

            ConfigState.State.MODE, "Mode",

            ConfigState.State.ABOUT, "About Sweepered"

        );

        // configSizes is the list of sizes that the window
        // can expand or contract to to fit each menu
        configSizes = Map.of(

            // Store the given width of the two panes, with
            // 12 and 38 added to make up for their borders
            // and also correct the size for the stage apparently.
            ConfigState.State.MENU, new double[]{

                options.getPrefWidth() + 12,
                options.getPrefHeight() + 38

            },

            ConfigState.State.MODE, new double[]{

                mode.getPrefWidth() + 12,
                mode.getPrefHeight() + 38

            },

            ConfigState.State.ABOUT, new double[]{243, 113}

        );

        // configPanes is a map of references to created panes
        // that are brought to the front when the menu is changed
        configPanes = Map.of(

            ConfigState.State.MENU, options,

            ConfigState.State.MODE, mode,

            ConfigState.State.ABOUT, options

        );

        show();

    }

    public void propertyChanged(final ConfigState changed) {

        ConfigState.State newState = changed.getState();

        String windowTitle = configTitles.get(newState);
        double[] size = configSizes.get(newState);

        setTitle(windowTitle);
        setWidth(size[0]);
        setHeight(size[1]);

        configPanes.get(newState).toFront();

        options.updateConfigState(newState);
        mode.updateConfigState(newState);

        // Update the master state once everything is done.
        masterState.setStateSilent(newState);

    }

}

// OxygenCobalt
