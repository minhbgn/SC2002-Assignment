package hms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Prompt {
    private static final ArrayList<String> BOOLEAN_TRUE_VALUES = new ArrayList<>(
        List.of("yes", "y", "true", "t", "1")
    );
    private static final ArrayList<String> BOOLEAN_FALSE_VALUES = new ArrayList<>(
        List.of("no", "n", "false", "f", "0")
    );
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    
    public Prompt(String message){
        throw new UnsupportedOperationException("Prompt should not be instantiated");
    }

    public static int getIntInput(String message){
        System.out.print(message);

        try {
            return Integer.parseInt(InputHandler.getInstance().getInput());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer value.");
            return getIntInput(message);
        }
    }

    public static String getStringInput(String message){
        System.out.print(message);

        return InputHandler.getInstance().getInput();
    }

    public boolean getBooleanInput(String message){
        System.out.print(message);

        String input = InputHandler.getInstance().getInput();
        
        if (BOOLEAN_TRUE_VALUES.contains(input.toLowerCase()))
            return true;

        if (BOOLEAN_FALSE_VALUES.contains(input.toLowerCase()))
            return false;
        
        System.out.println("Invalid input. Please enter a valid value (y/n)");
        return getBooleanInput(message);
    }

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
}
