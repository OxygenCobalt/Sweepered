// Logger
// Special printer that takes notices from programs and prints them/logs them in a file

package shared.log;

import java.time.Instant;

import shared.config.Configuration;

public final class Logger {

    // TODO: Allow logger to write to a file?

    private Logger() {

        // Not called

    }

    // Store the time when the program first started to be used later
    private static long startTime;

    // Int value of the configuration flag to show debug logs
    private static int debugValue = Configuration.getConfigValue("debugLogs");

    // Levels of concern used by Logger
    // to determine what to print and what color to use

    // SUCCESS is for when an operation is completed without issues
    // DEBUG is for minute operations that can be shown optionally
    // WARNING is for operations that fail, but do not cause much damage
    // FATAL is for critical operations that will crash the program if
    // they fail. These prints also cause logger to forcefully exit the program
    // if encountered.
    public enum Level {

        SUCCESS, DEBUG, WARNING, FATAL

    }

    public static void log(final String message,
                           final Level level,
                           final Object source) {

        // Dont output debug values unless the configuration flag is
        // set to one [Enabled]
        if (debugValue == 1 || level != Level.DEBUG) {

            // Get each value to be used in the final print statement
            String levelText = getLevelText(level);
            String sourceText = getClassName(source);
            String timeText = getTime();

            // Then print it out in this format
            // [LEVEL] [CLASSNAME@TIME]: MESSAGE
            System.out.println(

                "[" + levelText + "] [" + sourceText + "@" + timeText + "]: " + message

            );

            // If the log level is FATAL, also exit the program
            // to prevent a crash from the failed operation.
            if (level == Level.FATAL) {

                System.exit(1);

            }

        }

    }

    private static String getLevelText(final Level level) {

        // The coloring used for the logger is based
        // on the typical ansi color codes.

        String ansiColor = "";
        String ansiReset = "\u001B[0m";

        // SUCCESS has a green color
        // DEBUG has a blue color
        // WARNING has a yellow color
        // FATAL has a red color

        // FIXME: This doesnt work on command prompt,
        // need to disable it if the platform is windows
        switch (level) {

            case SUCCESS: ansiColor = "\u001B[32m"; break;
            case DEBUG: ansiColor = "\u001B[34m"; break;
            case WARNING: ansiColor = "\u001B[33m"; break;
            case FATAL: ansiColor = "\u001B[31m"; break;

        }

        // Return the full level string, usually formatted like [LEVEL]
        return ansiColor + String.valueOf(level) + ansiReset;

    }

    private static String getClassName(final Object source) {

        // Get the type of the given object as a string,
        // which will return something like "class package.package.Class"
        String classType = source.getClass().toString();

        int classIndex = 0;
        Character letter;

        // After getting the class name, crop it down to just the class name
        // [E.G "Class"] to then be returned
        for (int charIndex = 0; charIndex < classType.length(); charIndex++) {

            letter = classType.charAt(charIndex);

            if (Character.isUpperCase(letter)) {

                classIndex = charIndex;

                break;

            }

        }

        return classType.substring(classIndex, classType.length());

    }

    private static String getTime() {

        // Get the current time and compare that
        // with the initial time to get the amount of elapsed time
        long currentTime = Instant.now().getEpochSecond();

        int seconds = (int) (currentTime - startTime);

        // Get the amount of hours/minutes based on the
        // elapsed time in seconds as well
        int minutes = seconds / 60;
        int hours = minutes / 60;

        String stringHours = "";
        String stringMinutes = "";
        String stringSeconds = "";

        // Add the hours only if any have elapsed
        if (hours > 0) {

            if (hours < 10) {

                stringHours = "0";

            }

            stringHours = stringHours + String.valueOf(hours) + ":";

        }

        // If the minute/second counts are in the
        // single digits, add a 0 to the beginning
        // before adding the count to the string
        if (minutes < 10) {

            stringMinutes = "0";

        }

        if (seconds < 10) {

            stringSeconds = "0";

        }

        // Then add the time values to the strings
        stringMinutes = stringMinutes + String.valueOf(minutes) + ":";
        stringSeconds = stringSeconds + String.valueOf(seconds);

        // Return the full time
        return stringHours + stringMinutes + stringSeconds;

    }

    // Method called by SweeperedApp to notify Logger to
    // record the current time to be used later to find the
    // elapsed time
    public static void markStartTime() {

        startTime = Instant.now().getEpochSecond();

    }

}

// OxygenCobalt
