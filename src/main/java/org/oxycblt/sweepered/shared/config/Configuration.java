// Configuration
// Wrapper around the preferences api that reduces the amount of write operations
// and allows the creation of eventintegers.

package shared.config;

import java.util.prefs.Preferences;
import java.util.HashMap;

import shared.values.EventInteger;

public final class Configuration {

    private Configuration() {

        // Not Called

    }

    private static final String PREF_NODE = "/org/oxygencobalt/sweepered";
    private static final String[] PREF_KEYS = new String[]{

        "mode", "tileWidth", "tileHeight", "mineCount"

    };

    private static final int[] PREF_DEFAULTS = new int[]{

        1, 9, 9, 35

    };

    private static final HashMap<String, Integer> CONFIG_VALUES = new HashMap<String, Integer>();
    private static final HashMap<String, EventInteger> CONFIG_EVENT_VALUES = (

        new HashMap<String, EventInteger>()

    );

    // Read the config values and add them to the Map
    public static void init() {

        Preferences preferences = Preferences.userRoot().node(PREF_NODE);

        int val;

        for (int i = 0; i < PREF_KEYS.length; i++) {

            // Get the specific value [or its default if not present],
            // the value itself will be written at the end.
            val = preferences.getInt(PREF_KEYS[i], PREF_DEFAULTS[i]);

            CONFIG_VALUES.put(PREF_KEYS[i], val);

        }

    }

    // Write the config values, usually called at the end of the program
    // to reduce the number of preference calls [Which tends to be taxing]
    public static void end() {

        Preferences preferences = Preferences.userRoot().node(PREF_NODE);

        for (String key : CONFIG_VALUES.keySet()) {

            preferences.putInt(key, CONFIG_VALUES.get(key));

        }

    }

    // Retrieve a configuration value
    public static int getConfigValue(final String name) {

        if (CONFIG_VALUES.containsKey(name)) {

            return CONFIG_VALUES.get(name);

        } else {

            System.out.println("Config entry " + name + " not found, defaulting to zero.");

            return 0;

        }

    }

    // Set a configuration
    public static void setConfigValue(final String name, final int value) {

        if (CONFIG_VALUES.containsKey(name)) {

            CONFIG_VALUES.put(name, value);

            // Make sure to also iterate through the list of Event
            // values and update the values of all of them, notifying
            // any listeners

            for (String key : CONFIG_EVENT_VALUES.keySet()) {

                if (key.equals(name)) {

                    EventInteger eventValue = CONFIG_EVENT_VALUES.get(name);

                    eventValue.setValue(value);

                }

            }

        } else {

            System.out.println("Config entry " + name + " not found, setting nothing");

        }

    }

    // Create an EventInteger out of a specific config value
    public static EventInteger createEventConfigValue(final String name) {

        int rawValue = getConfigValue(name);

        // Create a new EventInteger w/that value and store it,
        // while returning the same value to whoever called the function
        CONFIG_EVENT_VALUES.put(

            name,

            new EventInteger(rawValue)

        );

        return CONFIG_EVENT_VALUES.get(name);

    }

    // Get an existing EventInteger as a configvalue
    public static EventInteger getEventConfigValue(final String name) {

        if (CONFIG_EVENT_VALUES.containsKey(name)) {

            return CONFIG_EVENT_VALUES.get(name);

        } else {

            System.out.println("Config entry " + name + " not found, defaulting to zero");

            return new EventInteger(0);

        }

    }

}

// OxygenCobalt
