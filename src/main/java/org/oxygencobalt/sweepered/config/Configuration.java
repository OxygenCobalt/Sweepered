// Configuration
// Class that stores the configuration values used across the game

package config;

import java.util.HashMap;
import java.util.ArrayList;

import java.io.File;
import java.util.Scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public final class Configuration {

    private static final HashMap<String, Integer> CONFIG_VALUES = new HashMap<String, Integer>();
    private static final HashMap<Integer, String> CONFIG_LINES = new HashMap<Integer, String>();

    private static final String CONFIGURATION_PATH = "src/main/resources/configuration";

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

            System.out.println("Configuration not found, reverting to default values");

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

    public static int getConfigValue(final String name) {

        if (CONFIG_VALUES.containsKey(name)) {

            return CONFIG_VALUES.get(name);

        } else {

            System.out.println("Config entry not found");

            return -1;

        }

    }

    public static void setConfigValue(final String name, final int value) {

        if (CONFIG_VALUES.containsKey(name)) {

            CONFIG_VALUES.put(name, CONFIG_VALUES.get(name));

        } else {

            System.out.println("Config entry not found.");

        }

    }

}

// OxygenCobalt
