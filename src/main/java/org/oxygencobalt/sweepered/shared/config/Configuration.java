// Configuration
// Class that stores the configuration values used across the game,
// but they're not global variables, I swear.

package shared.config;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.io.IOException;

import shared.values.EventInteger;

public final class Configuration {

    private static final HashMap<String, Integer> CONFIG_VALUES = new HashMap<String, Integer>();

    private static final HashMap<String, EventInteger> CONFIG_EVENT_VALUES = (

        new HashMap<String, EventInteger>()

    );

    private static final HashMap<Integer, String> CONFIG_LINES = new HashMap<Integer, String>();

    private static final String CONFIGURATION_PATH = "src/main/resources/config/configuration";

    private Configuration() {

        // Not Called

    }

    public static void readConfigFile() {

        try {

            // Open the configuration file, and its respective scanner to read it
            File configurationFile = new File(CONFIGURATION_PATH);
            Scanner configScanner = new Scanner(configurationFile);

            Boolean isValid;
            Boolean hasName;
            Boolean hasValue;

            String line;

            String name;
            String value;

            int lineNumber = 0;

            while (configScanner.hasNextLine()) {

                line = configScanner.nextLine();

                // Ignore all lines that are comments [//] or newlines
                isValid = !line.contains("//") && !line.isEmpty();

                // Also check if the correct brackets are actually present

                // FIXME: This does not account for bracket placement
                // or the contents between the brackets, but I have no
                // idea how I would do that w/regex
                hasName = line.contains("[") && line.contains("]");
                hasValue = line.contains("{") && line.contains("}");

                if (isValid && hasName && hasValue) {

                    // Get the index of the brackets and
                    // capture the string underneath them

                    // 1 is added to the beginning index as otherwise the
                    // extracted string would show the first bracket.
                    name = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                    value = line.substring(line.indexOf("{") + 1, line.indexOf("}"));

                    // Add this value to the dictionary
                    CONFIG_VALUES.put(name, Integer.parseInt(value));

                    // In another dictionary, log the line number this value was at
                    // in the case that the value needs to be rewritten
                    CONFIG_LINES.put(lineNumber, name);

                }

                lineNumber++;

            }

        } catch (IOException exception) {

            // I am unable to throw an exception like FileNotFoundException, so
            // simply print out a notice and exit the program instead before anything
            // bad happens.
            System.out.println("Cannot find configuration file, aborting...");

            System.exit(1);

        }

    }

    public static void writeToConfigFile() {

        try {

            // Open the configuration file, and its respective scanner to read it
            File configurationFile = new File(CONFIGURATION_PATH);
            Scanner configScanner = new Scanner(configurationFile);

            ArrayList<String> lines = new ArrayList<String>();

            // Iterate through the entire file, adding each line to an ArrayList of lines
            while (configScanner.hasNextLine()) {

                lines.add(configScanner.nextLine());

            }

            // Then, write those lines back with the new information
            PrintWriter writer = new PrintWriter(

                new BufferedWriter(

                    new FileWriter(CONFIGURATION_PATH)

                )

            );

            String newLine;

            String name;
            int value;

            int lineNumber = 0;

            for (String line : lines) {

                // If the line number matches one stored from
                // openConfigFile(), create a new line with the
                // [ValueName] {Value} syntax in order to be read later.
                if (CONFIG_LINES.containsKey(lineNumber)) {

                    name = CONFIG_LINES.get(lineNumber);
                    value = CONFIG_VALUES.get(name);

                    newLine = "[" + name + "]" + " " + "{" + value + "}";

                // Otherwise, just write the same line again
                } else {

                    newLine = line;

                }

                writer.println(newLine);

                lineNumber++;

            }

            // Once everything is done, close the writer,
            // therefore saving the changes made.
            writer.close();

        } catch (IOException exception) {

            System.out.println("Failed writing configuration to file");

        }

    }

    // Retrieve a configuration value
    public static int getConfigValue(final String name) {

        if (CONFIG_VALUES.containsKey(name)) {

            return CONFIG_VALUES.get(name);

        } else {

            System.out.println("Config entry not found, defaulting to zero.");

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

            System.out.println("Config entry not found, setting nothing");

        }

    }

    // Create an EventInteger out of a specific config value
    public static EventInteger createEventConfigValue(final String name) {

        int rawValue = getConfigValue(name);

        // Create a new EventInteger w/that value and store it,
        // while returning the same value to whoever called the function
        CONFIG_EVENT_VALUES.put(

            name,

            new EventInteger(rawValue, name)

        );

        return CONFIG_EVENT_VALUES.get(name);

    }

    // Get an existing EventInteger as a configvalue
    public static EventInteger getEventConfigValue(final String name) {

        if (CONFIG_EVENT_VALUES.containsKey(name)) {

            return CONFIG_EVENT_VALUES.get(name);

        } else {

            System.out.println("Config entry not found, defaulting to zero");

            return new EventInteger(0, "Invalid");

        }

    }

}

// OxygenCobalt
