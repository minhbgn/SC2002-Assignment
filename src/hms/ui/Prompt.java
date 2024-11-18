package hms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** A utility class for prompting the user for input. */
public class Prompt {
    /** A list of values that represent true. */
    private static final ArrayList<String> BOOLEAN_TRUE_VALUES = new ArrayList<>(
        List.of("yes", "y", "true", "t", "1"));
    /** A list of values that represent false. */
    private static final ArrayList<String> BOOLEAN_FALSE_VALUES = new ArrayList<>(
        List.of("no", "n", "false", "f", "0"));

    /** A date formatter for parsing dates in the format DD/MM/YYYY. */
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    /** A date formatter for parsing dates in the format DD/MM/YYYY HH:MM:SS. */
    private static final SimpleDateFormat detailedDateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    /** Prevent instantiation of this class by making constructor private. */
    private Prompt(){ }

    /**
     * Prompts the user for an integer input.
     * @param message The message to display to the user
     * @return The integer input by the user
     */
    public static int getIntInput(String message){
        System.out.print(message);

        try {
            return Integer.parseInt(InputHandler.getInstance().getInput());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer value.");
            return getIntInput(message);
        }
    }

    /**
     * Prompts the user for a double input.
     * @param message The message to display to the user
     * @return The double input by the user
     */
    public static String getStringInput(String message){
        System.out.print(message);

        return InputHandler.getInstance().getInput();
    }

    /**
     * Prompts the user for a double input.
     * @param message The message to display to the user
     * @return The double input by the user
     */
    public static boolean getBooleanInput(String message){
        System.out.print(message);

        String input = InputHandler.getInstance().getInput();
        
        if (BOOLEAN_TRUE_VALUES.contains(input.toLowerCase()))
            return true;

        if (BOOLEAN_FALSE_VALUES.contains(input.toLowerCase()))
            return false;
        
        System.out.println("Invalid input. Please enter a valid value (y/n)");
        return getBooleanInput(message);
    }

    /**
     * Prompts the user for a date input.
     * @param message The message to display to the user
     * @return The date input by the user
     */
    public static Date getDateInput(String message){
        System.out.println("Please enter the date in the format DD/MM/YYYY");
        System.out.print(message);

        String input = InputHandler.getInstance().getInput();

        try {
            return dateFormatter.parse(input);
        } catch (ParseException e) {
            System.out.println("Invalid date. Please enter a valid date in the format DD/MM/YYYY");
            return getDateInput(message);
        }
    }

    /**
     * Prompts the user for a detailed date input.
     * @param message The message to display to the user
     * @return The date input by the user
     */
    public static Date getDetailedDateInput(String message){
        System.out.println("Please enter the date in the format DD/MM/YYYY HH:MM:SS");
        System.out.print(message);

        String input = InputHandler.getInstance().getInput();

        try {
            return detailedDateFormatter.parse(input);
        } catch (ParseException e) {
            System.out.println("Invalid date. Please enter a valid date in the format DD/MM/YYYY HH:MM:SS");
            return getDetailedDateInput(message);
        }
    }
}
